package com.simon.tea;

import com.simon.tea.context.Context;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.zzp.am.Am;
import me.zzp.am.Record;

/**
 * @author zhouzhenyong
 * @since 2018/6/29 上午11:03
 */
@RequiredArgsConstructor(staticName = "of")
public class DBManager {

    @NonNull
    private Context context;
    private Am am;
    private String fileName;

    /**
     * select * from tableName;
     */
    public List<Record> all(String sql){
        fresh();
        return am.all(fileName, sql);
    }

    /**
     * 查询其中一个数据
     */
    public Record one(String sql){
        fresh();
        return am.one(fileName, sql);
    }

    public Integer count(){
        fresh();
        return 0;
//        am.execute()
    }

    public void fresh(){
        am = context.getAm();
        fileName = context.getCurrentModule();
    }
}
