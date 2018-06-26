package com.simon.tea;

import static com.simon.tea.Constant.BASE_CATALOG;
import static com.simon.tea.Constant.SYS_CMD;
import static com.simon.tea.Print.*;

import com.simon.tea.annotation.Module;
import com.simon.tea.context.Context;
import com.simon.tea.util.MapUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

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
    private Map<String, List<String>> configMap = new HashMap<>();

    void analyse() {
        Optional.ofNullable(context.getCmdHandlerMap().get(context.firstWord())).map(h->{
            h.handle(context);
            return "";
        }).orElseGet(()->{
            showError(context.getInput());
            return null;
        });
    }

    public void load(){
        context.getCmdHandlerMap().putAll(moduleCmdMap.get(context.getCatalog()));
    }

    public void unload(){
        MapUtil.removeAll(context.getCmdHandlerMap(), moduleCmdMap.get(context.getCatalog()));
    }

    public List<String> getCfgList(){
        return configMap.get(context.getCatalog());
    }

    void addModule(Module module, Map<String, CmdHandler> cmdMap){
        moduleCmdMap.putIfAbsent(module.name(), cmdMap);
        if(module.name().equals(SYS_CMD)){
            load();
        }else{
            //加载系统下的配置文件
            configMap.compute(SYS_CMD, (key, value)->{
                if(value == null){
                    List<String> list = new ArrayList<>();
                    list.add(module.name());
                    return list;
                }else{
                    value.add(module.name());
                }
                return value;
            });

            //其他的配置后面再根据对应的文件名进行新建文件夹和查找 todo
        }
    }
}
