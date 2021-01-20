package com.fib.gateway.message.packer;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import com.fib.commons.util.ClassUtil;
import com.fib.gateway.message.bean.CommonMessageBean;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.Field;
import com.fib.gateway.message.metadata.MacCalculator;
import com.fib.gateway.message.metadata.Message;
import com.fib.gateway.message.metadata.ValueRange;
import com.fib.gateway.message.util.ByteBuffer;
import com.fib.gateway.message.util.CodeUtil;
import com.fib.gateway.message.util.ConfigManager;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import com.fib.gateway.message.xml.script.BeanShellEngine;

import ch.qos.logback.core.util.FileUtil;

public class DefaultMessagePacker extends AbstractMessagePacker {

	protected TreeMap D = new TreeMap();
	protected List C = null;
	protected List F = null;
	protected List G = null;
	protected List I = null;
	protected List H = null;
	protected Set B = null;
	public static String E = "1";

	protected void G() {
		if (this.buf == null) {
			this.buf = new ByteBuffer(10240);
		}

		this.buf.clear();
		this.D.clear();
		this.C = null;
		this.F = null;
		this.B = null;
	}

	public byte[] pack() {
		if (null == this.message) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "message" }));
		} else if (null == this.messageBean) {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "messageBean" }));
		} else {
			this.G();
			if (0 == this.variableCache.size()) {
				this.loadVariable();
			}

			if (null != this.getParentBean()) {
				this.messageBean.setParent(this.getParentBean());
			}

			this.messageBean.setMetadata(this.message);
			if (this.message.getPrePackEvent() != null) {
				this.A("pre-pack", this.message.getPrePackEvent());
			}

			this.F();
			Iterator var1 = this.D.entrySet().iterator();
			Entry var2 = null;
			Object var3 = null;

			while (var1.hasNext()) {
				var2 = (Entry) var1.next();
				byte[] var4 = (byte[]) ((byte[]) var2.getValue());
				if (var4 != null) {
					this.buf.append(var4);
				}
			}

			if (this.message.getPostPackEvent() != null) {
				this.A("post-pack", this.message.getPostPackEvent());
			}

			return this.buf.toBytes();
		}
	}

	protected void A(String var1, String var2) {
		BeanShellEngine var3 = new BeanShellEngine();
		var3.set("messagePacker", this);
		var3.set("message", this.message);
		var3.set("messageBean", this.messageBean);
		var3.set("messageBuf", this.buf);
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

		var3.eval(var2);
		this.buf = (ByteBuffer) var3.get("messageBuf");
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
		var4.set("messagePacker", this);
		var4.set("message", this.message);
		var4.set("messageBean", this.messageBean);
		var4.set("messageBuf", this.buf);
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

	protected void B(Field var1, String var2, String var3, int var4, int var5) {
		BeanShellEngine var6 = new BeanShellEngine();
		var6.set("messagePacker", this);
		var6.set("message", this.message);
		var6.set("messageBean", this.messageBean);
		var6.set("messageBuf", this.buf);
		var6.set("field", var1);
		var6.set("rowNum", var4);
		var6.set("currRow", var5);
		Iterator var7;
		String var8;
		if (0 != this.variableCache.size()) {
			var7 = this.variableCache.keySet().iterator();
			var8 = null;

			while (var7.hasNext()) {
				var8 = (String) var7.next();
				var6.set(var8, this.variableCache.get(var8));
			}
		}

		var6.eval(var3);
		if (0 != this.variableCache.size()) {
			var7 = this.variableCache.keySet().iterator();
			var8 = null;

			while (var7.hasNext()) {
				var8 = (String) var7.next();
				this.variableCache.put(var8, var6.get(var8));
			}
		}

	}

	protected TreeMap F() {
		Iterator var1 = this.message.getFields().values().iterator();
		Field var2 = null;

		while (var1.hasNext()) {
			var2 = (Field) var1.next();
			this.E(var2);
		}

		this.D();
		this.E();
		this.B();
		this.H();
		this.C();
		return this.D;
	}

	protected void D() {
		if (this.G != null && this.G.size() > 0) {
			Field[] var1 = new Field[this.G.size()];
			this.G.toArray(var1);

			for (int var2 = var1.length - 1; var2 >= 0; --var2) {
				this.C(var1[var2]);
			}
		}

	}

	protected void E() {
		if (this.I != null && this.I.size() > 0) {
			Field[] var1 = new Field[this.I.size()];
			this.I.toArray(var1);

			for (int var2 = var1.length - 1; var2 >= 0; --var2) {
				this.B(var1[var2]);
			}
		}

	}

	protected void B(Field var1) {
		if (this.B == null || !this.B.contains(var1.getName())) {
			Object var2 = this.messageBean.getAttribute(var1.getName());
			BeanShellEngine var3 = new BeanShellEngine();
			String var4 = null;
			var3.set("messageBean", this.messageBean);
			if (0 != this.variableCache.size()) {
				Iterator var5 = this.variableCache.keySet().iterator();
				String var6 = null;

				while (var5.hasNext()) {
					var6 = (String) var5.next();
					var3.set(var6, this.variableCache.get(var6));
				}
			}

			Object var9 = var3.eval(var1.getExpression());
			if (var9 != null) {
				var4 = var9.toString();
			}

			if (0 != this.variableCache.size()) {
				Iterator var10 = this.variableCache.keySet().iterator();
				String var7 = null;

				while (var10.hasNext()) {
					var7 = (String) var10.next();
					this.variableCache.put(var7, var3.get(var7));
				}
			}

			ValueRange var11 = (ValueRange) var1.getValueRange().get(var4);
			if (null == var11) {
				var11 = (ValueRange) var1.getValueRange().get("default-ref");
			}

			Message var12 = var11.getReference();
			Field var8 = var1.copy();
			var8.setReference(var12);
			if (2004 == var1.getFieldType()) {
				this.D(var8, var2);
			} else {
				this.E(var8, var2);
			}

		}
	}

	protected void C(Field var1) {
		if (this.B == null || !this.B.contains(var1.getName())) {
			Object var2 = this.messageBean.getAttribute(var1.getName());
			String[] var3 = var1.getReferenceId().split("\\.");
			Message var4 = this.message;
			MessageBean var5 = this.messageBean;
			if (var3.length > 1) {
				for (int var6 = 0; var6 < var3.length - 1; ++var6) {
					var5 = var5.getParent();
				}

				var4 = var5.getMetadata();
			}

			Field var11 = var4.getField(var3[var3.length - 1]);
			Object var7 = var5.getAttribute(var3[var3.length - 1]);
			ValueRange var8 = (ValueRange) var11.getValueRange().get(var7);
			if (null == var8) {
				var8 = (ValueRange) var11.getValueRange().get("default-ref");
			}

			Message var9 = var8.getReference();
			Field var10 = var1.copy();
			var10.setReference(var9);
			if (2004 == var10.getFieldType()) {
				this.D(var10, var2);
			} else {
				this.E(var10, var2);
			}

		}
	}

	protected void C() {
		if (this.H != null && this.H.size() > 0) {
			Field[] var1 = new Field[this.H.size()];
			this.H.toArray(var1);

			for (int var2 = var1.length - 1; var2 >= 0; --var2) {
				this.A(var1[var2]);
			}
		}

	}

	protected void A(Field var1) {
		Field var2 = null;
		boolean var3 = false;
		Object var4 = null;
		boolean var5 = false;
		ByteBuffer var6 = new ByteBuffer(1024);
		boolean var7 = false;
		Iterator var8;
		byte[] var9;
		byte[] var10;
		byte[] var13;
		int var14;
		int var15;
		if (null != var1.getStartField()) {
			var8 = this.message.getFields().values().iterator();
			var3 = false;

			label113: while (true) {
				do {
					if (!var8.hasNext()) {
						break label113;
					}

					var2 = (Field) var8.next();
				} while (!var3 && !var2.equalTo(var1.getStartField()));

				var3 = true;
				var13 = (byte[]) ((byte[]) this.D.get(var2.getName()));
				if (var13 != null && !var2.getName().equals(var1.getName())) {
					var14 = 0;
					var15 = var13.length;
					if (null == var2.getExtendAttribute("remove_prefix")
							|| !"no".equalsIgnoreCase(var2.getExtendAttribute("remove_prefix"))) {
						if (null != var2.getPrefix()) {
							var15 -= var2.getPrefix().length;
							var14 = var2.getPrefix().length;
						}

						if (null != var2.getSuffix()) {
							var15 -= var2.getSuffix().length;
						}
					}

					if (var15 > 0) {
						var9 = new byte[var15];
						System.arraycopy(var13, var14, var9, 0, var15);
						var6.append(var9);
					}
				}

				if (var2.equalTo(var1.getEndField())) {
					var3 = false;
					break;
				}
			}
		} else {
			var8 = var1.getMacFldDataCache().values().iterator();
			String var18 = null;

			label90: while (true) {
				do {
					if (!var8.hasNext()) {
						break label90;
					}

					var18 = (String) var8.next();
					var13 = (byte[]) ((byte[]) this.D.get(var18));
					var2 = this.message.getField(var18);
				} while (null == var13);

				var14 = 0;
				var15 = var13.length;
				if (null == var2.getExtendAttribute("remove_prefix")
						|| !"no".equalsIgnoreCase(var2.getExtendAttribute("remove_prefix"))) {
					if (null != var2.getPrefix()) {
						var15 -= var2.getPrefix().length;
						var14 = var2.getPrefix().length;
					}

					if (null != var2.getSuffix()) {
						var15 -= var2.getSuffix().length;
					}
				}

				if (var15 > 0) {
					var10 = new byte[var15];
					System.arraycopy(var13, var14, var10, 0, var15);
					var6.append(var10);
				}
			}
		}

		int var11;
		if ("java".equalsIgnoreCase(var1.getCalcType())) {
			MacCalculator var16 = (MacCalculator) ClassUtil.createClassInstance(var1.getCombineOrTableFieldClassName());
			var9 = var16.calcMac(var6.toBytes());
			var10 = new byte[var1.getLength()];
			if (var1.getLength() <= var9.length) {
				System.arraycopy(var9, 0, var10, 0, var1.getLength());
			} else {
				System.arraycopy(var9, 0, var10, 0, var9.length);

				for (var11 = var9.length; var11 < var1.getLength(); ++var11) {
					var10[var11] = 0;
				}
			}

			this.D.put(var1.getName(), var10);
		} else {
			try {
				MessageDigest var17 = MessageDigest.getInstance(var1.getCalcType());
				var17.update(var6.toBytes());
				var9 = var17.digest();
				var10 = new byte[var1.getLength()];
				if (var1.getLength() <= var9.length) {
					System.arraycopy(var9, 0, var10, 0, var1.getLength());
				} else {
					System.arraycopy(var9, 0, var10, 0, var9.length);

					for (var11 = var9.length; var11 < var1.getLength(); ++var11) {
						var10[var11] = 0;
					}
				}

				this.D.put(var1.getName(), var10);
			} catch (NoSuchAlgorithmException var12) {
				var12.printStackTrace();
				ExceptionUtil.throwActualException(var12);
			}
		}

	}

	protected void H() {
		if (this.F != null && this.F.size() > 0) {
			Field[] var1 = new Field[this.F.size()];
			this.F.toArray(var1);

			for (int var2 = var1.length - 1; var2 >= 0; --var2) {
				this.D(var1[var2]);
			}
		}

	}

	protected void D(Field var1) {
		int var2 = 0;
		Iterator var3 = this.message.getFields().values().iterator();
		Field var4 = null;
		boolean var5 = false;
		Object var6 = null;

		while (var3.hasNext()) {
			var4 = (Field) var3.next();
			if (var5 || var4.equalTo(var1.getStartField())) {
				var5 = true;
				byte[] var10 = (byte[]) ((byte[]) this.D.get(var4.getName()));
				if (var10 != null) {
					var2 += var10.length;
				} else if (var4.equalTo(var1)) {
					int var7 = var1.getLength();
					if ((3001 == var1.getDataType() || 3011 == var1.getDataType()) && 4001 == var1.getDataEncoding()) {
						var7 = var7 % 2 == 0 ? var7 / 2 : var7 / 2 + 1;
					}

					var2 += var7;
				}

				if (var4.equalTo(var1.getEndField())) {
					var5 = false;
					break;
				}
			}
		}

		Object var11 = this.A(var1.getDataType(), var2);
		byte[] var8 = this.A(var1.getDataType(), var1.getDataEncoding(), var1.getLength(), var1.getPadding(),
				var1.getPaddingDirection(), var11, var1);
		if (2012 == var1.getFieldType()) {
			ByteBuffer var9 = new ByteBuffer();
			if (var2 <= 128) {
				var9.append(var8[3]);
			} else if (var2 > 128 && var2 <= 255) {
				var9.append((byte) -127);
				var9.append(var8[3]);
			} else if (var2 > 255 && var2 <= 65535) {
				var9.append((byte) -126);
				var9.append(var8[2]);
				var9.append(var8[3]);
			} else {
				if (var2 <= 65535 || var2 > 16777215) {
					throw new RuntimeException(
							MultiLanguageResourceBundle.getInstance().getString("DefaultMessagePacker.length.tolong"));
				}

				var9.append((byte) -125);
				var9.append(var8[1]);
				var9.append(var8[2]);
				var9.append(var8[3]);
			}

			var8 = var9.toBytes();
		}

		this.D.put(var1.getName(), var8);
	}

	protected Object A(int var1, int var2) {
		Object var3 = null;
		switch (var1) {
		case 3001:
			var3 = Integer.toString(var2);
			break;
		case 3002:
		case 3006:
		default:
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("dataType.unsupport",
					new String[] { "" + var1 }));
		case 3003:
		case 3007:
			var3 = var2;
			break;
		case 3004:
			var3 = (byte) var2;
			break;
		case 3005:
		case 3008:
			var3 = (short) var2;
			break;
		case 3009:
			var3 = (long) var2;
		}

		return var3;
	}

	protected int C(Field var1, Object var2) {
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
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("parseIntValue.dataType.unsupport",
							new String[] { "" + var1.getDataType(), "" + var1.getDataType() }));
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

	protected void B() {
		Field var1 = null;
		Field var2 = null;
		Object var3 = null;
		Object var4 = null;
		boolean var5 = false;
		if (this.C != null && this.C.size() > 0) {
			int var6;
			byte[] var8;
			byte[] var9;
			int var10;
			if (this.C.size() > 1) {
				var1 = (Field) this.C.get(1);
				if (3000 == var1.getDataType()) {
					var10 = var1.getLength() / 2;
				} else {
					var10 = var1.getLength();
				}

				var8 = new byte[var10];

				for (var6 = 0; var6 < var8.length; ++var6) {
					var8[var6] = 0;
				}

				boolean var11 = false;
				Iterator var7 = this.message.getFields().values().iterator();

				while (var7.hasNext()) {
					var2 = (Field) var7.next();
					if (var2.getIso8583_no() > 64 && var2.getIso8583_no() < 129) {
						var9 = (byte[]) ((byte[]) this.D.get(var2.getName()));
						if (var9 != null && var9.length > 0) {
							CodeUtil.setBit(var8, var2.getIso8583_no() - 64, 1);
							var11 = true;
						}
					}
				}

				if (var11 || var1.getIso8583_no() < 0) {
					if (3000 == var1.getDataType()) {
						this.D.put(var1.getName(), CodeUtil.BytetoHex(var8));
					} else {
						this.D.put(var1.getName(), var8);
					}
				}
			}

			var1 = (Field) this.C.get(0);
			if (3000 == var1.getDataType()) {
				var10 = var1.getLength() / 2;
			} else {
				var10 = var1.getLength();
			}

			var8 = new byte[var10];

			for (var6 = 0; var6 < var8.length; ++var6) {
				var8[var6] = 0;
			}

			Iterator var12 = this.message.getFields().values().iterator();

			while (var12.hasNext()) {
				var2 = (Field) var12.next();
				if (var2.getIso8583_no() > 0 && var2.getIso8583_no() < 65) {
					var9 = (byte[]) ((byte[]) this.D.get(var2.getName()));
					if (var9 != null && var9.length > 0) {
						CodeUtil.setBit(var8, var2.getIso8583_no(), 1);
					}
				}
			}

			if (3000 == var1.getDataType()) {
				this.D.put(var1.getName(), CodeUtil.BytetoHex(var8));
			} else {
				this.D.put(var1.getName(), var8);
			}
		}

	}

	protected void E(Field var1) {
		if (this.B == null || !this.B.contains(var1.getName())) {
			Object var2 = this.messageBean.getAttribute(var1.getName());
			if (null == var2 && var1.getIso8583_no() > 0) {
				if (2006 == var1.getFieldType()) {
					this.D.put(var1.getName(), (Object) null);
					if (this.C == null) {
						this.C = new ArrayList();
					}

					this.C.add(var1);
				}

			} else if (var1.getIso8583_no() <= 0 || !(var2 instanceof MessageBean)
					|| null != var2 && !((MessageBean) var2).isNull()) {
				if (var1.getIso8583_no() > 0 && E == "0") {
					if (var2 instanceof String) {
						if (((String) var2).length() == 0) {
							return;
						}
					} else if (var2 instanceof byte[] && ((byte[]) ((byte[]) var2)).length == 0) {
						return;
					}
				}

				if ("dynamic".equalsIgnoreCase(var1.getReferenceType())) {
					this.D.put(var1.getName(), (Object) null);
					if (null == this.G) {
						this.G = new ArrayList();
					}

					this.G.add(var1);
				} else if ("expression".equalsIgnoreCase(var1.getReferenceType())) {
					this.D.put(var1.getName(), (Object) null);
					if (null == this.I) {
						this.I = new ArrayList();
					}

					this.I.add(var1);
				} else {
					if (var1.getPrePackEvent() != null) {
						this.A(var1, "pre-pack", var1.getPrePackEvent());
					}

					switch (var1.getFieldType()) {
					case 2000:
						this.F(var1, var2);
						break;
					case 2001:
						this.I(var1, var2);
						break;
					case 2002:
					case 2008:
						this.E(var1, var2);
						break;
					case 2003:
					case 2009:
						this.H(var1, var2);
						break;
					case 2004:
					case 2011:
						this.D(var1, var2);
						break;
					case 2005:
						this.G(var1, var2);
						break;
					case 2006:
						this.D.put(var1.getName(), (Object) null);
						if (this.C == null) {
							this.C = new ArrayList();
						}

						this.C.add(var1);
						break;
					case 2007:
					case 2012:
						this.D.put(var1.getName(), new byte[var1.getLength()]);
						if (this.F == null) {
							this.F = new ArrayList();
						}

						this.F.add(var1);
						break;
					case 2010:
						this.D.put(var1.getName(), new byte[var1.getLength()]);
						if (this.H == null) {
							this.H = new ArrayList();
						}

						this.H.add(var1);
						break;
					default:
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
								.getString("field.fieldType.notExist", new String[] { "" + var1.getFieldType() }));
					}

					if (var1.getPostPackEvent() != null) {
						this.A(var1, "post-pack", var1.getPostPackEvent());
					}
				}

			}
		}
	}

	protected void G(Field var1, Object var2) {
		Object var3 = (List) this.messageBean.getAttribute(var1.getTable().getName());
		if (null == var3 && this.messageBean instanceof CommonMessageBean) {
			var3 = new ArrayList();
		}

		Object var4 = null;
		switch (var1.getDataType()) {
		case 3001:
			var4 = Integer.toString(((List) var3).size());
			break;
		case 3002:
		case 3006:
		default:
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"DefaultMessagePacker.packTableRowNumField.dataType.unsupport",
					new String[] { var1.getName(), "" + var1.getDataType() }));
		case 3003:
		case 3007:
			var4 = ((List) var3).size();
			break;
		case 3004:
			var4 = (byte) ((List) var3).size();
			break;
		case 3005:
		case 3008:
			var4 = (short) ((List) var3).size();
		}

		byte[] var5 = this.A(var1.getDataType(), var1.getDataEncoding(), var1.getLength(), var1.getPadding(),
				var1.getPaddingDirection(), var4, var1);
		this.D.put(var1.getName(), var5);
	}

	protected void D(Field var1, Object var2) {
		Object var3 = (List) var2;
		if (null == var3 && this.messageBean instanceof CommonMessageBean) {
			var3 = new ArrayList();
		}

		ByteBuffer var4 = new ByteBuffer(1024);
		if (null != var1.getTabPrefix()) {
			var4.append(var1.getTabPrefix());
		}

		Object var5 = null;

		for (int var6 = 0; var6 < ((List) var3).size(); ++var6) {
			if (null != var1.getPreRowPackEvent()) {
				this.B(var1, "row-pre-pack", var1.getPreRowPackEvent(), ((List) var3).size(), var6);
			}

			if (null != var1.getPrefix() && (var6 > 0 || var6 == 0 && var1.isFirRowPrefix())) {
				var4.append(var1.getPrefix());
			}

			byte[] var7 = this.B(var1, ((List) var3).get(var6));
			var4.append(var7);
			if (null != var1.getSuffix() && (var6 < ((List) var3).size() - 1
					|| var6 + 1 == ((List) var3).size() && var1.isLastRowSuffix())) {
				var4.append(var1.getSuffix());
			}

			if (null != var1.getPostRowPackEvent()) {
				this.B(var1, "row-post-pack", var1.getPostRowPackEvent(), ((List) var3).size(), var6);
			}
		}

		if (null != var1.getTabSuffix()) {
			var4.append(var1.getTabSuffix());
		}

		this.D.put(var1.getName(), var4.toBytes());
	}

	protected void H(Field var1, Object var2) {
		byte[] var3 = this.B(var1, var2);
		if (var1.getRefLengthField() == null) {
			var3 = this.A(var1, var3, var2);
		}

		this.D.put(var1.getName(), var3);
	}

	protected void E(Field var1, Object var2) {
		byte[] var3 = this.B(var1, var2);
		this.D.put(var1.getName(), var3);
	}

	protected byte[] B(Field var1, Object var2) {
		Object var3 = null;
		if (null == var2 && this.messageBean instanceof CommonMessageBean) {
			var3 = new CommonMessageBean();
		} else if (null == var2) {
			var3 = (MessageBean) ClassUtil.createClassInstance(var1.getReference().getClassName());
		} else {
			var3 = (MessageBean) var2;
		}

		Message var4 = var1.getReference();
		if (var4 == null) {
			var4 = new Message();
			var4.setId(this.message.getId() + "-" + var1.getName());
			var4.setShortText(var1.getShortText());
			var4.setFields(var1.getSubFields());
			var4.setMsgCharset(this.message.getMsgCharset());
		}

		AbstractMessagePacker var5 = MessagePackerFactory.getMessagePacker(var4);
		var5.setVariableCache(this.variableCache);
		var5.setMessage(var4);
		var5.setMessageBean((MessageBean) var3);
		var5.setParentBean(this.messageBean);
		byte[] var6 = var5.pack();
		String var7 = var1.getDataCharset();
		if (null == var7) {
			var7 = this.message.getMsgCharset();
		}

		if (null == var7) {
			var7 = this.getDefaultCharset();
		}

		if (1000 != this.message.getType() && null != var7) {
			try {
				var6 = (new String(var6, var7)).getBytes();
			} catch (UnsupportedEncodingException var9) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("field.encoding.unsupport", new String[] { var1.getName(), var7 }));
			}
		}

		return var6;
	}

	protected void I(Field var1, Object var2) {
		byte[] var3 = this.A(var1.getDataType(), var1.getDataEncoding(), var1.getLength(), var1.getPadding(), 5000,
				var2, var1);
		if (var1.getRefLengthField() == null) {
			var3 = this.A(var1, var3, var2);
		}

		this.D.put(var1.getName(), var3);
	}

	protected byte[] A(Field var1, byte[] var2, Object var3) {
		boolean var4 = false;
		int var9;
		if (4001 == var1.getDataEncoding()) {
			if (var1.getDataType() != 3000 && var1.getDataType() != 3001 && var1.getDataType() != 3006
					&& var1.getDataType() != 3010 && var1.getDataType() != 3011) {
				var9 = var2.length * 2;
			} else {
				if (null == var3) {
					var9 = 0;
				} else {
					var9 = ((String) var3).getBytes().length;
				}

				if (var1.getPrefix() != null) {
					var9 += var1.getPrefix().length;
				}

				if (var1.getSuffix() != null) {
					var9 += var1.getSuffix().length;
				}
			}
		} else {
			var9 = var2.length;
		}

		Object var5 = this.A(var1.getLengthFieldDataType(), var9);
		Field var6 = var1.copy();
		var6.setPrefix((byte[]) null);
		var6.setSuffix((byte[]) null);
		byte[] var7 = this.A(var6.getLengthFieldDataType(), var6.getLengthFieldDataEncoding(),
				var6.getLengthFieldLength(), (byte) 48, 5001, var5, var6);
		byte[] var8 = var2;
		var2 = new byte[var7.length + var2.length];
		System.arraycopy(var7, 0, var2, 0, var7.length);
		System.arraycopy(var8, 0, var2, var7.length, var8.length);
		return var2;
	}

	protected void F(Field var1, Object var2) {
		boolean var3 = false;
		int var9;
		if (-1 == var1.getLength()) {
			Object var4 = this.messageBean.getAttribute(var1.getLengthScript());
			String[] var5 = var1.getLengthScript().split("\\.");
			Message var6 = this.message;
			if (var5.length > 1) {
				MessageBean var7 = this.messageBean;

				for (int var8 = 0; var8 < var5.length - 1; ++var8) {
					var7 = var7.getParent();
				}

				var6 = var7.getMetadata();
			}

			Field var11 = var6.getField(var5[var5.length - 1]);
			var9 = this.C(var11, var4);
		} else {
			var9 = var1.getLength();
		}

		byte[] var10 = this.A(var1.getDataType(), var1.getDataEncoding(), var9, var1.getPadding(),
				var1.getPaddingDirection(), var2, var1);
		this.D.put(var1.getName(), var10);
	}

	protected byte[] A(int var1, int var2, int var3, byte var4, int var5, Object var6, Field var7) {
		Object var8 = null;
		byte[] var14;
		switch (var1) {
		case 3000:
		case 3001:
		case 3006:
		case 3010:
		case 3011:
			if (null == var6 && this.messageBean instanceof CommonMessageBean && null != var7.getValue()) {
				var6 = var7.getValue();
			}

			if (null == var6) {
				var14 = "".getBytes();
			} else {
				if (this.messageBean instanceof CommonMessageBean && 3010 == var1) {
					BigDecimal var9 = null;

					try {
						var9 = new BigDecimal((String) var6);
					} catch (Exception var13) {
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"Message.parameter.type.mustBigDecimal",
								new String[] { var7.getShortText() + "(" + var7.getName() + ")" }) + var13.getMessage(),
								var13);
					}

					int var10 = 0;
					if (-1 != var7.getPattern().indexOf(",")) {
						var10 = Integer.parseInt(var7.getPattern().substring(var7.getPattern().indexOf(",") + 1));
					}

					var6 = var9.divide(new BigDecimal(1), var10, 4).toString();
				}

				String var15 = var7.getDataCharset();
				if (null == var15) {
					var15 = this.message.getMsgCharset();
				}

				if (null == var15) {
					var15 = this.getDefaultCharset();
				}

				if (null != var15) {
					try {
						var14 = ((String) var6).getBytes(var15);
					} catch (UnsupportedEncodingException var12) {
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
								.getString("field.encoding.unsupport", new String[] { var7.getName(), var15 }));
					}
				} else {
					var14 = ((String) var6).getBytes();
				}
			}

			if (null != var7.getExtendAttribute("replace_all")) {
				String[] var17 = var7.getExtendAttribute("replace_all").split("\\|");
				String[] var16 = null;

				for (int var11 = 0; var11 < var17.length; ++var11) {
					var16 = var17[var11].split("=");
					if (2 != var16.length) {
						throw new RuntimeException("field[@name=" + var7.getName() + "]'s extended-attributes[@key="
								+ "replace_all" + "] pattern error. pattern : hex=hex;hex=hex!");
					}

					var14 = CodeUtil.replaceAll(var14, CodeUtil.HextoByte(var16[0].trim()),
							CodeUtil.HextoByte(var16[1].trim()), 0, var14.length);
				}
			}

			var14 = this.A(var3, var4, var5, var14);
			if (4001 == var2) {
				if (null != var7.getExtendedAttributeText()) {
					Map var18 = var7.getExtendedAttributes();
					if (null != var18.get("padding") && var14.length % 2 != 0) {
						byte[] var19 = new byte[var14.length + 1];
						if ("left".equalsIgnoreCase((String) var18.get("padding-direction"))) {
							var19[0] = ((String) var18.get("padding")).getBytes()[0];
							System.arraycopy(var14, 0, var19, 1, var14.length);
						} else {
							System.arraycopy(var14, 0, var19, 0, var14.length);
							var19[var19.length - 1] = CodeUtil.HextoByte((String) var18.get("padding"))[0];
						}

						var14 = var19;
					}
				}

				var14 = CodeUtil.ASCtoBCD(var14);
			}

			if (var7.getPrefix() != null || var7.getSuffix() != null) {
				ByteBuffer var20 = new ByteBuffer(var14.length + 10 > 16 ? 16 : var14.length + 10);
				if (var7.getPrefix() != null) {
					var20.append(var7.getPrefix());
				}

				var20.append(var14);
				if (var7.getSuffix() != null) {
					var20.append(var7.getSuffix());
				}

				var14 = var20.toBytes();
			}
			break;
		case 3002:
			if (null == var6 && this.messageBean instanceof CommonMessageBean && null != var7.getValue()) {
				var6 = CodeUtil.HextoByte(var7.getValue());
			}

			if (null == var6) {
				var14 = new byte[0];
			} else {
				var14 = (byte[]) ((byte[]) var6);
			}
			break;
		case 3003:
			if (null == var6 && this.messageBean instanceof CommonMessageBean && null != var7.getValue()) {
				var6 = Integer.parseInt(var7.getValue());
			}

			var14 = CodeUtil.IntToBytes((Integer) var6);
			break;
		case 3004:
			var14 = new byte[1];
			if (null == var6) {
				var14[0] = 0;
			} else {
				var14[0] = (Byte) var6;
			}
			break;
		case 3005:
			if (null == var6) {
				var14 = new byte[] { 0, 0 };
			} else {
				var14 = CodeUtil.ShortToBytes((Short) var6);
			}
			break;
		case 3007:
			if (null == var6) {
				var14 = new byte[] { 0, 0, 0, 0 };
			} else {
				var14 = CodeUtil.htoni((Integer) var6);
			}
			break;
		case 3008:
			if (null == var6) {
				var14 = new byte[] { 0, 0 };
			} else {
				var14 = CodeUtil.htons((Short) var6);
			}
			break;
		case 3009:
			if (null == var6) {
				var14 = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };
			} else {
				var14 = CodeUtil.LongToBytes((Long) var6);
			}
			break;
		default:
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("dataType.unsupport",
					new String[] { "" + var1 }));
		}

		return var14;
	}

	protected byte[] A(int var1, byte var2, int var3, byte[] var4) {
		if (5000 == var3) {
			return var4;
		} else {
			byte[] var5 = var4;
			if (var4.length < var1) {
				var5 = new byte[var1];
				int var6;
				if (5001 == var3) {
					for (var6 = 0; var6 < var1 - var4.length; ++var6) {
						var5[var6] = var2;
					}

					System.arraycopy(var4, 0, var5, var1 - var4.length, var4.length);
				} else {
					System.arraycopy(var4, 0, var5, 0, var4.length);

					for (var6 = var4.length; var6 < var1; ++var6) {
						var5[var6] = var2;
					}
				}
			}

			return var5;
		}
	}

	public void ignore(Field var1) {
		this.ignore(var1.getName());
	}

	public void ignore(String var1) {
		if (null == this.B) {
			this.B = new HashSet(32);
		}

		this.B.add(var1);
	}

	static {
//		try {
//			if (FileUtil.isExist(ConfigManager.getFullPathFileName("messagebean.properties"))) {
//				Properties var0 = ConfigManager.loadProperties("messagebean.properties");
//				if (null != var0.getProperty("oper_blank") && 0 != var0.getProperty("oper_blank").length()
//						&& "0".equalsIgnoreCase(var0.getProperty("oper_blank"))) {
//					E = "0";
//				}
//			}
//		} catch (Exception var1) {
//			var1.printStackTrace();
//			ExceptionUtil.throwActualException(var1);
//		}

	}
}
