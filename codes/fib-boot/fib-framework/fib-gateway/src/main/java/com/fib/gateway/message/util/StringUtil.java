package com.fib.gateway.message.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.GregorianCalendar;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;


public class StringUtil {
	public static String formatXmlString(String var0) {
		SAXReader var1 = new SAXReader();
		StringReader var2 = new StringReader(var0);
		Document var3 = null;

		try {
			var3 = var1.read(var2);
		} catch (DocumentException var9) {
			//ExceptionUtil.throwActualException(var9);
		}

		OutputFormat var4 = OutputFormat.createPrettyPrint();
		var4.setSuppressDeclaration(true);
		StringWriter var5 = new StringWriter();
		XMLWriter var6 = new XMLWriter(var5, var4);

		try {
			var6.write(var3);
		} catch (IOException var8) {
			//ExceptionUtil.throwActualException(var8);
		}

		return var5.toString();
	}

	public static String formatXmlValue(String var0) {
		if (var0 == null) {
			return var0;
		} else if (!isValidXmlString(var0)) {
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("StringUtil.formatXmlValue.isInvalidXmlString", new String[] { var0 }));
		} else {
			StringBuffer var1 = new StringBuffer(var0.length() > 16 ? var0.length() * 2 : 32);

			for (int var3 = 0; var3 < var0.length(); ++var3) {
				char var2 = var0.charAt(var3);
				if ('&' == var2) {
					var1.append("&amp;");
				} else if ('<' == var2) {
					var1.append("&lt;");
				} else if ('>' == var2) {
					var1.append("&gt;");
				} else if ('\'' == var2) {
					var1.append("&apos;");
				} else if ('"' == var2) {
					var1.append("&quot;");
				} else {
					var1.append(var2);
				}
			}

			return var1.toString();
		}
	}

	public static String getDateString() {
		StringBuffer var0 = new StringBuffer();
		GregorianCalendar var1 = new GregorianCalendar();
		int var2 = var1.get(1);
		int var3 = var1.get(2) + 1;
		int var4 = var1.get(5);
		var0.append(Integer.toString(var2));
		if (var3 < 10) {
			var0.append("0");
		}

		var0.append(Integer.toString(var3));
		if (var4 < 10) {
			var0.append("0");
		}

		var0.append(Integer.toString(var4));
		return var0.toString();
	}

	public static String getDateTimeMillisString() {
		StringBuffer var0 = new StringBuffer();
		GregorianCalendar var1 = new GregorianCalendar();
		int var2 = var1.get(1);
		int var3 = var1.get(2) + 1;
		int var4 = var1.get(5);
		int var5 = var1.get(11);
		int var6 = var1.get(12);
		int var7 = var1.get(13);
		int var8 = var1.get(14);
		var0.append(Integer.toString(var2));
		if (var3 < 10) {
			var0.append("0");
		}

		var0.append(Integer.toString(var3));
		if (var4 < 10) {
			var0.append("0");
		}

		var0.append(Integer.toString(var4));
		if (var5 < 10) {
			var0.append("0");
		}

		var0.append(Integer.toString(var5));
		if (var6 < 10) {
			var0.append("0");
		}

		var0.append(Integer.toString(var6));
		if (var7 < 10) {
			var0.append("0");
		}

		var0.append(Integer.toString(var7));
		var0.append(Integer.toString(var8));
		return var0.toString();
	}

	public static String getDateTimeString() {
		StringBuffer var0 = new StringBuffer();
		GregorianCalendar var1 = new GregorianCalendar();
		int var2 = var1.get(1);
		int var3 = var1.get(2) + 1;
		int var4 = var1.get(5);
		int var5 = var1.get(11);
		int var6 = var1.get(12);
		int var7 = var1.get(13);
		var0.append(Integer.toString(var2));
		if (var3 < 10) {
			var0.append("0");
		}

		var0.append(Integer.toString(var3));
		if (var4 < 10) {
			var0.append("0");
		}

		var0.append(Integer.toString(var4));
		if (var5 < 10) {
			var0.append("0");
		}

		var0.append(Integer.toString(var5));
		if (var6 < 10) {
			var0.append("0");
		}

		var0.append(Integer.toString(var6));
		if (var7 < 10) {
			var0.append("0");
		}

		var0.append(Integer.toString(var7));
		return var0.toString();
	}

	public static String getTimeString() {
		StringBuffer var0 = new StringBuffer();
		GregorianCalendar var1 = new GregorianCalendar();
		int var2 = var1.get(11);
		int var3 = var1.get(12);
		int var4 = var1.get(13);
		if (var2 < 10) {
			var0.append("0");
		}

		var0.append(Integer.toString(var2));
		if (var3 < 10) {
			var0.append("0");
		}

		var0.append(Integer.toString(var3));
		if (var4 < 10) {
			var0.append("0");
		}

		var0.append(Integer.toString(var4));
		return var0.toString();
	}

	public static String toLowerCaseFirstOne(String var0) {
		return Character.isLowerCase(var0.charAt(0)) ? var0 : Character.toLowerCase(var0.charAt(0)) + var0.substring(1);
	}

	public static String toUpperCaseFirstOne(String var0) {
		return Character.isUpperCase(var0.charAt(0)) ? var0 : Character.toUpperCase(var0.charAt(0)) + var0.substring(1);
	}

	public static String leftString(String var0, int var1) {
		byte[] var2 = var0.getBytes();
		if (var2.length <= var1) {
			return var0;
		} else {
			int var3 = 0;

			for (int var4 = 0; var4 < var1; ++var4) {
				if (var2[var4] >= 0 && var2[var4] <= 255) {
					++var3;
				} else {
					var3 += 2;
					++var4;
				}
			}

			if (var3 > var1) {
				var3 = var1 - 1;
			}

			return new String(var2, 0, var3);
		}
	}

	public static boolean isValidXmlString(String var0) {
		if (null == var0) {
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "string" }));
		} else {
			for (int var1 = 0; var1 < var0.length(); ++var1) {
				if (!isWatchableChar4XML(var0.charAt(var1))) {
					return false;
				}
			}

			return true;
		}
	}

	public static boolean isWatchableChar4XML(char var0) {
		return var0 < 0 || var0 >= ' ' || var0 == '\n' || var0 == '\r' || var0 == '\t';
	}
}
