package com.fib.ecny.trans.comp;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_order")
public class OrderEntity {
    /**
     *
     */
    private Long orderId;

    /**
     *
     */
    private String orderNm;

    /**
     *
     */
    private OrderStatusEnum orderStatus;
}
