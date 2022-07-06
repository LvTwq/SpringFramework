package com.southwind.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.southwind.context.JwtContext;
import com.southwind.utils.JwtUtil;

import io.jsonwebtoken.Claims;

/**
 * 拦截器
 * @author 吕茂陈
 * @date 2022/02/27 15:33
 */
public class JwtLoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 在请求处理之前进行调用
     *
     * @param request
     * @param response
     * @param handler
     * @return false：当前请求结束，不仅自身的拦截器失效，也会导致其它拦截器不再执行
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 简单的白名单，登录这个接口直接放行
        if (request.getRequestURI().contains("login")) {
            return true;
        }

        // 从请求头中获取token字符串并解析
        Claims claims = JwtUtil.parse(request.getHeader("Authorization"));
        // 已登录就直接放行
        if (claims != null) {
            // 将我们之前放到token中的userName给存到上下文对象中
            JwtContext.add(claims.getSubject());
            return true;
        }

        // 走到这里就代表是其他接口，且没有登录
        // 设置响应数据类型为json（前后端分离）
        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            // 设置响应内容，结束请求
            out.write("请先登录");
            out.flush();
        }
        return false;
    }

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 只有在 preHandle() 方法返回true，才会执行。
     * 在整个请求结束后，DispatcherServlet 渲染了对应的试图之后执行
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求结束后要从上下文对象删除数据，如果不删除则可能会导致内存泄露
        JwtContext.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
