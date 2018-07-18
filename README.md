# Tea
个人工作台项目，就是将自己工作用的一些功能，集合到一起作为一个项目进行统一使用，包括db处理，日志处理，还有其他各种各样的处理，方便自己平常工作中使用
地址：[https://github.com/SimonAlong/tea](https://github.com/SimonAlong/tea)

# Getting started
### 1.本地使用
* #### <a name="ir7phf"></a>在~/.bash\_profile 里面添加
```plain
source ~/.bashrc
```

* #### <a name="0azyrn"></a>在~/.bashrc文件里面添加
```plain
alias tea='java -jar yourPathName/tea-1.0-SNAPSHOT.jar'
```
其中yourPathName 是自己的tea的jar包放置的路径

<span data-type="background" style="background-color:#F5222D">注意：</span>
在tea-1.0-SNAPSHOT.jar相同包下面需要将active\_map.jar也放在同一目录，目前有点搓，还不能动态加载jar包中的jar包

### 2、brew下载
```plain
brew install tea
```
目前这种暂时还不支持

# 用法
## 一、<span data-type="color" style="color:rgb(57, 57, 57)">common命令集合</span>
`tea`
启动

`ls/ll `
展示当前配置下的配置，范围较下面的这小

`ls/ll moduleName `
展示模块内容中的所有配置

`cd log/db/...`
进入指定的模块

`exit`
退出系统

`quit`
退出当前模块，返回到上一层模块

`cat`
配置文件的查看

`edit`
配置文件的修改

`version`
版本号

`help`
查看命令信息

`rename`
配置文件重命名， 用法：`rename sourceFileName targetFileName`

`load`
载入配置（创建数据库连接）,用法`：load configName`

`touch/create`
新增配置文件	用法：`touche/create configName`

`rm/del`
配置文件删除		用法：`rm/delete/del configName`

-p: 用于展示页面索引：用法：`show xxxx -p 3` 展示当前页面的第三页
## 二、<span data-type="color" style="color:rgb(57, 57, 57)">db命令集合</span>
### a、数据库展示相关命令
* #### <a name="bduokb"></a>查询表
`show tableName`
展示第一页，每页"+ Print.PAGE\_SIZE+"行数据

`show tableNames`
展示当前库中的所有表，每页"+ Print.PAGE\_SIZE+"行数据

`show tableName -p num`
展示第num页的数据，每页"+ Print.PAGE\_SIZE+"行数据

`show tableName -lm 1,200`
展示前1~200条数据，每页"+ Print.PAGE\_SIZE+"行数据

其他的sql语句也是支持的：
`select * from xxx where ...`

* #### <a name="lwr2gx"></a>展示表的结构
`show struct dbname`    版本：0.2.0 支持
将表的结构打印出来

* #### <a name="ow6akz"></a>展示表的DDL
`show ddl dbname`    版本：0.2.0 支持

* #### <a name="vdi1nq"></a>展示索引
`show index tableName`

<span data-type="color" style="color:#F5222D">注意：</span>
以下是0.2.0版本期望
对于数据库配置这里有测试的，还有预发环境的，还有线上环境的
`test, pre, online`
其中获取的默认是测试环境的，如果想要获取对应环境的，那么需要这样
show configName:test和 show configName是一样的
show configName@dbName:test 和 show configName@dbName 是一样的

### b、数据库操作相关命令
对于所有数据库的增删改查都是可以的
`select ...`
`insert ...`
`update ...`
`delete ...`
这些都是支持的

##


