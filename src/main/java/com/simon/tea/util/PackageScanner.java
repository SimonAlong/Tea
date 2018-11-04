package com.simon.tea.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * 扫描包路径，返回包中的所有类名
 *
 * @author zhouzhenyong
 * @since 2018/7/15 下午5:31
 */
public class PackageScanner {

    private String basePackage;
    private TeaCup teaCup = TeaCup.getInstance();

    /**
     * @param basePackage com.simon.tea
     */
    PackageScanner(String basePackage) {
        this.basePackage = basePackage;
        teaCup.read(getRootPath(basePackage));
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

    public Class<?> loadClass(String classFullName) throws ClassNotFoundException {
        return teaCup.loadClass(classFullName);
    }

    /**
     * com.simon.tea -> "/user/zzy/foo.jar"
     */
    private String getRootPath(String basePackage) {
        return getRootPath(baseUrl(basePackage));
    }

    private URL baseUrl(String basePackage) {
        return Objects.requireNonNull(getClass().getClassLoader()
            .getResource(basePackage.replaceAll("\\.", "/")));
    }

    /**
     * "file:/home/whf/cn/fh" -> "/home/whf/cn/fh" "jar:file:/home/whf/foo.jar!cn/fh" -> "/home/whf/foo.jar"
     */
    private String getJarPath(URL url) {
        String fileUrl = url.getFile();
        int pos = fileUrl.indexOf('!');

        if (-1 == pos) {
            return fileUrl;
        }
        return fileUrl.substring(5, pos);
    }

    private String getRootPath(URL url) {
        String jarPath = getJarPath(url);
        int end = jarPath.lastIndexOf('/');
        return jarPath.substring(0, end);
    }

    /**
     * 扫描对应包下面的文件
     *
     * @param nameList 类名列表.
     */
    private List<String> doScan(String basePackage, List<String> nameList) throws IOException {
        String splashPath = basePackage.replaceAll("\\.", "/");
        List<String> names;
        String filePath = getJarPath(baseUrl(basePackage));

        if (isJarFile(filePath)) {
            names = readFromJarFile(filePath, splashPath);
        } else {
            names = readFromDirectory(filePath);
        }

        assert names !=null;
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
        return basePackage + '.' + trimExtension(basePackage, shortName);
    }

    /**
     * 从jar包里面读取对应的文件名列表
     *
     * @param jarPath jar包名路径
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

    /**
     * 返回路径中的路径数据
     *
     * @param basePackage com.simon.tea
     * @param name com/simon/tea/util/MapUtil.class
     * @return util.MapUtil
     */
    public String trimExtension(String basePackage, String name) {
        name = name.replaceAll("/", "\\.");
        int endPos = name.lastIndexOf('.');
        int startPos = basePackage.length();
        if (-1 != endPos) {
            if (name.startsWith(basePackage)) {
                return name.substring(startPos + 1, endPos);
            } else {
                return name.substring(0, endPos);
            }
        }
        return name;
    }

    private boolean isClassFile(String name) {
        return name.endsWith(".class");
    }

    private boolean isJarFile(String name) {
        return name.endsWith(".jar");
    }
}
