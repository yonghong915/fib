package com.fib.ecny;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TqzMessageLog {
    private String messageLogId;
    private String msgId;
    private String direction;
    private String responseBody;
    private String processStatus;
    private String createTm;

}
