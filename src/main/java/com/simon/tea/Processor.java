package com.simon.tea;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午4:23
 */
public interface Processor extends Command{
    ThreadLocal<String> local = new ThreadLocal<>();

    default boolean math(String input){
        this.local.set(input);
        return mathMode(input);
    }

    /**
     * 匹配器具体模式
     * @param input
     * @return
     */
    boolean mathMode(String input);

    /**
     * 解析器
     */
    void process();
}
