package com.fib.ecny;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class Uwap501Req {

    @JsonSerialize(using = AmountTextSerializer.class)
    @JsonDeserialize(using = AmountTextDeserializer.class)
    @JacksonXmlProperty(localName = "tranAmt")
    private ActivateCurrentAndAmount tranAmt;
}
