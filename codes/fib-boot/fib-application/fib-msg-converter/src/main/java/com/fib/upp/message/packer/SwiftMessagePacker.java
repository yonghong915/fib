package com.fib.upp.message.packer;

import java.util.Iterator;
import java.util.List;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.fib.upp.message.bean.MessageBean;
import com.fib.upp.message.metadata.Field;
import com.fib.upp.message.metadata.Message;
import com.giantstone.common.util.ByteBuffer;
import com.giantstone.common.util.ClassUtil;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.SortHashMap;

public class SwiftMessagePacker extends TagMessagePacker {

	public static final byte K[] = { 13, 10 };
	public static final int J = 78;
	public static final int L = 76;

	public SwiftMessagePacker() {
	}

	protected byte[] A(Field field, byte abyte0[], boolean flag) {
		if (E == "0" && abyte0.length == 0)
			return new byte[0];
		ByteBuffer bytebuffer = new ByteBuffer(128);
		abyte0 = A(field.getLength(), field.getPadding(), field.getPaddingDirection(), abyte0);
		if (field.getPrefix() != null)
			bytebuffer.append(field.getPrefix());
		if (flag)
			bytebuffer.append((byte) 58);
		bytebuffer.append(field.getTag().getBytes());
		if (flag)
			bytebuffer.append((byte) 58);
		if (null != field.getExtendedAttributeText() && null != field.getExtendedAttributes().get("basic_len")) {
			int i = Integer.parseInt((String) field.getExtendedAttributes().get("basic_len"));
			int j = i;
			if (null != (String) field.getExtendedAttributes().get("ext_len"))
				j = Integer.parseInt((String) field.getExtendedAttributes().get("ext_len"));
			abyte0 = A(abyte0, i, j);
		}
		bytebuffer.append(abyte0);
		if (field.getSuffix() != null)
			bytebuffer.append(field.getSuffix());
		return bytebuffer.toBytes();
	}

	protected byte[] A(Field field, List list) {
		ByteBuffer bytebuffer = new ByteBuffer(1024);
		bytebuffer.append(K);
		Object obj = null;
		Object obj1 = null;
		Object obj2 = null;
		Object obj3 = null;
		for (int i = 0; i < list.size(); i++) {
			if (null != field.getPreRowParseEvent())
				B(field, "row-pre-parse", field.getPreRowParseEvent(), list.size(), i);
			MessageBean messagebean = (MessageBean) list.get(i);
			ByteBuffer bytebuffer1 = new ByteBuffer(128);
			SortHashMap sorthashmap = null;
			Message message = field.getReference();
			if (null != message)
				sorthashmap = message.getFields();
			else
				sorthashmap = field.getSubFields();
			for (int j = 0; j < sorthashmap.size(); j++) {
				Field field1 = (Field) sorthashmap.get(j);
				Object obj4 = ClassUtil.getBeanAttributeValue(messagebean, field1.getName());
				if (2000 == field1.getFieldType()) {
					if (obj4 != null) {
						bytebuffer1.append((byte) 47);
						byte abyte0[] = ((String) obj4).getBytes();
						if (null != field1.getExtendAttribute("replace_all")) {
							String as[] = field1.getExtendAttribute("replace_all").split("\\|");
							Object obj5 = null;
							for (int k = 0; k < as.length; k++) {
								String as1[] = as[k].split("=");
								if (2 != as1.length)
									throw new RuntimeException(
											(new StringBuilder()).append("field[@name=").append(field1.getName())
													.append("]'s extended-attributes[@key=").append("replace_all")
													.append("] pattern error. pattern : hex=hex;hex=hex!").toString());
								abyte0 = CodeUtil.replaceAll(abyte0, CodeUtil.HextoByte(as1[0].trim()),
										CodeUtil.HextoByte(as1[1].trim()), 0, abyte0.length);
							}

						}
						bytebuffer1.append(abyte0);
						continue;
					}
					if (j < sorthashmap.size() - 1)
						bytebuffer1.append((byte) 47);
					continue;
				}
				if (2005 == field1.getFieldType()) {
					List list1 = (List) ClassUtil.getBeanAttributeValue(messagebean, field1.getTable().getName());
					String s = Integer.toString(list1.size());
					bytebuffer1.append((byte) 47);
					bytebuffer1.append(s.getBytes());
					continue;
				}
				if (2004 == field1.getFieldType())
					bytebuffer1.append(A(field1, (List) obj4));
				else
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("SwiftMessagePacker.packTableFieldValue.illegalField"));
			}

			bytebuffer.append(bytebuffer1.toBytes());
			if (K[0] != bytebuffer1.getByteAt(bytebuffer1.size() - 2)
					|| K[1] != bytebuffer1.getByteAt(bytebuffer1.size() - 1))
				bytebuffer.append(K);
			if (null != field.getPostRowParseEvent())
				B(field, "row-post-parse", field.getPostRowParseEvent(), list.size(), i);
		}

		return bytebuffer.toBytes();
	}

	protected byte[] A(byte abyte0[]) {
		ByteBuffer bytebuffer = new ByteBuffer(abyte0.length + 10);
		int i = 0;
		int j = CodeUtil.byteArrayIndexOf(abyte0, K, i);
		boolean flag = false;
		int l = 0;
		for (; j != -1; j = CodeUtil.byteArrayIndexOf(abyte0, K, i)) {
			int k = j - i;
			if (k > A(l)) {
				boolean flag1 = false;
				int j1 = i;
				for (; k > A(l); l++) {
					int i1 = 0;
					for (int k1 = 0; k1 < A(l); k1++)
						if (abyte0[j1 + k1] < 0) {
							i1 += 2;
							k1++;
						} else {
							i1++;
						}

					if (i1 > A(l))
						i1 = A(l) - 1;
					bytebuffer.append(abyte0, j1, i1);
					bytebuffer.append(K);
					bytebuffer.append((byte) 47);
					bytebuffer.append((byte) 47);
					j1 += i1;
					k = j - j1;
				}

				bytebuffer.append(abyte0, j1, k + 2);
			} else {
				bytebuffer.append(abyte0, i, k + 2);
			}
			i = j + 2;
		}

		return bytebuffer.toBytes();
	}

	private int A(int i) {
		return 0 != i ? 76 : 78;
	}

	protected void E(Field field, Object obj) {
		if (null == obj)
			return;
		MessageBean messagebean = (MessageBean) obj;
		ByteBuffer bytebuffer = new ByteBuffer(1024);
		Object obj1 = null;
		Object obj2 = null;
		SortHashMap sorthashmap = null;
		Message message = field.getReference();
		if (null != message)
			sorthashmap = message.getFields();
		else
			sorthashmap = field.getSubFields();
		Iterator iterator = sorthashmap.values().iterator();
		do {
			if (!iterator.hasNext())
				break;
			Field field1 = (Field) iterator.next();
			Object obj3 = ClassUtil.getBeanAttributeValue(messagebean, field1.getName());
			if (obj3 != null || 2005 == field1.getFieldType())
				if (2000 == field1.getFieldType()) {
					byte abyte0[] = null;
					abyte0 = ((String) obj3).getBytes();
					if (null != field1.getExtendAttribute("replace_all")) {
						String as[] = field1.getExtendAttribute("replace_all").split("\\|");
						Object obj4 = null;
						for (int i = 0; i < as.length; i++) {
							String as1[] = as[i].split("=");
							if (2 != as1.length)
								throw new RuntimeException(
										(new StringBuilder()).append("field[@name=").append(field1.getName())
												.append("]'s extended-attributes[@key=").append("replace_all")
												.append("] pattern error. pattern : hex=hex;hex=hex!").toString());
							abyte0 = CodeUtil.replaceAll(abyte0, CodeUtil.HextoByte(as1[0].trim()),
									CodeUtil.HextoByte(as1[1].trim()), 0, abyte0.length);
						}

					}
					bytebuffer.append(A(field1, abyte0, false));
				} else if (2005 == field1.getFieldType()) {
					List list = (List) ClassUtil.getBeanAttributeValue(messagebean, field1.getTable().getName());
					String s = Integer.toString(list.size());
					bytebuffer.append(A(field1, s.getBytes(), false));
				} else if (2004 == field1.getFieldType())
					bytebuffer.append(A(field1, A(A(field1, (List) obj3)), false));
				else
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("SwiftMessagePacker.packTableFieldValue.illegalField"));
		} while (true);
		D.put(field.getName(), A(field, bytebuffer.toBytes(), true));
	}

	protected void F(Field field, Object obj) {
		if (null == obj)
			return;
		byte abyte0[] = ((String) obj).getBytes();
		if (null != field.getExtendAttribute("replace_all")) {
			String as[] = field.getExtendAttribute("replace_all").split("\\|");
			Object obj1 = null;
			for (int i = 0; i < as.length; i++) {
				String as1[] = as[i].split("=");
				if (2 != as1.length)
					throw new RuntimeException((new StringBuilder()).append("field[@name=").append(field.getName())
							.append("]'s extended-attributes[@key=").append("replace_all")
							.append("] pattern error. pattern : hex=hex;hex=hex!").toString());
				abyte0 = CodeUtil.replaceAll(abyte0, CodeUtil.HextoByte(as1[0].trim()),
						CodeUtil.HextoByte(as1[1].trim()), 0, abyte0.length);
			}

		}
		D.put(field.getName(), A(field, abyte0, true));
	}

	public static byte[] A(byte abyte0[], int i, int j) {
		if (abyte0.length > i) {
			ByteBuffer bytebuffer = new ByteBuffer(abyte0.length + 16);
			int k = 0;
			int l = 0;
			int j1;
			for (j1 = 0; j1 < i;) {
				if (l / (i - 1) != 1) {
					if (abyte0[j1] < 0)
						k++;
					bytebuffer.append(abyte0[j1]);
				} else {
					if (abyte0[j1] < 0 && k % 2 == 0) {
						bytebuffer.append(K);
						break;
					}
					bytebuffer.append(abyte0[j1]);
					bytebuffer.append(K);
				}
				j1++;
				l++;
			}

			k = 0;
			for (int i1 = 1; j1 < abyte0.length; i1++) {
				if (i1 % j != 0) {
					if (abyte0[j1] < 0)
						k++;
					bytebuffer.append(abyte0[j1]);
				} else {
					if (abyte0[j1] < 0 && k % 2 == 0) {
						bytebuffer.append(K);
						j1--;
					} else {
						bytebuffer.append(abyte0[j1]);
						bytebuffer.append(K);
					}
					i1 = 0;
					k = 0;
				}
				j1++;
			}

			return bytebuffer.toBytes();
		} else {
			return abyte0;
		}
	}

}
