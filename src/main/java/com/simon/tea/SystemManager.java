package com.simon.tea;

/**
 * @author zhouzhenyong
 * @since 2018/7/6 上午11:54
 */
public abstract class SystemManager {
    /**
     * 解析 -p 获取要展示哪一页
     * @param otherMsg  入参
     * @return          -p 后面的页数
     */
    public int getPageIndex(String otherMsg){
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
}
