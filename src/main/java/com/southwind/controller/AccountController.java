package com.southwind.controller;

import com.southwind.config.RabbitConfiguration;
import com.southwind.entity.Account;
import com.southwind.entity.MqUser;
import com.southwind.service.impl.MqUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * @author 吕茂陈
 */
@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AccountController {

    private final MqUserService mqUserService;

    private final RabbitTemplate rabbitTemplate;

    @GetMapping("/{url}")
    public String redirect(@PathVariable("url") String url) {
        return url;
    }

    @PostMapping("/login")
    public String login(String username, String password, Model model) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            Account account = (Account) subject.getPrincipal();
            subject.getSession().setAttribute("account", account);
            return "index";
        } catch (UnknownAccountException e) {
            log.error("错误：", e);
            model.addAttribute("msg", "用户名错误！");
            return "login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码错误！");
            e.printStackTrace();
            return "login";
        }
    }

    @GetMapping("/unauth")
    @ResponseBody
    public String unauth() {
        return "未授权，无法访问！";
    }

    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }


    @GetMapping("register")
    @ResponseBody
    public void register(){
        //模拟10个用户注册
        IntStream.rangeClosed(1, 10).forEach(i -> {
            MqUser user = mqUserService.register();
            //模拟50%的消息可能发送失败
            if (ThreadLocalRandom.current().nextInt(10) % 2 == 0) {
                //通过RabbitMQ发送消息
                rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE, RabbitConfiguration.ROUTING_KEY, user);
                log.info("发送 mq user {}", user.getId());
            }
        });
    }
}
