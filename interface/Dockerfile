# 使用官方的 OpenJDK 8 作为基础镜像
FROM openjdk:8

# 维护者信息
MAINTAINER wuhao<1345498749@qq.com>

# 设置 Java 选项
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# 设置时区为 Asia/Shanghai
ENV TZ=Asia/Shanghai
RUN cp /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

VOLUME /springboot/api_gateway
ADD app.jar app.jar


# 暴露应用程序运行的端口
EXPOSE 8102

# 运行 Spring Boot 应用程序
ENTRYPOINT ["java", "-jar", "/app.jar"]