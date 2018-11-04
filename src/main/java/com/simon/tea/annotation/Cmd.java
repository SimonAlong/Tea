package com.simon.tea.annotation;

import static com.simon.tea.meta.CmdTypeEnum.NORMAL;

import com.simon.tea.meta.CmdTypeEnum;
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
     * 描述
     */
    String describe() default "";

    /**
     * 激活标记，默认为true，若设置为false，则需要 CmdTypeEnum.ACTIVITY 对应的命令执行后，才能使用
     */
    boolean active() default true;

    /**
     * 激活标记，默认为true，若设置为false，则需要有些指令，需要在一些执行执行后才能使用
     */
    CmdTypeEnum type() default CmdTypeEnum.NORMAL;
}
