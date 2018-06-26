package com.simon.tea.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import lombok.experimental.UtilityClass;

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
}
