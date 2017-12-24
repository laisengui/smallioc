package cn.lsg.smallioc.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lsg.smallioc.exception.SmalliocException;
import cn.lsg.smallioc.log.Logger;
import cn.lsg.smallioc.log.LoggerImpl;
import cn.lsg.smallioc.model.BeanDefine;
import cn.lsg.smallioc.model.FieldDefine;
import cn.lsg.smallioc.model.MethodDefine;
import cn.lsg.smallioc.util.SUtil;

/**
 * IOC容器
 * 
 * @author Norton Lai
 * @created 2017-12-24 上午12:30:13
 */
public class ApplicationContext {

    // 日志记录工具 默认使用自己的实现类，可以更换
    private static Logger logUtil;
    // 单例
    private static ApplicationContext app;
    // id---beandefine
    private HashMap<String, BeanDefine> idContext;
    // beandefine
    private List<BeanDefine> beanDefine;
    // class（inteface）---beandefine
    private HashMap<Class, BeanDefine> typeContext;
    // class----beandefine
    private HashMap<Class, BeanDefine> class2BeanDefine;

    /**
     * 私有构造
     * 
     * @author Norton Lai
     * @param packagePath
     * @created 2017-12-24 上午12:30:43
     */
    private ApplicationContext(String[] packagePath) {
        super();
        initContext(packagePath);
    }

    /**
     * 如果不满意 不想用 默认的日志记录器 可以替换其他日志记录器.<br>
     * 替换方法为 创建类实现Logger接口,并在接口方法内调用其他日志记录器的方法.只能是容器实例化前设置
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午5:02:30
     * @param log
     * @return 返回 替换是否成功
     */
    public static boolean replaceLogger(Logger log) {
        if (app == null && log != null) {
            logUtil = log;
            return true;
        }
        return false;
    }

    /**
     * 设置默认日志记录器的参数 对自定义的日志记录器无效.如果只想设置其中一个或几个参数 其他参数 设置Null 即可.只能是容器实例化前设置
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午4:58:27
     * @param print2Console
     *            是否输出到控制台
     * @param printFile
     *            是否输出到文件
     * @param logLevel
     *            输出等级
     * @param dirUrl
     *            输出到文件到 文件夹路径 不填 默认 运行路径
     * @return 返回日志配置是否更改成功
     */
    public static boolean setDefaultLoggerParams(Boolean print2Console, Boolean printFile, Logger.LEVEL logLevel,
            String dirUrl) {
        if (app != null) {
            return false;
        }
        if (printFile != null) {
            LoggerImpl.setPrint2File(printFile);
        }
        if (print2Console != null) {
            LoggerImpl.setPrint2Console(print2Console);
        }
        if (logLevel != null) {
            LoggerImpl.setLogLevel(logLevel);
        }
        if (SUtil.isNotEmp(dirUrl)) {
            LoggerImpl.setDirUrl(dirUrl);
        }
        return true;
    }

    /**
     * 返回日志记录
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午5:07:39
     * @return
     */
    public static String retDefaultLoggerParams() {
        return LoggerImpl.retParams();
    }

    /**
     * 获取记录器
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午5:11:06
     * @return
     */
    public static Logger getLog() {
        if (logUtil == null) {
            logUtil = new LoggerImpl();
        }
        return logUtil;
    }

    /**
     * 返回容器实例. 容器最多只能有一个,第一次调用必须传入至少一个有效的包路径
     * 
     * @author Norton Lai
     * @created 2017-12-24 上午12:33:54
     * @param packagePath
     *            包路径 例：com.lsg.util 、com
     * @return
     */
    public static ApplicationContext getContext(String... packagePath) {
        if (packagePath == null) {
            if (app == null) {
                throw new SmalliocException("容器未初始化，必须先传入扫描路径初始化");
            }
            return app;
        }
        app = new ApplicationContext(packagePath);
        return app;
    }

    /**
     * 根据类型获取bean
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午12:17:00
     * @param clazz
     * @return
     */
    public <T> T getbean(Class<T> clazz) {
        if (typeContext.containsKey(clazz)) {
            return (T) checkBean(typeContext.get(clazz));
        }
        throw new SmalliocException("未找到对应类型的bean，或对应类型到bean有多个");
    }

    /**
     * 根据id获取bean
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午12:17:11
     * @param id
     * @param clazz
     * @return
     */
    public <T> T getbean(String id, Class<T> clazz) {
        if (idContext.containsKey(id)) {
            return (T) checkBean(idContext.get(id));
        }
        throw new SmalliocException("未找到对应id的bean");
    }

    /**
     * 根据id获取bean
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午12:17:11
     * @param id
     * @param clazz
     * @return
     */
    public Object getbean(String id) {
        if (idContext.containsKey(id)) {
            return checkBean(idContext.get(id));
        }
        throw new SmalliocException("未找到对应id的bean");
    }

    /**
     * 提取出bean.如果是单例直接给 如果不是 先克隆再给
     * 
     * @author Norton Lai
     * @param <T>
     * @created 2017-12-24 下午12:22:05
     * @param beanDefine2
     * @return
     */
    private Object checkBean(BeanDefine beanDefine) {
        if (beanDefine.isSingle()) {
            return beanDefine.getBean();
        }
        getLog().debug("根据原型克隆新实例: " + beanDefine.getClazz().getSimpleName());
        return deepClone(beanDefine, null);
    }

    /**
     * 深度克隆
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午12:39:02
     * @param beanDefine
     * @param classContext
     *            深度克隆中 每个新建的多例 实例都会放入这里 而遇到同样的多例实例优先从这里取，这代表即使是多例
     *            在一次深度克隆中也只会创建一个实例
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private Object deepClone(BeanDefine beanDefine, Map<Class, Object> classContext) {
        try {
            if (classContext == null) {
                classContext = new HashMap<>();
            }

            Class clazz = beanDefine.getClazz();

            Object bean = beanDefine.getBean();// 原始对象

            Object newInst = clazz.newInstance();// 新对象
            getLog().debug("#克隆实例: " + newInst);

            classContext.put(clazz, newInst);

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                boolean staticOrFinal = Modifier.isStatic(field.getModifiers())
                        || Modifier.isFinal(field.getModifiers());
                if (staticOrFinal) {// 如果是静态或final 不管
                    continue;
                }
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Object resoult = field.get(bean);
                if (resoult == null) {// 如果为空 不管
                    continue;
                }

                if (!class2BeanDefine.containsKey(field.getType())) {
                    continue;// 如果不是受管类不管 因为克隆对象和原始对象都是刚newinstance出来的
                             // 不会有非受管对象方面的差异
                }

                BeanDefine targetBeanDefine = class2BeanDefine.get(field.getType());
                Object targetBean = null;
                if (targetBeanDefine.isSingle()) {
                    targetBean = targetBeanDefine.getBean();
                } else {
                    if (classContext.containsKey(targetBeanDefine.getClazz())) {
                        targetBean = classContext.get(targetBeanDefine.getClazz());
                    } else {
                        targetBean = deepClone(targetBeanDefine, classContext);
                    }
                }
                getLog().debug("##注入属性: " + targetBean + " ===> " + field.getName());
                field.set(newInst, targetBean);
            }

            return newInst;

        } catch (Exception e) {
            throw new SmalliocException("深度克隆多例bean异常", e);
        }
    }

    /**
     * 初始化容器
     * 
     * @author Norton Lai
     * @created 2017-12-24 上午12:40:17
     * @param packagePath
     */
    private void initContext(String[] packagePath) {
        getLog().debug("=================================初始化IOC容器【smallioc】===================================");
        ClassCollection coll = new ClassCollection();
        getLog().debug("扫描路径下所有类..............");
        List<Class> scanClass = coll.scanClass(packagePath);
        getLog().debug("扫描所有类下的被注解的类..............");
        List<Class> needManage = coll.filterNeedManage(scanClass);
        getLog().debug("创建beandefine..............");
        beanDefine = coll.createBeanDefine(needManage);

        // 对beandefine进行整理
        buildContext();

        // 初始化所有不管是否单例
        getLog().debug("初始化bean..............");
        for (BeanDefine b : beanDefine) {
            b.initBean();
            getLog().debug("初始化bean " + b.getBean());
        }
        // 对所有bean进行注入 此时多例的类也当成单例注入
        getLog().debug("注入组装..............");
        injectBean();

        // 对多例的bean原型进行克隆过 斩断和 之前注入的bean的关系
        getLog().debug("克隆生成多例bean原型..............");
        for (BeanDefine b : beanDefine) {
            if (!b.isSingle()) {
                getLog().debug("生成原型: " + b.getClazz().getSimpleName());
                b.setBean(deepClone(b, null));
            }
        }

        getLog().debug("=================================IOC容器【smallioc】初始化完成===================================");
        getLog().flush();
    }

    /**
     * 注入组装
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午12:11:57
     */
    private void injectBean() {
        for (BeanDefine b : beanDefine) {
            getLog().debug("#组装实例: " + b.getBean());
            List<FieldDefine> fieldDefine = b.getFieldDefine();
            for (FieldDefine f : fieldDefine) {
                Field field = f.getField();
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                BeanDefine target = findBeanDefine(f.isInjectById(), f.getId(), f.getType());
                try {
                    getLog().debug("##注入属性： " + target.getBean() + " ===> " + field.getName());
                    field.set(b.getBean(), target.getBean());
                } catch (IllegalArgumentException e) {
                    throw new SmalliocException("无效参数", e);
                } catch (IllegalAccessException e) {
                    throw new SmalliocException("属性不可访问", e);

                }
            }
            List<MethodDefine> methodDefine = b.getMethodDefine();
            for (MethodDefine m : methodDefine) {
                Method method = m.getMethod();
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                BeanDefine target = findBeanDefine(m.isInjectById(), m.getId(), m.getType());

                try {
                    getLog().debug("##注入方法 ： " + target.getBean() + " ===> " + method.getName());
                    method.invoke(b.getBean(), target.getBean());
                } catch (IllegalAccessException e) {
                    throw new SmalliocException("方法不可访问", e);
                } catch (IllegalArgumentException e) {
                    throw new SmalliocException("无效参数", e);
                } catch (InvocationTargetException e) {
                    throw new SmalliocException("底层异常", e);
                }
            }
        }
    }

    /**
     * 寻找对应id或类型的 bean定义
     * 
     * @author Norton Lai
     * @created 2017-12-24 上午11:36:17
     * @param isById
     * @param id
     * @param type
     * @return
     */
    private BeanDefine findBeanDefine(boolean isById, String id, Class type) {
        BeanDefine target = null;
        if (isById) {
            if (!idContext.containsKey(id)) {
                throw new SmalliocException("属性注入失败，未找到Id=" + id + "对应的bean");
            }
            target = idContext.get(id);
            // 判断 属性所表示的类型是否 和 找到的实例的类型一致 或者至少是其上层类或接口
            if (!type.isAssignableFrom(target.getClazz())) {
                throw new SmalliocException("id所属实例的类型和属性不匹配: id=" + id + " 属性类型: " + type.getName() + " 实例类型: "
                        + target.getClazz().getName());
            }

        } else {
            if (!typeContext.containsKey(type)) {
                throw new SmalliocException("根据类型找到类0个或多个实例:" + type.getName());
            }
            target = typeContext.get(type);
        }
        return target;
    }

    /**
     * 整理结构
     * 
     * @author Norton Lai
     * @created 2017-12-24 上午10:52:13
     */
    private void buildContext() {
        // 根据id进行整理
        getLog().debug("初始化ID容器");
        idContext = new HashMap<String, BeanDefine>();
        for (BeanDefine b : beanDefine) {
            if (idContext.containsKey(b.getId())) {
                throw new SmalliocException("指定的Member id冲突");
            }
            getLog().debug("ID容器: 【 " + b.getId() + " <===>" + b.toString() + " 】");
            idContext.put(b.getId(), b);
        }

        // 根据类型进行整理
        // 类型对应 beandefine 不算接口的
        getLog().debug("初始化类型容器");
        class2BeanDefine = new HashMap<Class, BeanDefine>();
        typeContext = new HashMap<Class, BeanDefine>();
        List<Class> disableInterface = new ArrayList<>();// 这些接口不能根据类型注入，因为存在多个管理的实现类
        for (BeanDefine b : beanDefine) {
            class2BeanDefine.put(b.getClazz(), b);
            typeContext.put(b.getClazz(), b);
            Class[] interfaces = b.getInterfaces();
            for (Class c : interfaces) {
                if (typeContext.containsKey(c)) {
                    disableInterface.add(c);
                    continue;
                }
                typeContext.put(c, b);
            }
            getLog().debug("类型容器: 【 " + b.getClazz().getSimpleName() + " <===>" + b.toString() + " 】");
        }
        for (Class d : disableInterface) {
            typeContext.remove(d);
            getLog().debug(d.getSimpleName() + " 类型同时存在多个实例，不能根据类型寻找实例，将从类型容器删除");
        }

    }

}
