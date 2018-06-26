package com.simon.tea;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 解析器
 *
 * @author zhouzhenyong
 * @since 2018/6/25 下午3:39
 */
@Builder
public class Parser implements BeanPostProcessor{
    private Context context = new Context();
    public AnalyseManager analyseManager = AnalyseManager.of(this);
    private List<CmdAnalyse> processors = new ArrayList<>();

    /**
     * 执行
     * @return
     */
    public void process(String input){
//        processors.stream().filter(processor -> processor.math(input)).forEach(processor->processor.process());
    }

    public void analyse(String input){
//        analyseManager;
        //todo
    }







    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
