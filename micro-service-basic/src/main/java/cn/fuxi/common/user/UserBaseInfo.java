package cn.fuxi.common.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户信息
 *
 * @author eddie.lys
 * @since 2023/5/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBaseInfo {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 角色权限
     */
    private List<String> authoritiesRoles;
}
