package com.simon.tea;

import com.simon.tea.annotation.Module;
import com.simon.tea.processor.Db;
import com.simon.tea.util.ClassUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.ClassUtils;

/**
 * 解析器
 *
 * @author zhouzhenyong
 * @since 2018/6/25 下午3:39
 */
@Accessors(chain = true)
public class Parser{
    public AnalyseManager analyseManager;

    public void process(){
        analyseManager.analyse();
    }

    public Parser(Context context){
        analyseManager = AnalyseManager.of(context);
        Set<Class<?>> classes = ClassUtil.readClsFromPath(ClassUtils.classPackageAsResourcePath(Db.class));
        classes.stream().forEach(cls->{
            if(Processor.class.isAssignableFrom(cls)){
                try {
                    analyseManager.addCmd(cls.getAnnotation(Module.class), cls.asSubclass(Processor.class).newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
