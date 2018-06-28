package com.simon.tea.context;

import static com.simon.tea.Constant.BASE_CATALOG;
import static com.simon.tea.Constant.MODULE_PATH;

import com.simon.tea.AnalyseManager;
import com.simon.tea.CmdHandler;
import com.simon.tea.util.StringUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.cache.annotation.Cacheable;
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
    //key 是当前拥有的命令
    private Map<String, CmdHandler> cmdHandlerMap = new HashMap<>();
    private AnalyseManager manager;

    public boolean isCfg(String module) {
        return manager.isCfg(module);
    }

    /**
     * 获取当前目录下的列表
     *
     * @return 配置文件名列表
     */
    public List<String> getCfgList() {
        return manager.getCfgList();
    }

    public List<String> getCfgList(String catalog) {
        return manager.getCfgList(catalog);
    }

    public void load() {
        manager.loadCmd();
    }

    public void unload() {
        manager.unloadCmd();
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
        return manager.getFilePath(fileName);
    }

    public void loadNewFile(String fileName) {
        manager.addNewCfg(fileName);
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
