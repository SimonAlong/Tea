package com.simon.cloud.core.processor;

import com.simon.cloud.core.Print;
import com.simon.cloud.core.annotation.Cmd;
import com.simon.cloud.core.annotation.Module;
import com.simon.cloud.core.context.Context;

/**
 * @author zhouzhenyong
 * @since 2018/6/26 下午5:52
 */
@Module(name = "log")
public class Log {

    @Cmd(value = "find", describe = "查找对应的数据")
    public void find(Context context) {
        Print.show("识别命令：find");
    }
}
