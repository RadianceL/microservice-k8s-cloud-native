package com.olympus.handler;

import cn.fuxi.common.response.Response;
import com.olympus.base.utils.support.globalization.GlobalMessage;
import com.olympus.logger.logger.SmileEventLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 全局异常处理器
 *
 * @author eddie.lys
 * @since 2021/1/7
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public Response<String> exceptionHandler(HttpServletRequest request, Exception e) {
        log.error("全局异常处理器拦截异常", e);
        return Response.ofError(e.getMessage());
    }

}
