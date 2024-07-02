package com.lvmc.controller;

import com.lvmc.constant.StateMachineConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/9/28 15:09
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StateMachineController {

    private final StateMachine<StateMachineConstant.States, StateMachineConstant.Events> stateMachine;




    @GetMapping("machine")
    public void setStateMachine() {
        stateMachine.sendEvent(StateMachineConstant.Events.START);

    }



    @PostConstruct
    public void init() {
        stateMachine.addStateListener(new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<StateMachineConstant.States, StateMachineConstant.Events> from, State<StateMachineConstant.States, StateMachineConstant.Events> to) {
                log.info("处理状态变化事件");
            }
        });
        stateMachine.start();
    }


}
