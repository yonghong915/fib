package com.fib.ecny;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class ActivateCurrentAndAmount {
    private String ccy;
    private String value;

    @JacksonXmlProperty(localName = "Ccy", isAttribute = true)
    public String getCcy() {
        return ccy;
    }

    @JacksonXmlText
    public String geValue() {
        return value;
    }
}
