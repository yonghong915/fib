package com.fib.ecny.base.dto.dcep.dcep022100101;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.io.Serializable;

@Data
@JacksonXmlRootElement(localName = "wstxns1:Dcep221")
public class Dcep022100101 implements Serializable {

    @JacksonXmlProperty(localName = "TranAmt")
    private String tranAmt;
}
