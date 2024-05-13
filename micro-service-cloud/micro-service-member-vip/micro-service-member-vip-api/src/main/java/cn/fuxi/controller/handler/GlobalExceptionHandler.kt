package cn.fuxi.controller.handler

import cn.fuxi.common.response.Response
import cn.fuxi.utils.ExceptionHelper
import com.olympus.base.utils.support.globalization.GlobalMessage
import com.olympus.logger.logger.SmileEventLogger
import com.olympus.smile.config.Environment
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import java.util.stream.Collectors

/**
 * 全局异常处理器
 *
 * @author eddie.lys
 * @since 2021/1/7
 */
@ResponseBody
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Throwable::class)
    fun exceptionHandler(request: HttpServletRequest?, e: Exception): Response<String> {
        SmileEventLogger.error("全局异常处理器拦截异常: {}", ExceptionHelper.getExceptionDetail(e))
        if (Environment.getInstance().isDaily || Environment.getInstance().isStaging) {
            return Response.ofError(e.message)
        }
        return Response.ofError(GlobalMessage.of("P-MATERIAL-00245"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun exceptionHandler(e: MethodArgumentNotValidException): Response<String> {
        SmileEventLogger.error("入参缺失拦截异常: {}", ExceptionHelper.getExceptionDetail(e))
        val message: String = e.bindingResult
            .allErrors
            .stream()
            .map { defaultMessageSourceResolvable -> GlobalMessage.of(defaultMessageSourceResolvable.getDefaultMessage()) }
            .collect(Collectors.joining(","))
        return Response.ofError(message)
    }

    /**
     * 针对参数解析的异常单独处理
     * 便于前端发现和定位问题
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun exceptionHandler(e: HttpMessageNotReadableException): Response<String> {
        SmileEventLogger.error("http请求参数转换异常: {}", ExceptionHelper.getExceptionDetail(e))
        return Response.ofError(GlobalMessage.of("P-MATERIAL-00245"), e.message)
    }
}
