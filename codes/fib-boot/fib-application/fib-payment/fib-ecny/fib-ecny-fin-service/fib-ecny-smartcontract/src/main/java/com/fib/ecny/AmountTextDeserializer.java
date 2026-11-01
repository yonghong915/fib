package com.fib.ecny;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class AmountTextDeserializer extends StdDeserializer<ActivateCurrentAndAmount> {

    protected AmountTextDeserializer() {
        super(ActivateCurrentAndAmount.class);
    }

    @Override
    public ActivateCurrentAndAmount deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String raw = p.getText();
        if (raw == null || raw.length() < 4) {
            throw new IllegalArgumentException("金额格式错误，示例：CNY0.01");
        }
        // 前3位是币种
        String ccy = raw.substring(0, 3);
        String numStr = raw.substring(3);
        BigDecimal val = new BigDecimal(numStr);

        ActivateCurrentAndAmount node = new ActivateCurrentAndAmount();
        node.setCcy(ccy);
        node.setValue(val);
        return node;
    }
}
