package com.fib.ecny.sm;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "Transaction")
public class EsbTransaction<T> {

    @JacksonXmlProperty(localName = "HEAD")
    private EsbHead head;

    @JacksonXmlProperty(localName = "BODY")
    private T body;
}
