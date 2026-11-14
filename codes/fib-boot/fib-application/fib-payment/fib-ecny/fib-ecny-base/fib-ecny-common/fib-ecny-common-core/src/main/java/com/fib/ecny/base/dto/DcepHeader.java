package com.fib.ecny.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DcepHeader {
    @JacksonXmlProperty(localName = "Ver")
    private String ver = "1.0";

    @JacksonXmlProperty(localName = "SndDtTm")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime sndDtTm;
    private String msgTp;
    private String msgSN;
    private String sender;
    private String receiver;
    private String signSN;
    private String ncrptnSN;
    private String dgtlEnvlp;

}
