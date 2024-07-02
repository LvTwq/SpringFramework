package com.lvmc.event;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 吕茂陈
 * @date 2022-07-07 10:51
 */
public class MessageEvent extends BaseEvent {

    @Getter
    @Setter
    private String msgId;

    public MessageEvent(String msgId) {
        super();
        this.msgId = msgId;
    }


}
