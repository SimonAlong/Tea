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
    private String name;
    private String path;
}
