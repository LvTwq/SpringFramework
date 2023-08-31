package com.southwind.interceptor;

import com.southwind.entity.LogCollectModel;
import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

/**
 * @author 吕茂陈
 * @description
 * @date 2022/11/30 15:18
 */
@Slf4j
public class RequestLogCollectInterceptor extends ServerHttpRequestDecorator {


    private LogCollectModel logCollectModel;

    public RequestLogCollectInterceptor(ServerHttpRequest delegate, LogCollectModel logCollectModel) {
        super(delegate);
        this.logCollectModel = logCollectModel;
    }


    @Override
    public Flux<DataBuffer> getBody() {
        return super.getBody().doOnNext(dataBuffer -> {
            try (ByteArrayOutputStream baoStream = new ByteArrayOutputStream()) {
                Channels.newChannel(baoStream).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
                String body = IOUtils.toString(baoStream.toByteArray(), StandardCharsets.UTF_8.name());
                logCollectModel.setTargetDetail(body);
            } catch (Exception e) {
                log.error("日志采集拦截请求获取请求体失败", e);
            }
        });
    }
}
