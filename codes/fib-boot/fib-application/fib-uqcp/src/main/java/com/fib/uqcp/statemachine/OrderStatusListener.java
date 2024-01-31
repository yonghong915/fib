package com.fib.uqcp.statemachine;

import javax.swing.event.ChangeEvent;

import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//@Component
//@WithStateMachine
//@Transactional
public class OrderStatusListener extends StateMachineListenerAdapter<OrderStatusEnum, OrderStatusChangeEventEnum> {

}
