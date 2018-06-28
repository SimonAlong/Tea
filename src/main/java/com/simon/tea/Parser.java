package com.simon.tea;

import com.simon.tea.annotation.Cmd;
import com.simon.tea.annotation.Module;
import com.simon.tea.context.Context;
import com.simon.tea.meta.CmdEntity;
import com.simon.tea.processor.Db;
import com.simon.tea.processor.SystemProcessor;
import com.simon.tea.util.ClassUtil;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
public class Parser {

    private AnalyseManager analyseManager;

    public void process() {
        analyseManager.analyse();
    }

    Parser(Context context) {
        init(context);
    }

    private void init(Context context) {
        initManager(context);
        initCmdMap();
    }

    private void initManager(Context context) {
        analyseManager = AnalyseManager.of(context);
        context.setManager(analyseManager);
    }

    private void initCmdMap() {
        Set<Class<?>> classes = ClassUtil.readClsFromPath(ClassUtils.classPackageAsResourcePath(SystemProcessor.class));
        classes.forEach(cls -> {
            Module module = cls.getAnnotation(Module.class);
            analyseManager.addModule(module, parseCls(module, cls));
        });
    }

    private Map<String, CmdHandler> parseCls(Module module, Class<?> cls) {
        Map<String, CmdHandler> cmdMap = new HashMap<>();
        Method[] methods = cls.getMethods();
        Arrays.stream(methods).forEach(method -> {
            Cmd cmd = method.getAnnotation(Cmd.class);
            Optional.ofNullable(cmd).map(c -> {
                try {
                    cmdMap.putIfAbsent(c.value(), CmdHandler.builder().cmdEntity(CmdEntity.build(cmd).setModule(module.name()))
                        .handler(method).obj(cls.newInstance()).build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });
        });
        return cmdMap;
    }
}
