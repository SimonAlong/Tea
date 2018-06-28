package com.simon.tea;

import com.simon.tea.annotation.Cmd;
import com.simon.tea.context.Context;
import com.simon.tea.meta.CmdEntity;
import java.lang.reflect.Method;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhouzhenyong
 * @since 2018/6/27 上午12:28
 */
@Getter
@Setter
@Builder
public class CmdHandler {
    private CmdEntity cmdEntity;
    private Method handler;
    private Object obj;

    public void handle(Context context){
        try {
            handler.invoke(obj, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
