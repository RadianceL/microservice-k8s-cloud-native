FROM openjdk:17-jdk-alpine

# 传入参数
ARG ACTIVE_PROFILE
ARG PROJECT_FILE

# copy arthas
# COPY --from=hengyunabc/arthas:latest /opt/arthas /opt/arthas

# 添加业务服务jar包
COPY $PROJECT_FILE /application.jar
ENV APPLICATION_PROFILE=$ACTIVE_PROFILE
##docker 服务启动参数
ENTRYPOINT ["java", \
    "-Xms1024M", \
    "--add-opens=java.base/java.lang=ALL-UNNAMED", \
    "--add-opens=java.base/java.util=ALL-UNNAMED", \
    "--add-opens=java.base/sun.net.util=ALL-UNNAMED", \
    "-XX:+UseG1GC", \
    "-XX:+UseStringDeduplication", \
    "-XX:+HeapDumpOnOutOfMemoryError", \
    "-XX:HeapDumpPath=/usr/local/app/oom", \
    "-Xlog:gc*", \
    "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:7080", \
    "-jar", "/application.jar", \
    "-Dreactor.netty.pool.leasingStrategy=lifo"]