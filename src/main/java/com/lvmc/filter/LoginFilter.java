package com.lvmc.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.lvmc.entity.User;

/**
 * @author 吕茂陈
 * @date 2022/02/27 14:34
 */
//@Component
public class LoginFilter extends OncePerRequestFilter {


    /**
     * 容器中的每一次请求都会调用该方法
     *
     * @param request
     * @param response
     * @param filterChain 用来调用下一个过滤器filter
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 简单的白名单，登录这个接口直接放行
        if ("/lmc/session/login".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        // 已登录就放行
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            filterChain.doFilter(request, response);
            return;
        }
        // 走到这里就代表是其他接口，且没有登录；设置响应数据类型为json（前后端分离）
        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            // 设置响应内容，结束请求
            out.write("请先登录");
            out.flush();
        }
    }

}
