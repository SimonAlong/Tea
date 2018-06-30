package com.simon.tea;

import static com.simon.tea.Print.*;

import com.simon.tea.context.Context;
import com.simon.tea.util.StringUtil;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import static org.fusesource.jansi.Ansi.Color.*;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午5:26
 */
public class Screen {
    private Context context = new Context();
    private Parser parser;

    void start() {
        try {
            while (!context.getStop()){
                showCatalog();
                context.setInput(new BufferedReader(new InputStreamReader(System.in)).readLine());
                parser.process();
            }
        }catch (Exception e){
            context.setStop(true);
            e.printStackTrace();
        }
    }

    private void showCatalog() {
        show(context.getShowCatalog(), CYAN);
        show("> ");
    }

    Screen(){
        parser = new Parser(context);
    }
}
