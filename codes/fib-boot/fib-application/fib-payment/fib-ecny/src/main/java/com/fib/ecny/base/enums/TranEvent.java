package com.fib.ecny.base.enums;

public enum TranEvent {
   /** 提交支付 */
    PAY_SUBMIT,
    /** 支付回调成功 */
    PAY_CALLBACK_SUCCESS,
    /** 支付回调失败 */
    PAY_CALLBACK_FAIL,
    /** 发起退款 */
    REFUND_APPLY,
    /** 退款回调成功 */
    REFUND_CALLBACK_SUCCESS
}
