package com.simon.tea.util;

import com.simon.tea.Constant;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

/**
 * @author zhouzhenyong
 * @since 2018/6/27 下午1:52
 */
@UtilityClass
public class FileUtil {
    private File getFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        file.setReadable(true);
        file.setWritable(true);
        return file;
    }

    public void createFile(String fileName) throws IOException {
        File file = new File(fileName);
        file.getParentFile().mkdirs();
        file.createNewFile();
        file.setReadable(true);
        file.setWritable(true);
    }

    public boolean rename(String oldFileName, String newFileName){
        return new File(oldFileName).renameTo(new File(newFileName));
    }

    public boolean fileExist(String fileName) throws IOException {
        File file = new File(fileName);
        return file.getParentFile().exists() && file.exists();
    }

    public boolean del(String fileName){
        return new File(fileName).delete();
    }

    public void writeToFile(String fileName, String content) throws IOException {
        File file = getFile(fileName);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(content.getBytes(Constant.BASE_CHARSET));
            outputStream.flush();
            outputStream.close();
        }
    }

    public BufferedReader readFile(File file) throws IOException {
        return Files.newBufferedReader(file.toPath(), Constant.BASE_CHARSET);
    }

    public String readFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedReader publicReader = readFile(file);
        StringBuilder sb = new StringBuilder();
        String temp;
        while ((temp = publicReader.readLine()) != null) {
            sb.append(temp).append("\n");
        }
        publicReader.close();
        return sb.toString();
    }

    /**
     * 从配置文件目录中读取对应的配置文件列表
     * @param dirPath 配置文件的文件夹目录
     * @return 配置文件中的配置文件列表
     */
    public List<String> readListFromPath(String dirPath){
        File file = new File(dirPath);
        if(file.isDirectory()){
            File[] files = file.listFiles();
            assert files != null;
            return Arrays.stream(files).map(File::getName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
