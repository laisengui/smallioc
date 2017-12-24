package cn.lsg.smallioc.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.lsg.smallioc.exception.SmalliocException;

/**
 * 类解析
 * 
 * @author Norton Lai
 * @created 2017-12-23 下午11:01:59
 */
public class ClassAnalysis {

    /**
     * 获取类下指定注解
     * @author Norton Lai
     * @created 2017-12-24 下午5:54:51
     * @param clazz
     * @param annoClass
     * @return
     */
    public static Annotation getClassAnnotation(Class clazz, Class annoClass) {
        return clazz.getAnnotation(annoClass);
    }

    /**
     * 是否有指定的注解
     * 
     * @author Norton Lai
     * @created 2017-12-23 下午11:20:49
     * @param clazz
     * @param annoClass
     * @return
     */
    public static boolean hasAnno(Class clazz, Class annoClass) {
        return getClassAnnotation(clazz, annoClass) == null ? false : true;
    }

    /**
     * 获取注解属性
     * @author Norton Lai
     * @created 2017-12-24 上午12:22:47
     * @param anno
     * @param methodName
     * @return
     */
    public static Object getAnnoVal(Annotation anno, String methodName) {
        Method m;
        try {
            m = anno.annotationType().getMethod(methodName);
            if (!m.isAccessible()) {
                m.setAccessible(true);
            }
            return m.invoke(anno);
        } catch (Exception e) {
            throw new SmalliocException("获取注解属性失败",e);
        }

    }

}
