package com.fib.ecny;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

import java.util.List;

@JacksonXmlRootElement(localName = "GrpHdr")
@JsonPropertyOrder({"userId","name","address","age"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class GrpHdr {
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private Long userId;

    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "age")
    private Integer age;

    // 嵌套对象
    @JacksonXmlProperty(localName = "address")
    private Address address;

    @JacksonXmlProperty(localName = "hobbies")
    private List<Hobby> hobbies;

    @JacksonXmlElementWrapper(localName = "hobbiesList")
    @JacksonXmlProperty(localName = "hobby")
    private List<String> hobbyList;

    @JacksonXmlProperty(localName = "tranAmt")
    private ActivateCurrentAndAmount tranAmt;


    @Data
    public static class Address {
        @JacksonXmlProperty(localName = "province")
        private String province;
        @JacksonXmlProperty(localName = "city")
        private String city;
    }

    @Data
    public static class Hobby {
        // 节点文本，没有子节点
        @JacksonXmlText
        private String value;
    }
}
