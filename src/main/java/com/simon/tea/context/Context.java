package com.simon.tea.context;

import static com.simon.tea.Constant.BASE_CATALOG;
import static com.simon.tea.Constant.MODULE_PATH;

import com.simon.tea.CfgManager;
import com.simon.tea.CmdHandler;
import com.simon.tea.DBManager;
import com.simon.tea.util.StringUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import me.zzp.am.Am;
import org.springframework.util.StringUtils;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午10:50
 */
@Data
public class Context {

    private String currentCatalog = BASE_CATALOG;
    private String currentModule = BASE_CATALOG;
    private String currentPath = MODULE_PATH;
    private String input = "";
    private Boolean stop = false;
    private Am am = new Am();
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
    }

    public String appendCatalog(String module) {
        return currentCatalog + "/" + module;
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

    public void startDb(String jdbcUrl, String userName, String password, String fileName){
        am = null;
        am = new Am();
        am.connect(jdbcUrl, userName, password).as(fileName);
    }

    public void show(){
        dbManager.show();
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
