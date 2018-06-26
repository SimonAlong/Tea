package com.simon.tea;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午10:05
 */
@RequiredArgsConstructor(staticName = "of")
public class AnalyseManager {
    @NonNull
    private Parser parser;
    List<CmdAnalyse> cmdAnalyses = new ArrayList<>();

}
