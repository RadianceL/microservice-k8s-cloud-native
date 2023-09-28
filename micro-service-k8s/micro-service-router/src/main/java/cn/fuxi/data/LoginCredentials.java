package cn.fuxi.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author eddie.lys
 * @since 2023/9/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCredentials {
    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 登陆类型
     */
    private String loginType;
}
