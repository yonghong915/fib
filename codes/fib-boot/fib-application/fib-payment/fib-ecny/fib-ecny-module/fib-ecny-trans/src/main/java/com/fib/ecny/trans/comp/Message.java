package com.fib.ecny.trans.comp;

public sealed interface Message permits ImageMessage, OrderMessage, TextMessage {
}
