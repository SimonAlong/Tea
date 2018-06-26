package com.simon.tea.processor;

import com.simon.tea.Processor;
import com.simon.tea.annotation.Module;
import java.util.Arrays;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import static com.simon.tea.Print.*;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午5:43
 */
@Module(name = "common")
public class CommonProcessor implements Processor {
    List<String> commonCmdList = Arrays.asList("tea","ll", "cd", "quit", "his");

    @Override
    public boolean mathMode(String input) {
        return true;
    }

    @Override
    public void process() {
        show("公共处理参数："+local.get());
    }

    @Override
    public boolean isModuleCmd(String input) {
        return commonCmdList.stream().anyMatch(cmd->input.startsWith(cmd));
    }
}
