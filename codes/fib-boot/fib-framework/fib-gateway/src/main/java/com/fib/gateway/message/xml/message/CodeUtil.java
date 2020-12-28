package com.fib.gateway.message.xml.message;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class CodeUtil {
	public static String NEW_LINE = System.getProperty("line.separator");

	public static void setBit(byte[] var0, int var1, int var2) {
		int var3 = 0 == var1 % 8 ? var1 / 8 - 1 : var1 / 8;
		int var4 = 0 == var1 % 8 ? 8 : var1 % 8;
		byte var5 = 1;
		byte var6 = (byte) (var5 << 8 - var4);
		if (var2 == 1) {
			var0[var3] |= var6;
		} else {
			var6 = (byte) (var6 ^ 255);
			var0[var3] &= var6;
		}

	}

	public static int getBit(byte[] var0, int var1) {
		int var2 = 0 == var1 % 8 ? var1 / 8 - 1 : var1 / 8;
		int var3 = 0 == var1 % 8 ? 8 : var1 % 8;
		byte var4 = var0[var2];
		var4 = (byte) (var4 >> 8 - var3);
		var4 = (byte) (var4 & 1);
		return var4;
	}

	public static int HexBytesToInt(byte[] var0) {
		return HexBytesToInt(var0, 0, var0.length);
	}

	public static int HexBytesToInt(byte[] var0, int var1, int var2) {
		int var3 = 0;
		int var4 = 1;

		for (int var5 = 0; var5 < var2; ++var5) {
			var3 += (var0[var1 + var2 - var5 - 1] & 15) * var4;
			var4 *= 16;
			var3 += (var0[var1 + var2 - var5 - 1] >>> 4 & 15) * var4;
			var4 *= 16;
		}

		return var3;
	}

	public static byte[] IntToHexBytes(int var0) {
		String var1 = Integer.toHexString(var0);
		return HextoByte(var1);
	}

	public static int ByteToInt(byte var0) {
		return var0 & 255;
	}

	public static int BytesToInt(byte[] var0) {
		int var1 = 0;

		for (int var2 = 0; var2 < 4; ++var2) {
			var1 |= ByteToInt(var0[var2]) << (3 - var2) * 8;
		}

		return var1;
	}

	public static int BytesToInt(byte[] var0, int var1) {
		int var2 = 0;

		for (int var3 = 0; var3 < 4; ++var3) {
			var2 |= ByteToInt(var0[var1 + var3]) << (3 - var3) * 8;
		}

		return var2;
	}

	public static long BytesToLong(byte[] var0) {
		long var1 = 0L;

		for (int var3 = 0; var3 < 8; ++var3) {
			var1 |= ByteToLong(var0[var3]) << (7 - var3) * 8;
		}

		return var1;
	}

	public static long ByteToLong(byte var0) {
		return (long) var0 & 255L;
	}

	public static short ByteToShort(byte var0) {
		return (short) (var0 & 255);
	}

	public static short BytesToShort(byte[] var0) {
		return BytesToShort(var0, 0);
	}

	public static short BytesToShort(byte[] var0, int var1) {
		short var2 = 0;

		for (int var3 = 0; var3 < 2; ++var3) {
			var2 = (short) (var2 | ByteToShort(var0[var1 + var3]) << (1 - var3) * 8);
		}

		return var2;
	}

	public static byte IntToByte(int var0) {
		return var0 > 255 ? -1 : (byte) var0;
	}

	public static byte[] IntToBytes(int var0) {
		byte[] var1 = new byte[] { 0, 0, 0, 0 };

		for (int var2 = 0; var2 < 4; ++var2) {
			var1[3 - var2] = (byte) (var1[3 - var2] | var0 >>> var2 * 8 & 255);
		}

		return var1;
	}

	public static byte ShortToByte(short var0) {
		return var0 > 255 ? -1 : (byte) var0;
	}

	public static byte[] ShortToBytes(short var0) {
		byte[] var1 = new byte[] { 0, 0 };

		for (int var2 = 0; var2 < 2; ++var2) {
			var1[1 - var2] = (byte) (var1[1 - var2] | var0 >>> var2 * 8 & 255);
		}

		return var1;
	}

	public static byte[] htoni(int var0) {
		byte[] var1 = new byte[4];

		for (int var2 = 0; var2 < 4; ++var2) {
			var1[var2] = (byte) (var0 >> var2 * 8 & 255);
		}

		return var1;
	}

	public static byte[] htons(int var0) {
		byte[] var1 = new byte[2];

		for (int var2 = 0; var2 < 2; ++var2) {
			var1[var2] = (byte) (var0 >> var2 * 8 & 255);
		}

		return var1;
	}

	public static int ntohi(byte[] var0) {
		int var1 = 0;

		for (int var2 = 0; var2 < 4; ++var2) {
			int var3 = (var0[var2] & 255) << var2 * 8;
			var1 |= var3;
		}

		return var1;
	}

	public static int ntohs(byte[] var0) {
		int var1 = 0;

		for (int var2 = 0; var2 < 2; ++var2) {
			int var3 = (var0[var2] & 255) << var2 * 8;
			var1 |= var3;
		}

		return var1;
	}

	public static byte[] LongToBytes(long var0) {
		byte[] var2 = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };

		for (int var3 = 0; var3 < 8; ++var3) {
			var2[7 - var3] = (byte) ((int) ((long) var2[7 - var3] | var0 >>> var3 * 8 & 255L));
		}

		return var2;
	}

	public static byte[] BytetoHex(byte var0) {
		byte[] var1 = new byte[2];
		boolean var2 = false;
		byte var3 = (byte) (var0 >>> 4 & 15);
		var1[0] = (byte) (var3 > 9 ? var3 - 10 + 65 : var3 + 48);
		var3 = (byte) (var0 & 15);
		var1[1] = (byte) (var3 > 9 ? var3 - 10 + 65 : var3 + 48);
		return var1;
	}

	public static byte[] BytetoHex(byte[] var0, int var1, int var2) {
		byte[] var3 = new byte[var2 * 2];
		boolean var4 = false;

		for (int var5 = 0; var5 < var2; ++var5) {
			byte var6 = (byte) (var0[var1 + var5] >>> 4 & 15);
			var3[2 * var5] = (byte) (var6 > 9 ? var6 - 10 + 65 : var6 + 48);
			var6 = (byte) (var0[var1 + var5] & 15);
			var3[2 * var5 + 1] = (byte) (var6 > 9 ? var6 - 10 + 65 : var6 + 48);
		}

		return var3;
	}

	public static byte[] BytetoHex(byte[] var0) {
		return BytetoHex(var0, 0, var0.length);
	}

	public static byte[] HextoByte(String var0) {
		return HextoByte(var0.toUpperCase().getBytes());
	}

	public static byte[] HextoByte(byte[] var0) {
		return HextoByte(var0, 0, var0.length);
	}

	public static byte[] HextoByte(byte[] var0, int var1, int var2) {
		Object var3 = null;
		byte[] var7;
		if (var2 % 2 != 0) {
			var7 = new byte[var2 + 1];
			System.arraycopy(var0, var1, var7, 1, var2);
			var7[0] = 48;
		} else {
			var7 = var0;
		}

		byte[] var4 = new byte[var7.length / 2];
		boolean var5 = false;

		for (int var6 = 0; var6 < var4.length; ++var6) {
			byte var8 = (byte) (var7[2 * var6] >= 65 ? var7[2 * var6] - 65 + 10 : var7[2 * var6] - 48);
			var4[var6] = (byte) (var8 << 4);
			var8 = (byte) ((var7[2 * var6 + 1] >= 65 ? var7[2 * var6 + 1] - 65 + 10 : var7[2 * var6 + 1] - 48) & 15);
			var4[var6] |= var8;
		}

		return var4;
	}

	public static void echoAllChar(byte[] var0, int var1, int var2) {
		for (int var3 = 0; var3 < var2; ++var3) {
			if (var0[var1 + var3] >= 0 && var0[var1 + var3] < 32) {
				var0[var1 + var3] = 32;
			}
		}

	}

	public static String bitString(byte[] var0) {
		StringBuffer var1 = new StringBuffer(var0.length);

		for (int var2 = 0; var2 < var0.length; ++var2) {
			for (int var3 = 0; var3 < 8; ++var3) {
				if (1 == (var0[var2] >> 7 - var3 & 1)) {
					var1.append("1");
				} else {
					var1.append("0");
				}
			}

			var1.append(" ");
		}

		return var1.toString();
	}

	public static String Int2BitString(int var0) {
		StringBuffer var1 = new StringBuffer(32);

		for (int var2 = 4; var2 > 0; --var2) {
			for (int var3 = 0; var3 < 8; ++var3) {
				if (1 == (var0 >> 8 * var2 - var3 - 1 & 1)) {
					var1.append("1");
				} else {
					var1.append("0");
				}
			}

			var1.append(" ");
		}

		return var1.toString();
	}

	public static String short2BitString(short var0) {
		StringBuffer var1 = new StringBuffer();

		for (int var2 = 2; var2 > 0; --var2) {
			for (int var3 = 0; var3 < 8; ++var3) {
				if (1 == (var0 >> 8 * var2 - var3 & 1)) {
					var1.append("1");
				} else {
					var1.append("0");
				}
			}

			var1.append(" ");
		}

		return var1.toString();
	}

	public static String Bytes2FormattedText(byte[] var0, int var1, int var2, String var3) {
		byte[] var4 = new byte[var2];
		System.arraycopy(var0, var1, var4, 0, var2);
		return Bytes2FormattedText(var4, var3);
	}

	public static String Bytes2FormattedText(byte[] var0, int var1, int var2) {
		return Bytes2FormattedText(var0, var1, var2, System.getProperty("file.encoding"));
	}

	public static String Bytes2FormattedText(byte[] var0, String var1) {
		if (null != var0 && var0.length > 0) {
			boolean var2 = false;
			boolean var3 = false;
			int var14;
			if (var0.length % 16 == 0) {
				var14 = var0.length / 16;
			} else {
				var14 = var0.length / 16 + 1;
			}

			StringBuffer var4 = new StringBuffer(var0.length + 20 * var14);

			try {
				for (int var5 = 0; var5 < var14; ++var5) {
					int var6 = var0.length - var5 * 16;
					int var7 = var6 > 16 ? 16 : var6;

					int var8;
					for (var8 = 0; var8 < var7; ++var8) {
						byte[] var9 = BytetoHex(var0[16 * var5 + var8]);
						var4.append(new String(var9, var1));
						var4.append(" ");
					}

					if (var7 < 16) {
						for (var8 = 0; var8 < 16 - var7; ++var8) {
							var4.append("   ");
						}
					}

					Object var15 = null;
					boolean var16 = false;
					byte[] var17;
					int var18;
					if (var2) {
						if (var0.length > 16 * var5 + var7) {
							var17 = new byte[var7 + 2];
							System.arraycopy(var0, 16 * var5 - 1, var17, 0, var7 + 2);
							var18 = var7 + 1;
						} else {
							var17 = new byte[var7 + 1];
							System.arraycopy(var0, 16 * var5 - 1, var17, 0, var7 + 1);
							var18 = var7 + 1;
						}
					} else if (var0.length > 16 * var5 + var7) {
						var17 = new byte[var7 + 1];
						System.arraycopy(var0, 16 * var5, var17, 0, var7 + 1);
						var18 = var7;
					} else {
						var17 = new byte[var7];
						System.arraycopy(var0, 16 * var5, var17, 0, var7);
						var18 = var7;
					}

					int var10 = -1;
					int var11 = -1;

					for (int var12 = 0; var12 < var18; ++var12) {
						if (var17[var12] < 32 && var17[var12] >= 0) {
							var17[var12] = 95;
						}

						if (var17[var12] < 0) {
							if (-1 == var10) {
								var10 = var12;
								var11 = var12;
							} else if (var12 - var11 > 1) {
								var10 = var12;
								var11 = var12;
							} else {
								var11 = var12;
							}
						}
					}

					if (var2) {
						if (var10 >= 0 && (var11 - var10) % 2 == 0) {
							var4.append("   ");
							var4.append(new String(var17, 0, var17.length, var1));
							var4.append(NEW_LINE);
							var2 = true;
						} else {
							if (var7 >= 16 && (var0.length % 16 != 0 || var14 - 1 != var5)) {
								var4.append("   ");
								var4.append(new String(var17, 0, var17.length - 1, var1));
								var4.append(NEW_LINE);
							} else {
								var4.append("   ");
								var4.append(new String(var17, 0, var17.length, var1));
								var4.append(NEW_LINE);
							}

							var2 = false;
						}
					} else if (var10 >= 0 && (var11 - var10) % 2 == 0) {
						var4.append("    ");
						var4.append(new String(var17, 0, var17.length, var1));
						var4.append(NEW_LINE);
						var2 = true;
					} else {
						if (var7 >= 16 && (var0.length % 16 != 0 || var14 - 1 != var5)) {
							var4.append("    ");
							var4.append(new String(var17, 0, var17.length - 1, var1));
							var4.append(NEW_LINE);
						} else {
							var4.append("    ");
							var4.append(new String(var17, 0, var17.length, var1));
							var4.append(NEW_LINE);
						}

						var2 = false;
					}
				}
			} catch (UnsupportedEncodingException var13) {
				//ExceptionUtil.throwActualException(var13);
			}

			return var4.toString();
		} else {
			return null;
		}
	}

	public static String Bytes2FormattedText(byte[] var0) {
		return Bytes2FormattedText(var0, System.getProperty("file.encoding"));
	}

	public static String formatAmount(String var0, int var1) {
		if (var1 < 0) {
			throw new RuntimeException("scale must be greater than 0");
		} else if (var1 == 0) {
			if (null != var0 && 0 != var0.length()) {
				return var0.length() != 1 || !"-".equals(var0.substring(0, 1)) && !"+".equals(var0.substring(0, 1))
						? var0
						: "0";
			} else {
				return "0";
			}
		} else {
			StringBuffer var2 = new StringBuffer();
			int var3;
			if (null != var0 && 0 != var0.length()) {
				var3 = var0.length();
				int var4;
				if (!"-".equals(var0.substring(0, 1)) && !"+".equals(var0.substring(0, 1))) {
					if (var3 <= var1) {
						var2.append("0.");

						for (var4 = 0; var4 < var1 - var3; ++var4) {
							var2.append("0");
						}

						var2.append(var0);
					} else {
						var2.append(var0.substring(0, var3 - var1));
						var2.append(".");
						var2.append(var0.substring(var3 - var1));
					}
				} else if (var3 - 1 <= var1) {
					var2.append("0.");

					for (var4 = 0; var4 < var1 - var3 + 1; ++var4) {
						var2.append("0");
					}

					if (var3 > 1) {
						var2.append(var0.substring(1));
						if ("-".equals(var0.substring(0, 1))) {
							var2.insert(0, "-");
						} else {
							var2.insert(0, "+");
						}
					}
				} else {
					var2.append(var0.substring(0, var3 - var1));
					var2.append(".");
					var2.append(var0.substring(var3 - var1));
				}

				return var2.toString();
			} else {
				var2.append("0.");

				for (var3 = 0; var3 < var1; ++var3) {
					var2.append("0");
				}

				return var2.toString();
			}
		}
	}

	public static String formatAmount(String var0) {
		if (null == var0) {
			return "0.00";
		} else {
			int var1 = var0.length();
			if (0 == var1) {
				return "0.00";
			} else if (1 == var1) {
				return !"-".equals(var0) && !"+".equals(var0) ? "0.0" + var0 : "0.00";
			} else if (2 == var1) {
				if ("-".equals(var0.substring(0, 1))) {
					return "-0.0" + var0.substring(var1 - 1);
				} else {
					return "+".equals(var0.substring(0, 1)) ? "+0.0" + var0.substring(var1 - 1) : "0." + var0;
				}
			} else {
				boolean var2 = false;
				String var3 = null;
				if ("-".equals(var0.substring(0, 1)) || "+".equals(var0.substring(0, 1))) {
					var2 = true;
					var3 = var0.substring(0, 1);
					var0 = var0.substring(1);
					var1 = var0.length();
				}

				int var4;
				for (var4 = 0; var4 < var1 && var0.charAt(var4) == '0'; ++var4) {
				}

				if (var4 < var1 - 2) {
					return var2 ? var3 + var0.substring(var4, var1 - 2) + "." + var0.substring(var1 - 2)
							: var0.substring(var4, var1 - 2) + "." + var0.substring(var1 - 2);
				} else {
					return var2 ? var3 + "0." + var0.substring(var1 - 2) : "0." + var0.substring(var1 - 2);
				}
			}
		}
	}

	public static String unFormatAmount4TwoDecimals(String var0) {
		return unFormatAmount(var0, 0, 2);
	}

	public static String unFormatAmount(String var0, int var1, int var2) {
		if (null == var0) {
			return null;
		} else {
			boolean var3 = false;
			String var4 = null;
			if ("-".equals(var0.substring(0, 1)) || "+".equals(var0.substring(0, 1))) {
				var3 = true;
				--var1;
				var4 = var0.substring(0, 1);
				var0 = var0.substring(1);
			}

			int var5 = var0.indexOf(".");
			int var6 = var0.length();
			StringBuffer var7 = new StringBuffer();
			var7.append(var0.substring(0, var5));
			var7.append(var0.substring(var5 + 1));

			int var8;
			for (var8 = var6 - var5 - 1; var8 < var2; ++var8) {
				var7.append("0");
			}

			if (var2 - (var6 - var5 - 1) > 0) {
				var8 = var1 - var6 + 1 - (var2 - (var6 - var5 - 1));
			} else {
				var8 = var1 - var6 + 1;
			}

			while (var8 > 0) {
				var7.insert(0, "0");
				--var8;
			}

			if (var3) {
				var7.insert(0, var4);
			}

			return var7.toString();
		}
	}

	public static String unFormatAmount(String var0, int var1) {
		if (null == var0) {
			return null;
		} else {
			boolean var2 = false;
			String var3 = null;
			if ("-".equals(var0.substring(0, 1)) || "+".equals(var0.substring(0, 1))) {
				var2 = true;
				--var1;
				var3 = var0.substring(0, 1);
				var0 = var0.substring(1);
			}

			int var4 = var0.indexOf(".");
			int var5 = var0.length();
			String var6 = var0.substring(0, var4) + var0.substring(var4 + 1);

			for (int var7 = var1 - var5 + 1; var7 > 0; --var7) {
				var6 = '0' + var6;
			}

			if (var2) {
				var6 = var3 + var6;
			}

			return var6;
		}
	}

	public static byte[] BCDtoASC(byte[] var0) {
		byte[] var1 = new byte[var0.length * 2];
		boolean var2 = false;

		for (int var3 = 0; var3 < var0.length; ++var3) {
			byte var4 = (byte) (var0[var3] >>> 4 & 15);
			var1[2 * var3] = (byte) (var4 > 9 ? var4 - 10 + 97 : var4 + 48);
			var4 = (byte) (var0[var3] & 15);
			var1[2 * var3 + 1] = (byte) (var4 > 9 ? var4 - 10 + 97 : var4 + 48);
		}

		return var1;
	}

	public static byte[] BCDtoASCUpperCase(String var0) {
		return BCDtoASCUpperCase(var0.getBytes());
	}

	public static byte[] BCDtoASCUpperCase(byte[] var0) {
		byte[] var1 = new byte[var0.length * 2];
		boolean var2 = false;

		for (int var3 = 0; var3 < var0.length; ++var3) {
			byte var4 = (byte) (var0[var3] >>> 4 & 15);
			var1[2 * var3] = (byte) (var4 > 9 ? var4 - 10 + 65 : var4 + 48);
			var4 = (byte) (var0[var3] & 15);
			var1[2 * var3 + 1] = (byte) (var4 > 9 ? var4 - 10 + 65 : var4 + 48);
		}

		return var1;
	}

	public static byte[] BCDtoASC(byte[] var0, int var1, int var2) {
		byte[] var3 = new byte[var2 * 2];
		boolean var4 = false;

		for (int var5 = 0; var5 < var2; ++var5) {
			byte var6 = (byte) (var0[var5 + var1] >>> 4 & 15);
			var3[2 * var5] = (byte) (var6 > 9 ? var6 - 10 + 97 : var6 + 48);
			var6 = (byte) (var0[var5 + var1] & 15);
			var3[2 * var5 + 1] = (byte) (var6 > 9 ? var6 - 10 + 97 : var6 + 48);
		}

		return var3;
	}

	public static byte[] ASCtoBCD(byte[] var0) {
		return HextoByte(var0, 0, var0.length);
	}

	public static byte[] ASCtoBCD(String var0) {
		return HextoByte(var0);
	}

	public static byte[] ASCtoBCD(byte[] var0, int var1, int var2) {
		return HextoByte(var0, var1, var2);
	}

	public static int BCDtoInt(byte[] var0) {
		int var1 = 0;

		for (int var2 = 0; var2 < var0.length; ++var2) {
			var1 *= 100;
			var1 += (var0[var2] >>> 4 & 15) * 10;
			var1 += var0[var2] & 15;
		}

		return var1;
	}

	public static int BCDtoInt(byte[] var0, int var1, int var2) {
		int var3 = 0;

		for (int var4 = 0; var4 < var2; ++var4) {
			var3 *= 100;
			var3 += (var0[var4 + var1] >>> 4 & 15) * 10;
			var3 += var0[var4 + var1] & 15;
		}

		return var3;
	}

	public static byte[] InttoBCD(int var0) {
		if (0 > var0) {
			throw new RuntimeException("num < 0!");
		} else {
			String var1 = "" + var0;
			return ASCtoBCD(var1);
		}
	}

	public static boolean isHexString(String var0) {
		if (null == var0) {
			return false;
		} else {
			byte[] var1 = var0.getBytes();
			if (var1.length % 2 != 0) {
				return false;
			} else {
				for (int var2 = 0; var2 < var1.length; ++var2) {
					if ((var1[var2] < 48 || var1[var2] > 57) && (var1[var2] < 97 || var1[var2] > 102)
							&& (var1[var2] < 65 || var1[var2] > 70)) {
						return false;
					}
				}

				return true;
			}
		}
	}

	public static boolean isNumeric(String var0) {
		if (var0.length() > 0) {
			return !"-".equalsIgnoreCase(var0.substring(0, 1)) && !"+".equalsIgnoreCase(var0.substring(0, 1))
					? isNumerical(var0)
					: isNumerical(var0.substring(1));
		} else {
			return true;
		}
	}

	public static boolean isNumerical(String var0) {
		for (int var1 = 0; var1 < var0.length(); ++var1) {
			char var2 = var0.charAt(var1);
			if (!chrIsNum(var2)) {
				return false;
			}
		}

		return true;
	}

	public static boolean isAlpha(String var0) {
		for (int var1 = 0; var1 < var0.length(); ++var1) {
			char var2 = var0.charAt(var1);
			if (!chrIsAlpha(var2)) {
				return false;
			}
		}

		return true;
	}

	public static boolean isAlphanumeric(String var0) {
		for (int var1 = 0; var1 < var0.length(); ++var1) {
			char var2 = var0.charAt(var1);
			if (!chrIsAlpha(var2) && !chrIsNum(var2)) {
				return false;
			}
		}

		return true;
	}

	public static boolean chrIsNum(char var0) {
		return var0 >= '0' && var0 <= '9';
	}

	public static boolean chrIsAlpha(char var0) {
		return var0 >= 'a' && var0 <= 'z' || var0 >= 'A' && var0 <= 'Z';
	}

	public static int byteArrayIndexOf(byte[] var0, byte[] var1, int var2) {
		for (int var3 = var2; var3 + var1.length <= var0.length; ++var3) {
			int var4;
			for (var4 = 0; var4 < var1.length && var0[var3 + var4] == var1[var4]; ++var4) {
			}

			if (var4 == var1.length) {
				return var3;
			}
		}

		return -1;
	}

	public static int byteArrayLastIndexOf(byte[] var0, byte[] var1, int var2) {
		int var3 = var2 - var1.length;
		if (var3 < 0) {
			return -1;
		} else {
			while (var3 >= 0) {
				int var4;
				for (var4 = 0; var4 < var1.length && var0[var3 + var4] == var1[var4]; ++var4) {
				}

				if (var4 == var1.length) {
					return var3;
				}

				--var3;
			}

			return -1;
		}
	}

	public static boolean equals4Bytes(byte[] var0, byte[] var1) {
		return Arrays.equals(var0, var1);
	}

	public static byte[] replaceAll(byte[] var0, byte var1, byte var2, int var3, int var4) {
		if (var3 < 0) {
			return var0;
		} else if (var4 > var0.length) {
			return var0;
		} else {
			for (int var5 = var3; var5 < var4; ++var5) {
				if (var0[var5] == var1) {
					var0[var5] = var2;
				}
			}

			return var0;
		}
	}

	

	public static boolean isLeapYear(String var0) {
		if (null == var0) {
			throw new RuntimeException("year is null!");
		} else if (0 == var0.length()) {
			throw new RuntimeException("year is Black!");
		} else {
			boolean var1 = false;

			int var4;
			try {
				var4 = Integer.parseInt(var0);
			} catch (Exception var3) {
				throw new RuntimeException("year must be a num!");
			}

			if (0 == var4 % 400) {
				return true;
			} else if (0 == var4 % 100) {
				return false;
			} else {
				return 0 == var4 % 4;
			}
		}
	}
}
