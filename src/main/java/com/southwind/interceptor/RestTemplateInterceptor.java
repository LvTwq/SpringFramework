package com.southwind.interceptor;

import com.southwind.utils.MdcUtil;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author 吕茂陈
 * @date 2022-07-28 10:31
 */
@Component
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {


    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        httpRequest.getHeaders().set("traceId", MdcUtil.get());
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}
