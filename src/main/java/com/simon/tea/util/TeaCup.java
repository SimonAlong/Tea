package com.simon.tea.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import static com.simon.tea.Print.*;

/**
 * @author zhouzhenyong
 * @since 2018/7/17 下午3:48
 */
public class TeaCup {

    /**
     * URLClassLoader的addURL方法
     */
    private static Method addURL = initAddMethod();

    private static URLClassLoader classloader = (URLClassLoader) ClassLoader.getSystemClassLoader();

    public static Class<?> loadClass(String clsName) throws ClassNotFoundException {
        return classloader.loadClass(clsName);
    }
    /**
     * 初始化addUrl 方法.
     *
     * @return 可访问addUrl方法的Method对象
     */
    private static Method initAddMethod() {
        try {
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
            add.setAccessible(true);
            return add;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载jar classpath。
     */
    public static void loadClasspath() {
        List<String> files = getJarFiles();
        for (String f : files) {
            loadClasspath(f);
        }
//
//        List<String> resFiles = getResFiles();
//
//        for (String r : resFiles) {
//            loadResourceDir(r);
//        }
    }

    private static void loadClasspath(String filepath) {
        File file = new File(filepath);
        loopFiles(file);
    }

    private static void loadResourceDir(String filepath) {
        File file = new File(filepath);
        loopDirs(file);
    }

    /**
     * 循环遍历目录，找出所有的资源路径。
     *
     * @param file 当前遍历文件
     */
    private static void loopDirs(File file) {
        // 资源文件只加载路径
        if (file.isDirectory()) {
            addURL(file);
            File[] tmps = file.listFiles();
            for (File tmp : tmps) {
                loopDirs(tmp);
            }
        }
    }

    /**
     * 循环遍历目录，找出所有的jar包。
     *
     * @param file 当前遍历文件
     */
    private static void loopFiles(File file) {
        if (file.isDirectory()) {
            File[] tmps = file.listFiles();
            for (File tmp : tmps) {
                loopFiles(tmp);
            }
        } else {
            if (file.getAbsolutePath().endsWith(".jar") || file.getAbsolutePath().endsWith(".zip")) {
                addURL(file);
            }
        }
    }

    /**
     * 通过filepath加载文件到classpath。
     *
     * @return URL
     * @throws Exception 异常
     */
    private static void addURL(File file) {
        try {
            addURL.invoke(classloader, new Object[]{file.toURI().toURL()});
        } catch (Exception e) {
        }
    }

    /**
     * 从配置文件中得到配置的需要加载到classpath里的路径集合。
     */
    private static List<String> getJarFiles() {
        // TODO 从properties文件中读取配置信息略
        return Arrays.asList("/Users/zhouzhenyong/project/private/tea/target/");
    }

    /**
     * 从配置文件中得到配置的需要加载classpath里的资源路径集合
     */
    private static List<String> getResFiles() {
        //TODO 从properties文件中读取配置信息略
        return null;
    }

    public static void main(String[] args) {
//        TeaCup.loadClasspath();
//
//        try {
//            show(TeaCup.loadClass("com.simon.tea.context").getName());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        //创建类加载器
        try {

        File file =new File("/Users/zhouzhenyong/project/private/tea/target/active-map-2.3.5.jar");//jar包的路径
        URL url = file.toURI().toURL();
        ClassLoader loader=new URLClassLoader(new URL[]{url});

            loader=new URLClassLoader(new URL[]{url});//创建类加载器

            Class<?> cls= null;//加载指定类，注意一定要带上类的包名
                cls = loader.loadClass("me.zzp.am.Record");

            System.out.println(cls.getSimpleName());//

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
