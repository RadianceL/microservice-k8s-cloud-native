package cn.fuxi.utils;

import com.olympus.base.utils.support.globalization.GlobalMessage;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 异常处理器 <br/>
 *
 * @author eddie.lys
 * @since 2021/1/19
 */
public class ExceptionHelper {

    public static boolean isDuplicateKeyException(Throwable e) {
        Throwable cause = e.getCause();
        return cause instanceof SQLIntegrityConstraintViolationException;
    }

    /**
     * 获取异常详情
     * @param throwable     异常信息
     * @return              异常信息
     */
    public static String getExceptionDetail(Throwable throwable) {
        StringBuilder builder = new StringBuilder();
        builder.append(GlobalMessage.of("P-BASICS-00022"));
        builder.append(throwable.getMessage());
        builder.append("<br/>");

        StackTraceElement[] stackTraces = throwable.getStackTrace();
        for (StackTraceElement trace : stackTraces) {
            builder.append(trace.toString()).append("<br/>");
        }
        return builder.toString();
    }
}
