package com.simon.tea;

import static com.simon.tea.Print.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午5:26
 */
public class Screen {
    private Context context = new Context();
    private Parser parser;

    public void start(){
        while(!context.getStop()){
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

    public void showCatalog() {
        showCyan(context.getCatalog());
        show(">");
    }

    public void end(){
        show("tea end");
    }


    public Screen(){
        parser = new Parser(context);
    }
}
