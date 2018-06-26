package com.simon.tea.processor;

import com.simon.tea.context.Context;
import com.simon.tea.Processor;
import com.simon.tea.annotation.Module;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.simon.tea.Constant.SYS_CMD;
import static com.simon.tea.Print.*;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午5:43
 */
@Module(name = SYS_CMD)
public class SystemProcessor implements Processor {
    Context context;
    List<String> commonCmdList = Arrays.asList("tea", "ll", "cd", "quit", "his", "exit");

    @Override
    public boolean isCmd(Context context) {
        return commonCmdList.stream().anyMatch(cmd -> readFirstWord(context.getInput()).equals(cmd));
    }

    @Override
    public void process(Context context) {
        this.context = context;
        String input = context.getInput();
        showLn("common 处理：input = ：" + input + " catalog = "+context.getCatalog());
        switch (input){
//            case "tea": tea();break;
            case "exit": exit();break;
            case "ll": ll();break;
//            case "cd": cd();break;
//            case "quit": quit();break;
//            case "his": his();break;
        }
        if(input.equals("exit")){
            context.setStop(true);        }
    }

    private void exit(){
        context.setStop(true);
    }

    private void ll(){
//        context.getCmdNames().forEach();
    }
    private String readFirstWord(String input){
        return input.split(" ")[0];
    }
}
