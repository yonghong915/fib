package com.fib.gateway.message.metadata;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.commons.exception.CommonException;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-30
 */
public class ValueRangeHandler extends DefaultHandler {
	private Map<String, Object> valueRangeMap;
	private String fileAbsolutePath;
	private String channelId;
	private String currentValue;

	public ValueRangeHandler() {
		this.valueRangeMap = new HashMap<>();
		this.currentValue = null;
	}

	public Map<String, Object> getValueRangeMap(String channelId, File file) {
		this.channelId = channelId;
		this.fileAbsolutePath = file.getAbsolutePath();
		SAXParserFactory saxParserFactory = SAXParserFactory.newDefaultInstance();
		try (FileInputStream fis = new FileInputStream(file)) {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse(fis, this);
		} catch (Exception e) {
			//
		}
		return this.valueRangeMap;
	}

	@Override
	public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
		ValueRange valueRange = null;
		String attrValue;
		if ("value-range".equals(var3)) {
			this.valueRangeMap.clear();
			attrValue = var4.getValue("default-ref");
			if (null != attrValue) {
				attrValue = attrValue.trim();
				if (0 == attrValue.trim().length()) {
					throw new CommonException(this.fileAbsolutePath + ": value-range/value-ref ");
				}

				this.valueRangeMap.put("default-ref", attrValue);
			}
		} else if ("value".equals(var3)) {
			valueRange = new ValueRange();
			attrValue = var4.getValue("value");
			if (null == attrValue) {
				throw new CommonException(this.fileAbsolutePath + ": value-range/value/@value "
						+ MultiLanguageResourceBundle.getInstance().getString("null"));
			}

			attrValue = attrValue.trim();
			if (0 == attrValue.length()) {
				throw new CommonException(this.fileAbsolutePath + ": value-range/value/@value ");
			}

			if (this.valueRangeMap.containsKey(attrValue)) {
				throw new CommonException(this.fileAbsolutePath + ": value-range/value[@value='" + attrValue + "'] "
						+ MultiLanguageResourceBundle.getInstance()
								.getString("MessageMetadataManager.startElement.reduplicate"));
			}

			valueRange.setValue(attrValue);
			valueRange.setShortText(var4.getValue("short-text"));
			attrValue = var4.getValue("reference");
			if (attrValue != null) {
				attrValue = attrValue.trim();
				if (0 == attrValue.length()) {
					throw new CommonException(this.fileAbsolutePath + ": value-range/value/@reference "
							+ MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				valueRange.setReferenceId(attrValue);
				Message var6 = MessageMetadataManager.getMessage(this.channelId, attrValue);
				valueRange.setReference(var6);
				if (null == this.valueRangeMap.get("default-ref")) {
					throw new CommonException(this.fileAbsolutePath + ": value-range[@value='" + valueRange.getValue()
							+ "']/@default-ref " + MultiLanguageResourceBundle.getInstance().getString("null"));
				}
			}

			this.valueRangeMap.put(valueRange.getValue(), valueRange);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (null == this.currentValue) {
			this.currentValue = new String(ch, start, length);
		} else {
			this.currentValue = this.currentValue + new String(ch, start, length);
		}
	}
}
