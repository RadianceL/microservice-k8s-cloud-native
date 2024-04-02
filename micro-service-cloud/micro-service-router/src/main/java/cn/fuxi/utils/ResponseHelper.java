package cn.fuxi.utils;

import cn.fuxi.common.response.Response;
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
    public static Mono<Void> writeWithSuccess(ServerHttpResponse serverHttpResponse, Object result) {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bits = objectMapper.writeValueAsString(Response.ofSuccess(result)).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(bits);
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return serverHttpResponse.writeWith(Mono.just(buffer));
    }

    @SneakyThrows
    public static Mono<Void> writeWithFail(ServerHttpResponse serverHttpResponse, String result) {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bits = objectMapper.writeValueAsString(Response.ofError(result)).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(bits);
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return serverHttpResponse.writeWith(Mono.just(buffer));
    }
}
