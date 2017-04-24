# org.lucius.iyanla
gradle任务
1. 构建
  gradle build
2. 发布到本地Virgo 3.7.0并启动
  gradle startVirgoTomcat
3. 停止Virgo 并清楚文件(logs/repository:usr/work等临时目录)
  gradle stopVirgoTomcat
4. 单独删除临时目录
  gradle clearVirgoTomcat
5. 打开日志目录
  gradle openLogDir
6. 发布私服
  gradle uploadArchives
7. 测试地址：http://localhost:8080/iyanla/auth/app/user/index
8. 官网下载的Virgo中带的spring版本为4.2.9，本应用升级至4.3.7
9. 官网下载的Virgo中带的blueprint版本为2.0.0.RELEASE，本应用升级至2.1.0.M2
10.需要删除官网下载Virgo中相关repository/ext中spring、blueprint、slf4j相关的jar包
11.如果遇到启动是要求jdk运行环境比如：
Require-Capability: osgi.ee;filter:="(&(osgi.ee=JavaSE)(version=1.8))"或者
Bundle-RequiredExecutionEnvironment: J2SE-1.6
可以删除这些jar包中的相关限制

12.参考资料：
https://www.eclipse.org/gemini/blueprint/documentation/reference/2.1.0.M2/html/index.html
http://docs.spring.io/spring-data/redis/docs/1.8.1.RELEASE/reference/pdf/spring-data-redis-reference.pdf
https://docs.gradle.org/current/dsl/
https://docs.gradle.org/current/userguide/userguide.html

13. 修改jackson-dataformat-msgpack-0.8.12.jar包中的Import-Package: sun.misc 后面加上;resolution:="optional"
