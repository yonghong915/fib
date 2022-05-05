
package com.fib.upp.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.GregorianCalendar;

import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.fib.commons.exception.CommonException;

public class StringUtil {

	private StringUtil() {
	}

	public static String formatXmlString(String s) {
		SAXReader saxreader = new SAXReader();
		StringReader stringreader = new StringReader(s);
		org.dom4j.Document document = null;
		try {
			document = saxreader.read(stringreader);
		} catch (DocumentException documentexception) {
			ExceptionUtil.throwActualException(documentexception);
		}
		OutputFormat outputformat = OutputFormat.createPrettyPrint();
		outputformat.setSuppressDeclaration(true);
		StringWriter stringwriter = new StringWriter();
		XMLWriter xmlwriter = new XMLWriter(stringwriter, outputformat);
		try {
			xmlwriter.write(document);
		} catch (IOException ioexception) {
			ExceptionUtil.throwActualException(ioexception);
		}
		return stringwriter.toString();
	}

	public static String formatXmlValue(String s) {
		if (s == null)
			return s;
		if (!isValidXmlString(s))
			throw new CommonException("StringUtil.formatXmlValue.isInvalidXmlString");
		StringBuffer stringbuffer = new StringBuffer(s.length() <= 16 ? 32 : s.length() * 2);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if ('&' == c) {
				stringbuffer.append("&amp;");
				continue;
			}
			if ('<' == c) {
				stringbuffer.append("&lt;");
				continue;
			}
			if ('>' == c) {
				stringbuffer.append("&gt;");
				continue;
			}
			if ('\'' == c) {
				stringbuffer.append("&apos;");
				continue;
			}
			if ('"' == c)
				stringbuffer.append("&quot;");
			else
				stringbuffer.append(c);
		}

		return stringbuffer.toString();
	}

	public static String getDateString() {
		StringBuffer stringbuffer = new StringBuffer();
		GregorianCalendar gregoriancalendar = new GregorianCalendar();
		int i = gregoriancalendar.get(1);
		int j = gregoriancalendar.get(2) + 1;
		int k = gregoriancalendar.get(5);
		stringbuffer.append(Integer.toString(i));
		if (j < 10)
			stringbuffer.append("0");
		stringbuffer.append(Integer.toString(j));
		if (k < 10)
			stringbuffer.append("0");
		stringbuffer.append(Integer.toString(k));
		return stringbuffer.toString();
	}

	public static String getDateTimeMillisString() {
		StringBuffer stringbuffer = new StringBuffer();
		GregorianCalendar gregoriancalendar = new GregorianCalendar();
		int i = gregoriancalendar.get(1);
		int j = gregoriancalendar.get(2) + 1;
		int k = gregoriancalendar.get(5);
		int l = gregoriancalendar.get(11);
		int i1 = gregoriancalendar.get(12);
		int j1 = gregoriancalendar.get(13);
		int k1 = gregoriancalendar.get(14);
		stringbuffer.append(Integer.toString(i));
		if (j < 10)
			stringbuffer.append("0");
		stringbuffer.append(Integer.toString(j));
		if (k < 10)
			stringbuffer.append("0");
		stringbuffer.append(Integer.toString(k));
		if (l < 10)
			stringbuffer.append("0");
		stringbuffer.append(Integer.toString(l));
		if (i1 < 10)
			stringbuffer.append("0");
		stringbuffer.append(Integer.toString(i1));
		if (j1 < 10)
			stringbuffer.append("0");
		stringbuffer.append(Integer.toString(j1));
		stringbuffer.append(Integer.toString(k1));
		return stringbuffer.toString();
	}

	public static String getDateTimeString() {
		StringBuffer stringbuffer = new StringBuffer();
		GregorianCalendar gregoriancalendar = new GregorianCalendar();
		int i = gregoriancalendar.get(1);
		int j = gregoriancalendar.get(2) + 1;
		int k = gregoriancalendar.get(5);
		int l = gregoriancalendar.get(11);
		int i1 = gregoriancalendar.get(12);
		int j1 = gregoriancalendar.get(13);
		stringbuffer.append(Integer.toString(i));
		if (j < 10)
			stringbuffer.append("0");
		stringbuffer.append(Integer.toString(j));
		if (k < 10)
			stringbuffer.append("0");
		stringbuffer.append(Integer.toString(k));
		if (l < 10)
			stringbuffer.append("0");
		stringbuffer.append(Integer.toString(l));
		if (i1 < 10)
			stringbuffer.append("0");
		stringbuffer.append(Integer.toString(i1));
		if (j1 < 10)
			stringbuffer.append("0");
		stringbuffer.append(Integer.toString(j1));
		return stringbuffer.toString();
	}

	public static String getTimeString() {
		StringBuffer stringbuffer = new StringBuffer();
		GregorianCalendar gregoriancalendar = new GregorianCalendar();
		int i = gregoriancalendar.get(11);
		int j = gregoriancalendar.get(12);
		int k = gregoriancalendar.get(13);
		if (i < 10)
			stringbuffer.append("0");
		stringbuffer.append(Integer.toString(i));
		if (j < 10)
			stringbuffer.append("0");
		stringbuffer.append(Integer.toString(j));
		if (k < 10)
			stringbuffer.append("0");
		stringbuffer.append(Integer.toString(k));
		return stringbuffer.toString();
	}

	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	public static String leftString(String s, int i) {
		byte abyte0[] = s.getBytes();
		if (abyte0.length <= i)
			return s;
		int j = 0;
		for (int k = 0; k < i; k++)
			if (abyte0[k] < 0 || abyte0[k] > 255) {
				j += 2;
				k++;
			} else {
				j++;
			}

		if (j > i)
			j = i - 1;
		return new String(abyte0, 0, j);
	}

	public static boolean isValidXmlString(String s) {
		if (null == s)
			throw new CommonException("parameter.null");
		for (int i = 0; i < s.length(); i++)
			if (!isWatchableChar4XML(s.charAt(i)))
				return false;

		return true;
	}

	public static boolean isWatchableChar4XML(char c) {
		return c < 0 || c >= ' ' || c == '\n' || c == '\r' || c == '\t';
	}
}
