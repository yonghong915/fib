package com.fib.ecny.base.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "soap:Envelope")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DcepRequest<T> {
    @JacksonXmlProperty(localName = "soap:Header")
    private DcepHeader dcepHeader;

    @JacksonXmlProperty(localName = "soap:Body")
    private T body;
}
