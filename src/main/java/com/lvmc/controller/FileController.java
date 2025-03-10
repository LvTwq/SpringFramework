package com.lvmc.controller;

import cn.hutool.core.io.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * @author 吕茂陈
 * @date 2022-07-12 15:42
 */
@Slf4j
@RestController
@RequestMapping("file")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FileController {

    @Value("${clientLog:/home/spring/clientlog}")
    private String path;

    @PostMapping("upload")
    public String uploadLog(@NonNull List<MultipartFile> multipartFiles, @RequestParam("SystemOS") String systemOs) {
        multipartFiles.forEach(multipartFile -> {
            String filename = multipartFile.getOriginalFilename();
            // 指定为绝对路径
            File file = new File(String.format("%s/%s/%s/%s", new File(path).getAbsolutePath(), systemOs, LocalDate.now(), filename));
            if (!FileUtil.exist(file)) {
                FileUtil.touch(file);
            }
            try {
                // 如果是相对路径，会拼上父路径，导致找不到
                multipartFile.transferTo(file);
            } catch (IOException e) {
                throw new RuntimeException(filename + "上传失败", e);
            }
            log.info("{} 上传成功", filename);
        });
        return "ok";
    }

    @GetMapping("/users/device/usage")
    public String queryDeviceUsage(@RequestParam @NotBlank String account,
                                   @RequestParam(value = "timelimit", required = false, defaultValue = "true") boolean timeLimit) {
        log.info("{}", account);
        log.info("timeLimit：{}", timeLimit);
        return "1";
    }


}
