package com.fib.ecny.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.time.LocalDateTime;

@JacksonXmlRootElement(localName = "GrpHdr")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class GrpHdr {
    @JacksonXmlProperty(localName = "MsgId")
    private String msgId;

    @JacksonXmlProperty(localName = "CreDtTm")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime creDtTm;
}
