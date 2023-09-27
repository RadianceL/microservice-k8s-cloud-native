package cn.fuxi.common.response;

import cn.fuxi.ApplicationAppConstants;
import com.olympus.base.utils.support.exception.ExtendRuntimeException;
import com.olympus.base.utils.support.globalization.GlobalMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 系统通用返回值
 *
 * @author eddie
 * @since 2020/2/5
 */
@Data
@NoArgsConstructor
public class Response<T> {

    public static <T> Response<T> invalidIpPermissions() {
        return ofInvalid(ApplicationAppConstants.RESPONSE_CODE_IP_PERMISSIONS, "P-BASICS-00011");
    }
    public static <T> Response<T> invalidToken() {
        return ofInvalid(ApplicationAppConstants.RESPONSE_CODE_TOKEN, "P-BASICS-00012");
    }

    /**
     * 标准返回代码
     */
    private String code;
    /**
     * 系统信息
     */
    private String message;
    /**
     * 业务参数返回
     */
    private T data;

    /**
     * 多语言无效返回值处理
     */
    public static <T> Response<T> ofInvalid(String code, String languageCode) {
        Response<T> response = new Response<>();
        response.setCode(code);
        response.setMessage(GlobalMessage.of(languageCode));
        return response;
    }

    public static Response<String> ofException(ExtendRuntimeException e) {
        Response<String> response = new Response<>();
        response.setCode(e.getErrorCode());
        response.setMessage(e.getErrorMessage());
        return response;
    }

    public Response(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> Response<T> ofSuccess(T data) {
        Response<T> response = new Response<>();
        response.setCode(ApplicationAppConstants.STATE_OK);
        response.setMessage("成功");
        response.setData(data);
        return response;
    }

    public static <T> Response<T> ofError(String systemMessage) {
        Response<T> response = new Response<>();
        response.setCode(ApplicationAppConstants.STATE_FAILED);
        response.setMessage(systemMessage);
        return response;
    }

    /**
     * TerryQi 补充，在某些情况下还需要设置data，可以在data中进一步描述错误，以便前端后端快速确定问题
     * <p>
     * 2021-09-25
     *
     * @param systemMessage
     * @param <T>
     * @return
     */
    public static <T> Response<T> ofError(String systemMessage, T data) {
        Response<T> response = new Response<>();
        response.setCode(ApplicationAppConstants.STATE_FAILED);
        response.setMessage(systemMessage);
        response.setData(data);
        return response;
    }

    public static <T> Response<T> ofError(String code, String message, T data) {
        Response<T> response = new Response<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> Response<List<T>> newListDataResponseInstance(List<T> data) {
        Response<List<T>> response = new Response<>();
        response.setCode(ApplicationAppConstants.STATE_OK);
        response.setMessage(GlobalMessage.of("P-BASICS-00020"));
        if (Objects.isNull(data)) {
            response.setData(new ArrayList<>(0));
        } else {
            response.setData(data);
        }
        return response;
    }
}
