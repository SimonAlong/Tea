package com.simon.tea.processor;

import com.simon.tea.context.Context;
import com.simon.tea.Processor;
import com.simon.tea.annotation.Module;

import static com.simon.tea.Print.*;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午4:21
 */
@Module(name = "db")
public class Db implements Processor {

    @Override
    public void process(Context context) {
        showBlueLn("db 处理：input = "+context.getInput()+", 目录："+context.getCatalog());
    }

    @Override
    public boolean isCmd(Context context) {
        if(context.getInput().startsWith("select")){
            return true;
        }
        return false;
    }
}
