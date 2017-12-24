package cn.lsg.smallioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 依赖注入 注解
 * 
 * @author Norton Lai
 * @created 2017-12-23 下午8:18:22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD })
public @interface Inject {
    // 注入的时候如果要根据类成员的id则填，否则直接根据类型自己寻找注入。未找到或找到多个抛异常
    public String id() default "";
}
