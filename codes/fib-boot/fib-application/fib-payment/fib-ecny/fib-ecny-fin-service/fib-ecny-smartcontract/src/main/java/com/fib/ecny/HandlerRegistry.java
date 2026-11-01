package com.fib.ecny;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.core.GenericTypeResolver;

import java.util.List;
import java.util.Optional;

public class HandlerRegistry {
    private TqzMessageLogMapper mapper;
    public HandlerRegistry(List<DcepMessageHandler<?>> handlers, DcepMessageRegistry registry ){
        for (DcepMessageHandler<?> handler : handlers){
            Class<?> bodyType = GenericTypeResolver.resolveTypeArgument(handler.getClass(),DcepMessageHandler.class);

        }
    }

    public Optional<String> findReplayableResponse(String msgId,MessageDirection direction){
        if(ObjectUtil.isEmpty(msgId) || direction!= MessageDirection.INBOUND){
            return Optional.empty();
        }
        TqzMessageLog first = mapper.selectOne(new LambdaQueryWrapper<TqzMessageLog>()
                .select(TqzMessageLog::getResponseBody)
                .eq(TqzMessageLog::getMsgId,msgId)
                .eq(TqzMessageLog::getDirection,directionCode(direction))
                .ne(TqzMessageLog::getProcessStatus,"DUPLICATE")
                .isNotNull(TqzMessageLog::getResponseBody)
                .ne(TqzMessageLog::getResponseBody,"")
                .orderByAsc(TqzMessageLog::getCreateTm)
                .orderByAsc(TqzMessageLog::getMessageLogId)
                .last("LIMIT 1")
        );
        return Optional.ofNullable(first).map(TqzMessageLog::getResponseBody);
    }

    private String directionCode(MessageDirection direction) {
        return switch (direction){
            case INBOUND -> "SEND";
            case OUTBOUND -> "RECEIVE";
        };
    }
}
