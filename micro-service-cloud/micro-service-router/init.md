### bootstrap.yml - on local
```yaml
spring:
  application:
    name: micro-service-router
  profiles:
    #指定启动环境
    active: dev
  cloud:
    nacos:
      server-addr: 124.222.147.194:8848
      config:
        # 区分环境：开发环境、测试环境、预发布环境、⽣产环境
        # local-dev的namespace-id
        namespace: 6007135b-5d03-4e83-b338-387e4951b1e6
        # 区分不同应⽤：同⼀个环境内，不同应⽤的配置，通过group来区分。
        group: xw-local-router
        # 文件格式
        file-extension: yaml
        # 自动刷新配置文件,默认true
        refresh-enabled: true
```

### application.yml - on nacos
```yaml
server:
  port: 8000
spring:
  data:
    redis:
      host: 124.222.147.194
      port: 6379
      password: Qwer7410
  cloud:
    inetutils:
      timeout-seconds: 10
    gateway:
      httpclient:
        pool:
          max-idle-time: 10000
      routes:
        - id: micro-service-sso
          uri: lb://micro-service-sso
          predicates:
            - Path=/sso/**
          filters:
            - StripPrefix=1
```