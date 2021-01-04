package com.fib.gateway.message.util;

import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

public class ByteBuffer {
	private byte[] buf;
	private int size;
	private float factor;

	public ByteBuffer() {
		this(256, 0.75F);
	}

	public ByteBuffer(int var1) {
		this(var1, 0.75F);
	}

	public ByteBuffer(int var1, float var2) {
		this.buf = null;
		this.size = 0;
		this.factor = 0.75F;
		this.buf = new byte[var1];
		this.factor = var2;
	}

	public int capacity() {
		return this.buf.length;
	}

	public int size() {
		return this.size;
	}

	public void append(byte var1) {
		if (this.size == this.buf.length) {
			this.expand(1);
		}

		this.buf[this.size++] = var1;
	}

	public void append(byte[] var1) {
		if (null == var1) {
			throw new IllegalArgumentException("bytes is null");
		} else {
			this.append(var1, 0, var1.length);
		}
	}

	public void append(byte[] var1, int var2, int var3) {
		if (null == var1) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("null", new String[] { "bytes" }));
		} else if (this.size + var3 > this.buf.length) {
			this.expand(this.size + var3 - this.buf.length);
			this.append(var1, var2, var3);
		} else {
			System.arraycopy(var1, var2, this.buf, this.size, var3);
			this.size += var3;
		}
	}

	public void append(ByteBuffer var1, int var2, int var3) {
		if (null == var1) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("null", new String[] { "bBuf" }));
		} else {
			this.append(var1.getBytesAt(var2, var3));
		}
	}

	public void append(ByteBuffer var1) {
		if (null == var1) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("null", new String[] { "bBuf" }));
		} else {
			this.append(var1, 0, var1.size());
		}
	}

	private void expand(int var1) {
		byte[] var2 = this.buf;
		int var3 = (int) ((float) this.buf.length * this.factor);
		if (this.buf.length - this.size + var3 >= var1) {
			this.buf = new byte[this.buf.length + var3];
		} else {
			this.buf = new byte[this.buf.length + var1];
		}

		System.arraycopy(var2, 0, this.buf, 0, this.size);
	}

	public byte getByteAt(int var1) {
		return this.buf[var1];
	}

	public byte[] getBytesAt(int var1, int var2) {
		byte[] var3 = new byte[var2];
		System.arraycopy(this.buf, var1, var3, 0, var3.length);
		return var3;
	}

	public void getBytesAt(int var1, byte[] var2, int var3, int var4) {
		if (null == var2) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("ByteBuffer.getBytesAt.destByte.null"));
		} else if (var1 + var4 > this.size) {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
					.getString("ByteBuffer.index.outOfBounds", new String[] { "" + var1, "" + var4, "" + this.size }));
		} else {
			System.arraycopy(this.buf, var1, var2, var3, var4);
		}
	}

	public void getBytesAt(int var1, byte[] var2) {
		if (null == var2) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("ByteBuffer.getBytesAt.destByte.null"));
		} else if (var1 + var2.length > this.size) {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString(
					"ByteBuffer.index.outOfBounds", new String[] { "" + var1, "" + var2.length, "" + this.size }));
		} else {
			System.arraycopy(this.buf, var1, var2, 0, var2.length);
		}
	}

	public void setByteAt(int var1, byte var2) {
		this.buf[var1] = var2;
	}

	public void setBytesAt(int var1, byte[] var2, int var3, int var4) {
		if (null == var2) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("ByteBuffer.sourceByte.null"));
		} else if (var1 + var4 > this.size) {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
					.getString("ByteBuffer.index.outOfBounds", new String[] { "" + var1, "" + var4, "" + this.size }));
		} else {
			for (int var5 = 0; var5 < var4; ++var5) {
				this.buf[var1 + var5] = var2[var3 + var5];
			}

		}
	}

	public void setBytesAt(int var1, byte[] var2) {
		if (null == var2) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("ByteBuffer.sourceByte.null"));
		} else {
			this.setBytesAt(var1, var2, 0, var2.length);
		}
	}

	public int indexOf(byte[] var1, int var2) {
		return CodeUtil.byteArrayIndexOf(this.buf, var1, var2);
	}

	public int indexOf(byte[] var1) {
		return this.indexOf(var1, 0);
	}

	public int lastIndexOf(byte[] var1) {
		return this.lastIndexOf(var1, this.size);
	}

	public int lastIndexOf(byte[] var1, int var2) {
		return CodeUtil.byteArrayLastIndexOf(this.buf, var1, var2);
	}

	public void remove(int var1) {
		this.remove(var1, 1);
	}

	public void remove(int var1, int var2) {
		if (var1 >= this.size()) {
			throw new IllegalArgumentException("fromIndex >= buffer's size!");
		} else if (0 > var1) {
			throw new IllegalArgumentException("fromIndex < 0!");
		} else if (0 >= var2) {
			throw new IllegalArgumentException("length <= 0");
		} else {
			byte[] var3 = new byte[this.size() - var2];
			System.arraycopy(this.buf, 0, var3, 0, var1);
			System.arraycopy(this.buf, var1 + var2, var3, var1, var3.length - var1);
			this.size -= var2;
			this.buf = var3;
		}
	}

	public int indexOf(byte var1, int var2) {
		for (int var3 = var2; var3 < this.size; ++var3) {
			if (var1 == this.buf[var3]) {
				return var3;
			}
		}

		return -1;
	}

	public byte[] toBytes() {
		byte[] var1 = new byte[this.size];
		System.arraycopy(this.buf, 0, var1, 0, this.size);
		return var1;
	}

	public String toString() {
		return new String(this.toBytes());
	}

	public void clear() {
		this.size = 0;
	}
}
