package com.simon.tea;

import com.simon.tea.annotation.Module;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午10:05
 */
@RequiredArgsConstructor(staticName = "of")
public class AnalyseManager {
    @NonNull
    private Context context;
    private CmdAnalyse commonCmdAnalyses = new CmdAnalyse();
    private List<CmdAnalyse> cmdAnalyses = new ArrayList<>();

    public void analyse() {
        if(commonCmdAnalyses.getProcessor().isCmd(context)){
            commonCmdAnalyses.getProcessor().process(context);
            return;
        }
        cmdAnalyses.stream().filter(cmdAnalyse -> cmdAnalyse.getProcessor().isCmd(context)).findFirst()
            .map(cmdAnalyse->{cmdAnalyse.getProcessor().process(context); return null;});
        return;
    }

    public void addCmd(Module module, Processor processor) {
        if (module.name().equals("common")) {
            commonCmdAnalyses.setModule(module).setProcessor(processor);
        } else {
            cmdAnalyses.add(new CmdAnalyse().setModule(module).setProcessor(processor));
        }
    }
}
