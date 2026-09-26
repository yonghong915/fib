package com.fib.ecny.base.enums;

import lombok.Data;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 交易状态流转配置表
 */
@Data
public class TranStatusTransfer {
    private TranStatusEnum fromStatus;
    private TranEvent event;
    private TranStatusEnum toStatus;
    private Predicate<TqaMainSerial> guard;
    private Consumer<TqaMainSerial> action;
}
