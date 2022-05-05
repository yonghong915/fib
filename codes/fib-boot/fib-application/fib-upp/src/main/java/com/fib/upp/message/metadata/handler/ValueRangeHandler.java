package com.fib.upp.message.metadata.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.commons.exception.CommonException;
import com.fib.upp.message.metadata.Message;
import com.fib.upp.message.metadata.ValueRange;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;

public class ValueRangeHandler extends DefaultHandler {

	private Map<String, ValueRange> CMap;
	private String fileAbsolutePath;
	private String A;
	private ValueRange valueRange;
	String nodeValue;

	public Map<String, ValueRange> parseValueRange(String s, File file) {
		try {
			A = s;
			fileAbsolutePath = file.getAbsolutePath();
			XmlUtil.readBySax(new FileInputStream(file), this);
		} catch (FileNotFoundException e) {
			throw new CommonException(e);
		}
		return CMap;
	}

	@Override
	public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
		if ("value-range".equals(s2)) {
			CMap.clear();
			String s3 = attributes.getValue("default-ref");
			if (!StrUtil.isEmptyIfStr(s3)) {
				// TODO CMap.put("default-ref", s3);
			}

		} else if ("value".equals(s2)) {
			valueRange = new ValueRange();
			String attrVal = attributes.getValue("value");

			Assert.notBlank(attrVal, () -> new CommonException("value-range/value/@value null blank"));

			if (CMap.containsKey(attrVal))
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": value-range/value[@value='").append(attrVal)
						.append("'] ").append("MessageMetadataManager.startElement.reduplicate").toString());
			valueRange.setValue(attrVal);
			valueRange.setShortText(attributes.getValue("short-text"));

			attrVal = attributes.getValue("reference");
			if (!StrUtil.isEmptyIfStr(attrVal)) {
				valueRange.setReferenceId(attrVal);
				Message message = MessageMetadataManager.getMessage(A, attrVal);
				valueRange.setReference(message);
				if (null == CMap.get("default-ref"))
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": value-range[@value='")
							.append(valueRange.getValue()).append("']/@default-ref ").append("null").toString());
			}
			CMap.put(valueRange.getValue(), valueRange);
		}
	}

	@Override
	public void endElement(String s, String s1, String s2) throws SAXException {
		//
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (null == this.nodeValue)
			this.nodeValue = new String(ch, start, length);
		else
			this.nodeValue += new String(ch, start, length);
	}

	public ValueRangeHandler() {
		CMap = new HashMap<>();
		nodeValue = null;
	}
}