package com.simon.tea.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import lombok.experimental.UtilityClass;
import me.zzp.am.Record;

/**
 * @author zhouzhenyong
 * @since 2018/6/27 上午1:38
 */
@UtilityClass
public class MapUtil {

    public void removeAll(Map sourceMap, Map toDeleteMap){
        Iterator<Entry> it = sourceMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = it.next();
            if(toDeleteMap.containsKey(entry.getKey())) {
                it.remove();
            }
        }
    }

    public Record sort(Record record){
        Map<String, Object> map = new TreeMap<>(Comparator.reverseOrder());
        map.putAll(record);
        return Record.of(map);
    }
}
