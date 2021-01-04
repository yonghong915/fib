package com.fib.gateway.message.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultAttribute;

import com.fib.commons.exception.CommonException;

public class XmlTools {
	private XmlTools() {
	}

	public static byte[] removeBlankNode(byte[] var0) {
		Document var1 = null;
		SAXReader var2 = new SAXReader();

		try {
			var1 = var2.read(new ByteArrayInputStream(var0));
		} catch (Exception var16) {
			var16.printStackTrace();
			throw new CommonException(var16.getMessage(), var16);
		}

		Element var3 = var1.getRootElement();
		removeBlankElement(var3, var3);
		ByteArrayOutputStream var4 = new ByteArrayOutputStream();
		XMLWriter var5 = null;

		try {
			var5 = new XMLWriter(var4);
			var5.write(var1);
		} catch (Exception var15) {
			var15.printStackTrace();
			throw new CommonException(var15.getMessage(), var15);
		} finally {
			if (null != var5) {
				try {
					var5.close();
				} catch (IOException var14) {
					var14.printStackTrace();
				}
			}

		}

		return var4.toByteArray();
	}

	private static void removeBlankElement(Element var0, Element var1) {
		List<?> var2 = var0.elements();
		Element var3 = null;

		for (int var4 = 0; var4 < var2.size(); ++var4) {
			var3 = (Element) var2.get(var4);
			if (0 < var3.elements().size()) {
				removeBlankElement(var3, var0);
			} else if ((null == var3.getText() || 0 == var3.getText().trim().length()) && 0 == var3.attributeCount()) {
				var0.remove(var3);
			} else if ((null == var3.getText() || 0 == var3.getText().trim().length()) && 0 < var3.attributeCount()) {
				List<?> var5 = var3.attributes();
				if (null != var5) {
					int var6 = 0;

					for (int var7 = 0; var7 < var5.size(); ++var7) {
						DefaultAttribute var8 = (DefaultAttribute) var5.get(var7);
						if (null == var8.getValue() || 0 == var8.getValue().trim().length()) {
							++var6;
						}
					}

					if (var6 == var5.size()) {
						var0.remove(var3);
					}
				}
			}
		}

		if (0 == var0.elements().size()) {
			var1.remove(var0);
		}

	}
}
