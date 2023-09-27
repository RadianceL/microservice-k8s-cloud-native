package cn.fuxi.filter;

import cn.fuxi.common.response.Response;
import com.olympus.base.utils.support.exception.ExtendRuntimeException;
import com.olympus.common.web.access.FilterFailureType;
import com.olympus.common.web.access.filter.AbstractRouterFilterVerify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 准许通过过滤器
 *
 * @author eddie
 * @since 2020/2/5
 */
@Slf4j
@Component
public class AccessCheckFilter extends AbstractRouterFilterVerify<Response<String>> {

    /**
     * 校验"/api"开头的接口
     */
    private static final String API_PREFIX = "/api";

    private static final String PROVIDER_PREFIX = "/provider";
    /**
     * 准许通过的路径 for test "*"
     */
    private static final List<String> ACROSS_PERMISSIONS = Collections.emptyList();

    @Override
    public boolean verifyRemoteIpPermissions(String realIp) {
        return true;
    }

    @Override
    public boolean verifyRequestParameter(String url, HttpHeaders httpHeaders, ServerHttpRequest serverHttpRequest) {
        return !url.contains(PROVIDER_PREFIX);
    }

    @Override
    public void onSuccessListener(String traceId, ServerHttpRequest serverHttpRequest) {
        super.addHttpHeader(serverHttpRequest, "source-type", "API-ROUTER");
    }

    @Override
    public Response<String> onFailureListener(ServerHttpRequest serverHttpRequest, FilterFailureType filterFailureType) {
        return switch (filterFailureType) {
            case REMOTE_IP -> Response.invalidIpPermissions();
            case HEADER_PARAMETER -> Response.invalidToken();
        };
    }

    @Override
    public Response<String> onExceptionListener(ServerHttpRequest serverHttpRequest, ExtendRuntimeException e) {
        return Response.ofException(e);
    }
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
