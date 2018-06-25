package com.simon.tea;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午3:39
 */
@Builder
public class Control implements BeanPostProcessor{
    @Getter
    public String catalog = "tea";
    private List<Processor> processors = new ArrayList<>();

    /**
     * 执行
     * @return
     */
    public void process(String input){
        processors.stream().filter(processor -> processor.math(input)).forEach(processor->processor.process());
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
