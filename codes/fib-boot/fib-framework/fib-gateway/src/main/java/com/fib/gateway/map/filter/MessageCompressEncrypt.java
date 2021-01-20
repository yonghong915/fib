package com.fib.gateway.map.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;

import com.fib.gateway.channel.nio.filter.AbstractMessageFilter;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
/**
 * 报文压缩加密
 * @author Administrator
 * 2014年7月16日15:14:04
 */
public class MessageCompressEncrypt implements AbstractMessageFilter{
	protected static final String SECRETKEY = "abcdefghj";
	protected Logger logger = null;
	@Override
	public byte[] doFilter(byte[] messageData) {
		byte[] messageByte = new byte[5];
		System.arraycopy(messageData, 0, messageByte, 0, 5);
		int messageLength = (messageByte[1] - 48) * 1000 + (messageByte[2] - 48) * 100
				+ (messageByte[3] - 48) * 10 + (messageByte[4] - 48);
		int fileLength = messageLength+5;
		byte[] messageDataByte = new byte[messageLength];//原始报文
    	System.arraycopy(messageData, 5, messageDataByte, 0, messageLength);
    	byte[] messageCompressByte = compress(messageDataByte);//压缩报文
    	byte[] messageEncryptByte= encrypt(messageCompressByte, SECRETKEY);//压缩加密报文
    	byte[] sendMessageByte = compressByteToSendByte(messageEncryptByte);//发送报文
        if(fileLength+5 < messageData.length){
        	int fileDataLength = messageData.length - fileLength;
        	byte[] sendMessageFileByte = new byte[fileDataLength+sendMessageByte.length];//新建发送byte数组
        	System.arraycopy(sendMessageByte, 0, sendMessageFileByte, 0, sendMessageByte.length);//拷贝报文内容
        	System.arraycopy(messageData, fileLength, sendMessageFileByte, sendMessageByte.length, fileDataLength);//拷贝文件内容
        	return sendMessageFileByte;
        }else{
        	return sendMessageByte;
        }
	}
	/**
	 * 将文件的byte数组按5000字符一截，
	 * 在每一小段前面加上！+长度（长度为四位整数不足前面补0）的byte数组
	 * 再将其每个byte数组组成大的byte数组再给于返回。
	 * @param fileData
	 * @return
	 */
	public byte[] fileByteToSendFileByte(byte[] fileData){
		int fileLength = fileData.length;
		int tempVariable = fileLength/5000+1;
		int returnFileByteLength= fileLength+tempVariable*5;
		byte[] fiveThousand = compressLength(5000).getBytes();
		byte[] fileMessageByte = new byte[returnFileByteLength];
		for (int i = 0; i < tempVariable; i++) {
			int temp = i*5005;
			if(i+1 == tempVariable){
				int fileListLength = fileLength-i*5000;
				byte[] fileListLengthByte = compressLength(fileListLength).getBytes();
				System.arraycopy(fileListLengthByte, 0, fileMessageByte, temp, 5);
				System.arraycopy(fileData, i*5000, fileMessageByte, temp+5, fileListLength);
			}else{
				System.arraycopy(fiveThousand, 0, fileMessageByte, temp, 5);
				System.arraycopy(fileData, i*5000, fileMessageByte, temp+5, 5000);
			}
		}
		return fileMessageByte;
	}
	/**
	 * 报文数据压缩
	 * @param data 待压缩数据
	 * @return byte[] 压缩后数据
	 */
	public  byte[] compress(byte[] data) {  
        byte[] output = new byte[0];  
        Deflater compresser = new Deflater();  
        compresser.reset();  
        compresser.setInput(data);  
        compresser.finish();  
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);  
        try {  
            byte[] buf = new byte[1024];  
            while (!compresser.finished()) {  
                int i = compresser.deflate(buf);  
                bos.write(buf, 0, i);  
            }  
            output = bos.toByteArray();  
        } catch (Exception e) {  
            output = data;  
            e.printStackTrace();  
        } finally {  
            try {  
                bos.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        compresser.end();  
        return output;  
    }
	
	/**
	 * 加密工程
	 * @param compressData
	 * @param key
	 * @return byte[]
	 */
	public  byte[] encrypt(byte[] compressData,String key){
		if(key.length()<8||key==null){
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("key.null or key.length<8"));
		}
       int iSum = 0;
       byte[] outData = new byte[compressData.length+10];
       byte[] xKey = key.getBytes();
       byte[] xK = new byte[8];
       System.arraycopy(xKey, 0, xK, 0, 8);
       for (int i=0;i<compressData.length;i++){
    	   iSum += compressData[i];
       }
       byte[] cSum = int2Byte(iSum);
       outData[0] = cSum[0];
       outData[9] = cSum[1];
       int j = 1;
       for (int i=0;i<8;i++) {
           j = 1-j;
           xK[i] = (byte) (xK[i]^cSum[j]);
       }
       System.arraycopy(xK, 0, outData, 1, 8);
       j = 0;
       for (int i=0;i<compressData.length;i++) {
           outData[i+10] = (byte) (compressData[i]^xKey[j]);
           j ++;
           if (j==8){
        	   j=0;
           }
       }
       return outData;
    }
	
	/** 
     * 文件数据压缩 
     *  
     * @param data 
     * @return 
     * @throws Exception 
     */  
    public  byte[] compressFile(byte[] data) throws Exception {  
        ByteArrayInputStream bais = new ByteArrayInputStream(data);  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        // 压缩  
        compressFile(bais, baos);  
        byte[] output = baos.toByteArray();  
        baos.flush();  
        baos.close();  
        bais.close();  
        return output;  
    }  
  
	
	/** 
     * 文件数据压缩 
     *  
     * @param is 
     * @param os 
     * @throws Exception 
     */  
    public  void compressFile(InputStream is, OutputStream os) throws Exception {  
  
        GZIPOutputStream gos = new GZIPOutputStream(os);  
  
        int count;  
        byte data[] = new byte[1024];  
        while ((count = is.read(data, 0, 1024)) != -1) {  
            gos.write(data, 0, count);  
        }  
        gos.finish();  
        gos.flush();  
        gos.close();  
    }  
    
	
	
	/**
	 * 传入dataByte按要求计算其长度再和原数组重新组织成新数组并返回
	 * @param dataByte 
	 * 压缩加密后的byte数组
	 * @return byte[]
	 * 核心要求的报文数组
	 */
	private byte[] compressByteToSendByte(byte[] dataByte){
		int dataLen = dataByte.length;
    	String compressDataLen = "";
    	if(dataLen/1000!=0){
    		compressDataLen = "!"+dataLen;
    	}else if(dataLen/100!=0){
    		compressDataLen = "!0"+dataLen;
    	}else if(dataLen/10!=0){
    		compressDataLen = "!00"+dataLen;
    	}else{
    		compressDataLen = "!000"+dataLen;
    	}
    	byte[] lengthByte= compressDataLen.getBytes();
    	int cL = lengthByte.length;
		int temp = cL + dataByte.length;
		byte[] writeData = new byte[temp];
		for (int i = 0; i < writeData.length; i++) {
			if(i<cL){
				writeData[i] = lengthByte[i];
			}else{
				writeData[i] = dataByte[i-cL];
			}
		}
		return writeData;
	}
	
	 /**
     * 传入长度返回字符串 
     * 例如：“！0041”
     * @param dataLen
     * @return String
     */
    public  String compressLength(int dataLen){
    	String compressDataLen = "";
    	if(dataLen/1000!=0){
    		compressDataLen = "!"+dataLen;
    		return compressDataLen;
    	}else if(dataLen/100!=0){
    		compressDataLen = "!0"+dataLen;
    		return compressDataLen;
    	}else if(dataLen/10!=0){
    		compressDataLen = "!00"+dataLen;
    		return compressDataLen;
    	}else{
    		compressDataLen = "!000"+dataLen;
    		return compressDataLen;
    	}
    }
	
	 /**
     * int类型的数转换为byte数组
     * @param intValue
     * @return byte[]
     */
    public  byte[] int2Byte(int intValue) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (intValue >> 8 * (3 - i) & 0xFF);
        } 
        return b;
    }
    /**
     * 将byte数组转换为int类型的数组
     * @param b
     * @return int
     */
    public  int byte2Int(byte[] b) {
        int intValue = 0;
        for (int i = 0; i < b.length; i++) {
            intValue += (b[i] & 0xFF) << (8 * (3 - i));
        }
        return intValue;
    }
}
