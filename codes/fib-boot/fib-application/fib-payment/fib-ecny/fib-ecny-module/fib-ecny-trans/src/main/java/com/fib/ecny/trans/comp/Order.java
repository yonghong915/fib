package com.fib.ecny.trans.comp;

public sealed interface Order permits PhysicalOrder, VirtualOrder {
    String orderId();
    OrderState state();
}