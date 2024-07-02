package com.lvmc.aop;

import com.google.common.util.concurrent.RateLimiter;
import com.lvmc.annotation.SimpleRateLimit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author 吕茂陈
 * @date 2022-10-08 15:41
 */
@Slf4j
@Aspect
@Component
public class RateLimitAspect {

	private final ConcurrentHashMap<String, RateLimiter> EXISTED_RATE_LIMITERS = new ConcurrentHashMap<>();

	@Pointcut("@annotation(com.lvmc.annotation.SimpleRateLimit)")
	public void rateLimit() {
	}


	@Around("rateLimit()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		SimpleRateLimit annotation = AnnotationUtils.findAnnotation(method, SimpleRateLimit.class);

		RateLimiter rateLimiter = EXISTED_RATE_LIMITERS.computeIfAbsent(method.getName(), new Function<String, RateLimiter>() {
			@Override
			public RateLimiter apply(String k) {
				return RateLimiter.create(annotation.limit());
			}
		});

		if (rateLimiter != null && rateLimiter.tryAcquire()) {
			return point.proceed();
		} else {
			throw new RuntimeException("too many requests, please try again later...");
		}
	}

}
