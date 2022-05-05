// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.fib.upp.util;


// Referenced classes of package com.giantstone.common.util:
//			CodeUtil

public class ByteBuffer
{

	private byte[] buf;
	private int size;
	private float factor;

	public ByteBuffer()
	{
		this(256, 0.75F);
	}

	public ByteBuffer(int i)
	{
		this(i, 0.75F);
	}

	public ByteBuffer(int i, float f)
	{
		buf = null;
		size = 0;
		factor = 0.75F;
		buf = new byte[i];
		factor = f;
	}

	public int capacity()
	{
		return buf.length;
	}

	public int size()
	{
		return size;
	}

	public void append(byte byte0)
	{
		if (size == buf.length)
			expand(1);
		buf[size++] = byte0;
	}

	public void append(byte abyte0[])
	{
		if (null == abyte0)
		{
			throw new IllegalArgumentException("bytes is null");
		} else
		{
			append(abyte0, 0, abyte0.length);
			return;
		}
	}

	public void append(byte abyte0[], int i, int j)
	{
		if (null == abyte0)
			throw new IllegalArgumentException("null");
		if (size + j > buf.length)
		{
			expand((size + j) - buf.length);
			append(abyte0, i, j);
			return;
		} else
		{
			System.arraycopy(abyte0, i, buf, size, j);
			size += j;
			return;
		}
	}

	public void append(ByteBuffer bytebuffer, int i, int j)
	{
		if (null == bytebuffer)
		{
			throw new IllegalArgumentException("null");
		} else
		{
			append(bytebuffer.getBytesAt(i, j));
			return;
		}
	}

	public void append(ByteBuffer bytebuffer)
	{
		if (null == bytebuffer)
		{
			throw new IllegalArgumentException("null");
		} else
		{
			append(bytebuffer, 0, bytebuffer.size());
			return;
		}
	}

	private void expand(int i)
	{
		byte abyte0[] = buf;
		int j = (int)((float)buf.length * factor);
		if ((buf.length - size) + j >= i)
			buf = new byte[buf.length + j];
		else
			buf = new byte[buf.length + i];
		System.arraycopy(abyte0, 0, buf, 0, size);
		abyte0 = null;
	}

	public byte getByteAt(int i)
	{
		return buf[i];
	}

	public byte[] getBytesAt(int i, int j)
	{
		byte abyte0[] = new byte[j];
		System.arraycopy(buf, i, abyte0, 0, abyte0.length);
		return abyte0;
	}

	public void getBytesAt(int i, byte abyte0[], int j, int k)
	{
		if (null == abyte0)
			throw new IllegalArgumentException("ByteBuffer.getBytesAt.destByte.null");
		if (i + k > size)
		{
			throw new IllegalArgumentException("ByteBuffer.index.outOfBounds");
		} else
		{
			System.arraycopy(buf, i, abyte0, j, k);
			return;
		}
	}

	public void getBytesAt(int i, byte abyte0[])
	{
		if (null == abyte0)
			throw new IllegalArgumentException("ByteBuffer.getBytesAt.destByte.null");
		if (i + abyte0.length > size)
		{
			throw new IllegalArgumentException("ByteBuffer.index.outOfBounds");
		} else
		{
			System.arraycopy(buf, i, abyte0, 0, abyte0.length);
			return;
		}
	}

	public void setByteAt(int i, byte byte0)
	{
		buf[i] = byte0;
	}

	public void setBytesAt(int i, byte abyte0[], int j, int k)
	{
		if (null == abyte0)
			throw new IllegalArgumentException("ByteBuffer.sourceByte.null");
		if (i + k > size)
			throw new IllegalArgumentException("ByteBuffer.index.outOfBounds");
		for (int l = 0; l < k; l++)
			buf[i + l] = abyte0[j + l];

	}

	public void setBytesAt(int i, byte abyte0[])
	{
		if (null == abyte0)
		{
			throw new IllegalArgumentException("ByteBuffer.sourceByte.null");
		} else
		{
			setBytesAt(i, abyte0, 0, abyte0.length);
			return;
		}
	}

	public int indexOf(byte abyte0[], int i)
	{
		return CodeUtil.byteArrayIndexOf(buf, abyte0, i);
	}

	public int indexOf(byte abyte0[])
	{
		return indexOf(abyte0, 0);
	}

	public int lastIndexOf(byte abyte0[])
	{
		return lastIndexOf(abyte0, size);
	}

	public int lastIndexOf(byte abyte0[], int i)
	{
		return CodeUtil.byteArrayLastIndexOf(buf, abyte0, i);
	}

	public void remove(int i)
	{
		remove(i, 1);
	}

	public void remove(int i, int j)
	{
		if (i >= size())
			throw new IllegalArgumentException("fromIndex >= buffer's size!");
		if (0 > i)
			throw new IllegalArgumentException("fromIndex < 0!");
		if (0 >= j)
		{
			throw new IllegalArgumentException("length <= 0");
		} else
		{
			byte abyte0[] = new byte[size() - j];
			System.arraycopy(buf, 0, abyte0, 0, i);
			System.arraycopy(buf, i + j, abyte0, i, abyte0.length - i);
			size -= j;
			buf = abyte0;
			return;
		}
	}

	public int indexOf(byte byte0, int i)
	{
		for (int j = i; j < size; j++)
			if (byte0 == buf[j])
				return j;

		return -1;
	}

	public byte[] toBytes()
	{
		byte abyte0[] = new byte[size];
		System.arraycopy(buf, 0, abyte0, 0, size);
		return abyte0;
	}

	public String toString()
	{
		return new String(toBytes());
	}

	public void clear()
	{
		size = 0;
	}
}
