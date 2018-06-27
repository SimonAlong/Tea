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

### 2、brew下载
```plain
brew install tea
```
目前这种暂时还不支持

# 用法
## 一、<span data-type="color" style="color:rgb(57, 57, 57)">common命令集合</span>
tea：启动
tea log/db/...：这种启动可以直接进入对应的模块

ls ：展示当前配置下的配置，范围较下面的这小
ls moduleName ：展示模块内容中的所有配置
ls -l：纵向展示（不一定搞）

cd log/db/...：进入指定的模块
exit：退出系统
quit：退出当前模块，返回到上一层模块
his：查看最近使用的命令

help: 显示每个命令的用法
help db或者log或者其他，可以直接查看每个模块中的其他的命令
cat: 命令展示对应配置的内容
cat db/cfgName 查看配置内容

## 二、<span data-type="color" style="color:rgb(57, 57, 57)">db命令集合</span>
### a、配置命令

show/cat：查看配置的数据
show/cat configName ：查看某个配置的内容，这个得在对应的某个数据库中才行
show/cat configName@dbName：查看某个数据库中的某个配置的内容，这个可以站在最外层

insert：增加配置
insert configName "name:xxx, password:xxx"： 增加某个配置的内容，这个得在对应的某个数据库中才行
insert configName@dbName "name:xxx, password:xxx"：直接在外层增加某个配置的内容（约定，一个数据库中不能有多个相同的内容）

delete：删除配置
同上
load：载入配置（启动数据库连接）
同上：
load config：同上
load configName@dbName：同上

<span data-type="color" style="color:#F5222D">注意：</span>
对于数据库配置这里有测试的，还有预发环境的，还有线上环境的
test, pre, online
其中获取的默认是测试环境的，如果想要获取对应环境的，那么需要这样
show configName:test和 show configName是一样的
show configName@dbName:test 和 show configName@dbName 是一样的

### b、数据库展示相关命令
* #### <a name="bduokb"></a>查询表
show/cat dbname
直接展示对应表的前20行

show/cat dbname 100
展示对应表的前100行

show/cat dbname 20,100
展示对应表的20到100行中间的数据

其他的sql语句也是支持的：
select \* from xxx where ...
* #### <a name="lwr2gx"></a>展示表的结构
show struct dbname
将表的结构打印出来
* #### <a name="ow6akz"></a>展示表的DDL
show ddl dbname
### c、数据库操作相关命令
对于所有数据库的增删改查都是可以的
select ...
insert ...
update ...
delete ...
这些都是支持的

## 三、<span data-type="color" style="color:rgb(57, 57, 57)">log命令集合</span>
### a、日志配置的命令


