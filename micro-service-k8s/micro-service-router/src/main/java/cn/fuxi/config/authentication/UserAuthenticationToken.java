package cn.fuxi.config.authentication;

import cn.fuxi.common.user.LoginTypeEnums;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author eddie.lys
 * @since 2023/5/11
 */
@Setter
@Getter
public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {
    /**
     * 登陆类型
     */
    private LoginTypeEnums loginType;


    public UserAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
        setAuthenticated(false);
    }

    public UserAuthenticationToken(Object principal, Object credentials, LoginTypeEnums loginType) {
        super(principal, credentials);
        this.loginType = loginType;
    }

    public UserAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, LoginTypeEnums loginType) {
        super(principal, credentials, authorities);
        this.loginType = loginType;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
