package cn.fuxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RestController;

/**
 * 路由服务启动类
 * @author eddie.lys
 * @since 2023/4/10
 */
@RestController
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class RouterApplication {
    public static void main(String[] args) {
        SpringApplication.run(RouterApplication.class, args);
    }
}