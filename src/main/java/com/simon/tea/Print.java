package com.simon.tea;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

import com.simon.tea.context.Context;
import com.simon.tea.util.StringUtil;
import com.sun.org.apache.regexp.internal.RE;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import lombok.val;
import me.zzp.am.Record;
import org.fusesource.jansi.Ansi.Color;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午3:52
 */
@UtilityClass
public class Print {
    /**
     * 颜色一共有这么几种：
     * BLACK(0, "BLACK"), RED(1, "RED"), GREEN(2, "GREEN"), YELLOW(3, "YELLOW"), BLUE(4, "BLUE"), MAGENTA(5,
     * "MAGENTA"), CYAN(6, "CYAN"), WHITE(7, "WHITE"), DEFAULT(9, "DEFAULT");
     */
    public void show(Object str) {
        System.out.print(ansi().fg(DEFAULT).render(StringUtil.valueOf(str)).reset());
    }

    public void show(Object str, Color color) {
        System.out.print(ansi().fg(color).render(StringUtil.valueOf(str)).reset());
    }

    public void showSpace(Object str) {
        System.out.print(ansi().fg(DEFAULT).render(StringUtil.valueOf(str)).reset() + "   ");
    }

    public void showSpace(Object str, Color color) {
        System.out.print(ansi().fg(color).render(StringUtil.valueOf(str)).reset() + "   ");
    }

    public void showLn(Object str) {
        System.out.println(ansi().fg(DEFAULT).render(StringUtil.valueOf(str)).reset());
    }

    public void showLn(Object str, Color color) {
        System.out.println(ansi().fg(color).render(StringUtil.valueOf(str)).reset());
    }

    public void showLn() {
        showLn("");
    }

    public void showCmdError(Object input) {
        showError("命令不识别：" + input);
    }

    public void showError(Object input) {
        showLn("error - " + input, RED);
    }

    public void showWarning(Object input) {
        showLn("warning - " + input, YELLOW);
    }

    public void showNumStr(Integer length, String str) {
        for (int i = 0; i < length; i++) {
            show(str);
        }
    }

    public void showNumStr(Integer length, String str, Color color) {
        for (int i = 0; i < length; i++) {
            show(str, color);
        }
    }

    public void showNumStrLn(Integer length, String str) {
        showNumStr(length, str);
        showLn();
    }

    public void showNumStrLn(Integer length, String str, Color color) {
        showNumStr(length, str, color);
        showLn();
    }

    /**
     * 每列数据后面填充的空格
     */
    private static final Integer COLUMN_FILL_TIP = 2;
    public static Integer PAGE_SIZE = 20;

    public void showTable(List<Record> bodies, Context context) {
        showTable(bodies, bodies.size(), 1, 0, context);
    }

    public void showTableList(List<String> bodies, String column, Context context) {
        List<Record> columns = bodies.stream().map(data->Record.of(column, data)).collect(Collectors.toList());
        showTable(columns, bodies.size(), 1, 0, context);
    }

    /**
     * 根据页签位置展示数据
     * @param bodies    数据体
     * @param totalSize 数据总个数
     * @param pageIndex 数据页面索引
     * @param context   数据上下文
     */
    public void showTable(List<Record> bodies, Integer totalSize, Integer pageIndex, Integer startIndex, Context context){
        if(!bodies.isEmpty()){
            val headList = preHandleHead(bodies);
            Integer endIndex = startIndex + PAGE_SIZE;
            List<Record> splitBodies = bodies;
            if(bodies.size() > PAGE_SIZE) {
                splitBodies = bodies.subList(startIndex, Math.min(endIndex, totalSize));
            }
            val columnLengthList = computeColumnMaxLength(splitBodies, headList, startIndex);
            Integer width = generateWidth(splitBodies, headList, startIndex);

            showTableHead(headList, columnLengthList, width);
            showTableBody(headList, startIndex, splitBodies, columnLengthList, width);
            showTableCnt(width, totalSize, pageIndex, splitBodies, context);
        } else {
            showWarning("数据为空");
        }
    }

    /**
     * 头部预处理，添加列index
     * @param bodies 数据体
     * @return       增加index列之后的头列表
     */
    private LinkedList<String> preHandleHead(List<Record> bodies){
        assert !bodies.isEmpty();
        Record record = bodies.get(0);
        val headList = new LinkedList<String>(record.keySet());
        headList.addFirst("index");
        return headList;
    }

    private List<Map<String, Object>> splitTable(List<Map<String, Object>> bodies, Integer pageIndex, Integer pageSize){
        Integer startIndex = (pageIndex == 0 ? 0 : pageIndex - 1) * pageSize;
        Integer endIndex = startIndex + pageSize;
        return bodies.subList(startIndex, endIndex);
    }

    /**
     * 输出表头
     *
     * @param headList 表头列表
     * @param columnLengthList 每列的长度
     * @param width 表最大长度
     */
    private void showTableHead(List<String> headList, List<Integer> columnLengthList, Integer width) {
        showNumStrLn(width, "-", GREEN);
        for (int i = 0; i < columnLengthList.size(); i++) {
            String column = headList.get(i);
            show(column, RED);
            showNumStr(columnLengthList.get(i) - column.length() + COLUMN_FILL_TIP, " ");
        }
        showLn();
        showNumStrLn(width, "-", GREEN);
    }

    /**
     * 展示表数据
     *
     * @param headList          表头数据
     * @param startIndex        表显示的开始索引
     * @param bodies            表数据体
     * @param columnLengthList  表列的最大长度列表
     * @param width             表一行长度
     */
    private void showTableBody(List<String> headList, Integer startIndex, List<Record> bodies, List<Integer> columnLengthList,
        Integer width) {
        for (int line = 0; line < bodies.size(); line++) {
            showValue(columnLengthList, 0, line + startIndex + 1, GREEN);
            for (int columnIndex = 1; columnIndex < headList.size(); columnIndex++) {
                String value = String.valueOf(bodies.get(line).get(headList.get(columnIndex)));
                showValue(columnLengthList, columnIndex, value);
            }
            showLn();
        }
        showNumStrLn(width, "-", GREEN);
    }

    /**
     * 打印对应的值
     * @param columnLengthList  每一列的最大长度
     * @param index             对应列的索引
     * @param value             要打印的数据
     */
    private void showValue(List<Integer> columnLengthList, Integer index, Object value){
        showValueJudgeColor(value);
        showNumStr(columnLengthList.get(index) - StringUtil.length(String.valueOf(value)) + COLUMN_FILL_TIP, " ");
    }

    /**
     * 打印对应的值
     * @param columnLengthList  每一列的最大长度
     * @param index             对应列的索引
     * @param value             要打印的数据
     */
    private void showValue(List<Integer> columnLengthList, Integer index, Object value, Color color){
        show(value, color);
        showNumStr(columnLengthList.get(index) - StringUtil.length(String.valueOf(value)) + COLUMN_FILL_TIP, " ");
    }

    /**
     *
     * @param totalSize 数据总个数
     * @param pageIndex 数据所在页面
     * @param bodies    分页后的数据体
     * @param context   数据上下文
     */
    private void showTableCnt(Integer width, Integer totalSize, Integer pageIndex, List<Record> bodies, Context context) {
        String totalSizeShow = "总数：" + totalSize + " 个， 耗时："+context.getTakeTime()+" ms，  ";
        showSpace(totalSizeShow);
        Integer pageNum = totalSize / PAGE_SIZE + (totalSize % PAGE_SIZE > 0 ? 1 : 0);
        boolean omit = false;
        int pageShowNum = 0;
        for (int i = 1; i <= pageNum; i++) {
            if((i != pageIndex) && (i == 1 || (i < pageIndex && i + 2 >= pageIndex) || (i > pageIndex && i - 2 <= pageIndex) || i == pageNum)){
                showSpace(i);
                pageShowNum += (String.valueOf(i).length() + 3);
                continue;
            }
            if (i == pageIndex) {
                showSpace(i, GREEN);
                pageShowNum += (String.valueOf(i).length() + 3);
                omit = false;
                continue;
            }
            if (!omit) {
                showSpace("...");
                pageShowNum += 6;
                omit = true;
            }
        }
        showLn();
        showNumStrLn(StringUtil.length(totalSizeShow) - 1 + pageShowNum, "-", GREEN);
    }

    /**
     * 计算一行的最长长度
     */
    private Integer generateWidth(List<Record> bodies, List<String> headList, Integer startIndex) {
        AtomicReference<Integer> width = new AtomicReference<>(0);
        //每列填充一个tip
        computeColumnMaxLength(bodies, headList, startIndex).forEach(l -> width.updateAndGet(v -> v + l + COLUMN_FILL_TIP));
        return width.get();
    }

    /**
     * 获取每一列中的最大长度的列表
     * 最左边的索引的长度和具体的数据的长度
     *
     * @param bodies table的数据
     * @param headList table的表头数据
     * @param startIndex 数据起始点
     * @return 最大长度列表
     */
    private List<Integer> computeColumnMaxLength(List<Record> bodies, List<String> headList, Integer startIndex) {
        List<Integer> columnMaxLengthList = new ArrayList<>(headList.size()).stream().map(h -> 0)
            .collect(Collectors.toList());
        for (int i = 0; i < headList.size(); i++) {
            AtomicInteger max = new AtomicInteger(headList.get(i).length());
            int finalI = i;
            AtomicInteger startInd = new AtomicInteger(startIndex);
            bodies.stream().map(body -> body.get(headList.get(finalI))).collect(Collectors.toList())
                .forEach(columnValue -> {
                    int m = Math.max(String.valueOf(startInd.getAndIncrement()).length(),
                        StringUtil.length(String.valueOf(columnValue)));
                    max.set(Math.max(m, max.get()));
                });
            columnMaxLengthList.add(max.get());
        }
        return columnMaxLengthList;
    }

    /**
     * 根据输入的类型进行添加不同的颜色
     */
    private void showValueJudgeColor(Object word){
        if(word.equals(Constant.SYS_CMD)){
            show(word, RED);
        }else{
            //todo 其他类型判断
            show(word);
        }
    }
}
