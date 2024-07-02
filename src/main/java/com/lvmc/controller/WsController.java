

package com.lvmc.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/2/20 10:19
 */
@Slf4j
@Controller("web_Scoket_system")
@RequestMapping("/api/socket")
public class WsController {


    @GetMapping("index/{userId}")
    public String socket(@PathVariable String userId) {
        return "wsIndex";
    }


    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public Map<String, Object> pushToWeb(@PathVariable String cid, String message) {
        Map<String, Object> result = new HashMap<>();
        try {
            WebSocketServer.sendInfo(message, cid);
            result.put("code", cid);
            result.put("msg", message);
        } catch (IOException e) {
            log.error("推送数据接口", e);
        }
        return result;
    }

}
