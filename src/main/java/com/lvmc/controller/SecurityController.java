package com.lvmc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lvmc.entity.LoginParam;

import lombok.RequiredArgsConstructor;

/**
 * @author 吕茂陈
 * @date 2022/02/28 08:54
 */
@RestController
@RequestMapping("security")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SecurityController {

//    private final AuthenticationManager authenticationManager;

//    private final PasswordEncoder passwordEncoder;

    @PostMapping("login")
    public String login(@RequestBody LoginParam param) {
        // 生成一个包含账号密码的认证信息
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(param.getUsername(), param.getPassword());
        // AuthenticationManager校验这个认证信息，返回一个已认证的 Authentication
//        Authentication authentication = authenticationManager.authenticate(token);
//        // 将返回的Authentication存到上下文中
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "登录成功";
    }


//    @PostMapping("register")
//    public String register(@RequestBody UserParam param) {
//        UserEntity user = new UserEntity();
//        user.setUsername(param.getUsername()).setPassword(passwordEncoder.encode(param.getP))
//    }
}
