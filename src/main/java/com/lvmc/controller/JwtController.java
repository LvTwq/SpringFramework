package com.lvmc.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lvmc.context.JwtContext;
import com.lvmc.entity.User;
import com.lvmc.utils.JwtUtil;

/**
 * @author 吕茂陈
 * @date 2022/02/27 15:21
 */
@RestController
@RequestMapping("jwt")
public class JwtController {

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        // 判断账号密码是否正确，这一步肯定是要读取数据库中的数据来进行校验的，这里为了模拟就省去了
        if ("admin".equals(user.getUsername()) && "admin".equals(user.getPassword())) {
            // 如果正确的话就返回生成的token（注意哦，这里服务端是没有存储任何东西的）
            return JwtUtil.generate(user.getUsername());
        }
        return "账号密码错误";
    }

    @GetMapping("api")
    public String api(HttpServletRequest request) {
        // 从请求头中获取token字符串
        String jwt = request.getHeader("Authorization");
        // 解析失败就提示用户登录
        if (JwtUtil.parse(jwt) == null) {
            return "请先登录";
        }
        // 解析成功就执行业务逻辑返回数据
        return "api成功返回数据";
    }

    @GetMapping("api2")
    public String api2() {
        return JwtContext.getCurrentUserName();
    }
}
