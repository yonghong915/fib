package com.fib.ecny.base.dto;

import cn.hutool.core.util.IdUtil;
import com.fib.ecny.base.common.XmlUtil;
import com.fib.ecny.base.dto.dcep.dcep022100101.Dcep022100101;

import java.io.IOException;
import java.time.LocalDateTime;

public class XmlTest {
    public static void main(String[] args) throws IOException {
        GrpHdr grpHdr = new GrpHdr();
        grpHdr.setCreDtTm(LocalDateTime.now());
        grpHdr.setMsgId(IdUtil.randomUUID());

        DcepRequest<Dcep022100101> dcepRequest = new DcepRequest<>();
        DcepHeader dcepHeader = new DcepHeader();
        dcepHeader.setVer("1.0");
        dcepHeader.setSndDtTm(LocalDateTime.now());
        dcepRequest.setDcepHeader(dcepHeader);

        Dcep022100101 dcep022100101 = new Dcep022100101();
        dcep022100101.setTranAmt("1.23");
        dcepRequest.setBody(dcep022100101);

        // 对象转XML
        String xml = XmlUtil.toXml(dcepRequest);
        System.out.println("=====生成XML=====");
        System.out.println(xml);

        // XML解析回对象
//        GrpHdr parseUser = XmlUtil.parseXml(xml, GrpHdr.class);
//        System.out.println("\n=====解析结果=====");
//        System.out.println(parseUser);


//        ObjectMapper jsonMapper = new ObjectMapper();
//        String jsonStr = "{\"tranAmt\":\"CNY0.01\"}";
//        Uwap501Req body = jsonMapper.readValue(jsonStr, Uwap501Req.class);
//        System.out.println(body.getTranAmt());
//
//        String outJson = jsonMapper.writeValueAsString(body);
//        System.out.println(outJson);
    }
}
