package com.fib.gateway.message.util;

import java.util.Properties;

import com.fib.gateway.message.xml.message.Constant;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

public class FieldDataLocator {
	// 定位方法定义
		// 索引定位法
		public static final int LOCATE_METHOD_INDEX = 0xF0;
		// 分隔符定位法
		public static final int LOCATE_METHOD_SEPARATOR = 0xF1;

		// ---------- 配置参数名 ----------
		public static final String LOCATE_METHOD_TEXT = "locateMethod";
		public static final String LOCATE_METHOD_INDEX_TEXT = "index";
		public static final String LOCATE_METHOD_SEPARATOR_TEXT = "separator";
		public static final String INDEX_TEXT = "index";
		public static final String PREFIX_TEXT = "prefix";
		// 前缀数据类型: str/bin
		public static final String PREFIX_DATA_TYPE_TEXT = "prefixDataType";
		public static final String SUFFIX_TEXT = "suffix";
		// 后缀数据类型: str/bin
		public static final String SUFFIX_DATA_TYPE_TEXT = "suffixDataType";
		public static final String DATA_TYPE_TEXT = "dataType";
		public static final String DATA_ENC_TEXT = "dataEncoding";
		public static final String DATA_LENGTH_TEXT = "dataLength";
		public static final String LENGTH = "length";

		public static final String PADDING_TEXT = "padding";
		public static final String PADDING_DIRECTION_TEXT = "paddingDirection";

		public static final String PADDING_DIRECTION_RIGHT_TEXT = "right";
		public static final String PADDING_DIRECTION_LEFT_TEXT = "left";
		public static final String PADDING_DIRECTION_NONE_TEXT = "none";
		public static final int PADDING_DIRECTION_RIGHT = 0x11;
		public static final int PADDING_DIRECTION_LEFT = 0x12;
		public static final int PADDING_DIRECTION_NONE = 0x10;

		// ------------------------------

		/**
		 * 定位方法
		 */
		private int locateMethod = LOCATE_METHOD_INDEX;

		// 域数据属性
		/**
		 * 数据类型：默认str
		 */
		private int dataType = Constant.DTA_TYP_STR;

		/**
		 * 数据编码：默认ASC，仅对num有效
		 */
		private int dataEncoding = Constant.DTA_ENC_TYP_ASC;

		// 索引定位法域数据属性
		/**
		 * 开始坐标
		 */
		private int index = 0;

		/**
		 * 数据长度
		 */
		private int dataLength;

		// 分隔符定位法数据属性
		/**
		 * 前缀
		 */
		private byte[] prefix;

		/**
		 * 前缀数据类型
		 */
		private int prefixDataType;

		/**
		 * 后缀
		 */
		private byte[] suffix;

		/**
		 * 后缀数据类型
		 */
		private int suffixDataType;

		/**
		 * 填充符
		 */
		private byte padding;

		/**
		 * 填充方向
		 */
		private int paddingDirection = 0x10;

		/**
		 * 参数
		 */
		private Properties properties;

		/**
		 * 从报文中定位域数据并返回
		 * 
		 * @param message
		 * @return
		 */
		public byte[] locate(byte[] message) {
			switch (locateMethod) {
			case LOCATE_METHOD_INDEX:
				return locateByIndex(message);
			case LOCATE_METHOD_SEPARATOR:
				return locateBySeparator(message);
			default:
				// throw new RuntimeException("Unsupport Locate Method :"
				// + locateMethod);
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"FieldDataLocator.locate.unsupport",
								new String[] { "" + locateMethod }));
			}
		}

		/**
		 * 按分隔符定位法定位数据并返回
		 * 
		 * @param message
		 * @return
		 */
		private byte[] locateBySeparator(byte[] message) {
			if (null == prefix) {
				// throw new IllegalArgumentException(
				// "prefix is null! please set it first!");
				throw new IllegalArgumentException(MultiLanguageResourceBundle
						.getInstance().getString(
								"FieldDataLocator.locateBySeparator.prefix.null"));
			}
			if (null == suffix) {
				// throw new IllegalArgumentException(
				// "suffix is null! please set it first!");
				throw new IllegalArgumentException(MultiLanguageResourceBundle
						.getInstance().getString(
								"FieldDataLocator.locateBySeparator.suffix.null"));
			}
			int startIndex = CodeUtil.byteArrayIndexOf(message, prefix, 0);
			if (-1 == startIndex) {
				// throw new
				// RuntimeException("can't find prefix in message! prefix="
				// + (prefixDataType == Constant.DTA_TYP_STR ? new String(
				// prefix) : new String(CodeUtil.BytetoHex(prefix))));
				throw new RuntimeException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"FieldDataLocator.locateBySeparator.prefix.canNotFind",
										new String[] { (prefixDataType == Constant.DTA_TYP_STR ? new String(
												prefix)
												: new String(CodeUtil
														.BytetoHex(prefix))) }));
			}
			startIndex += prefix.length;
			int endIndex = CodeUtil.byteArrayIndexOf(message, suffix, startIndex);
			if (-1 == endIndex) {
				// throw new
				// RuntimeException("can't find suffix in message! suffix="
				// + (suffixDataType == Constant.DTA_TYP_STR ? new String(
				// suffix) : new String(CodeUtil.BytetoHex(suffix))));
				throw new RuntimeException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"FieldDataLocator.locateBySeparator.suffix.canNotFind",
										new String[] { (suffixDataType == Constant.DTA_TYP_STR ? new String(
												suffix)
												: new String(CodeUtil
														.BytetoHex(suffix))) }));
			}

			int realLen = endIndex - startIndex;
			if (0 == realLen) {
				return new byte[0];
			}

			if (Constant.DTA_TYP_NUM == dataType
					&& Constant.DTA_ENC_TYP_BCD == dataEncoding) {
				realLen = realLen % 2 == 0 ? realLen / 2 : realLen / 2 + 1;
			}

			byte[] data = new byte[realLen];
			System.arraycopy(message, startIndex, data, 0, data.length);
			return data;
		}

		/**
		 * 从报文中定位域数据并返回数据的字符串形式
		 * 
		 * @param message
		 * @return
		 */
		public String locateAsString(byte[] message) {
			if (null == message) {
				// throw new IllegalArgumentException("message is null!");
				throw new IllegalArgumentException(MultiLanguageResourceBundle
						.getInstance().getString("inputParameter.null",
								new String[] { "message" }));
			}
			switch (locateMethod) {
			case LOCATE_METHOD_INDEX:
				return locateByIndexAsString(message);
			case LOCATE_METHOD_SEPARATOR:
				return locateBySeparatorAsString(message);
			default:
				// throw new RuntimeException("Unsupport Locate Method :"
				// + locateMethod);
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"FieldDataLocator.locate.unsupport",
								new String[] { "" + locateMethod }));
			}
		}

		private String locateBySeparatorAsString(byte[] message) {
			byte[] data = locateBySeparator(message);
			return byte2String(data);
		}

		/**
		 * 按索引定位法定位域数据并返回字符串形式
		 * 
		 * @param message
		 * @return
		 */
		private String locateByIndexAsString(byte[] message) {
			byte[] data = locateByIndex(message);
			if (paddingDirection != PADDING_DIRECTION_NONE) {
				data = removePadding(data);
			}
			return byte2String(data);
		}

		private byte[] removePadding(byte[] msg) {
			int startIndex = 0;
			int endIndex = msg.length - 1;
			if (PADDING_DIRECTION_LEFT == paddingDirection) {
				for (int i = 0; i < msg.length; i++) {
					if (padding == msg[i]) {
						startIndex++;
					} else {
						break;
					}
				}
			} else if (PADDING_DIRECTION_RIGHT == paddingDirection) {
				for (int i = msg.length; i > 0; i--) {
					if (padding == msg[i - 1]) {
						endIndex--;
					} else {
						break;
					}
				}
			}

			byte[] tmp = new byte[endIndex - startIndex + 1];
			System.arraycopy(msg, startIndex, tmp, 0, tmp.length);
			return tmp;
		}

		private String byte2String(byte[] data) {
			switch (dataType) {
			case Constant.DTA_TYP_BIN:
				return new String(CodeUtil.BytetoHex(data));
			case Constant.DTA_TYP_STR:
				return new String(data);
			case Constant.DTA_TYP_NUM:
				if (Constant.DTA_ENC_TYP_BCD == dataEncoding) {
					data = CodeUtil.BCDtoASC(data);
				}
				return new String(data);
			default:
				// throw new RuntimeException("Unsupport dataType :" + dataType);
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"FieldDataLocator.dataType.unsupport",
								new String[] { dataType + "" }));
			}
		}

		/**
		 * 按索引定位法定位域数据并返回
		 * 
		 * @param message
		 * @return
		 */
		private byte[] locateByIndex(byte[] message) {
			if (dataLength <= 0) {
				// throw new IllegalArgumentException("dataLength[" + dataLength
				// + "] <= 0");
				throw new IllegalArgumentException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"FieldDataLocator.locateByIndex.dataLength.lowerThan0",
										new String[] { "" + dataLength }));
			}
			if (index >= message.length) {
				// throw new IllegalArgumentException("index[" + index
				// + "] >= message.length[" + message.length + "] ");
				throw new IllegalArgumentException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"FieldDataLocator.locateByIndex.index.greaterThanMsgLen",
										new String[] { "" + index,
												"" + message.length }));
			}

			int realLen = dataLength;
			if (Constant.DTA_TYP_NUM == dataType
					&& Constant.DTA_ENC_TYP_BCD == dataEncoding) {
				realLen = dataLength % 2 == 0 ? dataLength / 2 : dataLength / 2 + 1;
			}

			if (index + realLen > message.length) {
				// throw new IllegalArgumentException("index+dataRealLen["
				// + (index + realLen) + "] >= message.length["
				// + message.length + "] ");
				throw new IllegalArgumentException(MultiLanguageResourceBundle
						.getInstance().getString(
								"FieldDataLocator.locateByIndex.greaterThanMsgLen",
								new String[] { String.valueOf(index + realLen),
										"" + message.length }));
			}

			byte[] data = new byte[realLen];
			System.arraycopy(message, index, data, 0, data.length);
			return data;
		}

		/**
		 * @return the locateMethod
		 */
		public int getLocateMethod() {
			return locateMethod;
		}

		/**
		 * @param locateMethod
		 *            the locateMethod to set
		 */
		public void setLocateMethod(int locateMethod) {
			this.locateMethod = locateMethod;
		}

		/**
		 * @return the dataType
		 */
		public int getDataType() {
			return dataType;
		}

		/**
		 * @param dataType
		 *            the dataType to set
		 */
		public void setDataType(int dataType) {
			this.dataType = dataType;
		}

		/**
		 * @return the dataEncoding
		 */
		public int getDataEncoding() {
			return dataEncoding;
		}

		/**
		 * @param dataEncoding
		 *            the dataEncoding to set
		 */
		public void setDataEncoding(int dataEncoding) {
			this.dataEncoding = dataEncoding;
		}

		/**
		 * @return the index
		 */
		public int getIndex() {
			return index;
		}

		/**
		 * @param index
		 *            the index to set
		 */
		public void setIndex(int index) {
			this.index = index;
		}

		/**
		 * @return the dataLength
		 */
		public int getDataLength() {
			return dataLength;
		}

		/**
		 * @param dataLength
		 *            the dataLength to set
		 */
		public void setDataLength(int dataLength) {
			this.dataLength = dataLength;
		}

		/**
		 * @return the prefix
		 */
		public byte[] getPrefix() {
			return prefix;
		}

		/**
		 * @param prefix
		 *            the prefix to set
		 */
		public void setPrefix(byte[] prefix) {
			this.prefix = prefix;
		}

		/**
		 * @return the suffix
		 */
		public byte[] getSuffix() {
			return suffix;
		}

		/**
		 * @param suffix
		 *            the suffix to set
		 */
		public void setSuffix(byte[] suffix) {
			this.suffix = suffix;
		}

		/**
		 * @return the properties
		 */
		public Properties getProperties() {
			return properties;
		}

		/**
		 * @param properties
		 *            the properties to set
		 */
		public void setProperties(Properties properties) {
			this.properties = properties;

			// locateMethod
			String value = properties.getProperty(LOCATE_METHOD_TEXT);
			if (null == value) {
				// throw new IllegalArgumentException(LOCATE_METHOD_TEXT +
				// " is null!");
				throw new IllegalArgumentException(MultiLanguageResourceBundle
						.getInstance().getString("inputParameter.null",
								new String[] { LOCATE_METHOD_TEXT }));
			}
			if (LOCATE_METHOD_INDEX_TEXT.equals(value)) {
				locateMethod = LOCATE_METHOD_INDEX;
			} else if (LOCATE_METHOD_SEPARATOR_TEXT.equals(value)) {
				locateMethod = LOCATE_METHOD_SEPARATOR;
			} else {
				// throw new IllegalArgumentException(LOCATE_METHOD_TEXT + "[" +
				// value
				// + "] is wrong! it must be \"" + LOCATE_METHOD_INDEX_TEXT
				// + "\" or \"" + LOCATE_METHOD_SEPARATOR_TEXT + "\".");
				throw new IllegalArgumentException(MultiLanguageResourceBundle
						.getInstance().getString(
								"FieldDataLocator.locateMethod.wrong",
								new String[] { LOCATE_METHOD_TEXT,
										LOCATE_METHOD_INDEX_TEXT,
										LOCATE_METHOD_SEPARATOR_TEXT }));
			}

			// dataType
			value = properties.getProperty(DATA_TYPE_TEXT);
			if (value != null) {
				dataType = Constant.getDataTypeByText(value);
			}

			// dataType
			value = properties.getProperty(DATA_ENC_TEXT);
			if (value != null) {
				if ("bcd".equalsIgnoreCase(value)) {
					dataEncoding = Constant.DTA_ENC_TYP_BCD;
				}
			}

			if (LOCATE_METHOD_INDEX == locateMethod) {
				// index
				value = properties.getProperty(INDEX_TEXT);
				if (null == value) {
					// throw new IllegalArgumentException(INDEX_TEXT + " is null!");
					throw new IllegalArgumentException(MultiLanguageResourceBundle
							.getInstance().getString("null",
									new String[] { INDEX_TEXT }));
				}
				index = Integer.parseInt(value);

				// dataLength
				value = properties.getProperty(DATA_LENGTH_TEXT);
				if (null == value) {
					value = properties.getProperty(LENGTH);
					if (null == value) {
						// throw new IllegalArgumentException(DATA_LENGTH_TEXT
						// + " is null!");
						throw new IllegalArgumentException(
								MultiLanguageResourceBundle.getInstance()
										.getString("null",
												new String[] { DATA_LENGTH_TEXT }));
					}
				}
				dataLength = Integer.parseInt(value);
				// paddingDirection
				value = properties.getProperty(PADDING_DIRECTION_TEXT);
				if (null != value) {
					value = value.trim();
					if (PADDING_DIRECTION_LEFT_TEXT.equals(value)) {
						paddingDirection = PADDING_DIRECTION_LEFT;
					} else if (PADDING_DIRECTION_RIGHT_TEXT.equals(value)) {
						paddingDirection = PADDING_DIRECTION_RIGHT;
					} else {
						throw new IllegalArgumentException(
								MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"FieldDataLocator.paddingDirection.wrong",
												new String[] {
														PADDING_DIRECTION_TEXT,
														value }));
					}
				}

				// padding
				value = properties.getProperty(PADDING_TEXT);
				if (null != value) {
					value = value.trim();
					if (value.startsWith("0x")) {
						padding = CodeUtil.HextoByte(value.substring(2))[0];
					} else {
						padding = value.getBytes()[0];
					}
				}

			} else if (LOCATE_METHOD_SEPARATOR == locateMethod) {
				// prefix
				value = properties.getProperty(PREFIX_TEXT);
				if (null == value) {
					// throw new IllegalArgumentException(PREFIX_TEXT +
					// " is null!");
					throw new IllegalArgumentException(MultiLanguageResourceBundle
							.getInstance().getString("null",
									new String[] { PREFIX_TEXT }));
				}
				String separatorDataType = properties
						.getProperty(PREFIX_DATA_TYPE_TEXT);
				if (null == separatorDataType) {
					separatorDataType = Constant.DTA_TYP_STR_TXT;
				}
				if (Constant.DTA_TYP_STR_TXT.equalsIgnoreCase(separatorDataType)) {
					prefix = value.getBytes();
					prefixDataType = Constant.DTA_TYP_STR;
				} else if (Constant.DTA_TYP_BIN_TXT
						.equalsIgnoreCase(separatorDataType)) {
					prefix = CodeUtil.HextoByte(value);
					prefixDataType = Constant.DTA_TYP_BIN;
				} else {
					// throw new IllegalArgumentException(PREFIX_DATA_TYPE_TEXT +
					// "["
					// + separatorDataType
					// + "] is wrong! it must be \"str\" or \"bin\"!");
					throw new IllegalArgumentException(MultiLanguageResourceBundle
							.getInstance().getString(
									"FieldDataLocator.dataType.wrong",
									new String[] { PREFIX_DATA_TYPE_TEXT,
											separatorDataType }));
				}

				// suffix
				value = properties.getProperty(SUFFIX_TEXT);
				if (null == value) {
					// throw new IllegalArgumentException(SUFFIX_TEXT +
					// " is null!");
					throw new IllegalArgumentException(MultiLanguageResourceBundle
							.getInstance().getString("null",
									new String[] { SUFFIX_TEXT }));
				}
				separatorDataType = properties.getProperty(SUFFIX_DATA_TYPE_TEXT);
				if (null == separatorDataType) {
					separatorDataType = Constant.DTA_TYP_STR_TXT;
				}
				if (Constant.DTA_TYP_STR_TXT.equalsIgnoreCase(separatorDataType)) {
					suffix = value.getBytes();
					suffixDataType = Constant.DTA_TYP_STR;
				} else if (Constant.DTA_TYP_BIN_TXT
						.equalsIgnoreCase(separatorDataType)) {
					suffix = CodeUtil.HextoByte(value);
					suffixDataType = Constant.DTA_TYP_BIN;
				} else {
					// throw new IllegalArgumentException(SUFFIX_DATA_TYPE_TEXT +
					// "["
					// + separatorDataType
					// + "] is wrong! it must be \"str\" or \"bin\"!");
					throw new IllegalArgumentException(MultiLanguageResourceBundle
							.getInstance().getString(
									"FieldDataLocator.dataType.wrong",
									new String[] { SUFFIX_DATA_TYPE_TEXT,
											separatorDataType }));
				}

			}
		}

		public int getPrefixDataType() {
			return prefixDataType;
		}

		public void setPrefixDataType(int prefixDataType) {
			this.prefixDataType = prefixDataType;
		}

		public int getSuffixDataType() {
			return suffixDataType;
		}

		public void setSuffixDataType(int suffixDataType) {
			this.suffixDataType = suffixDataType;
		}
}
