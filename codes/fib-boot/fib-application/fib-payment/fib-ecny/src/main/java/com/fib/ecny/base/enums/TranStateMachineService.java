package com.fib.ecny.base.enums;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Service
@Slf4j
public class TranStateMachineService {
    private Map<String, TranStatusTransfer> transitionMap;

    @PostConstruct
    public void initTransition() {
        transitionMap = new HashMap<>();

        // 1.INIT + PAY_SUBMIT → PROCESSING
        register(TranStatusEnum.INITIALIZED, TranEvent.PAY_SUBMIT, TranStatusEnum.PROCESSING,
                t -> true,
                this::actionPaySubmit);

        // 2.PROCESSING + PAY_CALLBACK_SUCCESS → SUCCESS
        register(TranStatusEnum.PROCESSING, TranEvent.PAY_CALLBACK_SUCCESS, TranStatusEnum.SUCCESS,
                t -> true,
                this::actionPaySuccess);

        // 3.PROCESSING + PAY_CALLBACK_FAIL → FAIL
        register(TranStatusEnum.PROCESSING, TranEvent.PAY_CALLBACK_FAIL, TranStatusEnum.FAILED,
                t -> true,
                this::actionPayFail);

        // 4.SUCCESS + REFUND_APPLY → REFUNDING；守卫：只有成功才允许退款
        register(TranStatusEnum.SUCCESS, TranEvent.REFUND_APPLY, TranStatusEnum.REVERSED,
                t -> TranStatusEnum.SUCCESS.equals(t.getTranStatusEnum()),
                this::actionRefundApply);

        // 5.REFUNDING + REFUND_CALLBACK_SUCCESS → REFUNDED
//        register(TranStatusEnum.REFUNDING, TranEvent.REFUND_CALLBACK_SUCCESS, TranStatusEnum.REFUNDED,
//                t -> true,
//                this::actionRefundSuccess);
    }

    private void register(TranStatusEnum from, TranEvent event, TranStatusEnum to,
                          Predicate<TqaMainSerial> guard,
                          Consumer<TqaMainSerial> action) {
        TranStatusTransfer t = new TranStatusTransfer();
        t.setFromStatus(from);
        t.setEvent(event);
        t.setToStatus(to);
        t.setGuard(guard);
        t.setAction(action);
        transitionMap.put(buildKey(from, event), t);
    }

    private String buildKey(TranStatusEnum status, TranEvent event) {
        return status.name() + "#" + event.name();
    }

    /**
     * 状态机对外统一入口
     *
     * @param tradeDO 数据库查询出来的交易记录（带版本号）
     * @param event   触发事件
     * @return true=流转成功；false=不需要流转（幂等，已是目标状态）
     */
    public boolean fireEvent(TqaMainSerial tradeDO, TranEvent event) {
        TranStatusEnum currentStatus = tradeDO.getTranStatusEnum();
        String key = buildKey(currentStatus, event);
        TranStatusTransfer transition = transitionMap.get(key);

        // 没有找到流转规则
        if (transition == null) {
            // 幂等兼容：如果当前状态已经是终态，直接返回成功，不要抛异常
            if (isFinalStatus(currentStatus)) {
                return false;
            }
            throw new IllegalStateException("非法状态流转，current:" + currentStatus + ",event:" + event);
        }

        // 守卫校验不通过
        if (!transition.getGuard().test(tradeDO)) {
            throw new IllegalStateException("守卫条件不满足，不允许流转,current:" + currentStatus + ",event:" + event);
        }

        // 执行业务action
        try {
            if (transition.getAction() != null) {
                transition.getAction().accept(tradeDO);
            }
        } catch (Exception e) {
            //log.error("[状态机Action执行异常] tranNo={},event={}", tranNo, event, e);
            throw new RuntimeException("状态机业务动作异常", e);
        }

        // 更新内存对象状态，后续mybatis-plus乐观锁更新DB
        tradeDO.setTranStatusEnum(transition.getToStatus());

        // 写入状态变更日志
//        TranStateLogDO logDo = new TranStateLogDO();
//        logDo.setTranNo(tranNo);
//        logDo.setOldStatus(oldStatus.getCode());
//        logDo.setEvent(event.name());
//        logDo.setNewStatus(newStatus.getCode());
//        tranStateLogMapper.insert(logDo);
        //  log.info("[状态机流转成功] tranNo={},{} → {} ,event={}", tranNo, oldStatus, newStatus, event);
        return true;
    }

    private boolean isFinalStatus(TranStatusEnum status) {
        return TranStatusEnum.SUCCESS.equals(status)
                || TranStatusEnum.FAILED.equals(status)
                || TranStatusEnum.REVERSED.equals(status) || TranStatusEnum.ADJUSTED.equals(status);
    }

    // ========== 流转后置动作，业务逻辑 ==========
    private void actionPaySubmit(TqaMainSerial trade) {
        // 例如：组装请求报文、调用支付渠道
    }

    private void actionPaySuccess(TqaMainSerial trade) {
        // 支付成功：记账、通知业务方
    }

    private void actionPayFail(TqaMainSerial trade) {
        // 支付失败：记录错误码
    }

    private void actionRefundApply(TqaMainSerial trade) {
        // 发起退款调用渠道
    }

    private void actionRefundSuccess(TqaMainSerial trade) {
        // 退款完成记账
    }
}
