package com.simon.tea.processor;

import static com.simon.tea.Print.*;

import com.simon.tea.annotation.Cmd;
import com.simon.tea.annotation.Module;
import com.simon.tea.context.Context;
import com.simon.tea.util.FileUtil;
import com.simon.tea.util.StringUtil;
import java.io.FileInputStream;
import java.io.IOException;
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
    @Cmd(value = "show", describe = "展示表的数据：show tableNam，跟select * from tableName一样")
    public void showTableData(Context context){
        show("显示表数据");
        context.show();
    }

    @Cmd(value = "load", describe = "载入配置")
    public void load(Context context){
        show("载入配置");

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
