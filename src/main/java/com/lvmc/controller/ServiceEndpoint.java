package com.lvmc.controller;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

/**
 * @author 吕茂陈
 * @description
 * @date 2024/10/15 10:12
 */
@Endpoint
public class ServiceEndpoint {

    @PayloadRoot(namespace = "http://example.com/service", localPart = "request")
    @ResponsePayload
    public Source handleRequest(@RequestPayload Source request) {
        // 处理请求
        String responseXml = "<response xmlns='http://example.com/service'>"
                + "<message>Hello, " + extractMessage(request) + "</message>"
                + "</response>";
        return new StreamSource(new StringReader(responseXml));
    }

    private String extractMessage(Source source) {
        // 这里可以使用DOM、SAX等解析XML
        // 为了简化，这里直接返回一个固定的字符串
        return "World";
    }
}
