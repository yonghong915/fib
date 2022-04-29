package com.fib.upp.message.metadata.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.commons.exception.CommonException;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.message.metadata.Message;
import com.giantstone.message.metadata.ValueRange;

public class ValueRangeHalder extends DefaultHandler {

	private Map<String, Object> CMap;
	private String fileAbsolutePath;
	private String A;
	private ValueRange valueRange;
	String D;

	public Map<String, Object> A(String s, File file) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxparser;
		FileInputStream fileinputstream = null;
		try {
			saxparser = saxParserFactory.newSAXParser();

			A = s;
			fileAbsolutePath = file.getAbsolutePath();
			fileinputstream = new FileInputStream(file);
			saxparser.parse(fileinputstream, this);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		if (fileinputstream != null)
			try {
				fileinputstream.close();
			} catch (Exception exception) {
				exception.printStackTrace(System.err);
			}
//		break MISSING_BLOCK_LABEL_143;
//		Exception exception1;
//		exception1;
//		exception1.printStackTrace(System.err);
//		ExceptionUtil.throwActualException(exception1);
//		if (fileinputstream != null)
//			try
//			{
//				fileinputstream.close();
//			}
//			catch (Exception exception2)
//			{
//				exception2.printStackTrace(System.err);
//			}
//		break MISSING_BLOCK_LABEL_143;
//		Exception exception3;
//		exception3;
//		if (fileinputstream != null)
//			try
//			{
//				fileinputstream.close();
//			}
//			catch (Exception exception4)
//			{
//				exception4.printStackTrace(System.err);
//			}
//		throw exception3;
		return CMap;
	}

	@Override
	public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
		if ("value-range".equals(s2)) {
			CMap.clear();
			String s3 = attributes.getValue("default-ref");
			if (null != s3) {
				s3 = s3.trim();
				if (0 == s3.trim().length())
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": value-range/value-ref ")
							.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				CMap.put("default-ref", s3);
			}
		} else if ("value".equals(s2)) {
			valueRange = new ValueRange();
			String s4 = attributes.getValue("value");
			if (null == s4)
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": value-range/value/@value ")
						.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
			s4 = s4.trim();
			if (0 == s4.length())
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": value-range/value/@value ")
						.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
			if (CMap.containsKey(s4))
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": value-range/value[@value='").append(s4)
						.append("'] ").append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.reduplicate"))
						.toString());
			valueRange.setValue(s4);
			valueRange.setShortText(attributes.getValue("short-text"));
			s4 = attributes.getValue("reference");
			if (s4 != null) {
				s4 = s4.trim();
				if (0 == s4.length())
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": value-range/value/@reference ")
							.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				valueRange.setReferenceId(s4);
				Message message = MessageMetadataManager.getMessage(A, s4);
				valueRange.setReference(message);
				if (null == CMap.get("default-ref"))
					throw new CommonException(
							(new StringBuilder()).append(fileAbsolutePath).append(": value-range[@value='").append(valueRange.getValue())
									.append("']/@default-ref ").append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
			}
			CMap.put(valueRange.getValue(), valueRange);
		}
	}

	@Override
	public void endElement(String s, String s1, String s2) throws SAXException {
		//
	}

	@Override
	public void characters(char ac[], int i, int j) throws SAXException {
//		if (null != D) goto _L2; else goto _L1
//_L1:
//		D = new String(ac, i, j);
//		  goto _L3
//_L2:
//		new StringBuilder();
//		this;
//		JVM INSTR dup_x1 ;
//		D;
//		append();
//		new String(ac, i, j);
//		append();
//		toString();
//		D;
//_L3:
	}

	public ValueRangeHalder() {
		CMap = new HashMap<>();
		D = null;
	}

}
