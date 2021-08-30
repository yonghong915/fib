package com.fib.msgconverter.commgateway.map.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

import com.fib.msgconverter.commgateway.channel.nio.filter.AbstractMessageFilter;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
/**
 * 报文解密解压文件解压
 * 将响应的byte数组转换为MB文件所需要的byte数组
 * @author Administrator
 * 2014年7月16日15:14:42
 */
public class MessageDecompressDecode implements AbstractMessageFilter{
	protected static final int PORT = 5000;
	@Override
	public byte[] doFilter(byte[] responseData) {
		byte[] messageByte = new byte[5];//取前五位的长度
		System.arraycopy(responseData, 0, messageByte, 0, 5);
		int messageLength = (messageByte[1] - 48) * 1000 + (messageByte[2] - 48) * 100
				+ (messageByte[3] - 48) * 10 + (messageByte[4] - 48);//计算报文的长度。
		int fileLength = messageLength+5;
    	byte[] responseMBData = receiveMessageByte(responseData,messageLength);//
        if(fileLength<responseData.length){
        	int responseFileLength = responseData.length - fileLength;//文件相关长度
        	byte[]receiveDataByte = new byte[responseFileLength+responseMBData.length];//新建接收报文数组
        	System.arraycopy(responseMBData, 0, receiveDataByte, 0, responseMBData.length);//拷贝报文内容
        	System.arraycopy(responseData, fileLength, receiveDataByte, responseMBData.length, responseFileLength);//拷贝文件内容
        	return receiveDataByte;
        }else{
        	if(responseMBData.length<=91){
        		 throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("交易平台信息错误"));
        	}else{
        		return responseMBData;
        	}
        }
	}
	/**
	 * 
	 * @param messageDeCompressByte
	 * @return
	 */
	private byte[] receiveMessageByte(byte[] responseData, int messageLength){
		byte[] messageDataByte = new byte[messageLength];//原始报文内容
    	System.arraycopy(responseData, 5, messageDataByte, 0, messageLength);//复制对应长度的报文
    	byte[] messageDeencryptByte= decode(messageDataByte);//解密
    	byte[] messageDeCompressByte = decompress(messageDeencryptByte);//解压
		byte[] messageLengthByte = intLengthToByte(messageDeCompressByte.length, 5);//将报文的长度转换为长度为5的byte数组
    	byte[] receiveMessageByte = new byte[messageDeCompressByte.length+5];//设置返回的数据 类型
    	System.arraycopy(messageLengthByte, 0, receiveMessageByte, 0, 5);
    	System.arraycopy(messageDeCompressByte, 0, receiveMessageByte, 5, messageDeCompressByte.length);
		return receiveMessageByte;
	}
	
	/**
	 * 解压缩文件
	 * @param data
	 * @return byte[]
	 */
	public byte[] responseFileDataToFileByte(byte[] data){
		int tempVariable = PORT;
		byte[] dataMessage = null;
		int i=0;
		while (tempVariable >= 5000) {
			byte[] fileMassgeBytelLength = new byte[5];
			if(data.length>(i*5005+5)){
				System.arraycopy(data, i*5005, fileMassgeBytelLength, 0, 5);
			}else{
				break;
			}
			int temp1 = readLength(fileMassgeBytelLength);//读取这一段byte数组的值
			tempVariable = temp1;
			if (temp1 <= 0) {
				break;
			}
			byte[] fileMassgeByte = new byte[temp1];
			System.arraycopy(data, i*5005+5, fileMassgeByte, 0, temp1);
			
			byte[] tempByte = new byte[5000*i+temp1];
			if(i!=0){
				System.arraycopy(dataMessage, 0, tempByte, 0, dataMessage.length);
			}
    		System.arraycopy(fileMassgeByte, 0, tempByte, i*5000, temp1);
    		dataMessage = tempByte;
    		i++;
		}
		byte[] fileByte = null;
		try {
			fileByte = decompressFile(dataMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileByte;
	}
	  /** 
     * 文件数据解压缩 
     *  
     * @param data 
     * @return 
     * @throws Exception 
     */  
    public byte[] decompressFile(byte[] data) throws Exception {  
        ByteArrayInputStream bais = new ByteArrayInputStream(data);  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        // 解压缩  
        decompressFile(bais, baos);  
        data = baos.toByteArray();  
        baos.flush();  
        baos.close();  
        bais.close();  
  
        return data;  
    }  
    /** 
     * 文件输入流数据解压缩 
     *  
     * @param is 
     * @param os 
     * @throws Exception 
     */  
    public void decompressFile(InputStream is, OutputStream os)  
            throws Exception {  
  
        GZIPInputStream gis = new GZIPInputStream(is);  
  
        int count;  
        byte data[] = new byte[1024];  
        while ((count = gis.read(data, 0, 1024)) != -1) {  
            os.write(data, 0, count);  
        }  
  
        gis.close();  
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
            try {  
                o.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
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
    
    public byte[] intLengthToByte(int length, int ByteLength){
    	String temp = length +"";
    	byte[] lengthMessageByte = new byte[ByteLength];
    	if(temp.length()<ByteLength){
    		for (int i = temp.length(); i < ByteLength; i++) {
    			temp = "0"+ temp;
			}
    		lengthMessageByte = temp.getBytes();
    	}else{
    		byte[] temp1 = temp.getBytes();
    		System.arraycopy(temp1, 0, lengthMessageByte, 0, ByteLength);
    	}
    	
    	return lengthMessageByte;
    }
    
    
}
