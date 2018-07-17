package com.simon.tea.util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
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


    /**
     * "file:/home/whf/cn/fh" -> "/home/whf/cn/fh"
     * "jar:file:/home/whf/foo.jar!cn/fh" -> "/home/whf/foo.jar"
     */
    public String getJarPath(URL url) {
        String fileUrl = url.getFile();
        int pos = fileUrl.indexOf('!');

        if (-1 == pos) {
            return fileUrl;
        }

        return fileUrl.substring(5, pos);
    }

    /**
     * "file:/home/whf/cn/fh" -> "/home/whf/cn/fh"
     * "jar:file:/home/whf/foo.jar!cn/fh" -> "/home/whf/"
     */
    public String getRootPath(URL url) {
        String fileUrl = url.getFile();
        int endPos = fileUrl.indexOf('!');

        if (-1 == endPos) {
            return fileUrl;
        }

        return fileUrl.substring(5, endPos);
    }

    /**
     * "cn.fh.lightning" -> "cn/fh/lightning"
     */
    public String dotToSplash(String name) {
        return name.replaceAll("\\.", "/");
    }

    /**
     *  返回路径中的路径数据
     * @param basePackage   com.simon.tea
     * @param name          com/simon/tea/util/MapUtil.class
     * @return              util.MapUtil
     */
    public String trimExtension(String basePackage, String name) {
        name = name.replaceAll("/", "\\.");
        int endPos = name.lastIndexOf('.');
        int startPos = basePackage.length();
        if(-1 != endPos){
            return name.substring(startPos + 1, endPos);
        }
        return name;
    }

    /**
     * /application/home -> /home
     */
    public String trimURI(String uri) {
        String trimmed = uri.substring(1);
        int splashIndex = trimmed.indexOf('/');

        return trimmed.substring(splashIndex);
    }
}
