package com.fib.ecny;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class AmountTextSerializer extends StdSerializer<ActivateCurrentAndAmount> {

    protected AmountTextSerializer() {
        super(ActivateCurrentAndAmount.class);
    }

    @Override
    public void serialize(ActivateCurrentAndAmount node, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (node == null) {
            gen.writeNull();
            return;
        }
        String text = node.getCcy() + node.getValue().toPlainString();
        gen.writeString(text);
    }
}
