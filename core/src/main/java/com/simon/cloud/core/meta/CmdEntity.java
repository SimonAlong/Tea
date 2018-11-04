package com.simon.cloud.core.meta;

import com.simon.cloud.core.annotation.Cmd;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 注解 @Cmd 的实体数据
 *
 * @author zhouzhenyong
 * @since 2018/6/28 上午11:00
 */
@Getter
@Setter
@Builder
@Accessors(chain = true)
public class CmdEntity {

    private String value;
    private String alias;
    private String describe;
    private Boolean oldActive;
    private Boolean active;
    private CmdTypeEnum type;

    public static CmdEntity build(Cmd cmd) {
        return CmdEntity.builder()
            .value(cmd.value())
            .alias(cmd.alias())
            .describe(cmd.describe())
            .oldActive(cmd.active())
            .active(cmd.active())
            .type(cmd.type())
            .build();
    }
}
