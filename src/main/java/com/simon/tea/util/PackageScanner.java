package com.simon.tea.util;

import com.simon.tea.Print;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import lombok.experimental.UtilityClass;

import static com.simon.tea.Print.*;

/**
 * @author zhouzhenyong
 * @since 2018/7/15 下午5:31
 */
public class PackageScanner {
    private String basePackage;
    private ClassLoader cl;
    private TeaCup teaCup = TeaCup.getInstance();

    PackageScanner(String basePackage) {
        this.basePackage = basePackage;
        this.cl = getClass().getClassLoader();
        teaCup.loadPath(getRootPath(basePackage));
    }

    List<String> scan() {
        List<String> classes = new ArrayList<>();
        try {
            classes = doScan(basePackage, classes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    Class<?> loadClass(String classFullName) throws ClassNotFoundException {
        return teaCup.loadClass(classFullName);
    }

    /**
     * com.simon.tea -> "/user/zzy/foo.jar"
     */
    private String getRootPath(String basePackage){
        return getJarPath(Objects.requireNonNull(cl.getResource(basePackage.replaceAll("\\.", "/"))));
    }

    /**
     * "file:/home/whf/cn/fh" -> "/home/whf/cn/fh"
     * "jar:file:/home/whf/foo.jar!cn/fh" -> "/home/whf/foo.jar"
     */
    private String getJarPath(URL url) {
        String fileUrl = url.getFile();
        int pos = fileUrl.indexOf('!');

        if (-1 == pos) {
            return fileUrl;
        }

        return fileUrl.substring(5, pos);
    }

    /**
     * 扫描对应包下面的文件
     *
     * @param nameList 类名列表.
     */
    private List<String> doScan(String basePackage, List<String> nameList) throws IOException {
        String splashPath = basePackage.replaceAll("\\.", "/");

        List<String> names;
        String filePath = getJarPath(Objects.requireNonNull(cl.getResource(splashPath)));
        if (isJarFile(filePath)) {
            names = readFromJarFile(filePath, splashPath);
        } else {
            names = readFromDirectory(filePath);
        }

        assert names != null;
        for (String name : names) {
            if (isClassFile(name)) {
                nameList.add(toFullyQualifiedName(name, basePackage));
            } else {
                doScan(basePackage + "." + name, nameList);
            }
        }
        return nameList;
    }

    /**
     * 将类名转换到全类名，如 String -> java.lang.String
     */
    private String toFullyQualifiedName(String shortName, String basePackage) {
        return basePackage + '.' + StringUtil.trimExtension(basePackage, shortName);
    }

    /**
     * 从jar包里面读取对应的文件名列表
     * @param jarPath               jar包名路径
     */
    private List<String> readFromJarFile(String jarPath, String splashedPackageName) throws IOException {
        JarInputStream jarIn = new JarInputStream(new FileInputStream(jarPath));
        JarEntry entry = jarIn.getNextJarEntry();

        List<String> nameList = new ArrayList<>();
        while (null != entry) {
            String name = entry.getName();
            if (name.startsWith(splashedPackageName) && isClassFile(name)) {
                nameList.add(name);
            }
            entry = jarIn.getNextJarEntry();
        }
        return nameList;
    }

    private List<String> readFromDirectory(String path) {
        String[] names = new File(path).list();

        if (null == names) {
            return null;
        }
        return Arrays.asList(names);
    }

    private boolean isClassFile(String name) {
        return name.endsWith(".class");
    }

    private boolean isJarFile(String name) {
        return name.endsWith(".jar");
    }
}
