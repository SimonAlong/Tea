package com.simon.tea;

import java.util.Arrays;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午9:08
 */
public interface Command {
    List<String> commonCmd = Arrays.asList("tea","ll", "cd", "quit", "his");

    @Cacheable
    default boolean isCmd(String input) {
        if(!commonCmd.contains(input)){
            return isModuleCmd(input);
        }
        return true;
    }

    boolean isModuleCmd(String input);
}
