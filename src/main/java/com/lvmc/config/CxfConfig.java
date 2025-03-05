package com.lvmc.config;

import com.lvmc.service.HelloService;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * @author 吕茂陈
 * @description
 * @date 2024/10/15 14:29
 */
@Configuration
public class CxfConfig {

    @Autowired
    private HelloService helloService;

    @Bean
    public ServletRegistrationBean<CXFServlet> disServlet() {
        return new ServletRegistrationBean<>(new CXFServlet(), "/webService1/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public Endpoint helloEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), helloService);
        endpoint.publish("/helloService");
        return endpoint;
    }

}
