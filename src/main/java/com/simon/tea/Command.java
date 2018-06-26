package com.simon.tea;

import com.simon.tea.context.Context;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午9:08
 */
public interface Command {
    boolean isCmd(Context context);
}
