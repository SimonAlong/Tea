package com.simon.tea;

import static com.simon.tea.Constant.BASE_CATALOG;
import static com.simon.tea.Constant.SYS_CMD;
import static com.simon.tea.Print.*;

import com.simon.tea.annotation.Module;
import com.simon.tea.context.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 命令分析和执行管理器
 *
 * @author zhouzhenyong
 * @since 2018/6/25 下午10:05
 */
@RequiredArgsConstructor(staticName = "of")
public class AnalyseManager {

    @NonNull
    private Context context;
    private Map<ModuleKey, Processor> cmdAnalyseMap = new HashMap<>();

    public void analyse() {
        Optional.ofNullable(cmdAnalyseMap.get(new ModuleKey().setCatalogStr(context.getCatalog()))).map(p -> {
            if (p.isCmd(context)) {
                p.process(context);
            } else {
                showError(context.getInput());
            }
            return null;
        });
    }

    public void addCmd(Module module, Processor processor) {
        ModuleKey key;
        if (!module.name().equals(SYS_CMD)) {
            key = new ModuleKey().setCatalogStr(BASE_CATALOG + "/" + module.name());
        } else {
            key = new ModuleKey().setCatalogStr(BASE_CATALOG);
        }
        cmdAnalyseMap.putIfAbsent(key, processor);
        addModule(module);
    }

    private void addModule(Module module) {
        if (!module.name().equals(SYS_CMD)) {
            context.getCmdNames().add(module.name());
        }
    }
}
