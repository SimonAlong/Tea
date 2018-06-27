package com.simon.tea;

import static com.simon.tea.Constant.SYS_CMD;
import static com.simon.tea.Print.*;

import com.simon.tea.annotation.Module;
import com.simon.tea.context.Context;
import com.simon.tea.meta.CfgPath;
import com.simon.tea.util.FileUtil;
import com.simon.tea.util.MapUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
    //key 是目录， value的key 是具体的命令如：db/log
    private Map<String, Map<String, CmdHandler>> moduleCmdMap = new HashMap<>();
    //key 是目录，value是对应目录下的配置集合
    private Map<String, List<CfgPath>> configMap = new HashMap<>();

    void analyse() {
        Optional.ofNullable(context.getCmdHandlerMap().get(context.firstWord())).map(h -> {
            h.handle(context);
            return "";
        }).orElseGet(() -> {
            showCmdError(context.getInput());
            return null;
        });
    }

    public void loadCmd() {
        context.getCmdHandlerMap().putAll(moduleCmdMap.get(context.getCatalog()));
    }

    public void unloadCmd() {
        MapUtil.removeAll(context.getCmdHandlerMap(), moduleCmdMap.get(context.getCatalog()));
    }

    public List<String> getCfgList() {
        return configMap.get(context.getCatalog()).stream().map(CfgPath::getName).collect(Collectors.toList());
    }

    public boolean isCfg(String module) {
        return getCfgList().contains(module);
    }

    void addModule(Module module, Map<String, CmdHandler> cmdMap) {
        if (module.name().equals(SYS_CMD)) {
            moduleCmdMap.putIfAbsent(module.name(), cmdMap);
            loadCmd();
        } else {
            moduleCmdMap.putIfAbsent(context.appendCatalog(module.name()), cmdMap);
            loadSysCfg(module.name());
            loadModuleCfg(module.name());
        }
    }

    /**
     * 装载系统配置
     *
     * @param moduleName 模块名字
     */
    private void loadSysCfg(String moduleName) {
        configMap.compute(SYS_CMD, (key, value) -> {
            if (value == null) {
                List<CfgPath> list = new ArrayList<>();
                list.add(CfgPath.builder().name(moduleName).build());
                return list;
            } else {
                value.add(CfgPath.builder().name(moduleName).build());
            }
            return value;
        });
    }

    /**
     * 装载模块配置
     *
     * @param moduleName 模块名字
     */
    private void loadModuleCfg(String moduleName) {
        try {
            //创建每个模块对应的文件夹方便存储配置数据
            if (!FileUtil.fileExist(Constant.MODULE_PATH + moduleName + "/.")) {
                FileUtil.createFile(Constant.MODULE_PATH + moduleName + "/.");
            } else {
                List<String> cfgList = FileUtil.readFromDir(Constant.MODULE_PATH + moduleName);
                String moduleCatalog = context.appendCatalog(moduleName);
                configMap.compute(moduleCatalog, (key, value) -> {
                    if (null == value) {
                        return cfgList.stream()
                            .map(cfg -> CfgPath.builder().name(cfg).path(Constant.MODULE_PATH + moduleName + cfg).build())
                            .collect(Collectors.toList());
                    } else {
                        showError("模块" + key + "已经存在");
                        return Collections.emptyList();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
