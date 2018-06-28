package com.simon.tea.util;

import java.io.UnsupportedEncodingException;
import lombok.experimental.UtilityClass;

/**
 * @author zhouzhenyong
 * @since 2018/6/28 上午11:53
 */
@UtilityClass
public class StringUtil {

    /**
     * 将路径：aaa/bbb/cccc 去除后面的ccc，也就是返回到上一层
     * @param inputPath 输入路径
     */
    public String backLast(String inputPath) {
        int index;
        if(inputPath.endsWith("/")){
            inputPath = inputPath.substring(0, inputPath.length() -1);
        }
        if ((index = inputPath.lastIndexOf('/')) != -1) {
            return inputPath.substring(0, index);
        }
        return inputPath;
    }

    /**
     * 计算含有中文的 String 的长度，英文一个字符长度，中文两个
     */
    public int length(String str){
        int m = 0;
        char arr[] = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            if ((c >= 0x0391 && c <= 0xFFE5)) {
                m = m + 2;
            } else if (c <= 0x00FF) {
                m = m + 1;
            }
        }
        return m;
    }
}
