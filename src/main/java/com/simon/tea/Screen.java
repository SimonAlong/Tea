package com.simon.tea;

import static com.simon.tea.Print.show;
import static com.simon.tea.Print.showCyan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午5:26
 */
public class Screen {
    private String input = "";
    private Parser parser = Parser.builder().build();

    public boolean judge() {
        return !input.equals("exit");
    }

    public void start(){
        try {
            showCatalog();
            process();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCatalog() {
        showCyan(parser.getCatalog());
        show(" >");
    }

    public void process() throws IOException {
        input = new BufferedReader(new InputStreamReader(System.in)).readLine();
        parser.process(input);
    }

    public void end(){
        show("tea end");
    }
}
