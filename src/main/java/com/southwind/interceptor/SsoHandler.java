package com.southwind.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.RequiredArgsConstructor;

/**
 * @author 吕茂陈
 * @date 2021/08/24 09:00
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SsoHandler implements HandlerInterceptor {
//
//    private final RestTemplate restTemplate;
//
//    /**
//     * sso-server地址
//     */
//    private static final String SSO_SERVER = "http://localhost:8080/sso";
//    private static final String SSO_SERVER_VERIFY = "/verify";
//    private static final String SSO_CHECK_LOGIN = "/checkLogin";
//
//    /**
//     * session 中的登录标志
//     */
//    private static final String IS_LOGIN = "isLogin";
//
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HttpSession session = request.getSession();
//
//        // 是否已经存在会话
//        boolean isLogin = (Boolean) session.getAttribute(IS_LOGIN);
//        if (isLogin) {
//            return true;
//        }
//        // 校验ticket
//        String ticket = request.getParameter("ticket");
//        if (StringUtils.isEmpty(ticket)) {
//            ssoSendRedirect(request, response);
//            return true;
//        } else {
//            // 去sso-server 校验ticket
//            if (verify(request, session, ticket)) {
//                return true;
//            } else {
//                ssoSendRedirect(request, response);
//            }
//        }
//        return false;
//    }
//
//    private boolean verify(HttpServletRequest request, HttpSession session, String token) {
//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("token", token);
//        paramMap.put("service", "/logout");
//        paramMap.put("jsessionid", session.getId());
//        String str = restTemplate.postForObject(SSO_SERVER + SSO_SERVER_VERIFY, paramMap, String.class);
//        if ("true".equals(str)) {
//            session.setAttribute(IS_LOGIN, true);
//            return true;
//        }
//        return false;
//    }
//
//
//    private void ssoSendRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String service = request.getServletPath();
//        response.sendRedirect((SSO_SERVER + SSO_CHECK_LOGIN + "?service=" + service));
//    }
}
