package com.lvmc.constant;

import lombok.experimental.UtilityClass;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/9/28 15:03
 */
@UtilityClass
public class StateMachineConstant {

    public enum States {
        IDLE, PROCESSING, COMPLETED
    }

    public enum Events {
        START, FINISH
    }

}
