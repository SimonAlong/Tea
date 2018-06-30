package com.simon.tea.context;

import static com.simon.tea.Constant.BASE_CATALOG;
import static com.simon.tea.Constant.MODULE_PATH;
import static com.simon.tea.Constant.SYS_MODULE;

import com.simon.tea.CfgManager;
import com.simon.tea.CmdHandler;
import com.simon.tea.DBManager;
import com.simon.tea.util.StringUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午10:50
 */
@Data
public class Context {

    private String showCatalog = BASE_CATALOG;
    private String currentCatalog = BASE_CATALOG;
    private String currentModule = BASE_CATALOG;
    private String currentPath = MODULE_PATH;
    private String input = "";
    private Boolean stop = false;
    private long startTime;
    //key 是当前拥有的命令
    private Map<String, CmdHandler> cmdHandlerMap = new HashMap<>();
    private CfgManager cfgManager;
    private DBManager dbManager;

    public boolean isCfg(String module) {
        return cfgManager.isCfg(module);
    }

    /**
     * 获取当前目录下的列表
     *
     * @return 配置文件名列表
     */
    public List<String> getCfgList() {
        return cfgManager.getCfgList();
    }

    public List<String> getCfgList(String catalog) {
        return cfgManager.getCfgList(catalog);
    }

    public void load() {
        cfgManager.loadCmd();
    }

    public void unload() {
        cfgManager.unloadCmd();
    }

    public void addCatalog(String module) {
        currentCatalog += "/" + module;
        showCatalog = currentCatalog;
    }

    public void addShowCatalog(String configName) {
        showCatalog = currentCatalog;
        showCatalog += ":" + configName;
    }

    public String appendCatalog(String module) {
        if(!module.equals(SYS_MODULE)){
            return currentCatalog + "/" + module;
        }
        return currentCatalog;
    }

    public String appendPath(String path) {
        return currentPath + "/" + path;
    }

    public String getFilePath(String fileName) {
        return cfgManager.getFilePath(fileName);
    }

    public void loadNewFile(String fileName) {
        cfgManager.addNewCfg(fileName);
    }

    public void setTakeTime(){
        startTime = System.currentTimeMillis();
    }

    public long getTakeTime(){
        return System.currentTimeMillis() - startTime;
    }

    /**
     * 根据当前目录返回文件的绝对文件名
     * @param fileName 文件名
     * @return  绝对路径
     */
    public String getAbsoluteFile(String fileName){
        return currentPath + "/" + fileName;
    }

    /**
     * 返回上一层目录
     */
    public void catalogQuit() {
        currentCatalog = StringUtil.backLast(currentCatalog);
        currentPath = StringUtil.backLast(currentPath);
    }

    public String firstWord() {
        if (StringUtils.hasText(input)) {
            return input.split(" ")[0];
        }
        return null;
    }

    public String secondWord() {
        if (StringUtils.hasText(input)) {
            String[] words = input.split(" ");
            if (words.length >= 2) {
                return input.split(" ")[1];
            }
        }
        return null;
    }
}
