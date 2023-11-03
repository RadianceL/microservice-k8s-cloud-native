package cn.fuxi.interceptor;

import cn.fuxi.common.keys.headers.RequestHeaders;
import cn.fuxi.common.user.UserInfo;
import com.alibaba.fastjson2.JSON;
import com.olympus.common.web.access.interceptor.AbstractAuthenticationInterceptor;
import com.olympus.logger.logger.SmileEventLogger;
import com.olympus.logger.utils.SmileLocalUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 拦截器
 *
 * @author eddie.lys
 * @since 2023/11/2
 */
public class HttpAuthenticationInterceptor extends AbstractAuthenticationInterceptor {
    @Override
    public boolean onPreHandleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        String authenticatedUserInfo = httpServletRequest.getHeader(RequestHeaders.X_AUTHENTICATED_USER_INFO);
        if (StringUtils.isNotBlank(authenticatedUserInfo)) {
            try {
                UserInfo userInfo = JSON.parseObject(authenticatedUserInfo, UserInfo.class);
                SmileLocalUtils.setCurrentUser(userInfo);
                return true;
            }catch (Throwable e) {
                SmileEventLogger.error("unable to parse user form router header", e);
            }
        }
        return false;
    }

    /**
     * 处理后调用(任何情况)
     */
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        SmileLocalUtils.clear();
    }
}
