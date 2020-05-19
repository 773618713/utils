## 工具类项目

之前的数据库连接url中端口和实例名中间使用 / （按理之前这样也是可以访问数据库的，不知道什么原因不能访问了，所以改为下面这样）
sc_db_url=jdbc:oracle:thin:@localhost:1521/orcl

修改后使用 :  (修改过后，才可以访问数据库)
sc_db_url=jdbc:oracle:thin:@localhost:1521:orcl