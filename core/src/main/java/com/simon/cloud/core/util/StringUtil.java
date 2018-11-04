package com.simon.cloud.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

/**
 * @author zhouzhenyong
 * @since 2018/6/28 上午11:53
 */
@UtilityClass
public class StringUtil {

    /**
     * 将路径：aaa/bbb/cccc 去除后面的ccc，也就是返回到上一层
     *
     * @param inputPath 输入路径
     */
    public String backLast(String inputPath) {
        if (inputPath.contains(":")) {
            return backLast(inputPath.substring(0, inputPath.indexOf(":")));
        } else {
            int index;
            if (inputPath.endsWith("/")) {
                inputPath = inputPath.substring(0, inputPath.length() - 1);
            }
            if ((index = inputPath.lastIndexOf('/')) != -1) {
                return inputPath.substring(0, index);
            }
        }
        return inputPath;
    }

    /**
     * 计算含有中文的 String 的长度，英文一个字符长度，中文两个，由于终端界面显示有问题，这里添加一个修正因子
     */
    public int length(Object str) {
        String s = valueOf(str);
        int m = 0;
        char arr[] = s.toCharArray();
        for (char c : arr) {
            if ((c >= 0x0391 && c <= 0xFFE5)) {
                m = m + 2;
            } else if (c <= 0x00FF) {
                m = m + 1;
            }
        }
        return m;
    }

    public String valueOf(Object input) {
        if (null == input) {
            return "";
        }
        return String.valueOf(input);
    }

    public String arrayToString(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String str : args) {
            sb.append(str).append(" ");
        }
        return sb.toString();
    }

    /**
     * 全是数字
     */
    public static boolean allNumber(String content) {
        String regex = "^[0-9]*$";
        Matcher m = Pattern.compile(regex).matcher(content);
        return m.matches();
    }

    /**
     * 将空格替换为下划线
     */
    public String spaceSwp(String str) {
        return str.replaceAll(" ", "_");
    }
}
