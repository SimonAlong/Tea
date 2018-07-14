package com.simon.tea.processor;

import com.simon.tea.annotation.Cmd;
import com.simon.tea.annotation.Module;
import com.simon.tea.meta.CmdTypeEnum;

/**
 * 该命令只有在 in tableName 命令之后才会正式启用
 * @author zhouzhenyong
 * @since 2018/7/6 下午10:19
 */
@Module(name = "table", visible = false)
public class Table {
    @Cmd(value = "index")
    public void index(){

    }

    @Cmd(value = "struct")
    public void struct(){

    }

    @Cmd(value = "ddl")
    public void ddl(){

    }

    @Cmd(value = "data")
    public void data(){

    }

    @Cmd(value = "columns", type = CmdTypeEnum.DEFAULT)
    public void columns(){

    }
}
