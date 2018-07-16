package com.simon.tea.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    PackageScanner(String basePackage) {
        this.basePackage = basePackage;
        this.cl = getClass().getClassLoader();
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

    /**
     * Actually perform the scanning procedure.
     *
     * @param nameList A list to contain the result.
     * @return A list of fully qualified names.
     */
    private List<String> doScan(String basePackage, List<String> nameList) throws IOException {
        // replace dots with splashes
        String splashPath = StringUtil.dotToSplash(basePackage);

        // get file path
        URL url = cl.getResource(splashPath);
        String filePath = StringUtil.getRootPath(url);

        // Get classes in that package.
        // If the web server unzips the jar file, then the classes will exist in the form of
        // normal file in the directory.
        // If the web server does not unzip the jar file, then classes will exist in jar file.
        List<String> names = null; // contains the name of the class file. e.g., Apple.class will be stored as "Apple"
        if (isJarFile(filePath)) {
            // jar file
            showLn("{} 是一个JAR包" + filePath);

            names = readFromJarFile(filePath, splashPath);
        } else {
            // directory
            showLn("{} 是一个目录" + filePath);

            names = readFromDirectory(filePath);
        }

        for (String name : names) {
            if (isClassFile(name)) {
                //nameList.add(basePackage + "." + StringUtil.trimExtension(name));
                nameList.add(toFullyQualifiedName(name, basePackage));
            } else {
                // this is a directory
                // check this directory for more classes
                // do recursive invocation
                doScan(basePackage + "." + name, nameList);
            }
        }

        return nameList;
    }

    /**
     * Convert short class name to fully qualified name. e.g., String -> java.lang.String
     */
    private String toFullyQualifiedName(String shortName, String basePackage) {
        StringBuilder sb = new StringBuilder(basePackage);
        sb.append('.');
        sb.append(StringUtil.trimExtension(shortName));

        return sb.toString();
    }

    private List<String> readFromJarFile(String jarPath, String splashedPackageName) throws IOException {
        showLn("从JAR包中读取类: {}" + jarPath);

        JarInputStream jarIn = new JarInputStream(new FileInputStream(jarPath));
        JarEntry entry = jarIn.getNextJarEntry();

        List<String> nameList = new ArrayList<>();
        while (null != entry) {
            String name = entry.getName();
            if (name.startsWith(splashedPackageName) && isClassFile(name)) {
                showLn("jar - splashedPackageName = "+splashedPackageName);
                showLn("jar - name = "+name);
                nameList.add(name);
            }

            entry = jarIn.getNextJarEntry();
        }

        return nameList;
    }

    private List<String> readFromDirectory(String path) {
        File file = new File(path);
        String[] names = file.list();

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
