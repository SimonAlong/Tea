package com.simon.tea.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhouzhenyong
 * @since 2018/6/26 下午11:59
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cmd {
    /**
     * 对应的命令名字
     * @return
     */
    String value();

    /**
     * 别名
     * @return
     */
    String alias() default "";

    /**
     * 对应的命令名字
     * @return
     */
    String describe() default "";
}
