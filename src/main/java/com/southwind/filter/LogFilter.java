package com.southwind.filter;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.southwind.entity.LogCollectModel;
import com.southwind.interceptor.RequestLogCollectInterceptor;
import com.southwind.interceptor.ResponseLogCollectInterceptor;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

/**
 * @author 吕茂陈
 * @date 2022-07-28 10:24
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LogFilter implements WebFilter {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();

    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getURI().getPath();
        PATH_MATCHER.match("", uri);

        String userAgentStr = request.getHeaders().getFirst(HttpHeaders.USER_AGENT);
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);

        LogCollectModel logCollectModel = new LogCollectModel();

        Mono<HandlerMethod> handlerMethodMono = requestMappingHandlerMapping.getHandler(exchange)
            .cast(HandlerMethod.class);
        Mono<Optional<HandlerMethod>> optionalHandlerMethodMono = handlerMethodMono.transform(
                mono -> mono.map(Optional::of))
            .defaultIfEmpty(Optional.empty());

        return optionalHandlerMethodMono.zipWhen(optionalHandlerMethod -> {
                ServerWebExchangeDecorator exchangeDecorator = new ServerWebExchangeDecorator(exchange) {
                    @Override
                    public ServerHttpRequest getRequest() {
                        return new RequestLogCollectInterceptor(super.getRequest(), logCollectModel);
                    }

                    @Override
                    public ServerHttpResponse getResponse() {
                        return new ResponseLogCollectInterceptor(super.getResponse(), logCollectModel);
                    }
                };
                return chain.filter(exchangeDecorator)
                    .doOnSuccess(aVoid -> {
                        // 如果是get请求，不会进入ResponseLogCollectInterceptor 中响应体的处理，需要在这里给个结果
                        if (Objects.isNull(logCollectModel.getEventOutcomeResult())) {
                            logCollectModel.setEventOutcomeResult("");
                        }
                    })
                    .doOnError(throwable -> {
                        // 没被捕获的特殊异常
                        if (Objects.isNull(logCollectModel.getEventOutcomeResult())) {
                            logCollectModel.setEventOutcomeResult("")
                                .setEventOutcomeReason(throwable.toString());
                        }
                        log.error("日志采集拦截，该请求失败", throwable);
                    });
            }).map(Tuple2::getT2)
            .doFinally(t -> {
                String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                HttpCookie accessToken = exchange.getRequest().getCookies().getFirst("access_token");
                // 首先取 authorization，authorization 取不到，则取 access_token

            });
    }
}
