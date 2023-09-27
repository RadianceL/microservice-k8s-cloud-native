package cn.fuxi.service.impl;

import cn.fuxi.service.SubmitOrderService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author eddie.lys
 * @since 2023/9/26
 */
@DubboService(version = "1.0")
public class SubmitOrderServiceImpl implements SubmitOrderService {
    @Override
    public String sayHello(String hello) {
        return null;
    }
}
