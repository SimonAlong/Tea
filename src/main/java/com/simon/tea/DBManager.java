package com.simon.tea;

import com.simon.tea.context.Context;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.zzp.am.Am;
import me.zzp.am.Ao;

/**
 * @author zhouzhenyong
 * @since 2018/6/29 上午11:03
 */
@RequiredArgsConstructor(staticName = "of")
public class DBManager {

    @NonNull
    private Context context;

    /**
     * select * from tableName;
     */
    public void show(){
        String sql = context.getInput();
        Am am = context.getAm();
        String fileName = context.getCurrentModule();
        am.all(fileName, sql);
    }
}
