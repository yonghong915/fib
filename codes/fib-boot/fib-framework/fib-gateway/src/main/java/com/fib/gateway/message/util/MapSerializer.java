package com.fib.gateway.message.util;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.commons.exception.CommonException;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

public class MapSerializer {
	private static class E {

		private String key;
		private String type;
		private boolean blank;

		public String getKey() {
			return key;
		}

		public void setKey(String s) {
			key = s;
		}

		public String getType() {
			return type;
		}

		public void setType(String s) {
			type = s;
		}

		public boolean isBlank() {
			return blank;
		}

		public void setBlank(boolean flag) {
			blank = flag;
		}

		private E() {
			blank = false;
		}

	}

	private static class XMLParser extends DefaultHandler {

		private Map parseredMap;
		private Stack mapStackForOther;
		private Stack mapStackForSelf;
		private Stack listStackForOther;
		private Stack listStackForSelf;
		private Stack entryStack;
		private Stack elementStack;
		private String charset;
		private boolean isByteToStr;
		private String elementValue;

		public Map getParseredMap() {
			return parseredMap;
		}

		public void characters(char ac[], int i, int j) throws SAXException {
			if (null != elementValue) {
				elementValue = elementValue + new String(ac, i, j);
			} else {
				elementValue = new String(ac, i, j);
			}
		}

		public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
			elementValue = null;
			String s3 = s2;
			if ("M".equals(s3)) {
				HashMap hashmap = new HashMap();
				mapStackForOther.push(hashmap);
				mapStackForSelf.push(hashmap);
			} else if ("e".equals(s3)) {
				String s4 = attributes.getValue("k");
				if (s4 == null || s4.length() < 1)
					throw new IllegalArgumentException(
							MultiLanguageResourceBundle.getInstance().getString("MapSerializer.key.notSet"));
				String s6 = attributes.getValue("t");
				E e1 = new E();
				e1.setKey(s4);
				e1.setType(s6);
				if ("T".equals(attributes.getValue("b")))
					e1.setBlank(true);
				else
					e1.setBlank(false);
				entryStack.push(e1);
			} else if ("L".equals(s3)) {
				LinkedList linkedlist = new LinkedList();
				listStackForOther.push(linkedlist);
				listStackForSelf.push(linkedlist);
			} else if ("l".equals(s3)) {
				String s5 = attributes.getValue("t");
				E e = new E();
				e.setType(s5);
				if ("T".equals(attributes.getValue("b")))
					e.setBlank(true);
				else
					e.setBlank(false);
				elementStack.push(e);
			}
		}

		public void endElement(String s, String s1, String s2) throws SAXException {
			String s3 = s2;
			if ("l".equals(s3)) {
				List list = (List) listStackForSelf.lastElement();
				E e = (E) elementStack.pop();
				String s4 = e.getType();
				if (s4 == null) {
					if (e.isBlank())
						list.add("");
					else
						list.add(null);
				} else if (!s4.equals("L") && !s4.equals("M"))
					list.add(getValue(s4, elementValue));
				else if (s4.equals("L"))
					list.add(listStackForOther.pop());
				else if (s4.equals("M"))
					list.add(mapStackForOther.pop());
				s4 = null;
			} else if ("e".equals(s3)) {
				Map map = (Map) mapStackForSelf.lastElement();
				E e1 = (E) entryStack.pop();
				String s5 = e1.getKey();
				String s6 = e1.getType();
				if (s6 == null) {
					if (e1.isBlank())
						map.put(s5, "");
					else
						map.put(s5, null);
				} else if (!s6.equals("L") && !s6.equals("M"))
					map.put(s5, getValue(s6, elementValue));
				else if (s6.equals("L"))
					map.put(s5, listStackForOther.pop());
				else if (s6.equals("M"))
					map.put(s5, mapStackForOther.pop());
				s5 = null;
				s6 = null;
			} else if ("L".equals(s3))
				listStackForSelf.pop();
			else if ("M".equals(s3))
				parseredMap = (Map) mapStackForSelf.pop();
			elementValue = null;
		}

		public Object getValue(String s, String s1)
		{
			if ("S".equals(s))
				return s1;
			if ("d".equals(s))
				return Double.valueOf(s1);
			if ("l".equals(s))
				return Long.valueOf(s1);
			if ("f".equals(s))
				return Float.valueOf(s1);
			if ("i".equals(s))
				return Integer.valueOf(s1);
			if ("s".equals(s))
				return Short.valueOf(s1);
			if ("b".equals(s))
				return Byte.valueOf(s1);
			return "";
//			if (!"B".equals(s))
//				break MISSING_BLOCK_LABEL_156;
//			if (null == s1)
//				if (isByteToStr)
//					return "";
//				else
//					return new byte[0];
//			if (!isByteToStr)
//				break MISSING_BLOCK_LABEL_151;
//			return new String(CodeUtil.HextoByte(s1), charset);
//			return CodeUtil.HextoByte(s1);
//			if ("o".equals(s))
//				return Boolean.valueOf(s1);
//			if ("M".equals(s))
//				return null;
//			if ("L".equals(s1))
//				return null;
//			if ("D".equals(s))
//				return new BigDecimal(s1);
//			if ("T".equals(s))
//				return new Timestamp(Long.parseLong(s1));
//			if ("SS".equals(s))
//				return s1.split(",");
//			else
//				throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("type.unsupport", new String[] {
//					s
//				}));
		}

		public XMLParser(String s, String s1) throws ParserConfigurationException, SAXException, IOException {
			mapStackForOther = new Stack();
			mapStackForSelf = new Stack();
			listStackForOther = new Stack();
			listStackForSelf = new Stack();
			entryStack = new Stack();
			elementStack = new Stack();
			charset = "utf-8";
			isByteToStr = false;
			elementValue = null;
			charset = s1;
			isByteToStr = true;

			SAXParserFactory saxParserFactory = SAXParserFactory.newDefaultInstance();
			try {
				SAXParser saxParser = saxParserFactory.newSAXParser();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(s));
				saxParser.parse(is, this);
			} catch (Exception e) {
				throw new CommonException("Failed to read file", e);
			}
		}

		public XMLParser(String s) throws ParserConfigurationException, SAXException, IOException {
			mapStackForOther = new Stack();
			mapStackForSelf = new Stack();
			listStackForOther = new Stack();
			listStackForSelf = new Stack();
			entryStack = new Stack();
			elementStack = new Stack();
			charset = "utf-8";
			isByteToStr = false;
			elementValue = null;

			SAXParserFactory saxParserFactory = SAXParserFactory.newDefaultInstance();
			try {
				SAXParser saxParser = saxParserFactory.newSAXParser();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(s));
				saxParser.parse(is, this);
			} catch (Exception e) {
				throw new CommonException("Failed to read file", e);
			}
		}
	}

	public MapSerializer() {
	}

	public static Map deserialize(String s) {
		if (s == null || s.length() == 0)
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("MapSerializer.deserialize.noBytes"));
		XMLParser xmlparser = null;
		try {
			xmlparser = new XMLParser(s);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmlparser.getParseredMap();
	}

	public static String serialize(Map map) {
		if (map == null) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "map" }));
		} else {
			StringBuffer stringbuffer = new StringBuffer(10240);
			stringbuffer.append("<?xml version=\"1.0\"?>");
			stringbuffer.append("<map-message>");
			stringbuffer.append(mapToElement(map));
			stringbuffer.append("</map-message>");
			return stringbuffer.toString();
		}
	}

	private static String mapToElement(Map map) {
		StringBuffer stringbuffer = new StringBuffer(10240);
		Iterator iterator = map.keySet().iterator();
		stringbuffer.append("<M>");
		while (iterator.hasNext()) {
			String s = iterator.next().toString();
			Object obj = map.get(s);
			if (obj == null)
				stringbuffer.append((new StringBuilder()).append("<e k=\"").append(s).append("\"/>").toString());
			else if ("".equals(obj)) {
				stringbuffer
						.append((new StringBuilder()).append("<e k=\"").append(s).append("\" b=\"T\" />").toString());
			} else {
				if (obj instanceof String) {
					stringbuffer
							.append((new StringBuilder()).append("<e k=\"").append(s).append("\" t=\"S\">").toString());
					stringbuffer.append(StringUtil.formatXmlValue((String) obj));
				} else if (obj instanceof Double) {
					stringbuffer
							.append((new StringBuilder()).append("<e k=\"").append(s).append("\" t=\"d\">").toString());
					stringbuffer.append(obj);
				} else if (obj instanceof Long) {
					stringbuffer
							.append((new StringBuilder()).append("<e k=\"").append(s).append("\" t=\"l\">").toString());
					stringbuffer.append(obj);
				} else if (obj instanceof Float) {
					stringbuffer
							.append((new StringBuilder()).append("<e k=\"").append(s).append("\" t=\"f\">").toString());
					stringbuffer.append(obj);
				} else if (obj instanceof Integer) {
					stringbuffer
							.append((new StringBuilder()).append("<e k=\"").append(s).append("\" t=\"i\">").toString());
					stringbuffer.append(obj);
				} else if (obj instanceof Short) {
					stringbuffer
							.append((new StringBuilder()).append("<e k=\"").append(s).append("\" t=\"s\">").toString());
					stringbuffer.append(obj);
				} else if (obj instanceof Byte) {
					stringbuffer
							.append((new StringBuilder()).append("<e k=\"").append(s).append("\" t=\"b\">").toString());
					stringbuffer.append(obj);
				} else if (obj instanceof byte[]) {
					stringbuffer
							.append((new StringBuilder()).append("<e k=\"").append(s).append("\" t=\"B\">").toString());
					stringbuffer.append(new String(CodeUtil.BytetoHex((byte[]) (byte[]) obj)));
				} else if (obj instanceof Boolean) {
					stringbuffer
							.append((new StringBuilder()).append("<e k=\"").append(s).append("\" t=\"o\">").toString());
					stringbuffer.append(obj);
				} else if (obj instanceof Map) {
					stringbuffer
							.append((new StringBuilder()).append("<e k=\"").append(s).append("\" t=\"M\">").toString());
					stringbuffer.append(mapToElement((Map) obj));
				} else if (obj instanceof List) {
					stringbuffer
							.append((new StringBuilder()).append("<e k=\"").append(s).append("\" t=\"L\">").toString());
					stringbuffer.append(listToElement((List) obj));
				} else if (obj instanceof BigDecimal) {
					stringbuffer
							.append((new StringBuilder()).append("<e k=\"").append(s).append("\" t=\"D\">").toString());
					stringbuffer.append(obj);
				} else if (obj instanceof Timestamp) {
					stringbuffer
							.append((new StringBuilder()).append("<e k=\"").append(s).append("\" t=\"T\">").toString());
					stringbuffer.append(((Timestamp) obj).getTime());
				} else if (obj instanceof String[]) {
					stringbuffer.append(
							(new StringBuilder()).append("<e k=\"").append(s).append("\" t=\"SS\">").toString());
					String as[] = (String[]) (String[]) obj;
					int i = 0;
					do {
						if (i >= as.length)
							break;
						stringbuffer.append(as[i]);
						if (++i < as.length)
							stringbuffer.append(",");
					} while (true);
				} else {
					throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString(
							"type.unsupport",
							new String[] { (new StringBuilder()).append(obj.getClass()).append("").toString() }));
				}
				stringbuffer.append("</e>");
			}
		}
		stringbuffer.append("</M>");
		return stringbuffer.toString();
	}

	private static String listToElement(List list) {
		Iterator iterator = list.iterator();
		StringBuffer stringbuffer = new StringBuffer(5120);
		stringbuffer.append("<L>");
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			if (obj == null)
				stringbuffer.append("<l/>");
			else if ("".equals(obj)) {
				stringbuffer.append("<l b=\"T\" />");
			} else {
				if (obj instanceof String) {
					stringbuffer.append("<l t=\"S\">");
					stringbuffer.append(StringUtil.formatXmlValue((String) obj));
				} else if (obj instanceof Double) {
					stringbuffer.append("<l t=\"d\">");
					stringbuffer.append(obj);
				} else if (obj instanceof Long) {
					stringbuffer.append("<l t=\"l\">");
					stringbuffer.append(obj);
				} else if (obj instanceof Float) {
					stringbuffer.append("<l t=\"f\">");
					stringbuffer.append(obj);
				} else if (obj instanceof Integer) {
					stringbuffer.append("<l t=\"i\">");
					stringbuffer.append(obj);
				} else if (obj instanceof Short) {
					stringbuffer.append("<l t=\"s\">");
					stringbuffer.append(obj);
				} else if (obj instanceof Byte) {
					stringbuffer.append("<l t=\"b\">");
					stringbuffer.append(obj);
				} else if (obj instanceof byte[]) {
					stringbuffer.append("<l t=\"B\">");
					stringbuffer.append(new String(CodeUtil.BytetoHex((byte[]) (byte[]) obj)));
				} else if (obj instanceof Boolean) {
					stringbuffer.append("<l t=\"o\">");
					stringbuffer.append(obj);
				} else if (obj instanceof Map) {
					stringbuffer.append("<l t=\"M\">");
					stringbuffer.append(mapToElement((Map) obj));
				} else if (obj instanceof List) {
					stringbuffer.append("<l t=\"L\">");
					stringbuffer.append(listToElement((List) obj));
				} else if (obj instanceof BigDecimal) {
					stringbuffer.append("<l t=\"D\">");
					stringbuffer.append(obj);
				} else if (obj instanceof Timestamp) {
					stringbuffer.append("<l t=\"T\">");
					stringbuffer.append(((Timestamp) obj).getTime());
				} else if (obj instanceof String[]) {
					stringbuffer.append("<l t=\"SS\">");
					String as[] = (String[]) (String[]) obj;
					int i = 0;
					do {
						if (i >= as.length)
							break;
						stringbuffer.append(as[i]);
						if (++i < as.length)
							stringbuffer.append(",");
					} while (true);
				} else {
					throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString(
							"type.unsupport",
							new String[] { (new StringBuilder()).append(obj.getClass()).append("").toString() }));
				}
				stringbuffer.append("</l>");
			}
		}
		stringbuffer.append("</L>");
		return stringbuffer.toString();
	}
}
