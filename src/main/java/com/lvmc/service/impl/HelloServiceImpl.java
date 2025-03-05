package com.lvmc.service.impl;

import com.lvmc.service.HelloService;
import com.lvmc.utils.JackJsonUtil;
import com.lvmc.vo.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import java.util.List;

/**
 * @author 吕茂陈
 * @description
 * @date 2024/10/15 15:48
 */
@Slf4j
@WebService(
        /* 和接口的服务名称保持一致 */
        serviceName = HelloService.SERVICE_NAME,
        /* 和接口的命名空间保持一致 */
        targetNamespace = HelloService.TARGET_NAMESPACE,
        /* 接口全路径 */
        endpointInterface = "com.lvmc.service.HelloService"
)
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
@Component
public class HelloServiceImpl implements HelloService {

    @Override
    public String hi(String userName) {
        log.info("HelloServiceImpl-hi-{}", userName);
        return "hi " + userName;
    }

//    @Override
//    public List<UserDto> activeUsers(List<UserDto> userDtos) {
//        List<UserDto> result = userDtos;
//        for (UserDto userDto : userDtos) {
//            log.info("HelloServiceImpl-activeUsers-{}", JackJsonUtil.obj2String(userDto));
//            userDto.setActive(Boolean.TRUE);
//        }
//        result.add(new UserDto());
//        return userDtos;
////        return userDtos;
//    }
}

