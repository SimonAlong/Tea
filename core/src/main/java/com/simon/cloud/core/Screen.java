package com.simon.cloud.core;

import static com.simon.cloud.core.Print.*;

import com.simon.cloud.core.context.Context;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import static org.fusesource.jansi.Ansi.Color.*;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午5:26
 */
@Component
public class Screen {

    private Context context = new Context();
    private Parser parser;

    @PostConstruct
    public void start() {
        try {
            while (!context.getStop()) {
                showCatalog();
                context.setInput(new BufferedReader(new InputStreamReader(System.in)).readLine());
                parser.process();
            }
        } catch (Exception e) {
            context.setStop(true);
            e.printStackTrace();
        }
    }

    private void showCatalog() {
        show(context.getShowCatalog(), CYAN);
        show("> ");
    }

    Screen() {
        parser = new Parser(context);
    }
}
