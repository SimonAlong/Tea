package com.simon.tea.context;

import static com.simon.tea.Constant.BASE_CATALOG;

import com.simon.tea.AnalyseManager;
import com.simon.tea.CmdHandler;
import com.simon.tea.util.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
    private List<String> moduleList = new ArrayList<>(5);

    public boolean isCfg(String module){
        return manager.isCfg(module);
    }

    /**
     * 获取当前目录下的列表
     *
     * @return 配置文件名列表
     */
    public List<String> getCfgList(){
        return manager.getCfgList();
    }

    public void load(){
        manager.loadCmd();
    }

    public void unload(){
        manager.unloadCmd();
    }

    public void addCatalog(String module){
        catalog += "/"+module;
    }

    public String appendCatalog(String module){
        return catalog + "/"+module;
    }

    /**
     * 返回上一层目录
     */
    public void catalogQuit() {
        catalog = StringUtil.backLast(catalog);
    }

    @Cacheable
    public String firstWord(){
        if(StringUtils.hasText(input)) {
            return input.split(" ")[0];
        }
        return null;
    }

    @Cacheable
    public String secondWord(){
        if(StringUtils.hasText(input)) {
            String[] words = input.split(" ");
            if(words.length >= 2) {
                return input.split(" ")[1];
            }
        }
        return null;
    }
}
