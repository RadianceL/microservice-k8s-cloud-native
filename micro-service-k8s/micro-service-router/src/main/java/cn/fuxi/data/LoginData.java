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
public class LoginData {

    private String username;

    private String password;
}
