package com.simon.tea.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

import static com.simon.tea.Print.*;

/**
 * @author zhouzhenyong
 * @since 2018/6/28 下午9:09
 */
@UtilityClass
public class ShellUtil {

    public void call(String shellStr) {
        try {
            Process process = Runtime.getRuntime().exec(shellStr);
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                showError("call shell failed. error code is :" + exitValue);
            }
        } catch (Throwable e) {
            showError("call shell failed. " + e);
        }
    }

    public String callShell(String shellStr){
        Process process = null;
        List<String> processList = new ArrayList<String>();
        try {
            process = Runtime.getRuntime().exec(shellStr);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                processList.add(line);
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder stringBuilder = new StringBuilder();

        processList.forEach(str->stringBuilder.append(str).append("\n"));
        return stringBuilder.toString();
    }
}
