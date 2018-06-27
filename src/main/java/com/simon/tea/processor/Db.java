package com.simon.tea.processor;

import static com.simon.tea.Print.*;

import com.simon.tea.annotation.Cmd;
import com.simon.tea.annotation.Module;
import com.simon.tea.context.Context;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午4:21
 */
@Module(name = "db")
public class Db {
    @Cmd(value = "show", describe = "展示表的数据：show tableNam，跟select * from tableName一样")
    public void showTable(Context context){
        show("显示表数据");
    }
}
