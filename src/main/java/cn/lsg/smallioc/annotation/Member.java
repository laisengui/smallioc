package cn.lsg.smallioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要控制反转的成员.
 * 
 * @author Norton Lai
 * @created 2017-12-23 下午7:49:44
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Member {
    //枚举
    public enum field{id,single}
    //被注解的类的id
    public String id() default "";

    //是否单例
    public boolean single() default true;

}
