package com.simon.tea.processor;

import com.simon.tea.Context;
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
    List<String> commonCmdList = Arrays.asList("tea", "ll", "cd", "quit", "his", "exit");

    @Override
    public boolean isCmd(Context context) {
        return commonCmdList.stream().anyMatch(cmd -> context.getInput().startsWith(cmd));
    }

    @Override
    public void process(Context context) {
        String input = context.getInput();
        showLn("common 处理：input = ：" + input + " catalog = "+context.getCatalog());
        if(input.equals("exit")){
            context.setStop(true);
        }
    }
}
