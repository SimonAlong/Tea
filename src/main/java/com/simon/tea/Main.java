package com.simon.tea;

import java.io.IOException;
import java.util.List;
import me.zzp.am.Ao;
import me.zzp.am.Record;

/**
 * @author zhouzhenyong
 * @since 2018/6/23 下午4:29
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Screen screen = new Screen();
        screen.start();

//        Ao ao = Ao.open("jdbc:mysql://127.0.0.1:3306/tea", "root", "");
//        List<Record> list = ao.all("select * from tea limit 0,10");
//
//        Print.showTable(list, list.size());
    }
}
