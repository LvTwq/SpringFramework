package com.southwind.aop;

/**
 * @author 吕茂陈
 * @date 2021/09/30 17:18
 */
public aspect CriticAspect {

    public CriticAspect() {
    }

    pointcut performance() : execution(* perform(..));

//    afterReturning() : performance(){
//
//    }
}
