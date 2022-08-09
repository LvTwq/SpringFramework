package com.southwind.filter;

import com.southwind.utils.MdcUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * @author 吕茂陈
 * @date 2022-07-28 10:24
 */
@Slf4j
//@Component
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        MdcUtil.add(String.valueOf(UUID.randomUUID()));
        log.info("记录请求日志");
        filterChain.doFilter(servletRequest, servletResponse);
        log.info("记录响应日志");
    }
}
