package com.simon.tea;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午4:23
 */
public interface Processor extends Command{
    /**
     * 解析器
     */
    void process(Context context);
}
