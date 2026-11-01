package com.fib.ecny;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class XmlTest {
    public static void main(String[] args) throws IOException {
        GrpHdr user = new GrpHdr();
        user.setUserId(1001L);
        user.setName("张三");
        user.setAge(22);

        GrpHdr.Address address = new GrpHdr.Address();
        address.setProvince("江苏");
        address.setCity("南京");
        user.setAddress(address);

        GrpHdr.Hobby h1 = new GrpHdr.Hobby();
        h1.setValue("篮球");
        GrpHdr.Hobby h2 = new GrpHdr.Hobby();
        h2.setValue("阅读");
        user.setHobbies(List.of(h1, h2));

        ActivateCurrentAndAmount tranAmt = new ActivateCurrentAndAmount();
        tranAmt.setCcy("CNY");
        tranAmt.setValue(new BigDecimal("123.34"));
        user.setTranAmt(tranAmt);

        // 对象转XML
        String xml = XmlUtil.toXml(user);
        System.out.println("=====生成XML=====");
        System.out.println(xml);

        // XML解析回对象
        GrpHdr parseUser = XmlUtil.parseXml(xml, GrpHdr.class);
        System.out.println("\n=====解析结果=====");
        System.out.println(parseUser);


        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonStr = "{\"tranAmt\":\"CNY0.01\"}";
        Uwap501Req body = jsonMapper.readValue(jsonStr, Uwap501Req.class);
        System.out.println(body.getTranAmt());

        String outJson = jsonMapper.writeValueAsString(body);
        System.out.println(outJson);
    }
}
