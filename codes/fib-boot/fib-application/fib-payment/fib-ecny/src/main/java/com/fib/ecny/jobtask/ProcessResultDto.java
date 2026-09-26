package com.fib.ecny.jobtask;

import lombok.Data;

@Data
public class ProcessResultDto {
    private Long batchId;
    private Long seqId;
    private boolean success;
    private String errorMsg;
}
