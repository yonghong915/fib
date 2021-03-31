package com.fib.gateway.message.parser;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fib.commons.exception.CommonException;
import com.fib.commons.util.ClassUtil;
import com.fib.commons.util.ClasspathUtil;
import com.fib.commons.xml.Dom4jUtils;
import com.fib.gateway.message.bean.CommonMessageBean;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.Field;
import com.fib.gateway.message.metadata.Message;
import com.fib.gateway.message.metadata.ValueRange;
import com.fib.gateway.message.util.CodeUtil;
import com.fib.gateway.message.util.ConfigManager;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.util.StringUtil;
import com.fib.gateway.message.xml.message.Constant;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import com.fib.gateway.message.xml.script.BeanShellEngine;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class XmlMessageParser extends AbstractMessageParser {
	Document document = null;

	@Override
	public MessageBean parse() {
		if (null == this.messageData) {
			throw new CommonException("messageData parameter.null");
		}
		if (null == this.message) {
			throw new CommonException("message parameter.null");
		}

		try {
			this.document = Dom4jUtils.getXPathDocument(new String(this.messageData),
					"urn:cnaps:std:beps:2010:tech:xsd:beps.121.001.01");
		} catch (DocumentException | SAXException e) {
		}
		MessageBean messageBean = this.A(this.message);
		return messageBean;
	}

	private synchronized void A(InputSource var1) {
		try {
			SchemaFactory var2 = SchemaFactory.newInstance(Constant.getSchemaType(this.message.getSchemaValidType()));
			File var3 = new File(ConfigManager.getFullPathFileName(this.message.getSchemaValidPath()));
			Schema var4 = var2.newSchema(var3);
			Validator var5 = var4.newValidator();
			SAXSource var6 = new SAXSource(var1);
			var5.validate(var6);
		} catch (Exception var7) {
			ExceptionUtil.throwActualException(var7);
		}

	}

	protected void A(String var1, String var2, MessageBean var3) {
		BeanShellEngine var4 = new BeanShellEngine();
		var4.set("messageParser", this);
		var4.set("message", this.message);
		var4.set("messageBean", var3);
		var4.set("messageData", this.messageData);
		Iterator var5;
		String var6;
		if (0 != this.variableCache.size()) {
			var5 = this.variableCache.keySet().iterator();
			var6 = null;

			while (var5.hasNext()) {
				var6 = (String) var5.next();
				var4.set(var6, this.variableCache.get(var6));
			}
		}

		var4.eval(var2);
		this.messageData = (byte[]) ((byte[]) var4.get("messageData"));
		if (0 != this.variableCache.size()) {
			var5 = this.variableCache.keySet().iterator();
			var6 = null;

			while (var5.hasNext()) {
				var6 = (String) var5.next();
				this.variableCache.put(var6, var4.get(var6));
			}
		}

	}

	public MessageBean A(Message var1) {
		return this.A((Message) var1, (Node) null);
	}

	public MessageBean A(Message var1, Node var2) {
		if (null == var1.getClassName()) {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
					.getString("XmlMessageParser.beanAssignment.message.className.null"));
		} else {
			MessageBean var3 = this.messageBean;

			Class var4;
			try {
				//var4 = Class.forName(var1.getClassName());
				var4 = ClassUtil.getClass(var1.getClassName());
				if (null != var3 && !var3.getClass().equals(var4) || null == var3) {
					var3 = (MessageBean) var4.newInstance();
				}
			} catch (Exception var8) {
				var8.printStackTrace(System.err);
				ExceptionUtil.throwActualException(var8);
			}

			if (null != this.getParentBean()) {
				var3.setParent(this.getParentBean());
			}

			var4 = null;
			String var9;
			if (null != var1.getXpath()) {
				var9 = var1.getXpath();
			} else {
				var9 = var1.getId();
			}

			List var5 = var1.getFieldList();

			for (int var6 = 0; var6 < var5.size(); ++var6) {
				Field var7 = (Field) var5.get(var6);
				if ((null != var7.getXpath() || 2000 != var7.getFieldType()) && var7.isEditable()) {
					if (var2 != null) {
						this.C(var3, var1, var7, (String) null, var2);
					} else {
						this.A(var3, var1, var7, var9);
					}
				}
			}

			return var3;
		}
	}

	private void A(MessageBean var1, Message var2, Field var3, String var4) {
		this.C(var1, var2, var3, var4, (Node) null);
	}

	private void C(MessageBean var1, Message var2, Field var3, String var4, Node var5) {
		if (null == var1) {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "MessageBean" }));
		} else if (null == var3) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "field" }));
		} else {
			switch (var3.getFieldType()) {
			case 2002:
			case 2008:
				this.B(var1, var2, var3, var4, var5);
				break;
			case 2003:
			case 2005:
			case 2006:
			case 2007:
			case 2009:
			case 2010:
			default:
				this.A(var1, var2, var3, var4, var5);
				break;
			case 2004:
				this.E(var1, var2, var3, var4, var5);
				break;
			case 2011:
				this.D(var1, var2, var3, var4, var5);
			}

		}
	}

	private void D(MessageBean var1, Message var2, Field var3, String var4, Node var5) {
		String var6 = null;
		if (var4 != null) {
			var6 = var4 + "/" + var3.getXpath();
		} else {
			var6 = var3.getXpath();
		}

		if (null != var3.getRowXpath()) {
			var6 = var6 + "/" + var3.getRowXpath();
		}

		Message var7 = null;
		Class var8 = null;
		if ("dynamic".equalsIgnoreCase(var3.getReferenceType())) {
			var7 = this.A(var3, var2, var1);
		} else {
			var8 = this.A(var1, var3);
			if (null != var3.getReference()) {
				var7 = var3.getReference();
			} else {
				var7 = new Message();
				var7.setId(var6);
				var7.setClassName(var8.getName());
				var7.setFields(var3.getSubFields());
			}
		}

		Map var9 = var7.getFields();
		String[] var10 = new String[var9.size()];
		String var11 = var6;
		if (null != var7.getXpath()) {
			var11 = var6 + "/" + var7.getXpath();
		}

		if (null != this.message.getNameSpaces() && 0 < this.message.getNameSpaces().size() && !var11.startsWith("/")) {
			var11 = "/" + var11;
		}

		for (int var12 = 0; var12 < var10.length; ++var12) {
			var10[var12] = var11 + "/"
					+ (null == ((Field) var9.get(var12)).getXpath() ? ((Field) var9.get(var12)).getName()
							: ((Field) var9.get(var12)).getXpath());
		}

		ArrayList var21 = new ArrayList();
		List var13 = null;

		int var14;
		for (var14 = 0; var14 < var10.length; ++var14) {
			if (var5 == null) {
				var13 = this.document.selectNodes(var10[var14]);
			} else {
				var13 = var5.selectNodes(var10[var14]);
			}

			if (0 != var13.size()) {
				var21.add(var13);
			}
		}

		if (0 != var21.size()) {
			var14 = ((List) var21.get(0)).size();
			int var15 = 0;
			ArrayList var16 = new ArrayList(var14);
			MessageBean var17 = null;

			for (int var18 = 0; var18 < var14; ++var18) {
				if (null != var3.getPreRowParseEvent()) {
					this.A(var3, "row-pre-parse", var3.getPreRowParseEvent(), var1, (MessageBean) null, var16, var14,
							var18);
				}

				try {
					Class var19 = Class.forName(var7.getClassName());
					var17 = (MessageBean) var19.newInstance();
				} catch (Exception var20) {
					var20.printStackTrace(System.err);
					ExceptionUtil.throwActualException(var20);
				}

				for (int var22 = 0; var22 < var21.size(); ++var22) {
					this.A(var17, var7, "text()", (Node) ((List) var21.get(var22)).get(var18), (Field) var9.get(var22));
				}

				++var15;
				var16.add(var17);
				if (null != var3.getPostRowParseEvent()) {
					this.A(var3, "row-post-parse", var3.getPostRowParseEvent(), var1, var17, var16, var14, var18);
				}
			}

			if (0 != var15) {
				this.A(var1, (Field) var3, (Class) List.class, (Object) var16);
			}
		}
	}

	private void E(MessageBean var1, Message var2, Field var3, String var4, Node var5) {
		String var6 = null;
		if (var4 != null) {
			var6 = var4 + "/" + var3.getXpath();
		} else {
			var6 = var3.getXpath();
		}

		if (null != var3.getRowXpath()) {
			var6 = var6 + "/" + var3.getRowXpath();
		}

		Message var7 = null;
		Class var8 = null;
		if ("dynamic".equalsIgnoreCase(var3.getReferenceType())) {
			var7 = this.A(var3, var2, var1);
		} else {
			var8 = this.A(var1, var3);
			if (null != var3.getReference()) {
				var7 = var3.getReference();
			} else {
				var7 = new Message();
				var7.setId(var6);
				var7.setClassName(var8.getName());
				var7.setFields(var3.getSubFields());
			}
		}

		ArrayList var11;
		MessageBean var12;
		String var25;
		if (var6.indexOf("@") == -1 && var6.indexOf("(") == -1) {
			Field var21 = (Field) var7.getFieldList().get(0);
			List var22;
			String var29;
			if (var21.getXpath().indexOf("${") == -1) {
				var22 = null;
				if (var5 == null) {
					if (null != this.message.getNameSpaces() && 0 < this.message.getNameSpaces().size()
							&& !var6.startsWith("/")) {
						var22 = this.document.selectNodes("/" + var6);
					} else {
						var22 = this.document.selectNodes(var6);
					}
				} else {
					var22 = var5.selectNodes(var6);
				}

				int var26 = 0;
				var29 = var6;
				String var30 = var7.getXpath();
				if (null != var30) {
					var29 = var6 + "/" + var30;
				}

				Message var34 = var7.copy();
				var34.setXpath(var29);
				ArrayList var16 = new ArrayList(var22.size());

				for (int var17 = 0; var17 < var22.size(); ++var17) {
					if (null != var3.getPreRowParseEvent()) {
						this.A(var3, "row-pre-parse", var3.getPreRowParseEvent(), var1, (MessageBean) null, var16,
								var22.size(), var17);
					}

					++var26;
					Node var27 = (Node) var22.get(var17);
					MessageBean var18 = this.A(var34, var27);
					var16.add(var18);
					if (null != var3.getPostRowParseEvent()) {
						this.A(var3, "row-post-parse", var3.getPostRowParseEvent(), var1, var18, var16, var22.size(),
								var17);
					}
				}

				if (0 == var26) {
					return;
				}

				this.A(var1, (Field) var3, (Class) List.class, (Object) var16);
			} else {
				if (null != var7.getXpath()) {
					var6 = var6 + "/" + var7.getXpath();
				}

				var22 = null;
				var11 = null;
				var12 = null;
				var29 = null;
				ArrayList var32 = new ArrayList();
				byte var35 = 0;
				boolean var33 = false;

				do {
					if (null != var3.getPreRowParseEvent()) {
						this.A(var3, "row-pre-parse", var3.getPreRowParseEvent(), var1, (MessageBean) null, var32,
								var35, var35);
					}

					Iterator var28 = var7.getFields().values().iterator();
					MessageBean var24 = (MessageBean) ClassUtil.createClassInstance(var7.getClassName());

					while (var28.hasNext()) {
						var21 = (Field) var28.next();
						var25 = var6 + "/" + var21.getXpath();
						var29 = this.variableCache.get(var25.substring(var25.indexOf("${") + 2, var25.indexOf("}")))
								.toString();
						var25 = var25.substring(0, var25.indexOf("${")) + var29
								+ var25.substring(var25.indexOf("}") + 1);
						var1.setMetadata(var2);
						var24.setParent(var1);
						this.A(var24, var2, var21, var25);
					}

					var32.add(var24);
					if (null != var3.getPostRowParseEvent()) {
						this.A(var3, "row-post-parse", var3.getPostRowParseEvent(), var1, var24, var32, var35, var35);
					}

					var33 = true;
					if (var24 instanceof CommonMessageBean) {
						if (0 == ((CommonMessageBean) var24).getValues().size()) {
							var33 = false;
						}
					} else {
						var33 = null != var24.toString();
					}
				} while (var33);

				var32.remove(var32.size() - 1);
				this.A(var1, (Field) var3, (Class) List.class, (Object) var32);
			}
		} else {
			String var9 = this.A(var6, var5, var3);
			if (null == var9 || 0 == var9.length()) {
				return;
			}

			int var10 = this.A(var3.getRowNumField(), var1.getAttribute(var3.getRowNumField().getName()));
			int var13;
			int var14;
			Object var15;
			byte[] var31;
			if ("dynamic".equalsIgnoreCase(var3.getReferenceType())) {
				var11 = new ArrayList(var10);
				var12 = null;
				var13 = 0;

				for (var14 = 0; var14 < var10; ++var14) {
					if (null != var3.getPreRowParseEvent()) {
						this.A(var3, "row-pre-parse", var3.getPreRowParseEvent(), var1, (MessageBean) null, var11,
								var10, var14);
					}

					var12 = (MessageBean) ClassUtil.createClassInstance(var7.getClassName());
					var1.setMetadata(var2);
					if (null != var2.getMsgCharset()) {
						var15 = null;

						try {
							var31 = var9.getBytes(var2.getMsgCharset());
						} catch (UnsupportedEncodingException var20) {
							var20.printStackTrace();
							throw new RuntimeException(
									MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport",
											new String[] { this.message.getId(), this.message.getMsgCharset() }));
						}

						var13 = this.A(var3, var7, var12, var13, var31);
					} else {
						var13 = this.A(var3, var7, var12, var13, var9.getBytes());
					}

					var11.add(var12);
					if (null != var3.getPostRowParseEvent()) {
						this.A(var3, "row-post-parse", var3.getPostRowParseEvent(), var1, var12, var11, var10, var14);
					}
				}

				var1.setAttribute(var3.getName(), var11);
			} else {
				var11 = null;
				var25 = "create" + StringUtil.toUpperCaseFirstOne(var3.getName());
				var13 = 0;

				for (var14 = 0; var14 < var10; ++var14) {
					if (null != var3.getPreRowParseEvent()) {
						this.A(var3, "row-pre-parse", var3.getPreRowParseEvent(), var1, (MessageBean) null, (List) null,
								var10, var14);
					}

					Object var23;
					if (var1 instanceof CommonMessageBean) {
						var15 = (List) var1.getAttribute(var3.getName());
						if (null == var15) {
							var15 = new ArrayList();
							var1.setAttribute(var3.getName(), var15);
						}

						var23 = new CommonMessageBean();
						((List) var15).add(var23);
					} else {
						var23 = (MessageBean) ClassUtil.invoke(var1, var25, (Class[]) null, (Object[]) null);
					}

					var1.setMetadata(var2);
					if (null != var2.getMsgCharset()) {
						var15 = null;

						try {
							var31 = var9.getBytes(var2.getMsgCharset());
						} catch (UnsupportedEncodingException var19) {
							var19.printStackTrace();
							throw new RuntimeException(
									MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport",
											new String[] { this.message.getId(), this.message.getMsgCharset() }));
						}

						var13 = this.A(var3, var7, (MessageBean) var23, var13, var31);
					} else {
						var13 = this.A(var3, var7, (MessageBean) var23, var13, var9.getBytes());
					}

					if (null != var3.getPostRowParseEvent()) {
						this.A(var3, "row-post-parse", var3.getPostRowParseEvent(), var1, (MessageBean) var23,
								(List) null, var10, var14);
					}
				}
			}
		}

	}

	private void A(MessageBean var1, Field var2, Class var3, Object var4) {
		if (null != var3 && null != var4) {
			var1.setAttribute(var2.getName(), var4);
		}

	}

	private int A(Field var1, Message var2, MessageBean var3, int var4, byte[] var5) {
		AbstractMessageParser var6 = MessageParserFactory.getMessageParser(var2);
		var6.setVariableCache(this.variableCache);
		var6.setMessage(var2);
		var6.setMessageData(var5);
		var6.setMessageBean(var3);
		var6.setParentBean(var3);
		return var6.parse(var4);
	}

	private MessageBean A(Message var1, MessageBean var2, byte[] var3) {
		AbstractMessageParser var4 = MessageParserFactory.getMessageParser(var1);
		var4.setVariableCache(this.variableCache);
		var4.setMessage(var1);
		var4.setMessageData(var3);
		var4.setParentBean(var2);
		return var4.parse();
	}

	private void B(MessageBean var1, Message var2, Field var3, String var4, Node var5) {
		String var6 = null;
		if (var4 != null) {
			var6 = var4 + "/" + var3.getXpath();
		} else {
			var6 = var3.getXpath();
		}

		Message var7 = null;
		Class var8 = null;
		if ("dynamic".equalsIgnoreCase(var3.getReferenceType())) {
			var8 = MessageBean.class;
			var7 = this.A(var3, var2, var1);
		} else {
			var8 = this.A(var1, var3);
			if (null != var3.getReference()) {
				var7 = var3.getReference();
			} else {
				var7 = new Message();
				var7.setId(var6);
				var7.setClassName(var8.getName());
				var7.setFields(var3.getSubFields());
			}
		}

		Message var9;
		MessageBean var10;
		if (null != var3.getXpath() && null == var3.getReference()) {
			MessageBean var11;
			if (var6.indexOf("@") == -1 && var6.indexOf("(") == -1) {
				var9 = null;
				List var14;
				if (var5 == null) {
					if (null != this.message.getNameSpaces() && 0 < this.message.getNameSpaces().size()
							&& !var6.startsWith("/")) {
						var14 = this.document.selectNodes("/" + var6);
					} else {
						var14 = this.document.selectNodes(var6);
					}
				} else {
					var14 = var5.selectNodes(var6);
				}

				if (var14.size() == 0) {
					return;
				}

				if (null != var7.getXpath()) {
					var6 = var6 + "/" + var7.getXpath();
				}

				Message var16 = var7.copy();
				var16.setXpath(var6);
				var11 = this.A(var16, (Node) var14.get(0));
				this.A(var1, (Field) var3, (Class) var8, (Object) var11);
			} else {
				String var13 = this.A(var6, var5, var3);
				if (null == var13 || 0 == var13.length()) {
					return;
				}

				var1.setMetadata(var2);
				if (null != var2.getMsgCharset()) {
					var10 = null;

					byte[] var15;
					try {
						var15 = var13.getBytes(var2.getMsgCharset());
					} catch (UnsupportedEncodingException var12) {
						var12.printStackTrace();
						throw new RuntimeException(
								MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport",
										new String[] { this.message.getId(), this.message.getMsgCharset() }));
					}

					var11 = this.A(var7, var1, var15);
					this.A(var1, (Field) var3, (Class) var8, (Object) var11);
				} else {
					var10 = this.A(var7, var1, var13.getBytes());
					this.A(var1, (Field) var3, (Class) var8, (Object) var10);
				}
			}
		} else {
			var9 = var7.copy();
			var9.setXpath(var4);
			if (null != var3.getXpath()) {
				var9.setXpath(var9.getXpath() + "/" + var3.getXpath());
			}

			if (null != var7.getXpath()) {
				var9.setXpath(var9.getXpath() + "/" + var7.getXpath());
			}

			var1.setMetadata(var2);
			var10 = this.A(var9, var1, this.messageData);
			if (null == var10) {
				return;
			}

			this.A(var1, (Field) var3, (Class) var8, (Object) var10);
		}

	}

	private void A(MessageBean var1, Message var2, String var3, Node var4, Field var5) {
		String var6 = this.A(var3, var4);
		var6 = new String(this.A(var5, var6.getBytes()));
		if (null != var6 && 0 != var6.length()) {
			String var7 = Constant.getJavaTypeByDataType(var5.getDataType());
			this.A(var1, var5, this.A(var7), this.A(var6, var7));
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
			String var8 = Constant.getJavaTypeByDataType(var3.getDataType());
			this.A(var1, var3, this.A(var8), this.A(var7, var8));
		}
	}

	protected Message A(Field var1, Message var2, MessageBean var3) {
		String[] var4 = var1.getReferenceId().split("\\.");
		Message var5 = var2;
		if (var4.length > 1) {
			MessageBean var6 = var3;

			for (int var7 = 0; var7 < var4.length - 1; ++var7) {
				var6 = var6.getParent();
			}

			var5 = var6.getMetadata();
		}

		Field var9 = var5.getField(var4[var4.length - 1]);
		Object var10 = var3.getAttribute(var1.getReferenceId());
		ValueRange var8 = (ValueRange) var9.getValueRange().get(var10);
		if (null == var8) {
			var8 = (ValueRange) var9.getValueRange().get("default-ref");
		}

		return var8.getReference();
	}

	private String A(String var1, Node var2) {
		return var2.getText();
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

	private Class A(String var1) {
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
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("XmlMessageParser.fieldType.illegal", new String[] { var1 }));
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
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("XmlMessageParser.fieldType.illegal", new String[] { var2 }));
		}
	}

	private Class A(MessageBean var1, Field var2) {
		String var3 = null;
		String var4;
		if (var1 instanceof CommonMessageBean) {
			var3 = "com.giantstone.message.bean.CommonMessageBean";
		} else if (null != var2.getReference()) {
			var3 = var2.getReference().getClassName();
		} else if (null != var2.getCombineOrTableFieldClassName()) {
			var3 = var2.getCombineOrTableFieldClassName();
		} else {
			var4 = var1.getClass().getName();
			var3 = var4 + StringUtil.toUpperCaseFirstOne(var2.getName());
		}

		var4 = null;

		try {
			//Class var7 = Class.forName(var3);
			Class var7 = Thread.currentThread().getContextClassLoader().loadClass(var3);
			return var7;
		} catch (ClassNotFoundException var6) {
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"XmlMessageParser.getFieldClass.class.notFound", new String[] { var3 }) + var6.getMessage(), var6);
		}
	}

	protected int A(Field var1, Object var2) {
		boolean var3 = false;
		int var4;
		switch (var1.getDataType()) {
		case 3001:
			if ("".equals(var2)) {
				var2 = "0";
			}

			var4 = Integer.parseInt((String) var2);
			break;
		case 3002:
		case 3006:
		default:
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"parseIntValue.dataType.unsupport", new String[] { "" + var1.getDataType(), var1.getName() }));
		case 3003:
		case 3007:
			var4 = (Integer) var2;
			break;
		case 3004:
			var4 = ((Byte) var2).intValue();
			break;
		case 3005:
		case 3008:
			var4 = ((Short) var2).intValue();
		}

		return var4;
	}

	protected byte[] A(Field var1, byte[] var2) {
		if (5000 == var1.getPaddingDirection()) {
			return var2;
		} else {
			String var3 = var1.getExtendAttribute("remove_start");
			int var4;
			byte[] var5;
			if (null != var3) {
				var4 = CodeUtil.byteArrayIndexOf(var2, CodeUtil.HextoByte(var3), 0);
				var5 = var2;
				if (-1 != var4) {
					var2 = new byte[var4];
					System.arraycopy(var5, 0, var2, 0, var4);
				}
			}

			boolean var6 = false;
			if (5001 == var1.getPaddingDirection()) {
				for (var4 = 0; var4 < var2.length && var2[var4] == var1.getPadding(); ++var4) {
				}

				if (0 == var4) {
					return var2;
				} else if (var2.length == var4) {
					return new byte[0];
				} else {
					var5 = new byte[var2.length - var4];
					System.arraycopy(var2, var4, var5, 0, var5.length);
					return var5;
				}
			} else {
				for (var4 = var2.length - 1; var4 >= 0 && var2[var4] == var1.getPadding(); --var4) {
				}

				if (var2.length - 1 == var4) {
					return var2;
				} else if (0 > var4) {
					return new byte[0];
				} else {
					var5 = new byte[var4 + 1];
					System.arraycopy(var2, 0, var5, 0, var5.length);
					return var5;
				}
			}
		}
	}

	protected void A(Field var1, String var2, String var3, MessageBean var4, MessageBean var5, List var6, int var7,
			int var8) {
		BeanShellEngine var9 = new BeanShellEngine();
		var9.set("messageParser", this);
		var9.set("message", this.message);
		var9.set("messageBean", var4);
		var9.set("messageData", this.messageData);
		var9.set("field", var1);
		var9.set("list", var6);
		var9.set("rowNum", var7);
		var9.set("currRow", var8);
		var9.set("currentBean", var5);
		Iterator var10;
		String var11;
		if (0 != this.variableCache.size()) {
			var10 = this.variableCache.keySet().iterator();
			var11 = null;

			while (var10.hasNext()) {
				var11 = (String) var10.next();
				var9.set(var11, this.variableCache.get(var11));
			}
		}

		var9.eval(var3);
		if (0 != this.variableCache.size()) {
			var10 = this.variableCache.keySet().iterator();
			var11 = null;

			while (var10.hasNext()) {
				var11 = (String) var10.next();
				this.variableCache.put(var11, var9.get(var11));
			}
		}

	}
}
