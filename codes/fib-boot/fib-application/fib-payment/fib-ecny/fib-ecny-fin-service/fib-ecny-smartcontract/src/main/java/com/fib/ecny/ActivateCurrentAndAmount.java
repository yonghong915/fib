package com.fib.ecny;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

import java.math.BigDecimal;

@Data
// 根节点就是 tranAmt
//@JacksonXmlRootElement(localName = "tranAmt")
public class ActivateCurrentAndAmount {

    @JacksonXmlProperty(localName = "Ccy", isAttribute = true)
    private String ccy = "CNY";

    @JacksonXmlText
    private BigDecimal value;
}
