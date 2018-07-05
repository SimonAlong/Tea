package com.simon.tea;

import com.simon.tea.annotation.Cmd;
import com.simon.tea.annotation.Module;
import com.simon.tea.context.Context;
import com.simon.tea.meta.CmdEntity;
import com.simon.tea.processor.SystemProcessor;
import com.simon.tea.util.ClassUtil;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.experimental.Accessors;
import lombok.val;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import static com.simon.tea.Print.*;

/**
 * 解析调度器
 *
 * @author zhouzhenyong
 * @since 2018/6/25 下午3:39
 */
@Accessors(chain = true)
public class Parser {

    private CfgManager cfgManager;

    public void process() {
        cfgManager.analyse();
    }

    Parser(Context context) {
        init(context);
    }

    private void init(Context context) {
        initManager(context);
        initCmdMap();
    }

    private void initManager(Context context) {
        cfgManager = CfgManager.of(context);
        context.setCfgManager(cfgManager);
        context.setDbManager(DBManager.of(context));
    }

    private void initCmdMap() {
        Set<Class<?>> classes = ClassUtil.readClsFromPath(ClassUtils.classPackageAsResourcePath(SystemProcessor.class));
        classes.forEach(cls -> {
            Module module = cls.getAnnotation(Module.class);
            cfgManager.addModule(module, parseCls(module, cls));
        });
    }

    private Map<String, CmdHandler> parseCls(Module module, Class<?> cls) {
        Map<String, CmdHandler> cmdMap = new HashMap<>();
        Method[] methods = cls.getMethods();
        Object clsObj = null;
        try {
            clsObj = cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object finalClsObj = clsObj;
        Arrays.stream(methods).forEach(method -> {
            Cmd cmd = method.getAnnotation(Cmd.class);
            Optional.ofNullable(cmd).map(c -> {
                val handler = CmdHandler.builder().cmdEntity(CmdEntity.build(cmd)).handler(method).obj(finalClsObj).build();
                cmdMap.putIfAbsent(c.value(), handler);
                if(StringUtils.hasText(c.alias()) && !c.alias().contains(" ")){
                    cmdMap.putIfAbsent(c.alias(), handler);
                }
                return "";
            }).orElseGet(() -> {
                String preRunMethod = module.cmdPreRun();
                if(!StringUtils.isEmpty(preRunMethod)){
                    return Arrays.stream(methods).filter(m -> m.getName().equals(preRunMethod))
                        .findFirst().map(m -> {
                            cmdMap.putIfAbsent(preRunMethod, CmdHandler.builder()
                                .handler(m).obj(finalClsObj).build());
                            return "";
                        }).orElseGet(() -> {
                            showError("没有找到命令前置执行函数：" + preRunMethod);
                            return null;
                        });
                }
                return "";
            });
        });
        return cmdMap;
    }
}
