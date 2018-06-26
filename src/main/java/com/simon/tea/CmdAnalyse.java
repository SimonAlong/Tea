package com.simon.tea;

import com.simon.tea.annotation.Module;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午10:01
 */
@Getter
@Setter
@EqualsAndHashCode(of = "module")
@Accessors(chain = true)
public class CmdAnalyse {
    private Module module;
    private Processor processor;
}
