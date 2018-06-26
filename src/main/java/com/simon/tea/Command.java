package com.simon.tea;

import java.util.Arrays;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午9:08
 */
public interface Command {
//    List<String> commonCmd = Arrays.asList("tea","ll", "cd", "quit", "his");
//
//    /**
//     * 判断当前输入是否是命令
//     * @param context
//     * @return
//     */
//    @Cacheable
//    default boolean isCmd(Context context) {
//        if(!commonCmd.stream().anyMatch(cmd->context.getInput().startsWith(cmd))){
//            return isModuleCmd(context);
//        }
//        return true;
//    }
//
//    /**
//     * 各个模块自己的命令匹配
//     * @param context
//     * @return
//     */
//    boolean isModuleCmd(Context context);

    boolean isCmd(Context context);
}
