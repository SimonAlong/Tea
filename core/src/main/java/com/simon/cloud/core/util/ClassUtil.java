package com.simon.cloud.core.util;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.experimental.UtilityClass;

/**
 * @author zhouzhenyong
 * @since 2018/6/26 下午2:21
 */
@UtilityClass
public class ClassUtil {

    public String classPath() {
        URL url = ClassLoader.getSystemResource("");
        return (null == url ? "" : url.getPath());
    }

    /**
     * 返回basePackage 包下面的所有的添加注解annotationCls的类
     *
     * @param basePackage 要读取的基本包
     * @param annotationCls 要解释的注解类型
     * @return 所有添加对应注解的类
     */
    public Set<Class<?>> getAnnotation(String basePackage, Class<?> annotationCls) {
        PackageScanner ps = new PackageScanner(basePackage);
        List<String> classStr = ps.scan();
        Set<Class<?>> classes = new HashSet<>();
        if (!classStr.isEmpty()) {
            classStr.forEach(aClassStr -> {
                try {
                    Class cls = ps.loadClass(aClassStr);
                    if (cls.isAnnotationPresent(annotationCls)) {
                        classes.add(cls);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
        return classes;
    }
}
