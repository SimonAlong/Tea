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
     * @return
     */
    String name();

    /**
     * 别名
     * @return
     */
    String alias() default "";
}
