package com.simon.tea.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Objects;

/**
 * 读取外部jar包用的工具类
 *
 * @author zhouzhenyong
 * @since 2018/7/17 下午3:48
 */
public class TeaCup {

    private Method addURL;
    private URLClassLoader loader;
    private static TeaCup teaCup = new TeaCup();

    private TeaCup() {
        addURL = initAddMethod();
        loader = URLClassLoader.class.cast(ClassLoader.getSystemClassLoader());
    }

    public static TeaCup getInstance() {
        return teaCup;
    }

    /**
     * 初始化addUrl 方法.
     *
     * @return 可访问addUrl方法的Method对象
     */
    private Method initAddMethod() {
        try {
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            add.setAccessible(true);
            return add;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Class<?> loadClass(String clsName) throws ClassNotFoundException {
        return loader.loadClass(clsName);
    }

    /**
     * 加载文件下面中的所有jar
     *
     * @param jarRootPath jar包所在文件路径，如：/local/test/work/tea.jar
     */
    public void loadPath(String jarRootPath) {
        File file = new File(jarRootPath);
        readFile(file);
    }

    /**
     * 加载文件下面中的所有jar
     *
     * @param url jar包所在文件的路径
     */
    public void loadPath(URL url) {
        try {
            readFile(new File(url.toURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载文件下面中的所有jar
     *
     * @param file jar包所在文件的路径
     */
    public void loadPath(File file) {
        readFile(file);
    }

    /**
     * 加载jar包中的所有类
     *
     * @param jarPath jar包所在文件的路径组
     */
    public void loadPath(String... jarPath) {
        Arrays.stream(jarPath).map(File::new).forEach(this::addURL);
    }

    /**
     * 加载jar包中的所有类
     *
     * @param urls jar包所在文件的路径组
     */
    public void loadPath(URL... urls) {
        Arrays.stream(urls).forEach(this::addURL);
    }

    /**
     * 加载jar包中的所有类
     *
     * @param jarPath jar包所在文件的路径组
     */
    public void loadPath(File... jarPath) {
        Arrays.stream(jarPath).forEach(this::addURL);
    }

    private void addURL(File file) {
        try {
            addURL.invoke(loader, file.toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addURL(URL url) {
        try {
            addURL.invoke(loader, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param rootFile 当前目录的文件
     */
    private void readFile(File rootFile) {
        if (rootFile.isDirectory()) {
            Arrays.stream(Objects.requireNonNull(rootFile.listFiles())).forEach(this::readFile);
        } else {
            if (rootFile.getAbsolutePath().endsWith(".jar") || rootFile.getAbsolutePath().endsWith(".zip")) {
                addURL(rootFile);
            }
        }
    }
}
