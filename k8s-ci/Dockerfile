FROM registry.cn-shanghai.aliyuncs.com/misumi/e-jdk:17.0.4.1

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
    "-XX:+UnlockDiagnosticVMOptions", \
    "-XX:+UnlockExperimentalVMOptions", \
    "-XX:+AlwaysPreTouch", \
    "-XX:MaxRAMPercentage=75", \
    "-XX:+DisableExplicitGC", \
    "-XX:+UseG1GC", \
    "-XX:G1HeapRegionSize=8m", \
    "-XX:G1ReservePercent=15", \
    "-XX:+UseStringDeduplication", \
    "--add-opens=java.base/java.lang=ALL-UNNAMED", \
    "--add-opens=java.base/java.util=ALL-UNNAMED", \
    "--add-opens=java.base/sun.net.util=ALL-UNNAMED", \
    "-jar", "/application.jar", \
    "-Dreactor.netty.pool.leasingStrategy=lifo", \
    "-Dio.netty.tryReflectionSetAccessible=true"]