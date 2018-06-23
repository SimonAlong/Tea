package com.simon.tea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author zhouzhenyong
 * @since 2018/6/23 下午4:29
 */
public class PlatformMain {
    public static void main(String[] args) throws IOException {
        String str="";
        while(!str.equals("exit") && !str.equals("quit")){
            System.out.print("workPlat> ");
            InputStreamReader is_reader = new InputStreamReader(System.in);

            // 通常使用 BufferedReader 来读取 InputStream 中的字符串内容。
            // BufferedReader 可以一次读取一行。
            str = new BufferedReader(is_reader).readLine();

            // 将读取的字符串输出到屏幕上。
            System.out.println("您输入的是：" + str);
        }
        System.out.println("end");
    }
}
