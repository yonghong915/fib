fpg-springboot
    fpg-cloud
        fpg-config
        fpg-eureka
        fpg-gateway
    fpg-framework
        fpg-api
        fpg-commons
        fpg-core
        fpg-mq
        fpg-workflow
    fpg-bank
        fpg-bip
        fpg-crm
        fpg-ecif
        fpg-esb
        fpg-oims
        fpg-pcms
        fpg-uias
        fpg-upp
        
docker 打包:
1.开启docker 2375端口
/usr/lib/systemd/system/docker.service文件中添加
ExecStart= -H tcp://0.0.0.0:2375
systemctl daemon-reload
systemctl restart docker

2.设置环境变量DOCKER_HOST DOCKER_HOST=tcp://192.168.56.11:2375

3.项目根目录构建docker file(Dockerfile)

4.POM文件中构建dockerfile-maven-plugin插件

5.运行maven打包
mvn clean package -Dmaven.test.skip=true dockerfile:build

6.服务器中启动应用 
docker run --name fib-eureka1 -p 8761:8761 -d 192.168.56.11:5000/fib-eureka:1.0.0.RELEASE