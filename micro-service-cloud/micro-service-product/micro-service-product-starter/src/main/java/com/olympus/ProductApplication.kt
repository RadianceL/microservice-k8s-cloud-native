package com.olympus

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.web.bind.annotation.RestController

/**
 * ${DESCRIPTION}
 *
 * @author eddie.lys
 * @since 2023/4/17
 */
@RestController
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
class ProductApplication

fun main(args: Array<String>) {
    SpringApplication.run(ProductApplication::class.java, *args)
}