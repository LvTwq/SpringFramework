package com.southwind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.southwind.config.TcsConfig;

/**
 * @author 吕茂陈
 * @date 2021/12/10 14:54
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(TcsConfig.class)
public @interface EnableConfig {
}
