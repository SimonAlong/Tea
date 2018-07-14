package com.simon.tea;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

import com.simon.tea.context.Context;
import com.simon.tea.util.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import lombok.val;
import me.zzp.am.Column;
import me.zzp.am.Index;
import me.zzp.am.Record;
import org.fusesource.jansi.Ansi.Color;
import org.springframework.util.CollectionUtils;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午3:52
 */
@UtilityClass
public class Print {

    public static Integer PAGE_SIZE = 20;
    private static final Integer COLUMN_FILL_TIP = 2;

    /**
     * 颜色一共有这么几种： BLACK(0, "BLACK"), RED(1, "RED"), GREEN(2, "GREEN"), YELLOW(3, "YELLOW"), BLUE(4, "BLUE"), MAGENTA(5,
     * "MAGENTA"), CYAN(6, "CYAN"), WHITE(7, "WHITE"), DEFAULT(9, "DEFAULT");
     */
    public void show(Object str) {
        show(str, DEFAULT);
    }

    public void show(Object str, Color color) {
        System.out.print(ansi().fg(color).render(StringUtil.valueOf(str)).reset());
    }

    public void showSpace(Object str) {
        showSpace(str, DEFAULT);
    }

    public void showSpace(Object str, Color color) {
        System.out.print(ansi().fg(color).render(StringUtil.valueOf(str)).reset() + "   ");
    }

    public void showLn(Object str) {
        if (null != str) {
            showLn(str, DEFAULT);
        }
    }

    public void showLn(Object str, Color color) {
        System.out.println(ansi().fg(color).render(StringUtil.valueOf(str)).reset());
    }

    public void showLn() {
        showLn("");
    }

    public void showCmdError(Object input) {
        showError("命令不识别或者命令未激活：" + input);
    }

    public void showError(Object input) {
        showLn("error - " + input, RED);
    }

    public void showWarning(Object input) {
        showLn("warning - " + input, YELLOW);
    }

    public void showInfo(Object input) {
        showLn("info - " + input, DEFAULT);
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

    public void showList(List<String> dataList) {
        PrintList.showList(dataList);
    }

    public void showIndexTable(List<Index> indexList, Context context) {
        val indexRecord = indexList.stream().map(index -> {
            Record record = Record.from(index);
            record.remove("column");
            record.putAll(Record.from(index.getColumn()));
            return record;
        }).collect(Collectors.toList());
        showTable(indexRecord, context);
    }

    public void showColumnTable(List<Column> columnList, Context context) {
        showTable(columnList.stream().map(c -> Record.from(c)).collect(Collectors.toList()), context);
    }

    public void showTable(List<Record> bodies, Context context) {
        PrintTable.showTable(bodies, context);
    }

    public void showTable(List<Record> bodies, Integer pageIndex, Context context) {
        PrintTable.showTable(bodies, pageIndex, context);
    }

    public void showTableList(List<String> bodies, String column, Context context) {
        PrintTable.showTableList(bodies, column, context);
    }

    public void showTable(List<Record> bodies, Integer totalSize, Integer pageIndex, Integer startIndex,
        Context context) {
        PrintTable.showTable(bodies, totalSize, pageIndex, startIndex, context);
    }

    @UtilityClass
    public static class PrintTable {

        /**
         * 每列数据后面填充的空格
         */

        void showTable(List<Record> allData, Integer pageIndex, Context context) {
            if (!CollectionUtils.isEmpty(allData)) {
                showTable(allData, allData.size(), pageIndex, (pageIndex - 1) * PAGE_SIZE, context);
            } else {
                showInfo("数据为空");
            }
        }

        void showTable(List<Record> bodies, Context context) {
            if (!CollectionUtils.isEmpty(bodies)) {
                showTable(bodies, bodies.size(), 1, 0, context);
            } else {
                showInfo("数据为空");
            }
        }

        void showTableList(List<String> bodies, String column, Context context) {
            if (!CollectionUtils.isEmpty(bodies)) {
                List<Record> records = new ArrayList<>();
                for (int i = 0; i < bodies.size(); i++) {
                    records.add(Record.of(column, bodies.get(i)));
                }
                showTable(records, bodies.size(), 1, 0, context);
            } else {
                showInfo("数据为空");
            }
        }

        /**
         * 根据页签位置展示数据
         *
         * @param bodies 数据体
         * @param totalSize 数据总个数
         * @param pageIndex 数据页面索引
         * @param context 数据上下文
         */
        void showTable(List<Record> bodies, Integer totalSize, Integer pageIndex, Integer startIndex, Context context) {
            if (!CollectionUtils.isEmpty(bodies)) {
                val headList = preHandleHead(bodies);
                Integer endIndex = startIndex + PAGE_SIZE;
                List<Record> splitBodies = bodies;
                if (bodies.size() > PAGE_SIZE) {
                    splitBodies = bodies.subList(startIndex, Math.min(endIndex, totalSize));
                }
                val columnLengthList = computeColumnMaxList(splitBodies, headList, startIndex);
                Integer width = generateWidth(columnLengthList);

                showTableHead(headList, columnLengthList, width);
                showTableBody(headList, startIndex, splitBodies, columnLengthList, width);
                showTableCnt(width, totalSize, pageIndex, splitBodies, context);
            } else {
                showInfo("数据为空");
            }
        }

        /**
         * 头部预处理，添加列index
         *
         * @param bodies 数据体
         * @return 增加index列之后的头列表
         */
        private LinkedList<String> preHandleHead(List<Record> bodies) {
            assert !bodies.isEmpty();
            Record record = bodies.get(0);
            val headList = new LinkedList<String>(record.keySet());
            headList.addFirst("index");
            return headList;
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
         * @param headList 表头数据
         * @param startIndex 表显示的开始索引
         * @param bodies 表数据体
         * @param columnLengthList 表列的最大长度列表
         * @param width 表一行长度
         */
        private void showTableBody(List<String> headList, Integer startIndex, List<Record> bodies,
            List<Integer> columnLengthList,
            Integer width) {
            for (int line = 0; line < bodies.size(); line++) {
                showValue(columnLengthList, 0, line + startIndex + 1, GREEN);
                for (int columnIndex = 1; columnIndex < headList.size(); columnIndex++) {
                    showValue(columnLengthList, columnIndex, bodies.get(line).get(headList.get(columnIndex)));
                }
                showLn();
            }
            showNumStrLn(width, "-", GREEN);
        }

        /**
         * 打印对应的值
         *
         * @param columnLengthList 每一列的最大长度
         * @param index 对应列的索引
         * @param value 要打印的数据
         */
        private void showValue(List<Integer> columnLengthList, Integer index, Object value) {
            showValueJudgeColor(value);
            showNumStr(columnLengthList.get(index) - StringUtil.length(String.valueOf(value)) + COLUMN_FILL_TIP, " ");
        }

        /**
         * 打印对应的值
         *
         * @param columnLengthList 每一列的最大长度
         * @param index 对应列的索引
         * @param value 要打印的数据
         */
        private void showValue(List<Integer> columnLengthList, Integer index, Object value, Color color) {
            show(value, color);
            showNumStr(columnLengthList.get(index) - StringUtil.length(String.valueOf(value)) + COLUMN_FILL_TIP, " ");
        }

        /**
         * @param totalSize 数据总个数
         * @param pageIndex 数据所在页面
         * @param bodies 分页后的数据体
         * @param context 数据上下文
         */
        private void showTableCnt(Integer width, Integer totalSize, Integer pageIndex, List<Record> bodies,
            Context context) {
            String totalSizeShow = "总数：" + totalSize + " 个， 耗时：" + context.getTakeTime() + " ms，  ";
            showSpace(totalSizeShow);
            Integer pageNum = totalSize / PAGE_SIZE + (totalSize % PAGE_SIZE > 0 ? 1 : 0);
            boolean omit = false;
            int pageShowNum = 0;
            for (int i = 1; i <= pageNum; i++) {
                if ((i != pageIndex) && (i == 1 || (i < pageIndex && i + 2 >= pageIndex) || (i > pageIndex
                    && i - 2 <= pageIndex) || i == pageNum)) {
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
        public Integer generateWidth(List<Integer> columnLengthList) {
            AtomicReference<Integer> width = new AtomicReference<>(0);
            //每列填充一个tip
            columnLengthList.forEach(l -> width.updateAndGet(v -> v + l + COLUMN_FILL_TIP));
            return width.get();
        }

        /**
         * 获取每一列中的最大长度的列表 最左边的索引的长度和具体的数据的长度
         *
         * @param bodies table的数据
         * @param headList table的表头数据
         * @param startIndex 数据起始点
         * @return 最大长度列表
         */
        private List<Integer> computeColumnMaxList(List<Record> bodies, List<String> headList, Integer startIndex) {
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
        private void showValueJudgeColor(Object word) {
            if (null != word) {
                if (word.equals(Constant.SYS_MODULE)) {
                    show(word, RED);
                } else {
                    //todo 其他类型判断
                    show(word);
                }
            } else {
                show("null");
            }
        }
    }

    @UtilityClass
    public static class PrintList {

        private static Integer SPLIT_NUM = 4;


        /**
         * 这里按照5列进行打印
         */
        void showList(List<String> dataList) {
            val columnSizeList = computeColumnMaxList(dataList);
            val width = PrintTable.generateWidth(columnSizeList);
            showNumStrLn(width, "-", GREEN);
            int totalSize = dataList.size();
            for (int line = 0; line < totalSize; line++) {
                val value = dataList.get(line);
                show(value, GREEN);
                showNumStr(columnSizeList.get(line % SPLIT_NUM) - StringUtil.length(value) + COLUMN_FILL_TIP, " ");
                if ((line + 1) % SPLIT_NUM == 0 || line + 1 == totalSize) {
                    showLn();
                }
            }
            showNumStrLn(width, "-", GREEN);
            showLn("总数：" + dataList.size() + " 个", BLUE);
        }

        /**
         * 计算每一列中的最大长度
         */
        private List<Integer> computeColumnMaxList(List<String> dataList) {
            List<Integer> columnMaxLengthList = new ArrayList<>(SPLIT_NUM).stream().map(h -> 0)
                .collect(Collectors.toList());
            val subMap = getSubListIndex(dataList);
            for (int i = 0; i < SPLIT_NUM; i++) {
                AtomicInteger max = new AtomicInteger(0);
                subMap.get(i).forEach(s -> {
                    int m = Math.max(max.get(), s.length());
                    max.set(Math.max(m, max.get()));
                });
                columnMaxLengthList.add(max.get());
            }
            return columnMaxLengthList;
        }

        /**
         * 生成按照列进行的映射为子列 如: 0,1,2,3,4,5,...,21转换为5列，那么输出如下
         *
         * 0: 0,5,10,15,20 1: 1,6,11,16,21 2: 2,7,12,17 3: 3,8,13,18 4: 4,9,14,19
         */
        private Map<Integer, List<String>> getSubListIndex(List<String> dataList) {
            val subMap = new HashMap<Integer, List<String>>();
            for (int i = 0; i < dataList.size(); i++) {
                for (int j = 0; j < SPLIT_NUM; j++) {
                    if (i % SPLIT_NUM == j) {
                        int finalI = i;
                        subMap.compute(j, (k, v) -> {
                            if (null == v) {
                                v = new ArrayList<>();
                            }
                            v.add(dataList.get(finalI));
                            return v;
                        });
                    }
                }
            }
            return subMap;
        }
    }
}
