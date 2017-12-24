package cn.lsg.smallioc.model;

import java.util.Arrays;
import java.util.List;

import cn.lsg.smallioc.exception.SmalliocException;

/**
 * 描述管控的class的各种属性
 * 
 * @author Norton Lai
 * @created 2017-12-23 下午11:23:32
 */
public class BeanDefine {

    /**
     * bean的id
     */
    private String id;

    /**
     * bean的类对象
     */
    private Class clazz;

    /**
     * 类的所有接口
     */
    private Class[] interfaces;

    /**
     * 是否单例
     */
    private boolean single = true;

    /**
     * 属性
     */
    private List<FieldDefine> fieldDefine;
    /**
     * 方法
     */
    private List<MethodDefine> methodDefine;

    /**
     * 实例
     */
    private Object bean;



    /**
     * 获取原始实例
     * @author Norton Lai
     * @created 2017-12-24 下午12:25:38
     * @return
     */
    public Object getBean() {
        return bean;
    }

    /**
     * 初始化实例 不管是否单例
     * 
     * @author Norton Lai
     * @created 2017-12-24 上午11:11:36
     */
    public void initBean() {
        if (bean == null) {
            try {
                bean = clazz.newInstance();
            } catch (InstantiationException e) {
                throw new SmalliocException("构造方法不存在", e);
            } catch (IllegalAccessException e) {
                throw new SmalliocException("构造方法不可访问", e);
            }
        }
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午12:04:38
     * @return type
     */
    public String getId() {
        return id;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午12:04:38
     * @return type
     */
    public Class getClazz() {
        return clazz;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午12:04:38
     * @return type
     */
    public boolean isSingle() {
        return single;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午12:04:38
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午12:04:38
     * @param clazz
     */
    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午12:04:38
     * @param single
     */
    public void setSingle(boolean single) {
        this.single = single;
    }

    /**
     * 描述
     * @author Norton Lai
     * @created 2017-12-24 下午5:32:04
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "BeanDefine [id=" + id + ", clazz=" + clazz + ", interfaces=" + Arrays.toString(interfaces)
                + ", single=" + single + ", fieldDefine=" + fieldDefine + ", methodDefine=" + methodDefine + "]";
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午9:45:06
     * @return type
     */
    public Class[] getInterfaces() {
        return interfaces;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午9:45:06
     * @param interfaces
     */
    public void setInterfaces(Class[] interfaces) {
        this.interfaces = interfaces;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午9:59:50
     * @return type
     */
    public List<FieldDefine> getFieldDefine() {
        return fieldDefine;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午9:59:50
     * @param fieldDefine
     */
    public void setFieldDefine(List<FieldDefine> fieldDefine) {
        this.fieldDefine = fieldDefine;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午10:21:45
     * @return type
     */
    public List<MethodDefine> getMethodDefine() {
        return methodDefine;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午10:21:45
     * @param methodDefine
     */
    public void setMethodDefine(List<MethodDefine> methodDefine) {
        this.methodDefine = methodDefine;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 下午1:31:34
     * @param bean
     */
    public void setBean(Object bean) {
        this.bean = bean;
    }

}
