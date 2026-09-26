package com.fib.ecny.base.enums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tran_status_change_log")
public class TranStatusChangeLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String tranNo;
    private String oldStatus;
    private String event;
    private String newStatus;
    private LocalDateTime createTime = LocalDateTime.now();
}
