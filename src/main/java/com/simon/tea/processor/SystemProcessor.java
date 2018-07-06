package com.simon.tea.processor;

import com.simon.tea.Print;
import com.simon.tea.annotation.Cmd;
import com.simon.tea.context.Context;
import com.simon.tea.annotation.Module;
import com.simon.tea.util.ClassUtil;
import com.simon.tea.util.FileUtil;
import com.simon.tea.util.MapUtil;
import com.simon.tea.util.ShellUtil;
import com.simon.tea.util.StringUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import me.zzp.am.Ao;
import me.zzp.am.Column;
import me.zzp.am.Record;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.util.StringUtils;

import static com.simon.tea.Constant.BASE_CATALOG;
import static com.simon.tea.Constant.SYS_MODULE;
import static com.simon.tea.Print.*;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午5:43
 */
@Module(name = SYS_MODULE)
public class SystemProcessor {

    @Cmd(value = "tea", describe = "系统命令：tea db或者log或者其他等模块，这样直接进入对应的模块")
    public void tea(Context context) {
        showLn("识别命令：tea");
    }

    @Cmd(value = "usage", describe = "用于展示某个命令的用法: usage show或者load")
    public void usage(Context context){
        String module = context.secondWord();
        if (StringUtils.hasText(module)) {
            Optional.ofNullable(context.getCmdHandlerMap().get(module)).map(h->{
                showTable(h.getUsage(), context.getPageIndex(), context);
                return "";
            }).orElseGet(()->{
                showError("当前模块，命令：" + module + "不存在");
               return "";
            });
        }
    }

    @Cmd(value = "config", describe = "展示系统的配置")
    public void systemConfig(Context context) {
        showLn("识别命令：config");
    }

    @Cmd(value = "set", describe = "修改系统的配置，用法：set xx=yy,mm=nn,等等")
    public void set(Context context) {
        showLn("识别命令：set");
    }

    @Cmd(value = "ll", alias = "ls", describe = "查看当前模块下的文件")
    public void ll(Context context) {
        String module = context.secondWord();
        if (StringUtils.hasText(module)) {
            context.getCfgList(context.appendCatalog(module)).forEach(Print::showSpace);
        }else{
            context.getCfgList().forEach(Print::showSpace);
        }
        showLn();
    }

    @Cmd(value = "cd", describe = "进入对应的模块：cd db或者log或者其他")
    public void cd(Context context) {
        String module = context.secondWord();
        if (StringUtils.hasText(module)) {
            if (context.isModule(module)) {
                context.addCatalog(module);
                context.load();
                context.setCurrentModule(module);
                context.setCurrentPath(context.getCurrentPath() + "/" + module);
            } else if(module.equals("..")){
                quit(context);
            } else{
                showError("模块：" + module + " 不存在");
            }
        }
    }

    @Cmd(value = "quit", describe = "返回到上一层")
    public void quit(Context context) {
        if(!context.getCurrentCatalog().equals(BASE_CATALOG)){
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

    @Cmd(value = "help", alias = "cmd", describe = "用于显示当前命令的用法")
    public void help(Context context) {
        List<Record> cmdMap = context.getCmdHandlerMap().values().stream().distinct()
            .map(cmdHandler -> Record.from(cmdHandler.getCmdEntity()))
            .map(MapUtil::sort)
            .collect(Collectors.toList());

        showTable(cmdMap, context.getPageIndex(), context);
    }

    @Cmd(value = "version", describe = "版本号")
    public void version(Context context){
        try {
            Properties properties = new Properties();
            properties.load(FileUtil.readFile(new File(
                StringUtil.backLast(ClassUtil.classPath()) + "/maven-archiver/pom.properties")));
            showLn(properties.get("version"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Cmd(value = "cat", describe = "查看文件内容")
    public void cat(Context context) {
        try {
            String fileName = context.secondWord();
            if (StringUtils.hasText(fileName)) {
                String filePath = context.appendPath(fileName);
                showLn(FileUtil.readFromFile(filePath));
            }
        } catch (IOException e) {
            showError("文件没有找到："+e.getLocalizedMessage());
        }
    }

    @Cmd(value = "edit", describe = "修改文件内容，用法：edit fileName 则打开一个编辑框直接编辑")
    public void edit(Context context){
        String fileName = context.secondWord();
        if (StringUtils.hasText(fileName)) {
            ShellUtil.call("open -e " + context.getAbsoluteFile(fileName));
        }
    }

    @Cmd(value = "create", alias = "touch", describe = "创建新的文件")
    public void create(Context context) {
        try {
            String fileName = context.secondWord();
            if (StringUtils.hasText(fileName)) {
                String abFile = context.getAbsoluteFile(fileName);
                if (!FileUtil.fileExist(abFile)) {
                    FileUtil.createFile(abFile);
                }else{
                    showError("不能创建相同文件");
                }
            }
            context.loadNewFile(fileName);
        } catch (IOException e) {
            showError("创建文件失败：" + e.getLocalizedMessage());
        }
    }
}
