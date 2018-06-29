package com.simon.tea.processor;

import static com.simon.tea.Print.*;

import com.simon.tea.DBManager;
import com.simon.tea.Print;
import com.simon.tea.annotation.Cmd;
import com.simon.tea.annotation.Module;
import com.simon.tea.annotation.Usage;
import com.simon.tea.context.Context;
import com.simon.tea.util.StringUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import me.zzp.am.Record;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午4:21
 */
@Module(name = "db")
public class Db {
    @Cmd(value = "show", describe = "展示表的数据：show tableNam，详情，请用命令: usage show")
    public void showTableData(Context context){
        String tableName = context.secondWord();
        if (StringUtils.hasText(tableName)) {
            int pageIndex = 1, startIndex, endIndex, showStartIndex = 1;
            String otherMsg = context.getInput().substring("show ".length() + tableName.length());
            String sql = "select * from " + tableName;
            String showSql = sql, actSql = sql;
            if (!StringUtils.isEmpty(otherMsg)) {//解析 show tableName
                pageIndex = getPageIndex(otherMsg);
                startIndex = getRangeStart(otherMsg);
//                endIndex = getRangeEnd(otherMsg);

                showStartIndex = startIndex + (pageIndex == 0 ? 0 : pageIndex - 1) * PAGE_SIZE;

                showSql += " limit " + showStartIndex + "," + String.valueOf(showStartIndex + PAGE_SIZE);
//                if(0 != startIndex && 0 != endIndex){
//                    actSql += " limit " + startIndex + "," + endIndex;
//                }
            }else{
                showSql = sql + " limit 0," + PAGE_SIZE;
            }
            DBManager db = context.getDbManager();
            showTable(db.all(showSql), db.count(actSql), pageIndex, showStartIndex, true);
        }
    }

    private int getPageIndex(String otherMsg){
        if(otherMsg.contains(" -p ")){
            int index = otherMsg.indexOf(" -p ");
            String endStr = otherMsg.substring(index + " -p ".length());
            int endIndex = endStr.indexOf(" ");
            if(endIndex != -1){
                return Integer.valueOf(endStr.substring(0, endIndex));
            }else{
                return Integer.valueOf(endStr);
            }
        }
        return 1;
    }

    /**
     *  获取range 字符串
     * @param otherMsg  后面的配置信息
     * @return a,b      这种：a和b都是数字
     */
    @Cacheable(cacheNames = "getRangeStr")
    public String getRangeStr(String otherMsg){
        if(otherMsg.contains(" -lm ")) {
            int index = otherMsg.indexOf(" -lm ");
            String endStr = otherMsg.substring(index + " -lm ".length());
            int endIndex = endStr.indexOf(" ");
            String rangeStr;
            if (endIndex != -1) {
                return endStr.substring(0, endIndex);
            } else {
                return endStr;
            }
        }
        return null;
    }

    /**
     * 获取 -lm 后面的起始位置
     * @param otherMsg 后面的配置信息
     * @return 0 没有 -lm a,b 这种格式，或者语法不正确
     */
    private int getRangeStart(String otherMsg){
        String rangeStr = getRangeStr(otherMsg);
        if(null != rangeStr){
            int spotIndex = rangeStr.indexOf(',');
            if(spotIndex != -1){
                return Integer.valueOf(rangeStr.substring(0, spotIndex));
            }else{
                showError("语法有错误: -lm 后面跟的是两个逗号隔开的数字，如: -lm 2,10");
                return 0;
            }
        }
        return 0;
    }

//    /**
//     * 获取 -lm 后面的起始位置
//     * @param otherMsg 后面的配置信息
//     * @return 0 没有 -lm a,b 这种格式，或者语法不正确
//     */
//    private int getRangeEnd(String otherMsg){
//        String rangeStr = getRangeStr(otherMsg);
//        if(null != rangeStr) {
//            int spotIndex = rangeStr.indexOf(',');
//            if(spotIndex != -1){
//                return Integer.valueOf(rangeStr.substring(spotIndex + 1));
//            }else{
//                showError("语法有错误: -lm 后面跟的是两个逗号隔开的数字，如: -lm 2,10");
//                return 0;
//            }
//        }
//        return 0;
//    }

    @Usage(target = "show")
    public Record usageOfShow(){
        Record uses = Record.of("用法", "解释");
        uses.put("show tableName", "展示第一页，每页"+ Print.PAGE_SIZE+"行数据");
        uses.put("show tableName -p num", "展示第num页的数据，每页"+ Print.PAGE_SIZE+"行数据");
        uses.put("show tableName -lm 1,200", "展示前1~200条数据，每页"+ Print.PAGE_SIZE+"行数据");
        return uses;
    }

    @Cmd(value = "load", describe = "载入配置")
    public void load(Context context){
        try {
            String fileName = context.secondWord();
            if (StringUtils.hasText(fileName)) {
                Properties properties = new Properties();
                properties.load(new FileInputStream(context.appendPath(fileName)));

                context.getDbManager().startDb(
                    StringUtil.valueOf(properties.get("jdbcUrl")),
                    StringUtil.valueOf(properties.get("username")),
                    StringUtil.valueOf(properties.get("password")),
                    fileName);
                context.addCatalog(fileName);
                context.setCurrentModule(fileName);
            }
        } catch (IOException e) {
            showError("文件没有找到");
        }
    }
}
