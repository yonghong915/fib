package com.fib.msgconverter.commgateway.channel.net.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ExceptionUtil;

/**
 * Reader抽象实现类
 * 
 * @author 刘恭亮
 * 
 */
public abstract class AbstractReader implements Reader {
	/**
	 * 默认通讯缓冲大小
	 */
	public static final int DEFAULT_BUFFER_SIZE = 1024;

	/**
	 * 参数
	 */
	protected Map parameters = null;

	/**
	 * 调试器
	 */
	protected Logger logger = null;

	/**
	 * @return the parameters
	 */
	public Map getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	/**
	 * 过滤器链
	 */
	protected List filterList = new ArrayList();

	public List getFilterList() {
		return filterList;
	}

	public void setFilterList(List filterList) {
		this.filterList = filterList;
	}

	public byte[] read(InputStream in) {
		byte[] readBuf = new byte[DEFAULT_BUFFER_SIZE]; // 读取缓冲
		byte[] messageBuf = null; // 消息缓冲
		int onceRead = 0; // 一次读取的长度
		while (true) {
			try {
				onceRead = in.read(readBuf, 0, readBuf.length);
			} catch (IOException e) {
				// e.printStackTrace();
				ExceptionUtil.throwActualException(e);
			}
			if (-1 == onceRead) {
				// throw new RuntimeException(
				// "onceRead = -1, remote socket maybe be closed!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("onceRead.-1"));
			}
			if (0 == onceRead) {
				// throw new RuntimeException(
				// "onceRead = 0, remote socket maybe be closed!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("onceRead.0"));
			}
			messageBuf = appendByteBuf(readBuf, onceRead, messageBuf);
			if (checkMessageComplete(messageBuf)) {
				break;
			}
		}

		return messageBuf;
	}

	/**
	 * 检查消息是否已读取完毕
	 * 
	 * @return
	 */
	public abstract boolean checkMessageComplete(byte[] message);

	/**
	 * 将src数组中len个字节合并到dest数组之后，创建新数组返回
	 * 
	 * @param src
	 * @param len
	 * @param dest
	 * @return
	 */
	protected byte[] appendByteBuf(byte[] src, int len, byte[] dest) {
		byte[] swapBuf = null;
		if (null == dest) {
			dest = new byte[len];
			System.arraycopy(src, 0, dest, 0, dest.length);
		} else {
			swapBuf = dest;
			dest = new byte[swapBuf.length + len];
			System.arraycopy(swapBuf, 0, dest, 0, swapBuf.length);
			System.arraycopy(src, 0, dest, swapBuf.length, len);
//			swapBuf = null;
		}
		return dest;
	}

	/**
	 * 从Socket输入流中读取指定长度的数据到byte[]
	 * 
	 * @param in
	 * @param len
	 * @return
	 */
	protected void read(InputStream in, byte[] buf, int startIndex, int length) {
		int onceRead = 0, hasRead = 0;
		while (hasRead < length) {
			try {
				onceRead = in.read(buf, startIndex + hasRead, length - hasRead);
			} catch (IOException e) {
				// e.printStackTrace();
				ExceptionUtil.throwActualException(e);
			}
			if (-1 == onceRead) {
				// throw new RuntimeException(
				// "onceRead = -1, remote socket maybe be closed!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("onceRead.-1"));
			}
			if (0 == onceRead) {
				// throw new RuntimeException(
				// "onceRead = 0, remote socket maybe be closed!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("onceRead.0"));
			}
			hasRead += onceRead;
		}
	}

	/**
	 * 从Socket输入流中读取指定长度的数据
	 * 
	 * @param in
	 * @param len
	 * @return
	 */
	protected byte[] read(InputStream in, int len) {
		byte[] b = new byte[len];
		read(in, b, 0, len);
		return b;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
