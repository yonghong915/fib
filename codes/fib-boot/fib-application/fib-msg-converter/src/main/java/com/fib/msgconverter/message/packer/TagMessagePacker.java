package com.fib.msgconverter.message.packer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.fib.msgconverter.message.metadata.Field;
import com.giantstone.common.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ByteBuffer;
import com.giantstone.common.util.ClassUtil;
import com.giantstone.common.util.CodeUtil;

public class TagMessagePacker extends DefaultMessagePacker {

	public TagMessagePacker() {
		//
	}

	public byte[] pack() {
		byte abyte0[] = super.pack();
		byte abyte1[] = message.getPrefix();
		byte abyte2[] = message.getSuffix();
		byte abyte3[] = new byte[abyte0.length + abyte1.length + abyte2.length];
		System.arraycopy(abyte1, 0, abyte3, 0, abyte1.length);
		System.arraycopy(abyte0, 0, abyte3, abyte1.length, abyte0.length);
		System.arraycopy(abyte2, 0, abyte3, abyte0.length + abyte1.length, abyte2.length);
		if (null != message.getMsgCharset())
			try {
				abyte3 = (new String(abyte3)).getBytes(message.getMsgCharset());
			} catch (UnsupportedEncodingException unsupportedencodingexception) {
				unsupportedencodingexception.printStackTrace();
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport",
						new String[] { message.getId(), message.getMsgCharset() }));
			}
		return abyte3;
	}

	protected void F(Field field, Object obj) {
		if (null == obj) {
			return;
		} else {
			byte abyte0[] = A(field.getDataType(), field.getDataEncoding(), field.getLength(), field.getPadding(), field.getPaddingDirection(), obj,
					field);
			String s = (new StringBuilder()).append(":").append(field.getTag()).append(":").append(new String(abyte0)).toString();
			D.put(field.getName(), s.getBytes());
			return;
		}
	}

	protected void D(Field field, Object obj) {
		List list = (List) obj;
		ByteBuffer bytebuffer = new ByteBuffer(1024);
		Object obj1 = null;
		for (int i = 0; i < list.size(); i++) {
			if (null != field.getPreRowPackEvent())
				B(field, "row-pre-pack", field.getPreRowPackEvent(), list.size(), i);
			byte abyte0[] = B(field, list.get(i));
			if (null == abyte0)
				continue;
			bytebuffer.append(":".getBytes());
			bytebuffer.append(field.getTag().getBytes());
			bytebuffer.append(":".getBytes());
			bytebuffer.append(abyte0);
			if (null != field.getPostRowPackEvent())
				B(field, "row-post-pack", field.getPostRowPackEvent(), list.size(), i);
		}

		D.put(field.getName(), bytebuffer.toBytes());
	}

	protected void E(Field field, Object obj) {
		byte abyte0[] = B(field, obj);
		String s = (new StringBuilder()).append(":").append(field.getTag()).append(":").append(new String(abyte0)).toString();
		D.put(field.getName(), s.getBytes());
	}

	protected void G(Field field, Object obj) {
		List list = (List) ClassUtil.getBeanAttributeValue(messageBean, field.getTable().getName());
		Object obj1 = null;
		switch (field.getDataType()) {
		case 3001:
			obj1 = Integer.toString(list.size());
			break;

		case 3004:
			obj1 = Byte.valueOf((byte) list.size());
			break;

		case 3003:
		case 3007:
			obj1 = Integer.valueOf(list.size());
			break;

		case 3005:
		case 3008:
			obj1 = Short.valueOf((short) list.size());
			break;

		case 3002:
		case 3006:
		default:
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("tableRowField.dataType.unsupport",
					new String[] { field.getName(), (new StringBuilder()).append("").append(field.getDataType()).toString() }));
		}
		String s = null;
		if (null == obj1)
			s = "";
		else
			s = (String) obj1;
		s = (new StringBuilder()).append(":").append(field.getTag()).append(":").append(s).toString();
		byte abyte0[] = s.getBytes();
		if (4001 == field.getDataEncoding())
			abyte0 = CodeUtil.ASCtoBCD(abyte0);
		D.put(field.getName(), abyte0);
	}
}
