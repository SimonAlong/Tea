package com.simon.tea.context;

import static com.simon.tea.Constant.BASE_CATALOG;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午10:50
 */
@Data
public class Context {
    private String catalog = BASE_CATALOG;
    private String input = "";
    private Boolean stop = false;
    private Set<String> cmdNames = new HashSet<>();
}
