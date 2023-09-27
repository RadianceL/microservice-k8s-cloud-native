package cn.fuxi.config.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author eddie.lys
 * @since 2023/9/27
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ResponseBody
//    @ExceptionHandler(value = BadCredentialsException.class)
//    public Object serviceExceptionHandler(BadCredentialsException ex) {
//        // 包装 CommonResult 结果
//        return ex.getMessage();
//    }

}
