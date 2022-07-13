package com.southwind.filter;

import com.southwind.service.impl.YwService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author 吕茂陈
 * @date 2022/03/01 15:59
 */
@Slf4j
//@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MyFilter implements Filter {

    private final YwService ywService;
    /**
     * 容器启动初始化过滤器时被调用，整个生命周期只会被调用一次（这个方法必须成功，不然过滤器不会起作用）
     *
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Filter 前置处理！！！");
    }

    /**
     * 每一次请求都会调用该方法（几乎对所有进入容器的请求起作用）
     *
     * @param request
     * @param response
     * @param chain    用来调用下一个过滤器Filter
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Filter 处理中！！！");
        ywService.test();
        chain.doFilter(request, response);
    }

    /**
     * 容器销毁时，过滤器实例调用该方法，一般在方法中销毁或者关闭资源，整个生命周期也只会被调用一次
     */
    @Override
    public void destroy() {
        log.info("Filter 后置处理！！！");
    }
}
