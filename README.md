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
