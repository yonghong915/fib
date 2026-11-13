package com.fib.ecny.sm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.math.BigDecimal;
import java.util.Objects;

public class XmlUtil {
    private static final XmlMapper XML_MAPPER;

    static {
        XML_MAPPER = new XmlMapper();
        XML_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static final TypeReference<EsbTransaction<EGZY302Request>> EGZY302_REQ_TYPE = new TypeReference<EsbTransaction<EGZY302Request>>() {
    };

    public static <T> T fromXml(String xml, TypeReference<T> typeRef) {
        try {
            return XML_MAPPER.readValue(xml, typeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toXml(Object obj) {
        try {
            return XML_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static XmlMapper getXmlMapper() {
        return XML_MAPPER;
    }



    public static BigDecimal fenToYuan(long fen) {
        return BigDecimal.valueOf(fen).movePointLeft(2);
    }

    public static long yuanToFen(String yuan) {
        return Objects.isNull(yuan) ? 0L : new BigDecimal(yuan).movePointRight(2).longValueExact();
    }
}
