package com.simon.tea.util;

import com.simon.tea.processor.Db;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.util.ClassUtils;

/**
 * @author zhouzhenyong
 * @since 2018/6/26 下午2:21
 */
@UtilityClass
public class ClassUtil {

    /**
     * 读取指定包中的类
     *
     * @param packagePath
     * @return
     */
    public Set<Class<?>> readClsFromPath(String packagePath){
        String filePath = ClassLoader.getSystemResource("").getPath() + packagePath;
        File file = new File(filePath);
        Set<String> clsNames = new LinkedHashSet<>();
        if(file.isDirectory()){
            Arrays.stream(file.listFiles()).forEach(clsFile->{
                String clsName = clsFile.getName();
                //去掉后面的.class
                clsNames.add(ClassUtils.convertResourcePathToClassName(packagePath + "/" + clsName.substring(0, clsName.length() - 6)));
            });
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
}
