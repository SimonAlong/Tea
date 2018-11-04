package com.simon.cloud.core;

import java.nio.charset.Charset;

/**
 * @author zhouzhenyong
 * @since 2018/6/25 下午9:43
 */
public interface Constant {

    String BASE_CATALOG = "tea";
    String SYS_MODULE = "tea";
    String DEFAULT_CMD = "default";
    String TABLES = "tables";
    String INDEX = "index";
    String FROM = "from";
    String STRUCT = "struct";

    Charset BASE_CHARSET = Charset.forName("UTF-8");
    String MODULE_PATH = "root".equals(System.getProperty("user.name")) ? "/var/tea"
        : System.getProperty("user.home") + "/.cache/tea";
}
