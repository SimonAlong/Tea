package com.simon.tea.processor;

import static com.simon.tea.Print.*;

import com.simon.tea.DBManager;
import com.simon.tea.Print;
import com.simon.tea.annotation.Cmd;
import com.simon.tea.annotation.Module;
import com.simon.tea.annotation.Usage;
import com.simon.tea.context.Context;
import com.simon.tea.util.FileUtil;
import com.simon.tea.util.StringUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import me.zzp.am.Record;
import org.springframework.util.StringUtils;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午4:21
 */
@Module(name = "db")
public class Db {
    @Cmd(value = "show", describe = "展示表的数据：show tableNam，详情，请用命令: usage show")
    public void showTableData(Context context){
        show("显示表数据");
        context.show();
        String fileName = context.secondWord();
        if (StringUtils.hasText(fileName)) {
            String sql = "select * from "+fileName;
            DBManager db = context.getDbManager();
            db.all(sql+" 0,"+String.valueOf(Print.PAGE_SIZE));
            db.count();
        }
    }

    @Usage(target = "show")
    public Record usageOfShow(){
        Record uses = Record.of("用法", "解释");
        uses.put("show tableName", "展示第一页，每页"+ Print.PAGE_SIZE+"行数据");
        uses.put("show tableName -p num", "展示第num页的数据，每页"+ Print.PAGE_SIZE+"行数据");
        uses.put("show tableName -p 1,200", "展示前1~200条数据，每页"+ Print.PAGE_SIZE+"行数据");
        return uses;
    }

    @Cmd(value = "load", describe = "载入配置")
    public void load(Context context){
        try {
            String fileName = context.secondWord();
            if (StringUtils.hasText(fileName)) {
                Properties properties = new Properties();
                properties.load(new FileInputStream(context.appendPath(fileName)));

                context.startDb(StringUtil.valueOf(properties.get("jdbcUrl")), StringUtil.valueOf(properties.get("username")),
                    StringUtil.valueOf(properties.get("password")), fileName);
                context.addCatalog(fileName);
                context.setCurrentModule(fileName);

                List<Record> list = context.getAm().all(fileName, "select * from audit_reject_type");
                showTable(list);
            }
        } catch (IOException e) {
            showError("文件没有找到");
        }
    }
}
