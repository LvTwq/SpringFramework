package com.lvmc.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvmc.annotation.Metrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 这个Bean的优先级设置为最高，对于 出操作，切面优先级越高，越后执行
 * 避免先执行会吞掉异常
 *
 * @author 吕茂陈
 * @date 2022-07-07 16:15
 */
@Aspect
@Component
@Slf4j
//@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MetricsAspect {

    private final ObjectMapper objectMapper;

    /**
     * 实现一个返回Java基本类型默认值的工具。
     * 其实，你也可以逐一写很多if-else判断类型，然后手动设置其默认值。这里为了减少代码量用了一个小技巧，
     * 即通过初始化一个具有1个元素的数组，然后通过获取这个数组的值来获取基本类型默认值
     */
    private static final Map<Class<?>, Object> DEFAULT_VALUES = Stream.of(
            boolean.class, byte.class, char.class, double.class, float.class, int.class, long.class, short.class
    ).collect(Collectors.toMap(Function.identity(), clazz -> Array.get(Array.newInstance(clazz, 1), 0)));


    public static Object getDefaultValue(Class<?> clazz) {
        return DEFAULT_VALUES.get(clazz);
    }

    /**
     * 方法上使用了 @Metrics
     */
    @Pointcut("@annotation(com.lvmc.annotation.Metrics)")
    public void metricsAnnotation() {
    }

    /**
     * 类上使用了 @Metrics
     */
    @Pointcut("within(@com.lvmc.annotation.Metrics *)")
    public void withMetricsAnnotation() {
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerBean() {
    }


    @Around("metricsAnnotation()")
//    @Around("metricsAnnotation() || controllerBean()")
    public Object metrics(ProceedingJoinPoint joinPoint) throws Throwable {
        // 尝试获取当前方法的类名和方法名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String name = String.format("【%s】【%s】", signature.getDeclaringType().toString(), signature.toLongString());

        // 先从当前方法上取
        Metrics metrics = signature.getMethod().getAnnotation(Metrics.class);
        if (metrics == null) {
            // 如果取不到，从类上取
            metrics = signature.getMethod().getDeclaringClass().getAnnotation(Metrics.class);
        }
        // 如果再取不到，说明是Controller和Repository，需要初始化一个 @Metrics 注解出来
        if (metrics == null) {
            @Metrics
            final class Temp {
            }
            metrics = Temp.class.getAnnotation(Metrics.class);
        }
        // 对于 web 项目，可以从上下文中获取一些额外的信息来丰富日志
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            name += String.format("【%s】", request.getRequestURL());
        }
        // 入参日志输出
        if (metrics.logParameters()) {
            log.info("【入参日志】调用 {} 的参数是：【{}】", name, objectMapper.writeValueAsString(joinPoint.getArgs()));
        }
        //实现连接点方法的执行，以及成功失败的打点，出现异常的时候记录日志
        Object returnValue;
        Instant start = Instant.now();
        try {
            // 执行目标方法，以上代码全都是前置处理，以下则是后置处理
            returnValue = joinPoint.proceed();
            if (metrics.recordSuccessMetrics()) {
                log.info("【成功打点】调用 {} 成功，耗时：{} ms", name, Duration.between(start, Instant.now()).toMillis());
            }
        } catch (Exception e) {
            if (metrics.recordFailMetrics()) {
                log.info("【失败打点】调用 {} 失败，耗时：{} ms", name, Duration.between(start, Instant.now()).toMillis());
            }
            if (metrics.logException()) {
                log.error("【异常日志】调用 {} 出现异常！", name, e);
            }

            // 如果忽略异常，直接返回默认值
            if (metrics.ignoreException()) {
                // 如果此切面先执行，会吞掉异常
                returnValue = getDefaultValue(signature.getReturnType());
            } else {
                // 不忽略异常，
                throw e;
            }
        }
        // 返回值的日志输出
        if (metrics.logReturn()) {
            log.info("【出参日志】调用 {} 的返回是：【{}】", name, returnValue);
        }
        return returnValue;
    }
}
