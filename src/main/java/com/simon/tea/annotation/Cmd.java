package com.simon.tea.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.Getter;
import lombok.Setter;

/**
 * 该命令要位于<code>@Module</code> 修饰的类下面才会生效
 *
 * @author zhouzhenyong
 * @since 2018/6/26 下午11:59
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cmd {
    /**
     * 对应的命令名字
     */
    String value();

    /**
     * 别名
     */
    String alias() default "";

    /**
     * 对应的命令名字
     */
    String describe() default "";

    /**
     * 模块默认命令
     */
    boolean isDefault() default false;

    /**
     * 是否使用预处理命令，Module 的 cmdPreRun 属性
     */
    boolean usePreCmd() default true;
}
