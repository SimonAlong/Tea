package com.simon.tea.context;

import static com.simon.tea.Constant.BASE_CATALOG;
import static com.simon.tea.Constant.MODULE_PATH;
import static com.simon.tea.Constant.SYS_MODULE;

import com.simon.tea.CfgManager;
import com.simon.tea.CmdHandler;
import com.simon.tea.DBManager;
import com.simon.tea.annotation.Cmd;
import com.simon.tea.meta.CmdTypeEnum;
import com.simon.tea.util.StringUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.val;
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
    private String[] inputs = new String[]{};
//    private Boolean loadCfg = false;
    private Boolean stop = false;
    private long startTime;
    //key 是当前拥有的命令
    private Map<String, CmdHandler> cmdHandlerMap = new HashMap<>();
    private CfgManager cfgManager;
    private DBManager dbManager;

    public boolean isModule(String module) {
        return cfgManager.isModule(module);
    }

    public void setInput(String input){
        this.input = input;
        inputs = input.split(" ");
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
        unActive();
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
        showCatalog = StringUtil.backLast(showCatalog);
    }

    public String firstWord() {
        if (inputs.length >= 1) {
            return inputs[0];
        }
        return null;
    }

    public String secondWord() {
        if (inputs.length >= 2) {
            return inputs[1];
        }
        return null;
    }

    public String thirdWord() {
        if (inputs.length >= 3) {
            return inputs[2];
        }
        return null;
    }

    public String fourWord() {
        if (inputs.length >= 4) {
            return inputs[3];
        }
        return null;
    }

    public int getPageIndex(){
        return cfgManager.getPageIndex(input);
    }

    public void postHandle(CmdHandler handler){
        // 将禁用的命令进行激活
        if(null != handler && handler.getCmdEntity().getType().equals(CmdTypeEnum.ACTIVITY)){
            cmdHandlerMap.values().forEach(h->{
                val cmd = h.getCmdEntity();
                cmd.setOldActive(cmd.getActive());
                cmd.setActive(true);
            });
        }
    }

    // 将初始禁用的命令禁用
    public void unActive(){
        cmdHandlerMap.values().forEach(h->{
            val cmd = h.getCmdEntity();
            cmd.setActive(cmd.getOldActive());
        });
    }
}
