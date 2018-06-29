package com.simon.tea.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解释每个命令的用法
 *
 * @author zhouzhenyong
 * @since 2018/6/29 下午5:37
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Usage {

    /**
     * 针对哪个命令
     */
    String target();
}
