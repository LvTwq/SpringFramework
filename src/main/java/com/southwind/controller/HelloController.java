package com.southwind.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 吕茂陈
 * @date 2022/02/17 18:11
 */
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RestController()
public class HelloController {

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
}
