package com.simon.tea.meta;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2018/6/27 下午2:45
 */
@Data
@Builder
@Accessors(chain = true)
public class CfgPath {

    /**
     * 配置名字
     */
    private String name;
    /**
     * 配置的路径
     */
    private String path;
}
