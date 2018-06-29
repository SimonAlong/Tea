package com.simon.tea;

import com.simon.tea.context.Context;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.zzp.am.Am;
import me.zzp.am.Ao;
import me.zzp.am.Record;

/**
 * @author zhouzhenyong
 * @since 2018/6/29 上午11:03
 */
@RequiredArgsConstructor(staticName = "of")
public class DBManager {

    @NonNull
    private Context context;
    private Ao ao;
    private String fileName;

    public void startDb(String jdbcUrl, String userName, String password, String fileName){
        ao = null;
        ao = Ao.open(jdbcUrl, userName, password);
    }

    /**
     * select * from tableName;
     */
    public List<Record> all(String sql){
        fresh();
        return ao.all(sql);
    }

    /**
     * 查询其中一个数据
     */
    public Record one(String sql){
        fresh();
        return ao.one(sql);
    }

    public Integer count(String sql){
        fresh();
        String countSql = "select count(1) " + sql.substring(sql.indexOf("from " + context.secondWord()));
        return ao.value(Integer.class, countSql);
    }

    public void fresh(){
        fileName = context.getCurrentModule();
    }
}
