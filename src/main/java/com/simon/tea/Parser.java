package com.simon.tea;

import com.simon.tea.annotation.Cmd;
import com.simon.tea.annotation.Module;
import com.simon.tea.annotation.Usage;
import com.simon.tea.context.Context;
import com.simon.tea.meta.CmdEntity;
import com.simon.tea.processor.SystemProcessor;
import com.simon.tea.util.ClassUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.Accessors;
import lombok.val;
import me.zzp.am.Record;
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
        Set<Class<?>> classes = ClassUtil.getAnnotation("com.simon.tea", Module.class);
        classes.forEach(cls -> {
            Module module = cls.getAnnotation(Module.class);
            if (module.visible()) {
                cfgManager.addModule(module, parseCls(cls));
            }
        });
    }

    private Map<String, CmdHandler> parseCls(Class<?> cls) {
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
                val handler = CmdHandler.builder().cmdEntity(CmdEntity.build(cmd)).handler(method).obj(finalClsObj)
                    .build();
                cmdMap.putIfAbsent(c.value(), handler);
                if (StringUtils.hasText(c.alias()) && !c.alias().contains(" ")) {
                    cmdMap.putIfAbsent(c.alias(), handler);
                }
                return "";
            });

            Usage usage = method.getAnnotation(Usage.class);
            Optional.ofNullable(usage).map(use -> {
                cmdMap.compute(use.target(), (k, v) -> {
                    if (null == v) {
                        return CmdHandler.builder().usage(getUsageList(method, finalClsObj)).build();
                    } else {
                        v.setUsage(getUsageList(method, finalClsObj));
                    }
                    return v;
                });
                return "";
            });
        });
        return cmdMap;
    }

    private List<Record> getUsageList(Method method, Object object) {
        try {
            return Record.class.cast(method.invoke(object)).entrySet().stream()
                .map(e -> Record.of("usage", e.getKey(), "detail", e.getValue()))
                .collect(Collectors.toList());
        } catch (Exception e) {
            showError("方法" + method.getName() + "的@Usage用法解析错误：" + e.getLocalizedMessage());
            return null;
        }
    }
}
