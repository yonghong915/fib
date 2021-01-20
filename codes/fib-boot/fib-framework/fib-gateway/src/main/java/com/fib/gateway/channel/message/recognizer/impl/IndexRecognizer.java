package com.fib.gateway.channel.message.recognizer.impl;

import java.util.Map;
import java.util.Properties;

import com.fib.gateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.gateway.message.util.FieldDataLocator;

/**
 * 索引定位方式的识别器。根据位置、域数据类型定义等配置从报文中取得域值。
 * 
 * @author 刘恭亮
 * 
 */
public class IndexRecognizer extends AbstractMessageRecognizer {
	// public static final String FLD_INX = "index";
	// public static final String FLD_DTA_TYP = "dataType";
	// public static final String FLD_DTA_ENC = "dataEncoding";
	// public static final String FLD_LEN = "dataLength";

	private FieldDataLocator locator;

	public IndexRecognizer() {
		locator = new FieldDataLocator();
		// locator.setLocateMethod(FieldDataLocator.LOCATE_METHOD_INDEX);
	}

	public String recognize(byte[] message) {
		return locator.locateAsString(message);
	}

	public void setParameters(Map parameters) {
		super.setParameters(parameters);

		Properties prop = new Properties();
		prop.putAll(parameters);
		prop.setProperty(FieldDataLocator.LOCATE_METHOD_TEXT,
				FieldDataLocator.LOCATE_METHOD_INDEX_TEXT);

		locator.setProperties(prop);

		// parseParameters();
	}

	// private void parseParameters() {
	//
	// // index
	// String tmp = (String) this.parameters.get(FLD_INX);
	// if (null == tmp) {
	// throw new RuntimeException("parameter " + FLD_INX + " is null!");
	// } else {
	// tmp = tmp.trim();
	// if (0 == tmp.length()) {
	// throw new RuntimeException("parameter " + FLD_INX
	// + " is blank!");
	// }
	// }
	// int index = Integer.parseInt(tmp);
	// if (index < 0) {
	// throw new RuntimeException("parameter " + FLD_INX + " must >= 0!");
	// }
	// locator.setIndex(index);
	//
	// // data-type
	// tmp = (String) this.parameters.get(FLD_DTA_TYP);
	// if (null != tmp) {
	// tmp = tmp.trim();
	// if (0 == tmp.length()) {
	// throw new RuntimeException("parameter " + FLD_DTA_TYP
	// + " is blank!");
	// }
	// locator.setDataType(Constant.getDataTypeByText(tmp));
	// if (!(locator.getDataType() == Constant.DTA_TYP_STR || locator
	// .getDataType() == Constant.DTA_TYP_NUM)) {
	// throw new RuntimeException("parameter " + FLD_DTA_TYP
	// + " is wrong! Unsupport Data Type[" + tmp
	// + "]! data type must be num/str");
	// }
	// }
	//
	// // data-encoding
	// tmp = (String) this.parameters.get(FLD_DTA_ENC);
	// if (tmp != null) {
	// tmp = tmp.trim();
	// if (0 == tmp.length()) {
	// throw new RuntimeException("parameter " + FLD_DTA_TYP
	// + " is blank!");
	// }
	// if ("bcd".equalsIgnoreCase(tmp)) {
	// locator.setDataEncoding(Constant.DTA_ENC_TYP_BCD);
	// }
	// }
	//
	// // length
	// tmp = (String) this.parameters.get(FLD_LEN);
	// if (null == tmp) {
	// throw new RuntimeException("parameter " + FLD_LEN + " is null!");
	// } else {
	// tmp = tmp.trim();
	// if (0 == tmp.length()) {
	// throw new RuntimeException("parameter " + FLD_LEN
	// + " is blank!");
	// }
	// }
	// int length = Integer.parseInt(tmp);
	// if (length < 0) {
	// throw new RuntimeException("parameter " + FLD_LEN + " must > 0!");
	// }
	// locator.setDataLength(length);
	//
	// }

}
