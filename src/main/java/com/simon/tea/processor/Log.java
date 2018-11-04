package com.simon.tea.processor;

import static com.simon.tea.Print.*;

import com.simon.tea.annotation.Cmd;
import com.simon.tea.annotation.Module;
import com.simon.tea.context.Context;

/**
 * @author zhouzhenyong
 * @since 2018/6/26 下午5:52
 */
@Module(name = "log")
public class Log {

    @Cmd(value = "find", describe = "查找对应的数据")
    public void find(Context context) {
        show("识别命令：find");
    }
}
