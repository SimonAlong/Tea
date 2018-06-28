package com.simon.tea.processor;

import com.simon.tea.Print;
import com.simon.tea.annotation.Cmd;
import com.simon.tea.context.Context;
import com.simon.tea.annotation.Module;
import com.simon.tea.meta.CmdEntity;
import com.simon.tea.util.FileUtil;
import com.simon.tea.util.StringUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import me.zzp.am.Record;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import static com.simon.tea.Constant.BASE_CATALOG;
import static com.simon.tea.Constant.SYS_CMD;
import static com.simon.tea.Print.*;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午5:43
 */
@Module(name = SYS_CMD)
public class SystemProcessor {

    @Cmd(value = "tea", describe = "系统命令：tea db或者log或者其他等模块，这样直接进入对应的模块")
    public void tea(Context context) {
        showLn("识别命令：tea");
    }

    @Cmd(value = "ll", alias = "ls", describe = "查看当前模块下的文件")
    public void ll(Context context) {
        context.getCfgList().forEach(Print::showSpace);
        showLn("");
    }

    @Cmd(value = "cd", describe = "进入对应的模块：cd db或者log或者其他")
    public void cd(Context context) {
        String module = context.secondWord();
        if (StringUtils.hasText(module)) {
            if (context.isCfg(module)) {
                context.addCatalog(module);
                context.load();
            } else {
                showError("配置：" + module + " 不存在");
            }
        }
    }

    @Cmd(value = "quit", describe = "返回到上一层")
    public void quit(Context context) {
        if(!context.getCatalog().equals(BASE_CATALOG)){
            context.unload();
            context.catalogQuit();
        }else{
            showError("已经处于最顶层");
        }
    }

    @Cmd(value = "his", describe = "显示最近使用的12条命令")
    public void his(Context context) {
        showLn("识别命令：his");
    }

    @Cmd(value = "exit", describe = "退出系统")
    public void exit(Context context) {
        context.setStop(true);
    }

    @Cmd(value = "help", describe = "用于显示当前命令的用法")
    public void help(Context context) {
        List<Map<String, Object>> cmdMap = context.getCmdHandlerMap().values().stream()
            .map(cmdHandler -> Record.from(CmdEntity.build(cmdHandler.getCmd())))
            .collect(Collectors.toList());
//        showTable(generateMapList(89), 9);    //测试数据用
    }

    @Cmd(value = "version", describe = "版本号")
    public void version(Context context){
        try {
            Properties properties = new Properties();
            properties.load(FileUtil.readFile(new File(
                StringUtil.backLast(ClassLoader.getSystemResource("").getPath()) + "/maven-archiver/pom.properties")));
            showLn(properties.get("version"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> generateMapList(Integer num){
        List dataList = new ArrayList(num);
        for(int i=0;i<num;i++){
            dataList.add(generateMap(num));
        }
        return dataList;
    }

    public Map<String, Object> generateMap(Integer cnt){
        Map<String, Object> data = new HashMap();
        String str = "动媕娿你哦安定";
//        String str = "sduowensdjofajdonfwejmdfolaujdoijalkejrowieksddfkujaowpieur";
        data.put("value", str.substring(RandomUtils.nextInt(0, str.length())));
        data.put("alias", str.substring(RandomUtils.nextInt(0, str.length())));
        data.put("describe", str.substring(RandomUtils.nextInt(0, str.length())));
        return data;
    }
}
