package com.lvmc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @description
 * @date 2025/1/23 9:35
 */
@Slf4j
@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DictController {

/*    private final LicenseDict licenseDict;


    @GetMapping("/license")
    public String getLicense() {
        return licenseDict.getLicenseBase64();
    }*/
}
