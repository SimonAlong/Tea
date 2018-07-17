package com.simon.tea.util;

import com.simon.tea.annotation.Cmd;
import com.simon.tea.processor.Db;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.util.ClassUtils;

import static com.simon.tea.Print.*;

/**
 * @author zhouzhenyong
 * @since 2018/6/26 下午2:21
 */
@UtilityClass
public class ClassUtil {

    /**
     * 读取指定包中的类
     *
     * @param packagePath 包路径
     * @return  包路径下所有的class类
     */
    public Set<Class<?>> readClsFromPath(String packagePath){
        String filePath = classPath() + packagePath;
        File file = new File(filePath);
        Set<String> clsNames = new LinkedHashSet<>();

        if(file.isDirectory()){
            Arrays.stream(file.listFiles()).forEach(clsFile->{
                String clsName = clsFile.getName();
                //去掉后面的.class
                clsNames.add(ClassUtils.convertResourcePathToClassName(packagePath + "/" + clsName.substring(0, clsName.length() - 6)));
            });
        }

        if(clsNames.isEmpty()){
            showError("class 加载失败");
        }

        return clsNames.stream().map(cls-> {
            try {
                return Class.forName(cls);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toSet());
    }

    public String classPath(){
        URL url = ClassLoader.getSystemResource("");
        return (null == url ? "" : url.getPath());
    }

    /**
     * 返回basePackage 包下面的所有的添加注解annotationCls的类
     * @param basePackage       要读取的基本包
     * @param annotationCls     要解释的注解类型
     * @return                  所有添加对应注解的类
     */
    public Set<Class<?>> getAnnotation(String basePackage, Class<?> annotationCls){
        PackageScanner ps = new PackageScanner(basePackage);
        List<String> classStr = ps.scan();
        Set<Class<?>> classes = new HashSet<>();
        if(!classStr.isEmpty()){
            for (String aClassStr : classStr) {
                try {
                    showLn("class.forname: "+aClassStr);
                    Class cls = ps.loadClass(aClassStr);
//                    Class cls = Class.forName(aClassStr);
                    showLn(cls.getSimpleName());
                    if (cls.isAnnotationPresent(annotationCls)) {
                        classes.add(cls);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }


}
