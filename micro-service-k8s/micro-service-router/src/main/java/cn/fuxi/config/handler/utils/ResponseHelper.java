package cn.fuxi.config.handler.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author eddie.lys
 * @since 2023/9/27
 */
public class ResponseHelper {

    @SneakyThrows
    public static Mono<Void> out(ServerHttpResponse response, Object result) {
        ObjectMapper objectMapper=new ObjectMapper();
        byte[] bits = objectMapper.writeValueAsString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}
