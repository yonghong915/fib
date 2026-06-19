package com.fib.ecny.trans.controller;

import cn.hutool.core.util.IdUtil;
import com.fib.common.bus.base.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.Contracts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hibernate.validator.internal.util.logging.Messages.MESSAGES;

@Slf4j
public class UwapBaseController extends BizBaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UwapBaseController.class);

    public UwapResponse<?> executor(UwapRequest<?> request) {
        return executor(request, null, true);
    }

    public UwapResponse<?> executor(UwapRequest<?> request, boolean isCheckParam) {
        return executor(request, null, isCheckParam);
    }

    public UwapResponse<?> executor(UwapRequest<?> request, Class<?> bizService) {
        return executor(request, bizService, true);
    }

    private UwapResponse<?> executor(UwapRequest<?> request, Class<?> bizService, boolean isCheckParam) {
        StopWatch watch = new StopWatch();
        BizContext context = new BizContext();
        RespEntity response = RespEntity.fail();
        boolean isInsertRequest = Boolean.FALSE;

        try {
            long sysSerial = IdUtil.getSnowflakeNextId();
            MDC.put("traceId", String.valueOf(sysSerial));

            initContext(context, request);

            insertMessage(context);
            if (isCheckParam) {
                checkField(context);
            }
            response = execute(context, response, bizService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //response(context,response);

            //错误码映射

            long cost = watch.getTotalTimeMillis();
            LOGGER.info("耗时：{}", cost);
        }
        UwapResponse uwapResponse = new UwapResponse<>(response);
        uwapResponse.setMsgHeader(request.getMsgHeader());
        return uwapResponse;
    }

    private void initContext(BizContext context, UwapRequest<?> request) {
        Contracts.assertNotNull(context, MESSAGES.validatedObjectMustNotBeNull());

        context.setRequest(request);
        MsgHeader msgHeader = request.getMsgHeader();
        context.setChannelSeqNo(msgHeader.getMsgId());
        //LocalDateTime sendDateTime = LocalDateTime.parse(msgHeader.getOrigSendDateTime(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));


    }
}
