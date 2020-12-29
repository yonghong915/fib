package com.fib.gateway.message.parser;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import com.fib.commons.exception.CommonException;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.Field;
import com.fib.gateway.message.metadata.Message;

import cn.hutool.core.util.StrUtil;

public class XmlMessageParser extends AbstractMessageParser {
	Document document = null;

	@Override
	public MessageBean parse() {
		if (null == this.messageData) {

		}
		if (null == this.message) {

		}

		SAXReader var1 = new SAXReader();
		var1.getDocumentFactory().setXPathNamespaceURIs(this.message.getNameSpaces());
		try {
			this.document = var1.read(new InputSource(new StringReader(new String(this.messageData))));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MessageBean messageBean = this.getMessageBeann(this.message);
		return messageBean;
	}

	private MessageBean getMessageBeann(Message message) {
		return this.getMessageBeann(message, null);
	}

	private MessageBean getMessageBeann(Message message, Node node) {
		String className = message.getClassName();
		if (StrUtil.isEmpty(className)) {
			throw new CommonException("XmlMessageParser.beanAssignment.message.className.null");
		}
		MessageBean messageBean = this.messageBean;
		try {
			Class<?> clazz = Class.forName(className);
			if (null != messageBean && !messageBean.getClass().equals(clazz) || null == messageBean) {
				messageBean = (MessageBean) clazz.getDeclaredConstructor().newInstance();
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {

			e.printStackTrace();
		}

		String xPath = "";
		if (StrUtil.isNotEmpty(message.getXpath())) {
			xPath = message.getXpath();
		} else {
			xPath = message.getId();
		}
		List<Field> var5 = message.getFieldList();
		for (int var6 = 0; var6 < var5.size(); ++var6) {
			Field field = var5.get(var6);
			if ((null != field.getXpath() || 2000 != field.getFieldType()) && field.isEditable()) {
				if (node != null) {
					// this.C(messageBean, message, field, (String) null, node);
				} else {
					this.A(messageBean, message, field, xPath);
					// this.A(var3, var1, var7, var9);
				}
			}
		}
		return messageBean;
	}

	private void A(MessageBean var1, Message var2, Field var3, String var4) {
		this.C(var1, var2, var3, var4, (Node) null);
	}

	private void C(MessageBean var1, Message var2, Field var3, String var4, Node var5) {
		if (null == var1) {
			throw new IllegalArgumentException("");
		} else if (null == var3) {
			throw new IllegalArgumentException("");
		} else {

			this.A(var1, var2, var3, var4, var5);

		}
	}

	private void A(MessageBean var1, Message var2, Field var3, String var4, Node var5) {
		String var6 = null;
		if (var3.getXpath().indexOf("${") == -1) {
			if (null == var4) {
				var6 = var3.getXpath();
			} else {
				var6 = var4 + "/" + var3.getXpath();
			}
		} else {
			var6 = var4;
		}

		String var7 = this.A(var6, var5, var3);
		var7 = new String(this.A(var3, var7.getBytes()));
		if (null != var7 && 0 != var7.length()) {
			String var8 = getJavaTypeByDataType(var3.getDataType());
			this.A(var1, var3, this.A(var8), this.A(var7, var8));
		}
	}

	private void A(MessageBean var1, Field var2, Class<?> var3, Object var4) {
		if (null != var3 && null != var4) {
			var1.setAttribute(var2.getName(), var4);
		}

	}

	private Class<?> A(String var1) {
		if ("String".equals(var1)) {
			return String.class;
		} else if ("byte[]".equals(var1)) {
			return byte[].class;
		} else if ("int".equals(var1)) {
			return Integer.TYPE;
		} else if ("byte".equals(var1)) {
			return Byte.TYPE;
		} else if ("short".equals(var1)) {
			return Short.TYPE;
		} else if ("long".equals(var1)) {
			return Long.TYPE;
		} else {
			throw new RuntimeException("ddd");
		}
	}

	private Object A(String var1, String var2) {
		if (null == var1) {
			return null;
		} else if ("String".equals(var2)) {
			return var1;
		} else if ("byte[]".equals(var2)) {
			return var1.getBytes();
		} else if ("int".equals(var2)) {
			if ("".equals(var1)) {
				var1 = "0";
			}

			return Integer.parseInt(var1);
		} else if ("byte".equals(var2)) {
			return !"".equals(var1) ? var1.getBytes()[0] : new Byte("0");
		} else if ("short".equals(var2)) {
			if ("".equals(var1)) {
				var1 = "0";
			}

			return Short.parseShort(var1);
		} else if ("long".equals(var2)) {
			if ("".equals(var1)) {
				var1 = "0";
			}

			return Long.parseLong(var1);
		} else {
			throw new RuntimeException("eeee");
		}
	}

	protected byte[] A(Field var1, byte[] var2) {
		return var2;
//		if (5000 == var1.getPaddingDirection()) {
//			return var2;
//		} else {
//			String var3 = var1.getExtendAttribute("remove_start");
//			int var4;
//			byte[] var5;
//			if (null != var3) {
//				var4 = CodeUtil.byteArrayIndexOf(var2, CodeUtil.HextoByte(var3), 0);
//				var5 = var2;
//				if (-1 != var4) {
//					var2 = new byte[var4];
//					System.arraycopy(var5, 0, var2, 0, var4);
//				}
//			}
//
//			boolean var6 = false;
//			if (5001 == var1.getPaddingDirection()) {
//				for (var4 = 0; var4 < var2.length && var2[var4] == var1.getPadding(); ++var4) {
//				}
//
//				if (0 == var4) {
//					return var2;
//				} else if (var2.length == var4) {
//					return new byte[0];
//				} else {
//					var5 = new byte[var2.length - var4];
//					System.arraycopy(var2, var4, var5, 0, var5.length);
//					return var5;
//				}
//			} else {
//				for (var4 = var2.length - 1; var4 >= 0 && var2[var4] == var1.getPadding(); --var4) {
//				}
//
//				if (var2.length - 1 == var4) {
//					return var2;
//				} else if (0 > var4) {
//					return new byte[0];
//				} else {
//					var5 = new byte[var4 + 1];
//					System.arraycopy(var2, 0, var5, 0, var5.length);
//					return var5;
//				}
//			}
//		}
	}

	private String A(String var1, Node var2, Field var3) {
		int var4 = var1.indexOf("/text()");
		if (-1 < var4) {
			var1 = var1.substring(0, var4);
		}

		Node var5 = null;
		if (var2 == null) {
			if (null != this.message.getNameSpaces() && 0 < this.message.getNameSpaces().size()
					&& !var1.startsWith("/")) {
				var5 = this.document.selectSingleNode("/" + var1);
			} else {
				var5 = this.document.selectSingleNode(var1);
			}
		} else {
			var5 = var2.selectSingleNode(var1);
		}

		return null == var5 ? "" : var5.getText();
	}

	public static String getFieldTypeText(int var0) {
		switch (var0) {
		case 2000:
			return "fixed-field";
		case 2001:
			return "var-field";
		case 2002:
			return "combine-field";
		case 2003:
			return "var-combine-field";
		case 2004:
			return "table";
		case 2005:
			return "table-row-num-field";
		case 2006:
			return "bitmap-field";
		case 2007:
			return "length-field";
		case 2008:
			return "reference-field";
		case 2009:
			return "var-reference-field";
		case 2010:
			return "mac-field";
		case 2011:
			return "var-table";
		case 2012:
			return "pboc-length-field";
		default:
			throw new RuntimeException("aaa");
		}
	}

	public static int getFieldTypeByText(String var0) {
		if ("fixed-field".equalsIgnoreCase(var0)) {
			return 2000;
		} else if ("var-field".equalsIgnoreCase(var0)) {
			return 2001;
		} else if ("combine-field".equalsIgnoreCase(var0)) {
			return 2002;
		} else if ("var-combine-field".equalsIgnoreCase(var0)) {
			return 2003;
		} else if ("table".equalsIgnoreCase(var0)) {
			return 2004;
		} else if ("var-table".equalsIgnoreCase(var0)) {
			return 2011;
		} else if ("bitmap-field".equalsIgnoreCase(var0)) {
			return 2006;
		} else if ("length-field".equalsIgnoreCase(var0)) {
			return 2007;
		} else if ("table-row-num-field".equalsIgnoreCase(var0)) {
			return 2005;
		} else if ("reference-field".equalsIgnoreCase(var0)) {
			return 2008;
		} else if ("var-reference-field".equalsIgnoreCase(var0)) {
			return 2009;
		} else if ("mac-field".equalsIgnoreCase(var0)) {
			return 2010;
		} else if ("pboc-length-field".equalsIgnoreCase(var0)) {
			return 2012;
		} else {
			throw new RuntimeException("bbb");
		}
	}

	public static String getJavaTypeByDataType(int var0) {
		switch (var0) {
		case 3000:
		case 3001:
		case 3006:
		case 3010:
		case 3011:
			return "String";
		case 3002:
			return "byte[]";
		case 3003:
		case 3007:
			return "int";
		case 3004:
			return "byte";
		case 3005:
		case 3008:
			return "short";
		case 3009:
			return "long";
		default:
			throw new RuntimeException("ccc");
		}
	}

	public static void main(String[] args) {
		byte[] a = "abcd".getBytes();
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}

	}
}
