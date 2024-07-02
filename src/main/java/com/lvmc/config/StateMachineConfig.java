package com.lvmc.config;

import com.lvmc.constant.StateMachineConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/9/28 15:04
 */
@Configuration
@EnableStateMachine
public class StateMachineConfig extends StateMachineConfigurerAdapter<StateMachineConstant.States, StateMachineConstant.Events> {
    @Override
    public void configure(StateMachineStateConfigurer<StateMachineConstant.States, StateMachineConstant.Events> states)
            throws Exception {
        states.withStates()
                .initial(StateMachineConstant.States.IDLE)
                .states(EnumSet.allOf(StateMachineConstant.States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<StateMachineConstant.States, StateMachineConstant.Events> transitions)
            throws Exception {
        transitions.withExternal()
                .source(StateMachineConstant.States.IDLE).target(StateMachineConstant.States.PROCESSING).event(StateMachineConstant.Events.START)
                .and()
                .withExternal()
                .source(StateMachineConstant.States.PROCESSING).target(StateMachineConstant.States.COMPLETED).event(StateMachineConstant.Events.FINISH);
    }
}
