package com.southwind.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.southwind.service.impl.YwService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 吕茂陈
 * @date 2022/03/01 16:02
 */
@Slf4j
@Component
public class MyInterceptor implements HandlerInterceptor {

    @Autowired
    private YwService ywService;

    /**
     * 请求处理之前（进入controller之前），进行调用
     *
     * @param request
     * @param response
     * @param handler
     * @return 返回值为false，视为该请求结束，不仅自身的拦截器会失效，还会导致其它拦截器不再执行
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Interceptor 前置！！！");
        return true;
    }

    /**
     * 只有在 preHandle() 方法返回值为true 时才会执行。
     * 会在Controller 中的方法调用之后，DispatcherServlet 返回渲染视图之前被调用
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("Interceptor 处理中！！！");
        ywService.test();
    }

    /**
     * 只有在 preHandle() 方法返回值为true 时才会执行。在整个请求结束之后， DispatcherServlet 渲染了对应的视图之后执行
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("Interceptor 后置！！！");
    }
}
