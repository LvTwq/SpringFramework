package com.lvmc.mybatis.encrypt;

import java.lang.annotation.*;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/10/19 17:17
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FieldEncrypt {


    Class<? extends IEncryptor> encryptor() default DefaultEncryptor.class;
}
