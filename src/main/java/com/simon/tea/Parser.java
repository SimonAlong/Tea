package com.simon.tea;

import com.simon.tea.annotation.Module;
import com.simon.tea.context.Context;
import com.simon.tea.processor.Db;
import com.simon.tea.processor.SystemProcessor;
import com.simon.tea.util.ClassUtil;
import java.util.Set;
import lombok.experimental.Accessors;
import org.springframework.util.ClassUtils;

/**
 * 解析调度器
 *
 * @author zhouzhenyong
 * @since 2018/6/25 下午3:39
 */
@Accessors(chain = true)
public class Parser{
    AnalyseManager analyseManager;

    public void process(){
        analyseManager.analyse();
    }

    Parser(Context context){
        init(context);
    }

    void init(Context context){
        analyseManager = AnalyseManager.of(context);
        Set<Class<?>> classes = ClassUtil.readClsFromPath(ClassUtils.classPackageAsResourcePath(SystemProcessor.class));
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
