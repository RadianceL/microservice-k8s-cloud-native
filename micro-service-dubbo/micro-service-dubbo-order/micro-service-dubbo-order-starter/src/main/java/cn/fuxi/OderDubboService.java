package cn.fuxi;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 订单
 *
 * @author eddie.lys
 * @since 2023/9/26
 */
@EnableDubbo
@SpringBootApplication
public class OderDubboService {

    public static void main(String[] args) {
        SpringApplication.run(OderDubboService.class, args);
    }

}
