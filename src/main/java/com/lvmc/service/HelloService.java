package com.lvmc.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * @author 吕茂陈
 * @description
 * @date 2024/10/15 15:47
 */
@WebService(name = HelloService.SERVICE_NAME, targetNamespace = HelloService.TARGET_NAMESPACE)
public interface HelloService {

    /**
     * 暴露服务名称
     */
    String SERVICE_NAME = "HelloService";

    /**
     * 命名空间,一般是接口的包名倒序
     */
    String TARGET_NAMESPACE = "http://service.lvmc.com";

    @WebMethod
    @WebResult(name = "String")
    String hi(@WebParam(name = "userName") String userName);

//    @WebMethod
//    @WebResult(name = "UserDto")
//    List<UserDto> activeUsers(@WebParam(name = "userDtos") List<UserDto> userDtos);
}

