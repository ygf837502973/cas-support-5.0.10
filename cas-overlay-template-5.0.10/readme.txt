#########2017.11.06 add by guanfeng_yang#########
1.编译完成后会缺少驱动jar包和验证码jar包，etc/lib可以找到
2.编译完成后，复制etc/config下application.properties和log4j2.xml到cas\WEB-INF\classes下替换原文件，再根据具体情况修改配置文件
3.该版本编译后启动会抛异常，删除cas\WEB-INF\lib\cas-server-support-reports-5.0.10.jar可解决问题，目前尚不知该jar包的作用。删除后对主要功能没影响