package cn.lsg.smallioc.core;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.lsg.smallioc.annotation.Inject;
import cn.lsg.smallioc.annotation.Member;
import cn.lsg.smallioc.exception.SmalliocException;
import cn.lsg.smallioc.log.Logger;
import cn.lsg.smallioc.model.BeanDefine;
import cn.lsg.smallioc.model.FieldDefine;
import cn.lsg.smallioc.model.MethodDefine;
import cn.lsg.smallioc.util.ClassAnalysis;
import cn.lsg.smallioc.util.ClassScanUtil;
import cn.lsg.smallioc.util.SUtil;

/**
 * class采集器
 * 
 * @author Norton Lai
 * @created 2017-12-23 下午11:15:51
 */
public class ClassCollection {

    /**
     * 扫描包下class
     * 
     * @author Norton Lai
     * @created 2017-12-23 下午11:22:24
     * @param url
     * @return
     */
    public List<Class> scanClass(String... urls) {
        List<Class> res = new ArrayList<Class>();
        for (String url : urls) {
            getLog().debug("扫描路径： " + url);
            res.addAll(ClassScanUtil.getClasssFromPackage(url));
        }
        if (res.size() == 0) {
            throw new SmalliocException("未扫描到任何class文件");
        }
        return res;
    }

    /**
     * 获取日志记录器
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午5:48:11
     * @return
     */
    private Logger getLog() {
        return ApplicationContext.getLog();
    }

    /**
     * 过滤出需要管理的class
     * 
     * @author Norton Lai
     * @created 2017-12-23 下午11:22:34
     * @param list
     * @return
     */
    public List<Class> filterNeedManage(List<Class> list) {
        List<Class> res = new ArrayList<Class>();
        for (Class c : list) {
            if (ClassAnalysis.hasAnno(c, Member.class)) {
                res.add(c);
            }
        }
        if (res.size() == 0) {
            throw new SmalliocException("未找到需要管理到类");
        }
        return res;
    }

    /**
     * 创建bean详情
     * 
     * @author Norton Lai
     * @created 2017-12-24 上午1:04:32
     * @param list
     * @return
     */
    public List<BeanDefine> createBeanDefine(List<Class> list) {
        List<BeanDefine> res = new ArrayList<BeanDefine>();
        for (Class c : list) {
            getLog().debug("解析Class： " + c.getName());
            BeanDefine b = new BeanDefine();
            b.setClazz(c);

            b.setInterfaces(c.getInterfaces());

            // 添加类注解
            Annotation anno = c.getAnnotation(Member.class);
            String id = ClassAnalysis.getAnnoVal(anno, Member.field.id.name()).toString();
            if (SUtil.isEmp(id)) {
                id = SUtil.firstLow(c.getSimpleName());
            }
            b.setId(id);
            boolean single = (boolean) ClassAnalysis.getAnnoVal(anno, Member.field.single.name());
            b.setSingle(single);

            getLog().debug("解析Class到beandefine： " + b.toString());

            // 添加属性注解
            List<FieldDefine> fd = createFieldDefine(c);
            b.setFieldDefine(fd);

            // 添加方法注解

            // 获取其属性的描述
            List<MethodDefine> md = createMethodDefine(c);
            b.setMethodDefine(md);

            res.add(b);
        }
        return res;
    }

    /**
     * 生成方法描述
     * 
     * @author Norton Lai
     * @created 2017-12-24 上午10:22:42
     * @param c
     * @return
     * @throws IntrospectionException
     */
    private List<MethodDefine> createMethodDefine(Class c) {
        List<MethodDefine> md = new ArrayList<>();
        PropertyDescriptor[] ps;
        try {
            ps = Introspector.getBeanInfo(c).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new SmalliocException("解析类方法异常", e);
        }
        for (PropertyDescriptor proderdesc : ps) {
            // 获取所有set方法
            Method setter = proderdesc.getWriteMethod();
            // 判断set方法是否定义了注解
            if (setter == null || !setter.isAnnotationPresent(Inject.class)) {
                continue;
            }

            // 获取当前注解，并判断name属性是否为空
            Inject inject = setter.getAnnotation(Inject.class);
            String mid = null;
            boolean injectById = false;
            if (SUtil.isNotEmp(inject.id())) {
                mid = inject.id();
                injectById = true;
            }
            MethodDefine m = new MethodDefine(mid, injectById, proderdesc.getPropertyType(), setter);
            getLog().debug("解析方法到methoddefine： " + m.toString());
            md.add(m);
        }
        return md;
    }

    /**
     * 生成属性描述
     * 
     * @author Norton Lai
     * @created 2017-12-24 上午10:02:15
     * @param c
     * @return
     */
    private List<FieldDefine> createFieldDefine(Class c) {
        List<FieldDefine> fd = new ArrayList<>();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if (field == null || !field.isAnnotationPresent(Inject.class)) {
                continue;
            }
            Inject inject = field.getAnnotation(Inject.class);
            boolean injectById = false;
            String fid = null;
            if (SUtil.isNotEmp(inject.id())) {
                injectById = true;
                fid = inject.id();
            }
            Class<?> type = field.getType();
            FieldDefine f = new FieldDefine(fid, injectById, type, field);
            getLog().debug("解析属性到fielddefine： " + f.toString());
            fd.add(f);
        }
        return fd;
    }
}
