package com.simon.tea;

import com.simon.tea.annotation.Cmd;
import com.simon.tea.context.Context;
import com.simon.tea.meta.CmdEntity;
import java.lang.reflect.Method;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import me.zzp.am.Record;

/**
 * @author zhouzhenyong
 * @since 2018/6/27 上午12:28
 */
@Getter
@Setter
@Builder
public class CmdHandler {
    private CmdEntity cmdEntity;
    private List<Record> usage;
    private Method handler;
    private Object obj;

    Object handle(Context context){
        try {
            val result = handler.invoke(obj, context);
            context.postHandle(this);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
