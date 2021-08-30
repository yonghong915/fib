package com.fib.msgconverter.commgateway.util;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.map.serializer.MapJsonSerializer;
import com.giantstone.common.map.serializer.MapSerializer;
import com.giantstone.common.util.JsonUtil;
import com.giantstone.common.util.MapUtil;
import com.giantstone.message.MessagePacker;
import com.giantstone.message.MessageParser;
import com.giantstone.message.bean.MessageBean;
import com.giantstone.message.metadata.MessageMetadataManager;

public class D
{
  public static final String C = "MB_GROUP_ID";
  public static final String B = "MB_ID";
  public static final String A = "CHARSET";

  public static byte[] A(Object paramObject, int paramInt, Map paramMap)
  {
    byte[] arrayOfByte = null;
    switch (paramInt)
    {
    case 137:
      arrayOfByte = A((MessageBean)paramObject, (String)paramMap.get("MB_GROUP_ID"), (String)paramMap.get("MB_ID"));
      break;
    case 136:
      arrayOfByte = B((Map)paramObject, (String)paramMap.get("CHARSET"));
      break;
    case 135:
      arrayOfByte = A((Map)paramObject, (String)paramMap.get("CHARSET"));
      break;
    case 134:
      arrayOfByte = C((Map)paramObject, (String)paramMap.get("CHARSET"));
      break;
    default:
      throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("MessageTransferHelper.unknownMessageDataType", new String[] { paramInt + "" }));
    }
    return arrayOfByte;
  }

  public static Object A(byte[] paramArrayOfByte, int paramInt, Map paramMap)
  {
    Object localObject = null;
    switch (paramInt)
    {
    case 137:
      localObject = A(paramArrayOfByte, (String)paramMap.get("MB_GROUP_ID"), (String)paramMap.get("MB_ID"));
      break;
    case 136:
      localObject = C(paramArrayOfByte, (String)paramMap.get("CHARSET"));
      break;
    case 135:
      localObject = B(paramArrayOfByte, (String)paramMap.get("CHARSET"));
      break;
    case 134:
      localObject = A(paramArrayOfByte, (String)paramMap.get("CHARSET"));
      break;
    default:
      throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("MessageTransferHelper.unknownMessageDataType", new String[] { paramInt + "" }));
    }
    return localObject;
  }

  private static Map A(byte[] paramArrayOfByte, String paramString)
  {
    String str = null;
    try
    {
      str = new String(paramArrayOfByte, paramString);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new RuntimeException(localUnsupportedEncodingException);
    }
    return MapJsonSerializer.deserialize(str);
  }

  private static Map B(byte[] paramArrayOfByte, String paramString)
  {
    String str = null;
    try
    {
      str = new String(paramArrayOfByte, paramString);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new RuntimeException(localUnsupportedEncodingException);
    }
    return JsonUtil.json2Map(str);
  }

  private static Map C(byte[] paramArrayOfByte, String paramString)
  {
    String str = null;
    try
    {
      str = new String(paramArrayOfByte, paramString);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new RuntimeException(localUnsupportedEncodingException);
    }
    return MapSerializer.deserialize(str);
  }

  private static MessageBean A(byte[] paramArrayOfByte, String paramString1, String paramString2)
  {
    MessageParser localMessageParser = new MessageParser();
    localMessageParser.setMessage(MessageMetadataManager.getMessage(paramString1, paramString2));
    localMessageParser.setMessageData(paramArrayOfByte);
    return localMessageParser.parse();
  }

  private static byte[] A(MessageBean paramMessageBean, String paramString1, String paramString2)
  {
    paramMessageBean.validate();
    MessagePacker localMessagePacker = new MessagePacker();
    localMessagePacker.setMessage(MessageMetadataManager.getMessage(paramString1, paramString2));
    localMessagePacker.setMessageBean(paramMessageBean);
    return localMessagePacker.pack();
  }

  private static byte[] B(Map paramMap, String paramString)
  {
    String str = MapSerializer.serialize(paramMap);
    try
    {
      return str.getBytes(paramString);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new RuntimeException(localUnsupportedEncodingException);
    }
  }

  private static byte[] A(Map paramMap, String paramString)
  {
	// dingjie  
	  System.out.println("dingjie1:");
	try{
		MapUtil.printMap(paramMap, System.out);
	}
	catch(Exception e){
		e.printStackTrace();
	}
	System.out.println("dingjie2:");
	Exception t = new RuntimeException();
	t.printStackTrace();
    String str = JsonUtil.map2Json(paramMap);
    System.out.println("dingjie3:"+str);
    try
    {
      return str.getBytes(paramString);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new RuntimeException(localUnsupportedEncodingException);
    }
  }

  private static byte[] C(Map paramMap, String paramString)
  {
    String str = MapJsonSerializer.serialize(paramMap);
    try
    {
      return str.getBytes(paramString);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new RuntimeException(localUnsupportedEncodingException);
    }
  }
}