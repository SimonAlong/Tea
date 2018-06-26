package com.simon.tea.processor;

import com.simon.tea.annotation.Cmd;
import com.simon.tea.context.Context;
import com.simon.tea.annotation.Module;

import static com.simon.tea.Constant.SYS_CMD;
import static com.simon.tea.Print.*;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午5:43
 */
@Module(name = SYS_CMD)
public class SystemProcessor{
    @Cmd("tea")
    public void tea(Context context){
        showLn("识别命令：tea");
    }

    @Cmd("ll")
    public void ll(Context context){
        showLn("识别命令：ll");
        context.getCfgList().forEach(cfg->{
            showSpace(cfg);
        });
        showLn("");
    }

    @Cmd("cd")
    public void cd(Context context){
        showLn("识别命令：cd");
    }

    @Cmd("quit")
    public void quit(Context context){
        showLn("识别命令：quit");
    }

    @Cmd("his")
    public void his(Context context){
        showLn("识别命令：his");
    }

    @Cmd("exit")
    public void exit(Context context){
        context.setStop(true);
    }
}
