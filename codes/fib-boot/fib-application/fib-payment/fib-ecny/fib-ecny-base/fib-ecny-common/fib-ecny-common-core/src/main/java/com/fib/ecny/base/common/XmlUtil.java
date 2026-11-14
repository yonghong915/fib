package com.fib.ecny.base.common;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class XmlUtil {
    private static final XmlMapper XML_MAPPER;

    static {
        XML_MAPPER = new XmlMapper();
        // 开启格式化输出（换行缩进）
        XML_MAPPER.enable(ToXmlGenerator.Feature.WRITE_XML_1_1);
        XML_MAPPER.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION); // 输出 <?xml version="1.1" encoding="UTF-8"?>
        //XML_MAPPER.enable(ToXmlGenerator.Feature.INDENT_XML);
        XML_MAPPER.registerModule(new JavaTimeModule());
    }

    /**
     * 对象 → XML字符串
     */
    public static String toXml(Object obj) throws IOException {
        return XML_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    /**
     * XML字符串 → 对象
     */
    public static <T> T parseXml(String xmlStr, Class<T> clazz) throws IOException {
        return XML_MAPPER.readValue(xmlStr, clazz);
    }

    public static XmlMapper getXmlMapper() {
        return XML_MAPPER;
    }
}
