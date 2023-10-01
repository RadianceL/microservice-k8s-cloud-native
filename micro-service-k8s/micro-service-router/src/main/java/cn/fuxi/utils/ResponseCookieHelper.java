package cn.fuxi.utils;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

/**
 * cookie辅助构造器
 * @author eddie.lys
 * @since 2023/10/1
 */
public class ResponseCookieHelper {

    public static ResponseCookie createCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .maxAge(Duration.ofDays(30))
                .httpOnly(true)
                .path("/")
                .build();
    }
}
