package com.lvmc.controller;

import cn.hutool.json.JSONObject;
import com.lvmc.service.SayService;
import com.lvmc.service.SubTestService;
import com.lvmc.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.lvmc.component.ConfigValue.endMomentConfig;

/**
 * @author 吕茂陈
 * @date 2022/02/17 18:11
 */
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class HelloController {

    private final List<SayService> sayServiceList;

    private final ApplicationContext applicationContext;

    /**
     * 单例的 controller 注入的 service 也是一次性创建的，即使 service 本身标识了 prototype 也没用
     * 只能让 service 以代理方式注入，这样虽然 controller 本身是单例的，但每次都能从代理获取 service
     * 调试可以发现，注入的 service 是 spring 生成的代理类
     */
    @GetMapping("test")
    public void test() {
        log.info("====================");
        sayServiceList.forEach(SayService::say);
    }


    @GetMapping("test2")
    public void test2() {
        log.info("====================");
        applicationContext.getBeansOfType(SayService.class).values().forEach(SayService::say);
    }

    @GetMapping("good")
    public ResponseEntity<JSONObject> good() {
        log.info("我是controller！！！");
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("message", "200");
        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }

    @GetMapping("good2")
    public JSONObject good2() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("message", "200");
        return jsonObject;
    }




    @GetMapping("bad")
    public ResponseEntity<JSONObject> bad() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("message", "555");
        return new ResponseEntity<>(jsonObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("bad2")
    public JSONObject bad2() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("code", HttpStatus.INTERNAL_SERVER_ERROR);
        jsonObject.putOnce("success", false);
        return jsonObject;
    }


    @GetMapping("{appName:.+}")
    public void test01(@PathVariable String appName) {
        // @PathVariable 对于特殊字符截断，windows-v1.3.1.0004 会被截成 windows-v1.3.1，加上 .+
        log.info("{}", appName);
    }


    @GetMapping("static")
    public String getStatic() {
        return endMomentConfig;
    }


    private final TestService testService;
    private final SubTestService subTestService;

    @GetMapping("test/service")
    public void testService() {
        testService.test();
        subTestService.test();
    }


}
