/**
 * 北京长信通信息技术有限公司
 * 2015年4月8日20:40:54
 */
package com.fib.msgconverter.commgateway.channel.nio.reader.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.apache.commons.io.IOUtils;

import com.fib.msgconverter.commgateway.channel.nio.filter.AbstractMessageFilter;
import com.fib.msgconverter.commgateway.channel.nio.reader.AbstractNioReader;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;


/**
 * 按核心的要求读取
 * 整体报文=!+4位数字+加密加压报文（长度=4位数字）+（可能有文件）!+4位数字+加压文件内容（4位数字=5000字节 还有内容,当 4位数字<5000字节时，报文结束）
 * @author 王海生
 * 
 */
public class LSLoopsNioReader extends AbstractNioReader {
	private int mesStrslen = 0;
	private String messageStr = ""; 
	public boolean checkMessageComplete() {
		try {
			messageStr = new String(messageBuffer.toBytes(),"GB18030");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String[] messageStrs = messageStr.split("!\\d{4}");
		mesStrslen = messageStrs.length;
		if(mesStrslen == 2){
			int messageNum = messageBuffer.toBytes().length-5;
			int strNum = getMatchLastStr(messageStr);
			if(messageNum == strNum){
				byte[] messageByte = messageBuffer.toBytes();
				byte[] messageDataByte = new byte[messageByte.length-5];
	        	System.arraycopy(messageByte, 5, messageDataByte, 0, messageByte.length-5);//拷贝不含文件的报文
	        	byte[] messageDeCompressByte = decompress(decode(messageDataByte));//解密报文,解压缩报文
	        	if(messageDeCompressByte[0]==48){
	        		return true;
	        	}else{
	        		return false;
	        	}
			}else{
				return false;
			}
			
		}else if(mesStrslen > 2){
			int t = getMatchLastStr(messageStr);
			int[] strNum = getMatchStrToInt(messageStr);
			int len=0;
			for (int i = 0; i < strNum.length; i++) {
				len = len+strNum[i]+5;
			}
			System.out.println("len="+len);
			mesStrslen = messageBuffer.toBytes().length;
			if(t < 5000 && len == mesStrslen){
				return true;
			}
			return false;
		}else{
			return false;
		}
	}

	public boolean read(SocketChannel channel, ByteBuffer commBuffer) {
		commBuffer.clear();
		onceRead = 0;
		// 1. 读取部分报文数据
		try {
			onceRead = channel.read(commBuffer);
		} catch (IOException e) {
			ExceptionUtil.throwActualException(e);
		}
		if (onceRead > 0) {
			// 从通讯缓冲中拷贝出读到的数据
			byte[] bytes = new byte[onceRead];
			commBuffer.flip();
			commBuffer.get(bytes);
			// 加到报文缓存中
			messageBuffer.append(bytes);
			
		} else if (onceRead == -1 || 0 == messageBuffer.size()) {
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("onceRead.-1"));
		}
		// 2. 判断是否已经读完
		boolean complete = checkMessageComplete();
			
		if (complete) {
			message = messageBuffer.toBytes();
			for (int i = 0; i < filterList.size(); i++) {
				AbstractMessageFilter filter = (AbstractMessageFilter) filterList
						.get(i);
				String filterClassName = filter.getClass().getName().trim();

				if (logger.isDebugEnabled()) {
					// logger.debug("message before filter[" + filterClassName
					// + "]:\n" + CodeUtil.Bytes2FormattedText(message));
					logger
							.debug(MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"message.beforeFilter",
											new String[] {
													filterClassName,
													CodeUtil
															.Bytes2FormattedText(message) }));
				}

				message = filter.doFilter(message);

				if (logger.isDebugEnabled()) {
					// logger.debug("message after filter[" + filterClassName
					// + "]:\n" + CodeUtil.Bytes2FormattedText(message));
					logger
							.debug(MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"message.afterFilter",
											new String[] {
													filterClassName,
													CodeUtil
															.Bytes2FormattedText(message) }));
				}
			}
			return true;
		} else {
			if (logger.isInfoEnabled()) {
				// logger
				// .info("read partial message : onceRead = "
				// + onceRead
				// + " hasRead="
				// + messageBuffer.size()
				// + " hasReadMessage:\n"
				// + CodeUtil.Bytes2FormattedText(messageBuffer
				// .toBytes()));
				byte[] tmp = new byte[onceRead];
				messageBuffer.getBytesAt(messageBuffer.size() - onceRead, tmp);
				logger.info(MultiLanguageResourceBundle.getInstance()
						.getString(
								"AbstractNioReader.read.partialMessage.read",
								new String[] { "" + onceRead,
										"" + messageBuffer.size(),
										CodeUtil.Bytes2FormattedText(tmp) }));
			}
			return false;
		}
	}
	
	
    /** 
     * 报文解压缩 
     *  
     * @param data 
     *            待解压的数据 
     * @return byte[] 解压缩后的数据 
     */  
    public byte[] decompress(byte[] data) {  
        byte[] output = new byte[0];  
  
        Inflater decompresser = new Inflater();  
        decompresser.reset();  
        decompresser.setInput(data);  
  
        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);  
        try {  
            byte[] buf = new byte[1024];  
            while (!decompresser.finished()) {  
                int i = decompresser.inflate(buf);  
                o.write(buf, 0, i);  
            }  
            output = o.toByteArray();  
        } catch (DataFormatException e) {  
            output = data;  
            e.printStackTrace();  
        } finally {  
        	IOUtils.closeQuietly(o); 
        }  
  
        decompresser.end();  
        return output;  
    } 
    /**
     * 通过ASC码将byte数组data转换为int类型的数
     * @param data
     * @return int
     */
    public int readLength(byte[] data){
    	return (data[1]-48)*1000+(data[2]-48)*100+(data[3]-48)*10+(data[4]-48);
    }
  
	/**
     * 解密工程
     * @param data
     * @return
     */
    public byte[] decode(byte[] data){
    	int j;
    	byte cSum[] = new byte[2];
    	byte xSb[] = new byte[data.length-10];
        byte[] xK = new byte[8];
        for (int i = 0; i < data.length; i++) {
 			if(i<8){
 				xK[i] = data[i+1];
 			}else{
 				break;
 			}
        }
    	   cSum[0] = data[0];
    	   cSum[1] = data[9];
    	   j = 1;
    	   for (int i=0;i<8;i++) {
    	       j = 1-j;
    	       xK[i] = (byte) (xK[i]^cSum[j]);
    	   }
    	   j = 0;
    	   for (int i=10;i<data.length;i++) {
			   xSb[i-10] = (byte) (data[i]^xK[j]);
			   j++;
			   if (j==8){
				   j=0;
			   }
    	   }
    	  
    	return xSb;
    }
    /**
     * 截取匹配正则表达式的最后一个字符串再转换为int类型
     * @param str
     * @return
     */
    public int getMatchLastStr(String str) {
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile("!\\d{4}");
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			list.add(matcher.group());
		}
		String lastStr = list.get(list.size()-1);
		lastStr = lastStr.substring(1);
		return Integer.valueOf(lastStr).intValue();
	}
    /**
     * 截取匹配正则表达式的最后一个字符串再转换为int[]类型
     * @param str
     * @return
     */
    public int[] getMatchStrToInt(String str) {
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile("!\\d{4}");
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			list.add(matcher.group());
		}
		int[] strNum = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			strNum[i] = Integer.valueOf(list.get(i).substring(1)).intValue();
		}
		return strNum;
	}
    /*public static void main(String[] args) {
		String t = "!4564dsgdsfgfgfgdf!3452fdgdfghgfhgj!0045fdgf";
		int[] strNum = getMatchStrToInt(t);
		int len=0;
		for (int i = 0; i < strNum.length; i++) {
			System.out.println(i+"="+strNum[i]);
			len = len+strNum[i]+5;
		}
		System.out.println("len="+len);
	}*/
}
