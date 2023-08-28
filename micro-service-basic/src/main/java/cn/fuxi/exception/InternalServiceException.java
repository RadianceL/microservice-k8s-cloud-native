package cn.fuxi.exception;


import com.olympus.base.utils.support.exception.ExtendRuntimeException;
import com.olympus.base.utils.support.globalization.ErrorMessage;
import lombok.Getter;

/**
 * 内服服务异常
 * @author eddie.lys
 * @since 2023/5/11
 */
@Getter
public class InternalServiceException extends ExtendRuntimeException {

    /**
     * 异常错误码
     */
    private String message;
    /**
     * 异常扩展数据
     */
    private Object data;

    public InternalServiceException() {
        super();
    }

    public InternalServiceException(String error, String message, Object data) {
        super(error);
        this.message = message;
        this.data = data;
    }

    public InternalServiceException(String error) {
        super(error);
    }

    public InternalServiceException(String error, Object... args) {
        super(error);
    }

    public InternalServiceException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public InternalServiceException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
