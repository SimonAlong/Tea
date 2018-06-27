package com.simon.tea;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午3:52
 */
@UtilityClass
public class Print {
    private static final Integer TABLE_FILL_NUM = 5;

    /**
     * 红色
     */
    public void showRed(String str) {
        System.out.print(ansi().fg(RED).render(str).reset());
    }

    public void showRedSpace(String str) {
        System.out.print(ansi().fg(RED).render(str).reset() + "   ");
    }

    public void showRedLn(String str) {
        System.out.println(ansi().fg(RED).render(str).reset());
    }

    /**
     * 黑色
     */
    public void showBlack(String str) {
        System.out.print(ansi().fg(BLACK).render(str).reset());
    }

    public void showBlackSpace(String str) {
        System.out.print(ansi().fg(BLACK).render(str).reset() + "   ");
    }

    public void showBlackLn(String str) {
        System.out.println(ansi().fg(BLACK).render(str).reset());
    }

    /**
     * 绿色
     */
    public void showGreen(String str) {
        System.out.print(ansi().fg(GREEN).render(str).reset());
    }

    public void showGreenSpace(String str) {
        System.out.print(ansi().fg(GREEN).render(str).reset() + "   ");
    }

    public void showGreenLn(String str) {
        System.out.println(ansi().fg(GREEN).render(str).reset());
    }

    /**
     * 黄色
     */
    public void showYellow(String str) {
        System.out.print(ansi().fg(YELLOW).render(str).reset());
    }

    public void showYellowSpace(String str) {
        System.out.print(ansi().fg(YELLOW).render(str).reset() + "   ");
    }

    public void showYellowLn(String str) {
        System.out.println(ansi().fg(YELLOW).render(str).reset());
    }

    /**
     * 蓝色
     */
    public void showBlue(String str) {
        System.out.print(ansi().fg(BLUE).render(str).reset());
    }

    public void showBlueSpace(String str) {
        System.out.print(ansi().fg(BLUE).render(str).reset() + "   ");
    }

    public void showBlueLn(String str) {
        System.out.println(ansi().fg(BLUE).render(str).reset());
    }

    /**
     * 品红
     */
    public void showMagenta(String str) {
        System.out.print(ansi().fg(MAGENTA).render(str).reset());
    }

    public void showMagentaSpace(String str) {
        System.out.print(ansi().fg(MAGENTA).render(str).reset() + "   ");
    }

    public void showMagentaLn(String str) {
        System.out.println(ansi().fg(MAGENTA).render(str).reset());
    }

    /**
     * 青色
     */
    public void showCyan(String str) {
        System.out.print(ansi().fg(CYAN).render(str).reset());
    }

    public void showCyanSpace(String str) {
        System.out.print(ansi().fg(CYAN).render(str).reset() + "   ");
    }

    public void showCyanLn(String str) {
        System.out.println(ansi().fg(CYAN).render(str).reset());
    }

    /**
     * 白色
     */
    public void showWhite(String str) {
        System.out.print(ansi().fg(WHITE).render(str).reset());
    }

    public void showWhiteSpace(String str) {
        System.out.print(ansi().fg(WHITE).render(str).reset() + "   ");
    }

    public void showWhiteLn(String str) {
        System.out.println(ansi().fg(WHITE).render(str).reset());
    }

    /**
     * 系统默认颜色
     */
    public void show(Object str) {
        System.out.print(ansi().fg(DEFAULT).render(String.valueOf(str)).reset());
    }

    public void showSpace(String str) {
        System.out.print(ansi().fg(DEFAULT).render(str).reset() + "   ");
    }

    public void showLn(String str) {
        System.out.println(ansi().fg(DEFAULT).render(str).reset());
    }

    public void showLn() {
        showLn("");
    }

    public void showCmdError(String input) {
        showError("命令不识别：" + input);
    }

    public void showError(String input) {
        showRedLn("error - " + input);
    }


    public void showTable(List<Map<String, Object>> bodies, String... heads) {
        LinkedList<String> headList = new LinkedList<>(Arrays.asList(heads));
        headList.addFirst("index");

        List<Integer> columnLengthList = computeColumnMaxLength(bodies, headList);
        Integer width = generateWidth(bodies, headList);
        showTableHead(headList, columnLengthList, width);
        showTableBody(headList, bodies, columnLengthList, width);
        showTableCnt();
    }

    /**
     * 输出表头
     * @param headList          表头列表
     * @param columnLengthList  每列的长度
     * @param width             表最大长度
     */
    private void showTableHead(List<String> headList, List<Integer> columnLengthList, Integer width) {
        showNumStrLn(width, "-");
        for (int i = 0; i < columnLengthList.size(); i++) {
            String column = headList.get(i);
            show(column);
            showNumStr(columnLengthList.get(i) - column.length() + TABLE_FILL_NUM, " ");
        }
        showLn();
        showNumStrLn(width, "-");
    }

    public void showNumStr(Integer length, String str) {
        for (int i = 0; i < length; i++) {
            show(str);
        }
    }

    public void showNumStrLn(Integer length, String str) {
        showNumStr(length, str);
        showLn();
    }

    private void showTableBody(List<String> headList, List<Map<String, Object>> bodies, List<Integer> columnLengthList, Integer width) {
        for (int line = 0; line < bodies.size(); line++) {
            show(line);
            showNumStr(columnLengthList.get(0) - String.valueOf(line).length() + TABLE_FILL_NUM, " ");
            for (int columnIndex = 1; columnIndex < headList.size(); columnIndex++) {
                String value = String.valueOf(bodies.get(line).get(headList.get(columnIndex)));
                show(value);
                showNumStr(columnLengthList.get(columnIndex) - String.valueOf(value).length() + TABLE_FILL_NUM, " ");
            }
            showLn();
            showNumStrLn(width, "-");
        }
    }

    private void showTableCnt() {

    }

    /**
     * 计算一行的最长长度
     */
    public Integer generateWidth(List<Map<String, Object>> bodies, List<String> headList) {
        AtomicReference<Integer> width = new AtomicReference<>(0);
        computeColumnMaxLength(bodies, headList).forEach(l -> width.updateAndGet(v -> v + l + TABLE_FILL_NUM));
        return width.get();
    }

    /**
     * 获取每一列中的最大长度的列表
     *
     * @param bodies table的数据
     * @param headList table的表头数据
     * @return 最大长度列表
     */
    public List<Integer> computeColumnMaxLength(List<Map<String, Object>> bodies, List<String> headList) {
        List<Integer> columnMaxLengthList = new ArrayList<>(headList.size()).stream().map(h -> 0)
            .collect(Collectors.toList());
        for (int i = 0; i < headList.size(); i++) {
            AtomicInteger max = new AtomicInteger(headList.get(i).length());
            int finalI = i;
            bodies.stream().map(body -> body.get(headList.get(finalI))).collect(Collectors.toList()).forEach(column -> {
                max.set(Math.max(max.get(), String.valueOf(column).length()));
            });
            columnMaxLengthList.add(max.get());
        }
        return columnMaxLengthList;
    }
}
