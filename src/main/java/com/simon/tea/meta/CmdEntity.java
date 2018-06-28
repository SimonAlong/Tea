package com.simon.tea.meta;

import com.simon.tea.annotation.Cmd;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2018/6/28 上午11:00
 */
@Getter
@Setter
@Builder
public class CmdEntity {
    private String value;
    private String alias;
    private String describe;
    
    public static CmdEntity build(Cmd cmd){
        return CmdEntity.builder().value(cmd.value()).alias(cmd.alias()).describe(cmd.describe()).build();
    }
}
