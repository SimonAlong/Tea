package com.simon.tea;

import static com.simon.tea.Print.*;

import com.simon.tea.context.Context;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.zzp.am.Ao;
import me.zzp.am.Column;
import me.zzp.am.Index;
import me.zzp.am.Record;
import org.springframework.util.StringUtils;

/**
 * @author zhouzhenyong
 * @since 2018/6/29 上午11:03
 */
@RequiredArgsConstructor(staticName = "of")
public class DBManager {

    @NonNull
    private Context context;
    @Setter
    private String tableName;
    @Getter
    private Ao ao = null;

    public boolean isStart() {
        return null != ao;
    }

    public void startDb(String jdbcUrl, String userName, String password) {
        ao = null;
        ao = Ao.open(jdbcUrl, userName, password);
    }

    /**
     * select * from tableName;
     */
    public List<Record> all(String sql) {
        return ao.all(sql);
    }

    public boolean containTable(String tableName) {
        return listTables().contains(tableName);
    }

    public List<String> listTables() {
        return ao.listTables(ao.getCatalog(), ao.getSchema()).stream().map(t -> t.getName())
            .collect(Collectors.toList());
    }

    public List<Column> listColumns() {
        return ao.listColumns(ao.getCatalog(), ao.getSchema(), tableName);
    }

    public List<Index> getIndex() {
        return ao.listIndexes(ao.getCatalog(), ao.getSchema(), tableName);
    }

    public String getCatalog() {
        return ao.getCatalog();
    }

    public String getSchema() {
        return ao.getSchema();
    }

    /**
     * 查询其中一个数据
     */
    public Record one(String sql) {
        return ao.one(sql);
    }

    public Integer count(String sql) {
        String countSql = "select count(1) " + sql.substring(sql.indexOf("from " + context.secondWord()));
        return ao.value(Integer.class, countSql);
    }

    public void execute() {
        try {
            String sql = context.getInput();
            if (StringUtils.hasText(sql)) {
                int selectValue = judgeSqlType(sql);
                if (0 == selectValue) {
                    showTable(ao.all(sql), context);
                } else if (1 == selectValue) {
                    showTableList(ao.values(sql), getSelectedColumn(sql), context);
                } else {
                    showLn(ao.execute(sql));
                }
            }
        } catch (Exception e) {
            showError("DB操作异常：" + e);
        }
    }

    /**
     * 根据查询类型判断应该走哪个查询函数
     *
     * @param input sql 语句
     * @return select a,b from xxx   返回 0 select * from xxx     返回 0 select c from xxx     返回 1 其他sql               返回 2
     */
    public int judgeSqlType(String input) {
        String selectColumn = getSelectedColumn(input);
        if (null != selectColumn) {
            if (selectColumn.equals("*")) {
                return 0;
            } else if (selectColumn.contains(",")) {
                return 0;
            } else {
                return 1;
            }
        }
        return 2;
    }

    /**
     * 根据查询类型判断应该走哪个查询函数
     *
     * @param input sql 语句
     * @return select a,b from xxx  返回 a,b select * from xxx    返回 * select c from xxx    返回 c
     */
    public String getSelectedColumn(String input) {
        if (input.startsWith("select")) {
            int fromIndex = input.indexOf(" from ");
            if (-1 != fromIndex) {
                return input.substring("select ".length(), fromIndex);
            }
        }
        return null;
    }
}
