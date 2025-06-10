package com.fib.uqcp.statemachine;


import org.springframework.statemachine.listener.StateMachineListenerAdapter;

//@Component
//@WithStateMachine
//@Transactional
public class OrderStatusListener extends StateMachineListenerAdapter<OrderStatusEnum, OrderStatusChangeEventEnum> {

}
