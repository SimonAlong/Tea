package com.simon.tea.context;

import static com.simon.tea.Constant.BASE_CATALOG;

import com.simon.tea.AnalyseManager;
import com.simon.tea.CmdHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午10:50
 */
@Data
public class Context {
    private String catalog = BASE_CATALOG;
    private String input = "";
    private Boolean stop = false;
    //key 是当前拥有的命令
    private Map<String, CmdHandler> cmdHandlerMap = new HashMap<>();
    private AnalyseManager manager;

    /**
     * 获取当前目录下的列表
     *
     * @return
     */
    public List<String> getCfgList(){
        return manager.getCfgList();
    }

    public void load(){
        manager.load();
    }

    public void unload(){
        manager.unload();
    }

    @Cacheable
    public String firstWord(){
        return input.split(" ")[0];
    }

    @Cacheable
    public String secondWord(){
        return input.split(" ")[1];
    }

}
