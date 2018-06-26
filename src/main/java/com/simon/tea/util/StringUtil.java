package com.simon.tea.util;

import lombok.experimental.UtilityClass;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author zhouzhenyong
 * @since 2018/6/27 上午1:44
 */
@UtilityClass
public class StringUtil {

    @Cacheable
    public String firstWord(String input){
        return input.split(" ")[0];
    }

    @Cacheable
    public String secondWord(String input){
        return input.split(" ")[1];
    }
}
