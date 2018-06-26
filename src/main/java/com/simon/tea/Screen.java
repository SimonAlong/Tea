package com.simon.tea;

import static com.simon.tea.Print.*;

import com.simon.tea.context.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午5:26
 */
public class Screen {
    private Context context = new Context();
    private Parser parser;

    public void start() {
        while (!context.getStop()) {
            try {
                showCatalog();
                context.setInput(new BufferedReader(new InputStreamReader(System.in)).readLine());
                parser.process();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        end();
    }

    void showCatalog() {
        showCyan(context.getCatalog());
        show(">");
    }

    void end() {
        showBlueLn("tea end");
    }

    Screen() {
        parser = new Parser(context);
    }
}
