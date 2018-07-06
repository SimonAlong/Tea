package com.simon.tea.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模块
 * @author zhouzhenyong
 * @since 2018/6/25 下午4:17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Module {

    /**
     * 模块名，这个用于匹配
     */
    String name();

    /**
     * 别名
     */
    String alias() default "";

    /**
     * 是否可用
     */
    boolean available() default true;

    /**
     * 界面是否可显示
     */
    boolean visible() default true;

    /**
     * 命令函数执行前需要执行的函数，必须是模块内的函数名
     */
    String cmdPreRun() default "";
}
