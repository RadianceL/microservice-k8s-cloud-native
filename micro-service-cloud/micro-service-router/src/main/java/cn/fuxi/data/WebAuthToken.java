package cn.fuxi.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务端认证token
 *
 * @author eddie.lys
 * @since 2023/10/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebAuthToken {
    /**
     * 认证JWT
     */
    private String authToken;
    /**
     * 刷新token
     */
    private String refreshToken;
}
