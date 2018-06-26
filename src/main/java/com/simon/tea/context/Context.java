package com.simon.tea.context;

import static com.simon.tea.Constant.BASE_CATALOG;

import com.simon.tea.AnalyseManager;
import com.simon.tea.CmdHandler;
import com.simon.tea.CmdKey;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午10:50
 */
@Data
public class Context {
    private String catalog = BASE_CATALOG;
    private String input = "";
    private Boolean stop = false;
    private Map<CmdKey, CmdHandler> cmdHandlerMap = new HashMap<>();
    private AnalyseManager manager;

    public boolean containCmd(String cmd){
        return cmdNames.contains(cmd);
    }

    public void enterCmd(String cmd){
        catalog = catalog + "/" + cmd;
    }

    public void load(AnalyseManager manager){
        manager.load(catalog);
    }

    public void unload(){

    }

}
