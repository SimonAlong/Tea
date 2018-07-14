package com.simon.tea.meta;

/**
 * @author zhouzhenyong
 * @since 2018/7/14 下午5:04
 */
public enum CmdTypeEnum {
    NORMAL("普通命令"),     // 普通的命令
    ACTIVITY("激活命令");   // 用于激活其他禁用的命令

    private String name;

    CmdTypeEnum(String name){
        this.name = name;
    }
}
