package com.fib.gateway.message.parser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.fib.commons.util.ClassUtil;
import com.fib.gateway.message.bean.CommonMessageBean;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.Field;
import com.fib.gateway.message.metadata.Message;
import com.fib.gateway.message.metadata.ValueRange;
import com.fib.gateway.message.util.ByteBuffer;
import com.fib.gateway.message.util.CodeUtil;
import com.fib.gateway.message.util.ConfigManager;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.util.StringUtil;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import com.fib.gateway.message.xml.script.BeanShellEngine;

public class DefaultMessageParser extends AbstractMessageParser {
	public static boolean B = false;
	public static Map G = new HashMap();
	public static boolean F = false;
	protected byte[] E = null;
	protected Set C = null;
	protected int D = 1;
	protected boolean H = false;

	protected void A() {
		if (!F) {
			this.messageBean = null;
		}

		this.E = null;
		this.C = null;
	}

	public MessageBean parse() {
		if (null == this.message) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "message" }));
		} else if (null == this.messageData) {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "messageData" }));
		} else {
			String var1 = null;

			for (Iterator var2 = G.keySet().iterator(); var2
					.hasNext(); this.messageData = CodeUtil.replaceAll(this.messageData, CodeUtil.HextoByte(var1),
							CodeUtil.HextoByte((String) G.get(var1)), 0, this.messageData.length)) {
				var1 = (String) var2.next();
			}

			this.A();
			if (0 == this.variableCache.size()) {
				this.loadVariable();
			}

			if (null == this.messageBean) {
				ClassUtil.setClassLoader(Thread.currentThread().getContextClassLoader());
				this.messageBean = (MessageBean) ClassUtil.createClassInstance(this.message.getClassName());
			}

			this.messageBean.setMetadata(this.message);
			if (null != this.getParentBean()) {
				this.messageBean.setParent(this.getParentBean());
			}

			this.H = true;
			if (this.message.getPreParseEvent() != null) {
				this.B("pre-parse", this.message.getPreParseEvent());
			}

			this.parse(0);
			if (this.message.getPostParseEvent() != null) {
				this.B("post-parse", this.message.getPostParseEvent());
			}

			return this.messageBean;
		}
	}

	protected void B(String var1, String var2) {
		BeanShellEngine var3 = new BeanShellEngine();
		var3.set("messageParser", this);
		var3.set("message", this.message);
		var3.set("messageBean", this.messageBean);
		var3.set("messageData", this.messageData);
		Iterator var4;
		String var5;
		if (0 != this.variableCache.size()) {
			var4 = this.variableCache.keySet().iterator();
			var5 = null;

			while (var4.hasNext()) {
				var5 = (String) var4.next();
				var3.set(var5, this.variableCache.get(var5));
			}
		}

		//var3.eval(var2);
		this.messageData = (byte[]) ((byte[]) var3.get("messageData"));
		if (0 != this.variableCache.size()) {
			var4 = this.variableCache.keySet().iterator();
			var5 = null;

			while (var4.hasNext()) {
				var5 = (String) var4.next();
				this.variableCache.put(var5, var3.get(var5));
			}
		}

	}

	protected void A(Field var1, String var2, String var3) {
		BeanShellEngine var4 = new BeanShellEngine();
		var4.set("messageParser", this);
		var4.set("message", this.message);
		var4.set("messageBean", this.messageBean);
		var4.set("messageData", this.messageData);
		var4.set("field", var1);
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

		var4.eval(var3);
		if (0 != this.variableCache.size()) {
			var5 = this.variableCache.keySet().iterator();
			var6 = null;

			while (var5.hasNext()) {
				var6 = (String) var5.next();
				this.variableCache.put(var6, var4.get(var6));
			}
		}

	}

	protected void A(Field var1, String var2, String var3, MessageBean var4, List var5, int var6, int var7) {
		BeanShellEngine var8 = new BeanShellEngine();
		var8.set("messageParser", this);
		var8.set("message", this.message);
		var8.set("messageBean", this.messageBean);
		var8.set("messageData", this.messageData);
		var8.set("field", var1);
		var8.set("list", var5);
		var8.set("rowNum", var6);
		var8.set("currRow", var7);
		var8.set("currentBean", var4);
		Iterator var9;
		String var10;
		if (0 != this.variableCache.size()) {
			var9 = this.variableCache.keySet().iterator();
			var10 = null;

			while (var9.hasNext()) {
				var10 = (String) var9.next();
				var8.set(var10, this.variableCache.get(var10));
			}
		}

		var8.eval(var3);
		if (0 != this.variableCache.size()) {
			var9 = this.variableCache.keySet().iterator();
			var10 = null;

			while (var9.hasNext()) {
				var10 = (String) var9.next();
				this.variableCache.put(var10, var8.get(var10));
			}
		}

	}

	protected int parse(int var1) {
		if (!this.H && null != this.getParentBean()) {
			this.messageBean.setParent(this.getParentBean());
		}

		Iterator var2 = this.message.getFields().values().iterator();

		for (Field var3 = null; var2.hasNext(); var1 = this.B(var3, var1)) {
			var3 = (Field) var2.next();
		}

		return var1;
	}

	protected int A(Field var1, MessageBean var2, Message var3, int var4, byte[] var5, int var6) {
		Message var7 = this.message;
		MessageBean var8 = var2;
		Field var9 = null;
		boolean var10 = false;
		String[] var11;
		int var17;
		if (null == var1.getRowNumField()) {
			var11 = var1.getRowNumFieldName().split("\\.");
			int var12 = 0;

			while (var12 < var11.length) {
				var9 = ((Message) var7).getField(var11[var12].trim());
				var8 = (MessageBean) var8.getAttribute(var11[var12].trim());
				++var12;
				if (var12 < var11.length) {
					if (null != var9.getReference()) {
						var7 = var9.getReference();
					} else if (0 != var9.getSubFields().size()) {
						var7 = new Message();
						((Message) var7).setFields(var9.getSubFields());
						((Message) var7).setMsgCharset(this.message.getMsgCharset());
					}
				}
			}

			var17 = this.B(var9, var8.getAttribute(var11[var11.length - 1]));
		} else {
			var9 = var1.getRowNumField();
			var17 = this.B(var9, var2.getAttribute(var1.getRowNumFieldName()));
		}

		var11 = null;
		LinkedHashMap var19 = null;
		ArrayList var13 = null;
		if (this.needIndex) {
			var13 = new ArrayList(var17);
			this.fieldIndexCache.put(var1.getName(), var13);
		}

		ArrayList var14 = new ArrayList(var17);

		for (int var16 = 0; var16 < var17; ++var16) {
			if (null != var1.getPreRowParseEvent()) {
				this.A(var1, "row-pre-parse", var1.getPreRowParseEvent(), (MessageBean) null, var14, var17, var16);
			}

			Object var18;
			if (var2 instanceof CommonMessageBean) {
				var18 = new CommonMessageBean();
			} else {
				var18 = (MessageBean) ClassUtil.createClassInstance(var3.getClassName());
			}

			if (this.needIndex) {
				var19 = new LinkedHashMap();
				var4 = this.A(var1, var3, (MessageBean) var18, var4, var5, var19, var6);
				var13.add(var19);
			} else {
				var4 = this.B(var1, var3, (MessageBean) var18, var4, var5);
			}

			var14.add(var18);
			if (null != var1.getPostRowParseEvent()) {
				this.A(var1, "row-post-parse", var1.getPostRowParseEvent(), (MessageBean) var18, var14, var17, var16);
			}
		}

		var2.setAttribute(var1.getName(), var14);
		return var4;
	}

	protected int B(Field var1, MessageBean var2, Message var3, int var4, byte[] var5, int var6) {
		MessageBean var8 = (MessageBean) ClassUtil.createClassInstance(var3.getClassName());
		LinkedHashMap var9 = null;
		if (this.needIndex) {
			var9 = new LinkedHashMap();
			var4 = this.A(var1, var3, var8, var4, var5, var9, var6);
			this.fieldIndexCache.put(var1.getName(), var9);
		} else {
			var4 = this.B(var1, var3, var8, var4, var5);
		}

		(new StringBuilder()).append("set").append(StringUtil.toUpperCaseFirstOne(var1.getName())).toString();

		try {
			var2.setAttribute(var1.getName(), var8);
		} catch (Exception var12) {
			var12.printStackTrace();
			ExceptionUtil.throwActualException(var12);
		}

		return var4;
	}

	protected Message B(Field var1, Message var2, MessageBean var3) {
		String[] var4 = var1.getReferenceId().split("\\.");
		Message var5 = var2;
		MessageBean var6 = var3;
		if (var4.length > 1) {
			for (int var7 = 0; var7 < var4.length - 1; ++var7) {
				var6 = var6.getParent();
			}

			var5 = var6.getMetadata();
		}

		Field var10 = var5.getField(var4[var4.length - 1]);
		Object var8 = var6.getAttribute(var4[var4.length - 1]);
		ValueRange var9 = (ValueRange) var10.getValueRange().get(var8);
		if (null == var9) {
			var9 = (ValueRange) var10.getValueRange().get("default-ref");
		}

		return var9.getReference();
	}

	protected Message C(Field var1, Message var2, MessageBean var3) {
		BeanShellEngine var4 = new BeanShellEngine();
		BeanShellEngine var5 = new BeanShellEngine();
		String var6 = null;
		var5.set("messageBean", var3);
		if (0 != this.variableCache.size()) {
			Iterator var7 = this.variableCache.keySet().iterator();
			String var8 = null;

			while (var7.hasNext()) {
				var8 = (String) var7.next();
				var4.set(var8, this.variableCache.get(var8));
			}
		}

		Object var10 = var5.eval(var1.getExpression());
		if (0 != this.variableCache.size()) {
			Iterator var11 = this.variableCache.keySet().iterator();
			String var9 = null;

			while (var11.hasNext()) {
				var9 = (String) var11.next();
				this.variableCache.put(var9, var4.get(var9));
			}
		}

		if (var10 != null) {
			if (var10 instanceof String) {
				var6 = (String) var10;
			} else {
				var6 = var10.toString();
			}
		}

		ValueRange var12 = (ValueRange) var1.getValueRange().get(var6);
		if (null == var12) {
			var12 = (ValueRange) var1.getValueRange().get("default-ref");
		}

		return var12.getReference();
	}

	protected int B(Field var1, int var2) {
		if (this.C != null && this.C.contains(var1.getName())) {
			return var2;
		} else {
			if (var1.getIso8583_no() > 0 && this.E != null) {
				if (8 == this.E.length && var1.getIso8583_no() > 64) {
					return var2;
				}

				++this.D;
				if (0 == CodeUtil.getBit(this.E, var1.getIso8583_no())) {
					return var2;
				}
			}

			if (var1.getPreParseEvent() != null) {
				this.A(var1, "pre-parse", var1.getPreParseEvent());
			}

			Message var3;
			if ("dynamic".equalsIgnoreCase(var1.getReferenceType())) {
				var3 = this.B(var1, this.message, this.messageBean);
				if (2004 == var1.getFieldType()) {
					var2 = this.A(var1, this.messageBean, var3, var2, this.messageData, this.indexOffSet);
				} else {
					var2 = this.B(var1, this.messageBean, var3, var2, this.messageData, this.indexOffSet);
				}
			} else if ("expression".equalsIgnoreCase(var1.getReferenceType())) {
				var3 = this.C(var1, this.message, this.messageBean);
				if (2004 == var1.getFieldType()) {
					var2 = this.A(var1, this.messageBean, var3, var2, this.messageData, this.indexOffSet);
				} else {
					var2 = this.B(var1, this.messageBean, var3, var2, this.messageData, this.indexOffSet);
				}
			} else {
				switch (var1.getFieldType()) {
				case 2000:
					var2 = this.J(var1, var2);
					break;
				case 2001:
					var2 = this.L(var1, var2);
					break;
				case 2002:
				case 2008:
					var2 = this.E(var1, var2);
					break;
				case 2003:
					var2 = this.H(var1, var2);
					break;
				case 2004:
					var2 = this.F(var1, var2);
					break;
				case 2005:
					var2 = this.K(var1, var2);
					break;
				case 2006:
					Object[] var4 = new Object[1];
					var2 = this.A(var1, var2, var4);
					this.messageBean.setAttribute(var1.getName(), var4[0]);
					if (this.E == null) {
						if (var4[0] instanceof String) {
							this.E = CodeUtil.HextoByte(((String) var4[0]).getBytes());
						} else {
							this.E = (byte[]) ((byte[]) var4[0]);
						}
					} else {
						ByteBuffer var5 = new ByteBuffer(this.E.length * 2);
						var5.append(this.E);
						if (var4[0] instanceof String) {
							var5.append(CodeUtil.HextoByte(((String) var4[0]).getBytes()));
						} else {
							var5.append((byte[]) ((byte[]) var4[0]));
						}

						this.E = var5.toBytes();
					}
					break;
				case 2007:
				case 2012:
					var2 = this.G(var1, var2);
					break;
				case 2009:
					var2 = this.D(var1, var2);
					break;
				case 2010:
					var2 = this.I(var1, var2);
					break;
				case 2011:
					var2 = this.A(var1, var2);
					break;
				default:
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("field.fieldType.notExist", new String[] { "" + var1.getFieldType() }));
				}
			}

			if (var1.getPostParseEvent() != null) {
				this.A(var1, "post-parse", var1.getPostParseEvent());
			}

			return var2;
		}
	}

	protected int I(Field var1, int var2) {
		Object[] var3 = new Object[1];
		byte[] var4 = new byte[var1.getLength()];
		System.arraycopy(this.messageData, var2, var4, 0, var4.length);
		var3[0] = var4;
		this.messageBean.setAttribute(var1.getName(), var3[0]);
		if (this.needIndex) {
			int[] var5 = new int[] { var2 + this.indexOffSet, var2 + var1.getLength() + this.indexOffSet };
			this.fieldIndexCache.put(var1.getName(), var5);
		}

		var2 += var1.getLength();
		return var2;
	}

	protected int A(Field var1, int var2) {
		int var3 = 0;
		boolean var4 = false;
		if (null != var1.getTabPrefix()) {
			var2 += var1.getTabPrefix().length;
		}

		Message var6;
		MessageBean var7;
		int var14;
		if (null != var1.getRefLengthFieldName()) {
			String[] var5 = var1.getRefLengthFieldName().split("\\.");
			var6 = this.message;
			var7 = this.messageBean;
			if (var5.length > 1) {
				for (int var8 = 0; var8 < var5.length - 1; ++var8) {
					var7 = var7.getParent();
				}

				var6 = var7.getMetadata();
			}

			Field var18 = var6.getField(var5[var5.length - 1]);
			Object var9 = var7.getAttribute(var5[var5.length - 1]);
			if (null == var9) {
				throw new RuntimeException("field[@name='" + var18.getName() + "'] "
						+ MultiLanguageResourceBundle.getInstance().getString("null"));
			}

			var14 = this.B(var18, var9);
			var14 -= var1.getRefLengthFieldOffset();
		} else if (null != var1.getTabSuffix()) {
			int var15 = CodeUtil.byteArrayIndexOf(this.messageData, var1.getTabSuffix(), var2);
			if (var15 == -1) {
				var14 = this.messageData.length - var2;
			} else {
				var14 = var15 - var2;
			}
		} else {
			if (var1.isLastRowSuffix()) {
				return this.C(var1, var2);
			}

			var14 = this.messageData.length - var2;
		}

		byte[] var16 = new byte[var14];
		System.arraycopy(this.messageData, var2, var16, 0, var14);
		var6 = var1.getReference();
		if (null == var6) {
			var6 = new Message();
			var6.setId(this.message.getId() + "-" + var1.getName());
			var6.setShortText(var1.getShortText());
			var6.setFields(var1.getSubFields());
			var6.setMsgCharset(this.message.getMsgCharset());
		}

		var7 = null;
		String var19 = "create" + StringUtil.toUpperCaseFirstOne(var1.getName());
		int var20 = 0;
		boolean var10 = false;
		LinkedHashMap var11 = null;
		ArrayList var12 = null;
		if (this.needIndex) {
			var12 = new ArrayList();
			this.fieldIndexCache.put(var1.getName(), var12);
		}

		for (; var20 < var16.length; ++var3) {
			if (null != var1.getPrefix() && (var3 == 0 && var1.isFirRowPrefix() || var3 > 0)) {
				var20 += var1.getPrefix().length;
			}

			if (null != var1.getPreRowParseEvent()) {
				this.A(var1, "row-pre-parse", var1.getPreRowParseEvent(), (MessageBean) null, (List) null, var3, var3);
			}

			Object var17;
			if (this.messageBean instanceof CommonMessageBean) {
				Object var13 = (List) this.messageBean.getAttribute(var1.getName());
				if (null == var13) {
					var13 = new ArrayList();
					this.messageBean.setAttribute(var1.getName(), var13);
				}

				var17 = new CommonMessageBean();
				((List) var13).add(var17);
			} else {
				var17 = (MessageBean) ClassUtil.invoke(this.messageBean, var19, (Class[]) null, (Object[]) null);
			}

			if (var1.isRowCut()) {
				int var21 = CodeUtil.byteArrayIndexOf(var16, var1.getSuffix(), var20);
				if (-1 == var21) {
					var21 = var16.length;
				}

				byte[] var22 = new byte[var21 - var20];
				System.arraycopy(var16, var20, var22, 0, var22.length);
				if (this.needIndex) {
					var11 = new LinkedHashMap();
					this.A(var1, var6, (MessageBean) var17, 0, var22, var11, var2 + var20 + this.indexOffSet);
					var12.add(var11);
				} else {
					this.B(var1, var6, (MessageBean) var17, 0, var22);
				}

				var20 = var21;
			} else if (this.needIndex) {
				var11 = new LinkedHashMap();
				var20 = this.A(var1, var6, (MessageBean) var17, var20, var16, var11, var2 + this.indexOffSet);
				var12.add(var11);
			} else {
				var20 = this.B(var1, var6, (MessageBean) var17, var20, var16);
			}

			if (null != var1.getSuffix()) {
				var20 += var1.getSuffix().length;
			}

			if (null != var1.getPostRowParseEvent()) {
				this.A(var1, "row-post-parse", var1.getPostRowParseEvent(), (MessageBean) var17, (List) null, var3,
						var3);
			}
		}

		var2 += var16.length;
		if (null != var1.getTabSuffix()) {
			var2 += var1.getTabSuffix().length;
		}

		return var2;
	}

	protected int C(Field var1, int var2) {
		int var3 = 0;
		boolean var4 = false;
		boolean var5 = false;
		Object var6 = null;
		Message var7 = var1.getReference();
		if (null == var7) {
			var7 = new Message();
			var7.setId(this.message.getId() + "-" + var1.getName());
			var7.setShortText(var1.getShortText());
			var7.setFields(var1.getSubFields());
			var7.setMsgCharset(this.message.getMsgCharset());
		}

		Object var8 = null;
		String var9 = "create" + StringUtil.toUpperCaseFirstOne(var1.getName());
		LinkedHashMap var10 = null;
		ArrayList var11 = null;
		if (this.needIndex) {
			var11 = new ArrayList();
			this.fieldIndexCache.put(var1.getName(), var11);
		}

		while (true) {
			if (null != var1.getPrefix() && (var3 == 0 && var1.isFirRowPrefix() || var3 > 0)) {
				var2 += var1.getPrefix().length;
			}

			int var13 = CodeUtil.byteArrayIndexOf(this.messageData, var1.getSuffix(), var2);
			if (var13 == -1) {
				if (null != var1.getPrefix() && (var3 == 0 && var1.isFirRowPrefix() || var3 > 0)) {
					var2 -= var1.getPrefix().length;
				}
				break;
			}

			int var14 = var13 - var2;
			if (0 == var14) {
				break;
			}

			byte[] var15 = new byte[var14];
			System.arraycopy(this.messageData, var2, var15, 0, var15.length);
			if (null != var1.getPreRowParseEvent()) {
				this.A(var1, "row-pre-parse", var1.getPreRowParseEvent(), (MessageBean) null, (List) null, var3, var3);
			}

			if (this.messageBean instanceof CommonMessageBean) {
				Object var12 = (List) this.messageBean.getAttribute(var1.getName());
				if (null == var12) {
					var12 = new ArrayList();
					this.messageBean.setAttribute(var1.getName(), var12);
				}

				var8 = new CommonMessageBean();
				((List) var12).add(var8);
			} else {
				var8 = (MessageBean) ClassUtil.invoke(this.messageBean, var9, (Class[]) null, (Object[]) null);
			}

			if (this.needIndex) {
				var10 = new LinkedHashMap();
				this.A(var1, var7, (MessageBean) var8, 0, var15, var10, var2 + this.indexOffSet);
				var11.add(var10);
			} else {
				this.B(var1, var7, (MessageBean) var8, 0, var15);
			}

			var2 += var14;
			if (null != var1.getSuffix()) {
				var2 += var1.getSuffix().length;
			}

			if (null != var1.getPostRowParseEvent()) {
				this.A(var1, "row-post-parse", var1.getPostRowParseEvent(), (MessageBean) var8, (List) null, var3,
						var3);
			}

			++var3;
		}

		return var2;
	}

	protected int F(Field var1, int var2) {
		boolean var3 = false;
		Message var4;
		MessageBean var5;
		String[] var7;
		int var11;
		if (null != var1.getRowNumField()) {
			var11 = this.B(var1.getRowNumField(), this.messageBean.getAttribute(var1.getRowNumField().getName()));
		} else {
			var4 = this.message;
			var5 = this.messageBean;
			Field var6 = null;
			var7 = var1.getRowNumFieldName().split("\\.");
			int var8 = 0;

			while (var8 < var7.length) {
				var6 = ((Message) var4).getField(var7[var8].trim());
				var5 = (MessageBean) var5.getAttribute(var7[var8].trim());
				++var8;
				if (var8 < var7.length) {
					if (null != var6.getReference()) {
						var4 = var6.getReference();
					} else if (0 != var6.getSubFields().size()) {
						var4 = new Message();
						((Message) var4).setFields(var6.getSubFields());
					}
				}
			}

			var11 = this.B(var6, var5.getAttribute(var7[var7.length - 1]));
		}

		var4 = var1.getReference();
		if (null == var4) {
			var4 = new Message();
			var4.setId(this.message.getId() + "-" + var1.getName());
			var4.setShortText(var1.getShortText());
			var4.setFields(var1.getSubFields());
			var4.setMsgCharset(this.message.getMsgCharset());
		}

		var5 = null;
		String var13 = "create" + StringUtil.toUpperCaseFirstOne(var1.getName());
		var7 = null;
		ArrayList var15 = null;
		if (this.needIndex) {
			var15 = new ArrayList(var11);
			this.fieldIndexCache.put(var1.getName(), var15);
		}

		for (int var9 = 0; var9 < var11; ++var9) {
			if (null != var1.getPreRowParseEvent()) {
				this.A(var1, "row-pre-parse", var1.getPreRowParseEvent(), (MessageBean) null, (List) null, var11, var9);
			}

			Object var12;
			if (this.messageBean instanceof CommonMessageBean) {
				Object var10 = (List) this.messageBean.getAttribute(var1.getName());
				if (null == var10) {
					var10 = new ArrayList();
					this.messageBean.setAttribute(var1.getName(), var10);
				}

				var12 = new CommonMessageBean();
				((List) var10).add(var12);
			} else {
				var12 = (MessageBean) ClassUtil.invoke(this.messageBean, var13, (Class[]) null, (Object[]) null);
			}

			if (null != var1.getPrefix() && (var9 == 0 && var1.isFirRowPrefix() || var9 > 0)) {
				var2 += var1.getPrefix().length;
			}

			if (this.needIndex) {
				LinkedHashMap var14 = new LinkedHashMap();
				var2 = this.A(var1, var4, (MessageBean) var12, var2, this.messageData, var14, this.indexOffSet);
				var15.add(var14);
			} else {
				var2 = this.B(var1, var4, (MessageBean) var12, var2, this.messageData);
			}

			if (null != var1.getSuffix() && (var9 + 1 == var11 && var1.isLastRowSuffix() || var9 < var11 - 1)) {
				var2 += var1.getSuffix().length;
			}

			if (null != var1.getPostRowParseEvent()) {
				this.A(var1, "row-post-parse", var1.getPostRowParseEvent(), (MessageBean) var12, (List) null, var11,
						var9);
			}
		}

		return var2;
	}

	protected int D(Field var1, int var2) {
		if (1001 == var1.getReference().getType()) {
			int[] var3 = new int[1];
			var2 = this.A(var1, var2, var3);
			byte[] var4 = new byte[var3[0]];
			System.arraycopy(this.messageData, var2, var4, 0, var4.length);
			XmlMessageParser var5 = new XmlMessageParser();
			var5.setVariableCache(this.variableCache);
			var5.setMessage(var1.getReference());
			var5.setMessageData(var4);
			Object var6 = (MessageBean) this.messageBean.getAttribute(var1.getName());
			if (null == var6 && this.messageBean instanceof CommonMessageBean) {
				var6 = new CommonMessageBean();
				this.messageBean.setAttribute(var1.getName(), var6);
			}

			var5.messageBean = (MessageBean) var6;
			var5.setNeedIndex(this.needIndex);
			var5.setIndexOffSet(var2 + this.indexOffSet);
			var5.parse();
			if (this.needIndex) {
				this.fieldIndexCache.put(var1.getName(), var5.getFieldIndexCache());
			}

			return var2 + var3[0];
		} else {
			return this.H(var1, var2);
		}
	}

	protected int H(Field var1, int var2) {
		int[] var3 = new int[1];
		var2 = this.A(var1, var2, var3);
		int var4;
		if (var1.getReference() != null && 1001 == var1.getReference().getType()) {
			byte[] var5 = new byte[var3[0]];
			System.arraycopy(this.messageData, var2, var5, 0, var5.length);
			AbstractMessageParser var6 = MessageParserFactory.getMessageParser(var1.getReference());
			//var6.setVariableCache(this.variableCache);
			var6.setMessage(var1.getReference());
			var6.setMessageData(var5);
			var6.setNeedIndex(this.needIndex);
			var6.setIndexOffSet(var2 + this.indexOffSet);
			this.messageBean.setAttribute(var1.getName(), var6.parse());
			if (this.needIndex) {
				this.fieldIndexCache.put(var1.getName(), var6.getFieldIndexCache());
			}

			var4 = var2 + var5.length;
		} else {
			var4 = this.E(var1, var2);
			if (var4 - var2 != var3[0]) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"DefaultMessageParser.parseVarCombineField.varCombineField.dataRealLength.wrong",
						new String[] { var1.getName(), String.valueOf(var4 - var2), "" + var3[0] }));
			}
		}

		return var4;
	}

	protected int E(Field var1, int var2) {
		Message var3 = var1.getReference();
		if (null != var3 && 1001 == var3.getType()) {
			int var8 = this.messageData.length - var2;
			byte[] var9 = new byte[var8];
			System.arraycopy(this.messageData, var2, var9, 0, var9.length);
			XmlMessageParser var6 = new XmlMessageParser();
			var6.setVariableCache(this.variableCache);
			var6.setMessage(var1.getReference());
			var6.setMessageData(var9);
			Object var7 = (MessageBean) this.messageBean.getAttribute(var1.getName());
			if (null == var7 && this.messageBean instanceof CommonMessageBean) {
				var7 = new CommonMessageBean();
				this.messageBean.setAttribute(var1.getName(), var7);
			}

			var6.messageBean = (MessageBean) var7;
			var6.setNeedIndex(this.needIndex);
			var6.setIndexOffSet(var2 + this.indexOffSet);
			MessageBean var6bean = var6.parse();
			this.messageBean.setAttribute(var1.getName(), var6bean);
			if (this.needIndex) {
				this.fieldIndexCache.put(var1.getName(), var6.getFieldIndexCache());
			}

			return var2 + var8;
		} else {
			if (null == var3) {
				var3 = new Message();
				var3.setId(this.message.getId() + "-" + var1.getName());
				var3.setShortText(var1.getShortText());
				var3.setFields(var1.getSubFields());
				var3.setMsgCharset(this.message.getMsgCharset());
			}

			Object var4 = (MessageBean) this.messageBean.getAttribute(var1.getName());
			if (null == var4 && this.messageBean instanceof CommonMessageBean) {
				var4 = new CommonMessageBean();
				this.messageBean.setAttribute(var1.getName(), var4);
			}

			if (this.needIndex) {
				LinkedHashMap var5 = new LinkedHashMap();
				var2 = this.A(var1, var3, (MessageBean) var4, var2, this.messageData, var5, this.indexOffSet);
				this.fieldIndexCache.put(var1.getName(), var5);
				return var2;
			} else {
				return this.B(var1, var3, (MessageBean) var4, var2, this.messageData);
			}
		}
	}

	protected int B(Field var1, Message var2, MessageBean var3, int var4, byte[] var5) {
		AbstractMessageParser var6 = MessageParserFactory.getMessageParser(var2);
		//var6.setVariableCache(this.variableCache);
		var6.setMessage(var2);
		var6.setMessageData(var5);
		var6.messageBean = var3;
		var6.setParentBean(this.messageBean);
		var4 = var6.parse(var4);
		return var4;
	}

	protected int A(Field var1, Message var2, MessageBean var3, int var4, byte[] var5, Map var6, int var7) {
		AbstractMessageParser var8 = MessageParserFactory.getMessageParser(var2);
		var8.setVariableCache(this.variableCache);
		var8.setMessage(var2);
		var8.setMessageData(var5);
		var8.messageBean = var3;
		var8.setParentBean(this.messageBean);
		var8.setNeedIndex(this.needIndex);
		var8.setIndexOffSet(var7);
		var4 = var8.parse(var4);
		var6.putAll(var8.getFieldIndexCache());
		return var4;
	}

	protected int L(Field var1, int var2) {
		int[] var3 = new int[1];
		var2 = this.A(var1, var2, var3);
		Field var4 = new Field();
		var4.setFieldType(2001);
		var4.setName(var1.getName());
		var4.setDataType(var1.getDataType());
		var4.setDataEncoding(var1.getDataEncoding());
		var4.setPaddingDirection(5000);
		var4.setPattern(var1.getPattern());
		var4.setLengthFieldLength(var1.getLength());
		var4.setStrictDataLength(true);
		if (4001 == var1.getDataEncoding()) {
			var4.setLength(var3[0] * 2);
		} else {
			var4.setLength(var3[0]);
		}

		var4.setDataCharset(var1.getDataCharset());
		var4.setRefLengthField(var1.getRefLengthField());
		var4.setExtendedAttributeText(var1.getExtendedAttributeText());
		var4.setExtendedAttributes(var1.getExtendedAttributes());
		var2 = this.J(var4, var2);
		return var2;
	}

	protected int A(Field var1, int var2, int[] var3) {
		if (var1.getRefLengthField() != null) {
			Object var4 = this.messageBean.getAttribute(var1.getRefLengthField().getName());
			var3[0] = this.B(var1.getRefLengthField(), var4);
			var3[0] -= var1.getRefLengthFieldOffset();
			var1.setLength(var3[0]);
		} else {
			Field var6 = new Field();
			var6.setName(var1.getName() + ".length");
			var6.setFieldType(2000);
			var6.setDataType(var1.getLengthFieldDataType());
			var6.setDataEncoding(var1.getLengthFieldDataEncoding());
			var6.setLength(var1.getLengthFieldLength());
			var6.setPaddingDirection(5001);
			var6.setPattern(var1.getPattern());
			var6.setPadding((byte) 48);
			Object[] var5 = new Object[1];
			var2 = this.A(var6, var2, var5);
			var3[0] = this.B(var6, var5[0]);
			var1.setLength(var3[0]);
			if (4001 == var1.getDataEncoding()) {
				var3[0] = var3[0] % 2 == 0 ? var3[0] / 2 : var3[0] / 2 + 1;
			}
		}

		return var2;
	}

	protected int B(Field var1, Object var2) {
		if (null != var2 && !"".equals(var2)) {
			boolean var3 = false;
			int var4;
			switch (var1.getDataType()) {
			case 3001:
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
				var4 = CodeUtil.ByteToInt((Byte) var2);
				break;
			case 3005:
			case 3008:
				var4 = ((Short) var2).intValue();
				break;
			case 3009:
				var4 = ((Long) var2).intValue();
			}

			return var4;
		} else {
			return 0;
		}
	}

	protected int K(Field var1, int var2) {
		return this.J(var1, var2);
	}

	protected int G(Field var1, int var2) {
		return this.J(var1, var2);
	}

	protected int J(Field var1, int var2) {
		Object[] var3 = new Object[1];
		if (-1 == var1.getLength()) {
			Object var4 = ClassUtil.getBeanValueByPath(this.messageBean, var1.getLengthScript());
			String[] var5 = var1.getLengthScript().split("\\.");
			Message var6 = this.message;
			if (var5.length > 1) {
				MessageBean var7 = this.messageBean;

				for (int var8 = 0; var8 < var5.length - 1; ++var8) {
					var7 = var7.getParent();
				}

				var6 = var7.getMetadata();
			}

			Field var9 = var6.getField(var5[var5.length - 1]);
			var1 = var1.copy();
			var1.setLength(this.B(var9, var4));
		}

		var2 = this.A(var1, var2, var3);
		if (var1.isEditable()) {
			switch (var1.getDataType()) {
			case 3003:
			case 3007:
				this.messageBean.setAttribute(var1.getName(), var3[0]);
				break;
			case 3004:
				this.messageBean.setAttribute(var1.getName(), var3[0]);
				break;
			case 3005:
			case 3008:
				this.messageBean.setAttribute(var1.getName(), var3[0]);
				break;
			case 3006:
			default:
				if (!(var3[0] instanceof String) || 3001 == var1.getDataType() || 0 != ((String) var3[0]).length()) {
					this.messageBean.setAttribute(var1.getName(), var3[0]);
				}
				break;
			case 3009:
				this.messageBean.setAttribute(var1.getName(), var3[0]);
			}
		}

		return var2;
	}

	protected int A(Field var1, int var2, Object[] var3) {
		int[] var4 = null;
		if (this.needIndex) {
			var4 = new int[] { var2 + this.indexOffSet, 0 };
		}

		boolean var5 = false;
		Object var6 = null;
		int var12;
		byte[] var13;
		switch (var1.getDataType()) {
		case 3000:
		case 3001:
		case 3006:
		case 3010:
		case 3011:
			byte[] var7;
			int var16;
			if (var1.getPrefix() != null) {
				var7 = var1.getPrefix();

				for (var16 = 0; var16 < var7.length; ++var16) {
					if (var7[var16] != this.messageData[var2 + var16]) {
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"DefaultMessageParser.parseFixedFieldValue.prefix.illegal",
								new String[] { var1.getName(), new String(var1.getPrefix()), "" + var2 }));
					}
				}

				var2 += var7.length;
				if (this.needIndex) {
					var4[0] += var7.length;
				}
			}

			int var14;
			if (!var1.isStrictDataLength() && var1.getPaddingDirection() == 5000) {
				if (var1.getSuffix() == null
						&& var1 != this.message.getFields().get(this.message.getFields().size() - 1)) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"DefaultMessageParser.parseFixedFieldValue.field.mustHaveSuffix",
							new String[] { var1.getName() }));
				}

				if (var1.getSuffix() != null) {
					var14 = CodeUtil.byteArrayIndexOf(this.messageData, var1.getSuffix(), var2);
					if (-1 == var14) {
						if (var1 != this.message.getFields().get(this.message.getFields().size() - 1)) {
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
									"DefaultMessageParser.parseFixedFieldValue.field.noSuffixInMessage",
									new String[] { var1.getName(), new String(var1.getSuffix()), "" + var2 }));
						}

						var14 = this.messageData.length;
					}

					var12 = var14 - var2;
				} else {
					var12 = this.messageData.length - var2;
				}
			} else if (null != var1.getSuffix()) {
				var14 = CodeUtil.byteArrayIndexOf(this.messageData, var1.getSuffix(), var2);
				if (-1 == var14) {
					if (var1 != this.message.getFields().get(this.message.getFields().size() - 1)) {
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"DefaultMessageParser.parseFixedFieldValue.field.noSuffixInMessage",
								new String[] { var1.getName(), new String(var1.getSuffix()), "" + var2 }));
					}

					var14 = this.messageData.length;
				}

				var12 = var14 - var2;
			} else {
				var12 = var1.getLength();
				if (4001 == var1.getDataEncoding()) {
					var12 = var12 % 2 == 0 ? var12 / 2 : var12 / 2 + 1;
				}
			}

			if (var12 <= 0) {
				if (var1.getSuffix() != null) {
					var2 += var1.getSuffix().length;
				}

				var3[0] = "";
			} else {
				var13 = new byte[var12];

				try {
					System.arraycopy(this.messageData, var2, var13, 0, var13.length);
				} catch (Exception var11) {
					throw new RuntimeException(
							"parse messageBean[" + this.message.getId() + "] field[" + var1.getName()
									+ "]'s value failed! startIndex=[" + var2 + "], valueLength=[" + var12 + "]",
							var11);
				}

				if ("true".equals(var1.getExtendAttribute("remove_over_length")) && var13.length > var1.getLength()) {
					var7 = var13;
					var13 = new byte[var1.getLength()];
					System.arraycopy(var7, 0, var13, 0, var13.length);
				}

				if (4001 == var1.getDataEncoding()) {
					var13 = CodeUtil.BCDtoASC(var13);
				}

				var13 = this.B(var1, var13);
				if (4001 == var1.getDataEncoding() || B) {
					if (2001 == var1.getFieldType() && var1.getRefLengthField() == null) {
						if (var1.getLengthFieldLength() < var13.length) {
							var7 = new byte[var1.getLengthFieldLength()];
							if (null != var1.getExtendedAttributeText() && "left"
									.equalsIgnoreCase((String) var1.getExtendedAttributes().get("padding-direction"))) {
								System.arraycopy(var13, var13.length - var1.getLengthFieldLength(), var7, 0,
										var7.length);
							} else {
								System.arraycopy(var13, 0, var7, 0, var1.getLengthFieldLength());
							}

							var13 = var7;
						}
					} else if (2000 == var1.getFieldType() && var1.getLength() < var13.length) {
						var7 = new byte[var1.getLength()];
						if (null != var1.getExtendedAttributeText() && "left"
								.equalsIgnoreCase((String) var1.getExtendedAttributes().get("padding-direction"))) {
							System.arraycopy(var13, var13.length - var1.getLength(), var7, 0, var7.length);
						} else {
							System.arraycopy(var13, 0, var7, 0, var1.getLength());
						}

						var13 = var7;
					}
				}

				if (3010 == var1.getDataType()) {
					if (var1.getPattern().indexOf(",") == -1) {
						if (null == var13 || var13.length == 0) {
							var13 = "0".getBytes();
						}
					} else if (null != var13 && var13.length != 0) {
						if (var13[0] == 46) {
							var7 = new byte[var13.length + 1];
							var7[0] = 48;
							System.arraycopy(var13, 0, var7, 1, var13.length);
							var13 = var7;
						}
					} else {
						var13 = "0".getBytes();
					}
				}

				if (var1.isRemoveUnwatchable()) {
					for (var14 = 0; var14 < var13.length && 0 <= var13[var14] && var13[var14] < 32; ++var14) {
					}

					for (var16 = var13.length - 1; var14 != var13.length && 0 < var13.length && 0 <= var13[var16]
							&& var13[var16] < 32; --var16) {
					}

					byte[] var17 = new byte[var16 - var14 + 1];
					System.arraycopy(var13, var14, var17, 0, var17.length);
					var13 = new byte[var17.length];
					System.arraycopy(var17, 0, var13, 0, var17.length);
				}

				String var18 = var1.getDataCharset();
				if (null == var18) {
					var18 = this.message.getMsgCharset();
				}

				if (null == var18) {
					var18 = this.getDefaultCharset();
				}

				if (null != var18) {
					try {
						var3[0] = new String(var13, var18);
					} catch (UnsupportedEncodingException var10) {
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
								.getString("field.encoding.unsupport", new String[] { var1.getName(), var18 }));
					}
				} else {
					var3[0] = new String(var13);
				}

				if (var1.getSuffix() != null) {
					var2 += var1.getSuffix().length;
				}
			}
			break;
		case 3002:
			if (var1 == this.message.getFields().get(this.message.getFields().size() - 1)) {
				var12 = this.messageData.length - var2;
			} else {
				var12 = var1.getLength();
			}

			if (var12 > var1.getLength()) {
				var12 = var1.getLength();
			}

			var13 = new byte[var12];
			System.arraycopy(this.messageData, var2, var13, 0, var13.length);
			var3[0] = var13;
			break;
		case 3003:
			var12 = var1.getLength();
			if (2012 == var1.getFieldType()) {
				byte[] var8 = new byte[1];
				System.arraycopy(this.messageData, var2, var8, 0, var8.length);
				if (var8[0] == 131) {
					var12 = 3;
					++var2;
				} else if (var8[0] == 130) {
					var12 = 2;
					++var2;
				} else if (var8[0] == 129) {
					var12 = 1;
					++var2;
				} else {
					var12 = 1;
				}
			}

			var13 = new byte[var12];
			System.arraycopy(this.messageData, var2, var13, 0, var13.length);
			if (4 > var12) {
				ByteBuffer var15 = new ByteBuffer();

				for (int var9 = 0; var9 < 4 - var12; ++var9) {
					var15.append((byte) 0);
				}

				var15.append(var13);
				var13 = var15.toBytes();
			}

			var3[0] = CodeUtil.BytesToInt(var13);
			break;
		case 3004:
			var12 = var1.getLength();
			var13 = new byte[var12];
			System.arraycopy(this.messageData, var2, var13, 0, var13.length);
			var3[0] = var13[0];
			break;
		case 3005:
			var12 = var1.getLength();
			var13 = new byte[var12];
			System.arraycopy(this.messageData, var2, var13, 0, var13.length);
			var3[0] = CodeUtil.BytesToShort(var13);
			break;
		case 3007:
			var12 = var1.getLength();
			var13 = new byte[var12];
			System.arraycopy(this.messageData, var2, var13, 0, var13.length);
			var3[0] = CodeUtil.ntohi(var13);
			break;
		case 3008:
			var12 = var1.getLength();
			var13 = new byte[var12];
			System.arraycopy(this.messageData, var2, var13, 0, var13.length);
			var3[0] = (short) CodeUtil.ntohs(var13);
			break;
		case 3009:
			var12 = var1.getLength();
			var13 = new byte[var12];
			System.arraycopy(this.messageData, var2, var13, 0, var13.length);
			var3[0] = CodeUtil.BytesToLong(var13);
			break;
		default:
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("dataType.unsupport",
					new String[] { "" + var1.getDataType() }));
		}

		var2 += var12;
		if (this.needIndex) {
			var4[1] = var2 + this.indexOffSet;
			if (null != var1.getSuffix()) {
				var4[1] -= var1.getSuffix().length;
			}

			this.fieldIndexCache.put(var1.getName(), var4);
		}

		return var2;
	}

	protected byte[] B(Field var1, byte[] var2) {
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

	public void ignore(Field var1) {
		this.ignore(var1.getName());
	}

	public void ignore(String var1) {
		if (null == this.C) {
			this.C = new HashSet(32);
		}

		this.C.add(var1);
	}

//	static {
//		try {
//			if (FileUtil.isExist(ConfigManager.getFullPathFileName("messagebean.properties"))) {
//				Properties var0 = ConfigManager.loadProperties("messagebean.properties");
//				if (null != var0.getProperty("cutAccordingToLength")
//						&& 0 != var0.getProperty("cutAccordingToLength").length()
//						&& "1".equalsIgnoreCase(var0.getProperty("cutAccordingToLength"))) {
//					B = true;
//				}
//
//				if (null != var0.getProperty("replace_all") && 0 != var0.getProperty("replace_all").length()) {
//					String[] var1 = var0.getProperty("replace_all").split("\\|");
//					String[] var2 = null;
//
//					for (int var3 = 0; var3 < var1.length; ++var3) {
//						var2 = var1[var3].split(":");
//						if (2 != var2.length) {
//							throw new RuntimeException(
//									"file messagebean.properties[@key=replace_all] pattern error. pattern : hex=hex;hex=hex!");
//						}
//
//						G.put(var2[0], var2[1]);
//					}
//				}
//
//				if (null != var0.getProperty("use_bean") && 0 != var0.getProperty("use_bean").length()
//						&& "true".equalsIgnoreCase(var0.getProperty("use_bean"))) {
//					F = true;
//				}
//			}
//		} catch (Exception var4) {
//			var4.printStackTrace();
//			ExceptionUtil.throwActualException(var4);
//		}
//
//	}

}
