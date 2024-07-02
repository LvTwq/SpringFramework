package com.lvmc.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Properties;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/10/19 16:30
 */
@Slf4j
@Intercepts({@Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class),})
@Component
public class ParameterHandlerInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 拦截 ParameterHandler 的 setParameters 方法 动态设置参数
        if (invocation.getTarget() instanceof ParameterHandler) {
            //参数批量标记
            boolean batchFlag = false;

            ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
            // 反射获取参数对象
            Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
        }
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
