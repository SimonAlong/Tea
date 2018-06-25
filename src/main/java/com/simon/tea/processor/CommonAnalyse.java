package com.simon.tea.processor;

import com.simon.tea.Processor;
import com.simon.tea.annotation.Module;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午5:43
 */
@Module(name = "common")
public class CommonAnalyse implements Processor{

    @Override
    public boolean mathMode(String input) {
        return false;
    }

    @Override
    public void process() {

    }
}
