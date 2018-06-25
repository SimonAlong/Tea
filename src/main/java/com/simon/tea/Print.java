package com.simon.tea;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

import lombok.experimental.UtilityClass;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午3:52
 */
@UtilityClass
public class Print {

    /**
     * 红色
     * @param str
     */
    public void showRed(String str){
        System.out.print(ansi().fg(RED).render(str).reset());
    }

    public void showRedLn(String str){
        System.out.println(ansi().fg(RED).render(str).reset());
    }

    /**
     * 黑色
     * @param str
     */
    public void showBlack(String str){
        System.out.print(ansi().fg(BLACK).render(str).reset());
    }

    public void showBlackLn(String str){
        System.out.println(ansi().fg(BLACK).render(str).reset());
    }

    /**
     * 绿色
     * @param str
     */
    public void showGreen(String str){
        System.out.print(ansi().fg(GREEN).render(str).reset());
    }

    public void showGreenLn(String str){
        System.out.println(ansi().fg(GREEN).render(str).reset());
    }

    /**
     * 黄色
     * @param str
     */
    public void showYellow(String str){
        System.out.print(ansi().fg(YELLOW).render(str).reset());
    }

    public void showYellowLn(String str){
        System.out.println(ansi().fg(YELLOW).render(str).reset());
    }

    /**
     * 蓝色
     * @param str
     */
    public void showBlue(String str){
        System.out.print(ansi().fg(BLUE).render(str).reset());
    }

    public void showBlueLn(String str){
        System.out.println(ansi().fg(BLUE).render(str).reset());
    }

    /**
     * 品红
     * @param str
     */
    public void showMagenta(String str){
        System.out.print(ansi().fg(MAGENTA).render(str).reset());
    }

    public void showMagentaLn(String str){
        System.out.println(ansi().fg(MAGENTA).render(str).reset());
    }

    /**
     * 青色
     * @param str
     */
    public void showCyan(String str){
        System.out.print(ansi().fg(CYAN).render(str).reset());
    }

    public void showCyanLn(String str){
        System.out.println(ansi().fg(CYAN).render(str).reset());
    }

    /**
     * 白色
     * @param str
     */
    public void showWhite(String str){
        System.out.print(ansi().fg(WHITE).render(str).reset());
    }

    public void showWhiteLn(String str){
        System.out.println(ansi().fg(WHITE).render(str).reset());
    }

    /**
     * 系统默认颜色
     * @param str
     */
    public void show(String str){
        System.out.print(ansi().fg(DEFAULT).render(str).reset());
    }

    public void showLn(String str){
        System.out.println(ansi().fg(DEFAULT).render(str).reset());
    }
}
