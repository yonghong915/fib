package com.fib.ecny.base.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fib.ecny.base.*;
import com.fib.ecny.base.service.EcnyErrorCode;
import com.fib.ecny.base.service.MsgHeader;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@Component
public class DcepController extends BizBaseController {

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
            //系统流水号，sysSequenceComp.getLocalSeqNo();
            String sysSerial = IdUtil.getSnowflakeNextIdStr();
            if (StrUtil.isBlank(sysSerial)) {
                return UwapResponse.fail();
            }
            context.setSerialNo(sysSerial);
            MDC.put("traceId", String.valueOf(sysSerial));

            log.info("===== 交易请求流水初始化开始 =====");
            log.info("Uwap渠道业务层接收数据:{}", JSONUtil.toJsonStr(request));
            initContext(context, request);

            try {
                //交易请求流水信息保存
               // isInsertRequest = insertMessage(context);
            } catch (Exception e) {
                log.error("交易请求流水保存失败", e);
                //exceptionResolver.throwIfStrict(EcnyErrorCode.UWAP_R_9999, BizException.class);
            }

            //参数校验
            if (isCheckParam) {
                //checkField(context);
            }

            response = execute(context, response, bizService);
        } catch (ValidationException e) {
            //ExcepUtils.printError(response.setFailed(EcnyErrorCode.UWAP_R_0001), e);
        } catch (Exception e) {
            // response = ExcepUtils.of(e, response);
        } finally {
            log.info("业务层返回类型:{}", response.getRespType());
            log.info("业务层返回代码:{}", response.getRespCode());
            log.info("业务层返回秒数:{}", response.getRespDesc());
            log.info("业务层返回数据:{}", JSONUtil.toJsonStr(response));

            //复制请求头header属性到返回头header中
            response(context, response);

            //错误码映射
            if (response.unsucc() && response.getNeedErrorCodeMapping()) {
                UwapResponse<?> respData = response.getRespData();
                doMappingErrorCode(context, respData);
            }

            //获取执行耗时
            long cost = watch.getTotalTimeMillis();
            //context.put(TqaMessageSerialDommain::getElpsdTm, (int) cost);

            if (isInsertRequest) {
                updateMessage(context, response);
            } else {
                log.error("交易请求流水保存失败，不执行更新流水信息操作");
            }

            log.info("返回数据:{}", JSONUtil.toJsonStr(response.getRespData()));
            log.info("交易耗时：[{}] ms", cost);
        }
        UwapResponse uwapResponse = new UwapResponse<>(response);
        uwapResponse.setMsgHeader(request.getMsgHeader());
        return response.getRespData();
    }

    private void updateMessage(BizContext context, RespEntity response) {
    }

    /**
     * 返回前处理，赋值header对象，处理状态码
     *
     * @param context  上下文对象
     * @param response 组件统一返回对象
     */
    private void response(BizContext context, RespEntity response) {
        UwapResponse<?> respData = null;
        MsgHeader header;
        //判断业务返回数据类型
        if (Objects.isNull(response.getRespData())) {
            header = new MsgHeader();
            respData = new UwapResponse<>(header, response.getRespData());
            response.setRespData(respData);
        } else if (response.getRespData() instanceof UwapResponse<?>) {
            respData = response.getRespData();
            header = respData.getMsgHeader();
            if (Objects.isNull(header)) {
                header = new MsgHeader();
                respData.setMsgHeader(header);
            }
        } else {
            header = new MsgHeader();
            respData = new UwapResponse<>(header, response.getRespData());
            response.setRespData(respData);
        }
        //复制请求报文头到返回报文头中
        MsgHeader msgHeader = ((UwapRequest<?>) context.getRequest()).getMsgHeader();
        BeanUtil.copyProperties(msgHeader, header);

        //将发送发和接收方值调换一下
        if (StrUtil.isBlank(header.getOrigSender())) {
            header.setOrigSender(msgHeader.getOrigReceiver());
        }
        if (StrUtil.isBlank(header.getOrigReceiver())) {
            header.setOrigSender(msgHeader.getOrigSender());
        }
        if (StrUtil.isBlank(header.getOrigSendDateTime())) {
            header.setOrigSendDateTime(LocalDateTimeUtil.now().format(DateTimeFormatter.ofPattern(DatePattern.UTC_SIMPLE_PATTERN)));
        }

        if (StrUtil.isBlank(header.getMesgType())) {
            header.setMesgType(getMsgType(respData.getMsgBody()));
        }

        //成功的情况直接赋值成功枚举
        if (response.okey()) {
            respData.setProcInf(EcnyErrorCode.UWAP_R_0000.getMessage());
            respData.setProcCd(EcnyErrorCode.UWAP_R_0000.getStrCode());
            respData.setProcSts(EcnyErrorCode.UWAP_R_0000.getIntCode().toString());
            return;
        }

        //不成功的情况下，优先使用业务返回的状态码
        if (StrUtil.isBlank(respData.getProcCd())) {
            respData.setProcCd(response.getRespCode());
        }
        if (StrUtil.isBlank(respData.getProcSts())) {
            respData.setProcSts(response.getRespProCd());
        }
        if (StrUtil.isBlank(respData.getProcInf())) {
            respData.setProcInf(response.getRespDesc());
        }

    }

    private String getMsgType(Object msgBody) {
        return "";
    }

    /**
     * 初始化上下文对象
     *
     * @param context 上下文对象
     * @param request 请求参数对象
     */
    private void initContext(BizContext context, UwapRequest<?> request) {

    }

    /**
     * 错误码映射
     * 先根据校验码+渠道号+错误码查询配置
     * 查询不到再根据uwap+渠道号+错误码查询配置
     * 查询不到再根据渠道号获取默认配置
     * 查询不到最后给予默认错误码
     *
     * @param context  上下文对象
     * @param respData 请求返回数据
     */
    private void doMappingErrorCode(BizContext context, UwapResponse<?> respData) {
        //1.根据校验码+渠道号+错误码查询配置
//        ErrorCodeMappingUtil errorCodeMappingUtil = new ErrorCodeMappingUtil();
//        TqpRspcodemapParam param = errorCodeMappingUtil.query(context.getTransCode(), context.getChannelCode(), respData.getProcCd(), context.getLang());
//        if (Objects.nonNull(param)) {
//            respData.setProcCd(param.getRspCode());
//            respData.setProcSts(param.getBusiRetStatus());
//            respData.setProcInf(param.getRspMsg());
//            return;
//        }

        //2.根据uwap+渠道号+错误码查询配置
//        param = errorCodeMappingUtil.query(Constant.SYSTEM_TYPE_UWAP, context.getChannelCode(), respData.getProcCd(), context.getLang());
//        if (Objects.nonNull(param)) {
//            respData.setProcCd(param.getRspCode());
//            respData.setProcSts(param.getBusiRetStatus());
//            respData.setProcInf(param.getRspMsg());
//            return;
//        }

        //3.根据渠道号获取默认配置
//        param = errorCodeMappingUtil.query(context.getChannelCode(), context.getLang());
//        if (Objects.nonNull(param)) {
//            respData.setProcCd(param.getRspCode());
//            respData.setProcSts(param.getBusiRetStatus());
//            respData.setProcInf(param.getRspMsg());
//            return;
//        }
        //4.如果从映射表中未查询到，设置默认错误码
        respData.setProcCd(EcnyErrorCode.UWAP_R_9999.getIntCode());
        respData.setProcSts(EcnyErrorCode.UWAP_R_9999.getStrCode());
        respData.setProcInf(EcnyErrorCode.UWAP_R_9999.getMessage());
        log.warn("error code mapping not found,tranCode:{},channelNo:{},errorCode:{}", context.getTransCode(), context.getChannelCode(), respData.getProcCd());
    }
}
