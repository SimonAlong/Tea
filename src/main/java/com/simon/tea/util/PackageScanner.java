package com.simon.tea.util;

import com.simon.tea.Print;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
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
    private URLClassLoader urlClassLoader;

    PackageScanner(String basePackage) {
        this.basePackage = basePackage;
        this.cl = getClass().getClassLoader();
        showLn("");
    }

    List<String> scan() {
        List<String> classes = new ArrayList<>();
        try {
//            doScan("lib", classes);
//            scanLib("lib");
            classes = doScan(basePackage, classes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    Class<?> loadClass(String classFullName) throws ClassNotFoundException {
        return urlClassLoader.loadClass(classFullName);
//        return null;
    }

//    public void scanLib(String libPath){
//            URL url = cl.getResource("lib");
//            showLn("url = " + url.toString());
//
//
//        String filePath = StringUtil.getJarPath(url);
//
//        try {
//            readFromJarFile(filePath, "lib");
//
//
//
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }


//    public String rootPath(String basePackage){
//        String splashPath = StringUtil.dotToSplash(basePackage);
//        URL url = cl.getResource(splashPath);
//        String rootPath = StringUtil.getRootPath(url);
//
//    }

    /**
     * Actually perform the scanning procedure.
     *
     * @param nameList A list to contain the result.
     * @return A list of fully qualified names.
     */
    private List<String> doScan(String basePackage, List<String> nameList) throws IOException {
        String splashPath = StringUtil.dotToSplash(basePackage);
        String splashPath2 = StringUtil.dotToSplash(" me.zzp.am");

        showLn("jar 包的路径"+splashPath);

        URL url = cl.getResource(splashPath);
        URL url2 = cl.getResource(splashPath2);
        URL url3= new URL("jar:file:/Users/zhouzhenyong/project/private/tea/target/active-map-2.3.5.jar!/me/zzp");
        showLn("url = "+url.toString());
        showLn("url = "+url3.toString());

        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url, url3});
        this.urlClassLoader = urlClassLoader;
        try {
            showLn(urlClassLoader.loadClass("com.simon.tea.processor.Db").getSimpleName());
            showLn(urlClassLoader.loadClass("com.simon.tea.Constant").getSimpleName());
            showLn(urlClassLoader.loadClass("com.simon.tea.util.StringUtil").getSimpleName());
            showLn(urlClassLoader.loadClass("com.simon.tea.Screen").getSimpleName());
            showLn(urlClassLoader.loadClass("me.zzp.am.Record").getSimpleName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String filePath = StringUtil.getRootPath(url);

        List<String> names = null;
        if (isJarFile(filePath)) {
            // jar file
            showLn(filePath+" 是一个JAR包");

            names = readFromJarFile(filePath, splashPath);
        } else {
            // directory
            showLn(filePath+" 是一个目录");

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
        sb.append(StringUtil.trimExtension(basePackage, shortName));

        return sb.toString();
    }

    private List<String> readFromJarFile(String jarPath, String splashedPackageName) throws IOException {
        showLn("从JAR包中读取类: " + jarPath);

        JarInputStream jarIn = new JarInputStream(new FileInputStream(jarPath));
        JarEntry entry = jarIn.getNextJarEntry();

        List<String> nameList = new ArrayList<>();
        while (null != entry) {
            String name = entry.getName();
            if (name.startsWith(splashedPackageName) && isClassFile(name)) {
//                showLn("jar - splashedPackageName = "+splashedPackageName);
//                showLn("jar - name = "+name);
                nameList.add(name);
            }

            entry = jarIn.getNextJarEntry();
            if(entry.getName().contains("Record")){
                show("jarName = "+entry.getName());
            }
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
