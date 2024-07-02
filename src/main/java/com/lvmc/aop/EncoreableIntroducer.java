package com.lvmc.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;

import com.lvmc.service.Encoreable;
import com.lvmc.service.impl.DefaultEncoreable;

/**
 * @author 吕茂陈
 * @date 2021/09/30 17:04
 */
@Aspect
public class EncoreableIntroducer {

    @DeclareParents(value = "com.lvmc.service.Performance+", defaultImpl = DefaultEncoreable.class)
    public static Encoreable encoreable;
}
