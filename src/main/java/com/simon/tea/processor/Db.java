package com.simon.tea.processor;

import com.simon.tea.Command;
import com.simon.tea.Processor;
import com.simon.tea.annotation.Module;
import org.springframework.stereotype.Component;

import static com.simon.tea.Print.*;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午4:21
 */
@Module(name = "db")
@Component
public class Db implements Processor {

    @Override
    public boolean mathMode(String input) {
        return true;
    }

    @Override
    public void process() {
        showBlue("db 接收到并进行处理了：" + local.get());
    }

    @Override
    public boolean isModuleCmd(String input) {
        return false;
    }
}
