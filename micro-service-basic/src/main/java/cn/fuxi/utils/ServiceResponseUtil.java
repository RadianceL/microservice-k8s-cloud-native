package cn.fuxi.utils;


import cn.fuxi.common.response.ServiceResponse;
import cn.fuxi.exception.InternalServiceException;

import java.util.Objects;

/**
 * 服务间返回值处理工具
 *
 * @author eddie.lys
 * @since 2023/5/11
 */
public class ServiceResponseUtil {

    /**
     * 获取返回值数据
     */
    public static <T> T getResponseData(ServiceResponse<T> response) {
        T data = response.getData();
        if (!response.isSuccess()) {
            throw new InternalServiceException("internal service response failure", response.getMessage(), data);
        }
        if (Objects.isNull(data)) {
            throw new InternalServiceException("internal service response data is null", response.getMessage(), data);
        }
        return response.getData();
    }
}
