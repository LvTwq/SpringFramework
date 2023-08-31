package com.southwind.interceptor;

import com.southwind.entity.LogCollectModel;
import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author 吕茂陈
 * @description
 * @date 2022/11/30 15:29
 */
@Slf4j
public class ResponseLogCollectInterceptor extends ServerHttpResponseDecorator {


    private LogCollectModel logCollectModel;

    public ResponseLogCollectInterceptor(ServerHttpResponse delegate, LogCollectModel logCollectModel) {
        super(delegate);
        this.logCollectModel = logCollectModel;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        Flux<DataBuffer> buffer = Flux.from(body);
        return super.writeWith(buffer.publishOn(Schedulers.boundedElastic()).doOnNext(dataBuffer -> {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                Channels.newChannel(outputStream).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
                // 响应体
                String bodyRes = IOUtils.toString(outputStream.toByteArray(), StandardCharsets.UTF_8.name());
                // 某些请求是重定向，没有响应体
            } catch (Exception e) {
                log.error("日志采集拦截请求获取响应体失败", e);
            }
        }));
    }
}
