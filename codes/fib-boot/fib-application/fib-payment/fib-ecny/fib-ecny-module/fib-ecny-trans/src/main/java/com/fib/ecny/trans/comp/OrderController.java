package com.fib.ecny.trans.comp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.CompletableFuture;

public class OrderController {
    @Autowired
    @Qualifier("businessTaskExecutor")
    private TaskExecutor taskExecutor;

    @PostMapping("/pay/callback")
    @Idempotent(key = "#dto.outTradeNo",
            type = Idempotent.IdemType.BUSINESS,
            expire = 600,
            msg = "重复回调，无需处理")
    public Object payCallback(@RequestBody OrderDto dto) {
        taskExecutor.execute(()->{});

        CompletableFuture.runAsync(()->{},taskExecutor);
        // 更新订单、发放优惠券
        return "success";
    }

//    @PostMapping("/order")
//    public Result create(@Validated @RequestBody OrderDto dto) {
//        return Result.ok(orderService.create(dto));
//    }
}
