package com.fib.upp.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.fib.commons.exception.CommonException;

public class CodeUtil {

	public static final String NEW_LINE = System.getProperty("line.separator");

	private CodeUtil() {
	}

	public static void setBit(byte[] abyte0, int i, int j) {
		int k = 0 != i % 8 ? i / 8 : i / 8 - 1;
		int l = 0 != i % 8 ? i % 8 : 8;
		int i1 = 1;
		i1 <<= 8 - l;
		if (j == 1) {
			abyte0[k] |= i1;
		} else {
			i1 ^= 0xff;
			abyte0[k] &= i1;
		}
	}

	public static int getBit(byte[] abyte0, int i) {
		int j = 0 != i % 8 ? i / 8 : i / 8 - 1;
		int k = 0 != i % 8 ? i % 8 : 8;
		byte byte0 = abyte0[j];
		byte0 >>= 8 - k;
		byte0 &= 1;
		return byte0;
	}

	public static int hexBytesToInt(byte[] abyte0) {
		return hexBytesToInt(abyte0, 0, abyte0.length);
	}

	public static int hexBytesToInt(byte[] abyte0, int i, int j) {
		int k = 0;
		int l = 1;
		for (int i1 = 0; i1 < j; i1++) {
			k += (abyte0[(i + j) - i1 - 1] & 0xf) * l;
			l *= 16;
			k += (abyte0[(i + j) - i1 - 1] >>> 4 & 0xf) * l;
			l *= 16;
		}

		return k;
	}

	public static byte[] intToHexBytes(int i) {
		String s = Integer.toHexString(i);
		return hextoByte(s);
	}

	public static int byteToInt(byte byte0) {
		return byte0 & 0xff;
	}

	public static int bytesToInt(byte[] abyte0) {
		int i = 0;
		for (int j = 0; j < 4; j++)
			i |= byteToInt(abyte0[j]) << (3 - j) * 8;

		return i;
	}

	public static int bytesToInt(byte[] abyte0, int i) {
		int j = 0;
		for (int k = 0; k < 4; k++)
			j |= byteToInt(abyte0[i + k]) << (3 - k) * 8;

		return j;
	}

	public static long bytesToLong(byte[] abyte0) {
		long l = 0L;
		for (int i = 0; i < 8; i++)
			l |= byteToLong(abyte0[i]) << (7 - i) * 8;

		return l;
	}

	public static long byteToLong(byte byte0) {
		return byte0 & 255L;
	}

	public static short byteToShort(byte byte0) {
		return (short) (byte0 & 0xff);
	}

	public static short bytesToShort(byte[] abyte0) {
		return bytesToShort(abyte0, 0);
	}

	public static short bytesToShort(byte[] abyte0, int i) {
		short word0 = 0;
		for (int j = 0; j < 2; j++)
			word0 |= byteToShort(abyte0[i + j]) << (1 - j) * 8;

		return word0;
	}

	public static byte intToByte(int i) {
		if (i > 255)
			return -1;
		else
			return (byte) i;
	}

	public static byte[] intToBytes(int i) {
		byte[] abyte0 = new byte[4];
		abyte0[0] = 0;
		abyte0[1] = 0;
		abyte0[2] = 0;
		abyte0[3] = 0;
		for (int j = 0; j < 4; j++)
			abyte0[3 - j] |= i >>> j * 8 & 0xff;

		return abyte0;
	}

	public static byte shortToByte(short word0) {
		if (word0 > 255)
			return -1;
		else
			return (byte) word0;
	}

	public static byte[] shortToBytes(short word0) {
		byte[] abyte0 = new byte[2];
		abyte0[0] = 0;
		abyte0[1] = 0;
		for (int i = 0; i < 2; i++)
			abyte0[1 - i] |= word0 >>> i * 8 & 0xff;

		return abyte0;
	}

	public static byte[] htoni(int i) {
		byte[] abyte0 = new byte[4];
		for (int j = 0; j < 4; j++)
			abyte0[j] = (byte) (i >> j * 8 & 0xff);

		return abyte0;
	}

	public static byte[] htons(int i) {
		byte[] abyte0 = new byte[2];
		for (int j = 0; j < 2; j++)
			abyte0[j] = (byte) (i >> j * 8 & 0xff);

		return abyte0;
	}

	public static int ntohi(byte[] abyte0) {
		int i = 0;
		for (int j = 0; j < 4; j++) {
			int k = (abyte0[j] & 0xff) << j * 8;
			i |= k;
		}

		return i;
	}

	public static int ntohs(byte[] abyte0) {
		int i = 0;
		for (int j = 0; j < 2; j++) {
			int k = (abyte0[j] & 0xff) << j * 8;
			i |= k;
		}

		return i;
	}

	public static byte[] longToBytes(long l) {
		byte[] abyte0 = new byte[8];
		abyte0[0] = 0;
		abyte0[1] = 0;
		abyte0[2] = 0;
		abyte0[3] = 0;
		abyte0[4] = 0;
		abyte0[5] = 0;
		abyte0[6] = 0;
		abyte0[7] = 0;
		for (int i = 0; i < 8; i++)
			abyte0[7 - i] |= l >>> i * 8 & 255L;

		return abyte0;
	}

	public static byte[] bytetoHex(byte byte0) {
		byte[] abyte0 = new byte[2];
		byte byte1 = 0;
		byte1 = (byte) (byte0 >>> 4 & 0xf);
		abyte0[0] = (byte) (byte1 <= 9 ? byte1 + 48 : (byte1 - 10) + 65);
		byte1 = (byte) (byte0 & 0xf);
		abyte0[1] = (byte) (byte1 <= 9 ? byte1 + 48 : (byte1 - 10) + 65);
		return abyte0;
	}

	public static byte[] bytetoHex(byte[] abyte0, int i, int j) {
		byte[] abyte1 = new byte[j * 2];
		for (int k = 0; k < j; k++) {
			byte byte0 = (byte) (abyte0[i + k] >>> 4 & 0xf);
			abyte1[2 * k] = (byte) (byte0 <= 9 ? byte0 + 48 : (byte0 - 10) + 65);
			byte0 = (byte) (abyte0[i + k] & 0xf);
			abyte1[2 * k + 1] = (byte) (byte0 <= 9 ? byte0 + 48 : (byte0 - 10) + 65);
		}

		return abyte1;
	}

	public static byte[] bytetoHex(byte[] abyte0) {
		return bytetoHex(abyte0, 0, abyte0.length);
	}

	public static byte[] hextoByte(String s) {
		return hextoByte(s.toUpperCase().getBytes());
	}

	public static byte[] hextoByte(byte[] abyte0) {
		return hextoByte(abyte0, 0, abyte0.length);
	}

	public static byte[] hextoByte(byte[] abyte0, int i, int j) {
		byte[] abyte1 = null;
		if (j % 2 != 0) {
			abyte1 = new byte[j + 1];
			System.arraycopy(abyte0, i, abyte1, 1, j);
			abyte1[0] = 48;
		} else {
			abyte1 = abyte0;
		}
		byte[] abyte2 = new byte[abyte1.length / 2];
		for (int k = 0; k < abyte2.length; k++) {
			byte byte0 = (byte) (abyte1[2 * k] < 65 ? abyte1[2 * k] - 48 : (abyte1[2 * k] - 65) + 10);
			abyte2[k] = (byte) (byte0 << 4);
			byte0 = (byte) ((abyte1[2 * k + 1] < 65 ? abyte1[2 * k + 1] - 48 : (abyte1[2 * k + 1] - 65) + 10) & 0xf);
			abyte2[k] |= byte0;
		}

		return abyte2;
	}

	public static void echoAllChar(byte[] abyte0, int i, int j) {
		for (int k = 0; k < j; k++)
			if (abyte0[i + k] >= 0 && abyte0[i + k] < 32)
				abyte0[i + k] = 32;

	}

	public static String bitString(byte[] abyte0) {
		StringBuilder stringbuffer = new StringBuilder(abyte0.length);
		for (int i = 0; i < abyte0.length; i++) {
			for (int j = 0; j < 8; j++)
				if (1 == (abyte0[i] >> 7 - j & 1))
					stringbuffer.append("1");
				else
					stringbuffer.append("0");

			stringbuffer.append(" ");
		}

		return stringbuffer.toString();
	}

	public static String int2BitString(int i) {
		StringBuilder stringbuffer = new StringBuilder(32);
		for (int j = 4; j > 0; j--) {
			for (int k = 0; k < 8; k++)
				if (1 == (i >> 8 * j - k - 1 & 1))
					stringbuffer.append("1");
				else
					stringbuffer.append("0");

			stringbuffer.append(" ");
		}

		return stringbuffer.toString();
	}

	public static String short2BitString(short word0) {
		StringBuilder stringbuffer = new StringBuilder();
		for (int i = 2; i > 0; i--) {
			for (int j = 0; j < 8; j++)
				if (1 == (word0 >> 8 * i - j & 1))
					stringbuffer.append("1");
				else
					stringbuffer.append("0");

			stringbuffer.append(" ");
		}

		return stringbuffer.toString();
	}

	public static String bytes2FormattedText(byte[] abyte0, int i, int j, String s) {
		byte[] abyte1 = new byte[j];
		System.arraycopy(abyte0, i, abyte1, 0, j);
		return bytes2FormattedText(abyte1, s);
	}

	public static String bytes2FormattedText(byte[] abyte0, int i, int j) {
		return bytes2FormattedText(abyte0, i, j, System.getProperty("file.encoding"));
	}

	public static String bytes2FormattedText(byte[] abyte0, String s) {
		if (null == abyte0 || abyte0.length <= 0)
			return null;
		boolean flag = false;
		int i = 0;
		if (abyte0.length % 16 == 0)
			i = abyte0.length / 16;
		else
			i = abyte0.length / 16 + 1;
		StringBuilder stringbuffer = new StringBuilder(abyte0.length + 20 * i);
		try {
			for (int j = 0; j < i; j++) {
				int k = abyte0.length - j * 16;
				int l = k <= 16 ? k : 16;
				for (int i1 = 0; i1 < l; i1++) {
					byte[] abyte2 = bytetoHex(abyte0[16 * j + i1]);
					stringbuffer.append(new String(abyte2, s));
					stringbuffer.append(" ");
				}

				if (l < 16) {
					for (int j1 = 0; j1 < 16 - l; j1++)
						stringbuffer.append("   ");

				}
				byte[] abyte1 = null;
				int k1 = 0;
				if (flag) {
					if (abyte0.length > 16 * j + l) {
						abyte1 = new byte[l + 2];
						System.arraycopy(abyte0, 16 * j - 1, abyte1, 0, l + 2);
						k1 = l + 1;
					} else {
						abyte1 = new byte[l + 1];
						System.arraycopy(abyte0, 16 * j - 1, abyte1, 0, l + 1);
						k1 = l + 1;
					}
				} else if (abyte0.length > 16 * j + l) {
					abyte1 = new byte[l + 1];
					System.arraycopy(abyte0, 16 * j, abyte1, 0, l + 1);
					k1 = l;
				} else {
					abyte1 = new byte[l];
					System.arraycopy(abyte0, 16 * j, abyte1, 0, l);
					k1 = l;
				}
				int l1 = -1;
				int i2 = -1;
				for (int j2 = 0; j2 < k1; j2++) {
					if (abyte1[j2] < 32 && abyte1[j2] >= 0)
						abyte1[j2] = 95;
					if (abyte1[j2] >= 0)
						continue;
					if (-1 == l1) {
						l1 = j2;
						i2 = j2;
						continue;
					}
					if (j2 - i2 > 1) {
						l1 = j2;
						i2 = j2;
					} else {
						i2 = j2;
					}
				}

				if (flag) {
					if (l1 >= 0 && (i2 - l1) % 2 == 0) {
						stringbuffer.append("   ");
						stringbuffer.append(new String(abyte1, 0, abyte1.length, s));
						stringbuffer.append(NEW_LINE);
						flag = true;
					} else {
						if (l < 16 || abyte0.length % 16 == 0 && i - 1 == j) {
							stringbuffer.append("   ");
							stringbuffer.append(new String(abyte1, 0, abyte1.length, s));
							stringbuffer.append(NEW_LINE);
						} else {
							stringbuffer.append("   ");
							stringbuffer.append(new String(abyte1, 0, abyte1.length - 1, s));
							stringbuffer.append(NEW_LINE);
						}
						flag = false;
					}
				} else if (l1 >= 0 && (i2 - l1) % 2 == 0) {
					stringbuffer.append("    ");
					stringbuffer.append(new String(abyte1, 0, abyte1.length, s));
					stringbuffer.append(NEW_LINE);
					flag = true;
				} else {
					if (l < 16 || abyte0.length % 16 == 0 && i - 1 == j) {
						stringbuffer.append("    ");
						stringbuffer.append(new String(abyte1, 0, abyte1.length, s));
						stringbuffer.append(NEW_LINE);
					} else {
						stringbuffer.append("    ");
						stringbuffer.append(new String(abyte1, 0, abyte1.length - 1, s));
						stringbuffer.append(NEW_LINE);
					}
					flag = false;
				}
			}

		} catch (UnsupportedEncodingException e) {
			throw new CommonException(e);
		}
		return stringbuffer.toString();
	}

	public static String bytes2FormattedText(byte[] abyte0) {
		return bytes2FormattedText(abyte0, System.getProperty("file.encoding"));
	}

	public static String formatAmount(String s, int i) {
		if (i < 0)
			throw new CommonException("scale must be greater than 0");
		if (i == 0) {
			if (null == s || 0 == s.length())
				return "0";
			if (s.length() == 1 && ("-".equals(s.substring(0, 1)) || "+".equals(s.substring(0, 1))))
				return "0";
			else
				return s;
		}
		StringBuilder stringbuffer = new StringBuilder();
		if (null == s || 0 == s.length()) {
			stringbuffer.append("0.");
			for (int j = 0; j < i; j++)
				stringbuffer.append("0");

			return stringbuffer.toString();
		}
		int k = s.length();
		if ("-".equals(s.substring(0, 1)) || "+".equals(s.substring(0, 1))) {
			if (k - 1 <= i) {
				stringbuffer.append("0.");
				for (int l = 0; l < (i - k) + 1; l++)
					stringbuffer.append("0");

				if (k > 1) {
					stringbuffer.append(s.substring(1));
					if ("-".equals(s.substring(0, 1)))
						stringbuffer.insert(0, "-");
					else
						stringbuffer.insert(0, "+");
				}
			} else {
				stringbuffer.append(s.substring(0, k - i));
				stringbuffer.append(".");
				stringbuffer.append(s.substring(k - i));
			}
		} else if (k <= i) {
			stringbuffer.append("0.");
			for (int i1 = 0; i1 < i - k; i1++)
				stringbuffer.append("0");

			stringbuffer.append(s);
		} else {
			stringbuffer.append(s.substring(0, k - i));
			stringbuffer.append(".");
			stringbuffer.append(s.substring(k - i));
		}
		return stringbuffer.toString();
	}

	public static String formatAmount(String s) {
		if (null == s)
			return "0.00";
		int i = s.length();
		if (0 == i)
			return "0.00";
		if (1 == i)
			if ("-".equals(s) || "+".equals(s))
				return "0.00";
			else
				return (new StringBuilder()).append("0.0").append(s).toString();
		if (2 == i) {
			if ("-".equals(s.substring(0, 1)))
				return (new StringBuilder()).append("-0.0").append(s.substring(i - 1)).toString();
			if ("+".equals(s.substring(0, 1)))
				return (new StringBuilder()).append("+0.0").append(s.substring(i - 1)).toString();
			else
				return (new StringBuilder()).append("0.").append(s).toString();
		}
		boolean flag = false;
		String s1 = null;
		if ("-".equals(s.substring(0, 1)) || "+".equals(s.substring(0, 1))) {
			flag = true;
			s1 = s.substring(0, 1);
			s = s.substring(1);
			i = s.length();
		}
		int j;
		for (j = 0; j < i && s.charAt(j) == '0'; j++)
			;
		if (j < i - 2)
			if (flag)
				return (new StringBuilder()).append(s1).append(s.substring(j, i - 2)).append(".").append(s.substring(i - 2)).toString();
			else
				return (new StringBuilder()).append(s.substring(j, i - 2)).append(".").append(s.substring(i - 2)).toString();
		if (flag)
			return (new StringBuilder()).append(s1).append("0.").append(s.substring(i - 2)).toString();
		else
			return (new StringBuilder()).append("0.").append(s.substring(i - 2)).toString();
	}

	public static String unFormatAmount4TwoDecimals(String s) {
		return unFormatAmount(s, 0, 2);
	}

	public static String unFormatAmount(String s, int i, int j) {
		if (null == s)
			return null;
		boolean flag = false;
		String s1 = null;
		if ("-".equals(s.substring(0, 1)) || "+".equals(s.substring(0, 1))) {
			flag = true;
			i--;
			s1 = s.substring(0, 1);
			s = s.substring(1);
		}
		int k = s.indexOf(".");
		int l = s.length();
		StringBuilder stringbuffer = new StringBuilder();
		stringbuffer.append(s.substring(0, k));
		stringbuffer.append(s.substring(k + 1));
		for (int i1 = l - k - 1; i1 < j; i1++)
			stringbuffer.append("0");

		int j1;
		if (j - (l - k - 1) > 0)
			j1 = ((i - l) + 1) - (j - (l - k - 1));
		else
			j1 = (i - l) + 1;
		for (; j1 > 0; j1--)
			stringbuffer.insert(0, "0");

		if (flag)
			stringbuffer.insert(0, s1);
		return stringbuffer.toString();
	}

	public static String unFormatAmount(String s, int i) {
		if (null == s)
			return null;
		boolean flag = false;
		String s1 = null;
		if ("-".equals(s.substring(0, 1)) || "+".equals(s.substring(0, 1))) {
			flag = true;
			i--;
			s1 = s.substring(0, 1);
			s = s.substring(1);
		}
		int j = s.indexOf(".");
		int k = s.length();
		String s2 = (new StringBuilder()).append(s.substring(0, j)).append(s.substring(j + 1)).toString();
		for (int l = (i - k) + 1; l > 0; l--)
			s2 = (new StringBuilder()).append('0').append(s2).toString();

		if (flag)
			s2 = (new StringBuilder()).append(s1).append(s2).toString();
		return s2;
	}

	public static byte[] bCDtoASC(byte[] abyte0) {
		byte[] abyte1 = new byte[abyte0.length * 2];
		for (int i = 0; i < abyte0.length; i++) {
			byte byte0 = (byte) (abyte0[i] >>> 4 & 0xf);
			abyte1[2 * i] = (byte) (byte0 <= 9 ? byte0 + 48 : (byte0 - 10) + 97);
			byte0 = (byte) (abyte0[i] & 0xf);
			abyte1[2 * i + 1] = (byte) (byte0 <= 9 ? byte0 + 48 : (byte0 - 10) + 97);
		}

		return abyte1;
	}

	public static byte[] bCDtoASCUpperCase(String s) {
		return bCDtoASCUpperCase(s.getBytes());
	}

	public static byte[] bCDtoASCUpperCase(byte[] abyte0) {
		byte[] abyte1 = new byte[abyte0.length * 2];
		for (int i = 0; i < abyte0.length; i++) {
			byte byte0 = (byte) (abyte0[i] >>> 4 & 0xf);
			abyte1[2 * i] = (byte) (byte0 <= 9 ? byte0 + 48 : (byte0 - 10) + 65);
			byte0 = (byte) (abyte0[i] & 0xf);
			abyte1[2 * i + 1] = (byte) (byte0 <= 9 ? byte0 + 48 : (byte0 - 10) + 65);
		}

		return abyte1;
	}

	public static byte[] bCDtoASC(byte[] abyte0, int i, int j) {
		byte[] abyte1 = new byte[j * 2];
		for (int k = 0; k < j; k++) {
			byte byte0 = (byte) (abyte0[k + i] >>> 4 & 0xf);
			abyte1[2 * k] = (byte) (byte0 <= 9 ? byte0 + 48 : (byte0 - 10) + 97);
			byte0 = (byte) (abyte0[k + i] & 0xf);
			abyte1[2 * k + 1] = (byte) (byte0 <= 9 ? byte0 + 48 : (byte0 - 10) + 97);
		}

		return abyte1;
	}

	public static byte[] aSCtoBCD(byte[] abyte0) {
		return hextoByte(abyte0, 0, abyte0.length);
	}

	public static byte[] aSCtoBCD(String s) {
		return hextoByte(s);
	}

	public static byte[] aSCtoBCD(byte[] abyte0, int i, int j) {
		return hextoByte(abyte0, i, j);
	}

	public static int bCDtoInt(byte[] abyte0) {
		int i = 0;
		for (int j = 0; j < abyte0.length; j++) {
			i *= 100;
			i += (abyte0[j] >>> 4 & 0xf) * 10;
			i += abyte0[j] & 0xf;
		}

		return i;
	}

	public static int bCDtoInt(byte[] abyte0, int i, int j) {
		int k = 0;
		for (int l = 0; l < j; l++) {
			k *= 100;
			k += (abyte0[l + i] >>> 4 & 0xf) * 10;
			k += abyte0[l + i] & 0xf;
		}

		return k;
	}

	public static byte[] inttoBCD(int i) {
		if (0 > i) {
			throw new CommonException("num < 0!");
		} else {
			String s = (new StringBuilder()).append("").append(i).toString();
			return aSCtoBCD(s);
		}
	}

	public static boolean isHexString(String s) {
		if (null == s)
			return false;
		byte[] abyte0 = s.getBytes();
		if (abyte0.length % 2 != 0)
			return false;
		for (int i = 0; i < abyte0.length; i++)
			if ((abyte0[i] < 48 || abyte0[i] > 57) && (abyte0[i] < 97 || abyte0[i] > 102) && (abyte0[i] < 65 || abyte0[i] > 70))
				return false;

		return true;
	}

	public static boolean isNumeric(String s) {
		if (s.length() > 0) {
			if ("-".equalsIgnoreCase(s.substring(0, 1)) || "+".equalsIgnoreCase(s.substring(0, 1)))
				return isNumerical(s.substring(1));
			else
				return isNumerical(s);
		} else {
			return true;
		}
	}

	public static boolean isNumerical(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!chrIsNum(c))
				return false;
		}

		return true;
	}

	public static boolean isAlpha(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!chrIsAlpha(c))
				return false;
		}

		return true;
	}

	public static boolean isAlphanumeric(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!chrIsAlpha(c) && !chrIsNum(c))
				return false;
		}

		return true;
	}

	public static boolean chrIsNum(char c) {
		return c >= '0' && c <= '9';
	}

	public static boolean chrIsAlpha(char c) {
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
	}

	public static int byteArrayIndexOf(byte[] abyte0, byte[] abyte1, int i) {
		for (int j = i; j + abyte1.length <= abyte0.length; j++) {
			int k;
			for (k = 0; k < abyte1.length && abyte0[j + k] == abyte1[k]; k++)
				;
			if (k == abyte1.length)
				return j;
		}

		return -1;
	}

	public static int byteArrayLastIndexOf(byte[] abyte0, byte[] abyte1, int i) {
		int j = i - abyte1.length;
		if (j < 0)
			return -1;
		for (; j >= 0; j--) {
			int k;
			for (k = 0; k < abyte1.length && abyte0[j + k] == abyte1[k]; k++)
				;
			if (k == abyte1.length)
				return j;
		}

		return -1;
	}

	public static boolean equals4Bytes(byte[] abyte0, byte[] abyte1) {
		return Arrays.equals(abyte0, abyte1);
	}

	public static byte[] replaceAll(byte[] abyte0, byte byte0, byte byte1, int i, int j) {
		if (i < 0)
			return abyte0;
		if (j > abyte0.length)
			return abyte0;
		for (int k = i; k < j; k++)
			if (abyte0[k] == byte0)
				abyte0[k] = byte1;

		return abyte0;
	}

	public static byte[] replaceAll(byte[] abyte0, byte[] abyte1, byte[] abyte2, int i, int j) {
		if (i < 0)
			return abyte0;
		if (j > abyte0.length)
			return abyte0;
		int i1 = abyte1.length;
		ByteBuffer bytebuffer = new ByteBuffer(abyte0.length + 16);
		for (int j1 = i; j1 < j;)
			if (abyte0[j1] != abyte1[0]) {
				bytebuffer.append(abyte0[j1++]);
			} else {
				int k;
				for (k = 0; k < i1 && abyte0[j1 + k] == abyte1[k]; k++)
					;
				if (k < i1) {
					int l = 0;
					while (l < i1) {
						bytebuffer.append(abyte0[j1++]);
						l++;
					}
				} else {
					bytebuffer.append(abyte2);
					j1 += i1;
				}
			}

		return bytebuffer.toBytes();
	}

	public static boolean isLeapYear(String s) {
		if (null == s)
			throw new CommonException("year is null!");
		if (0 == s.length())
			throw new CommonException("year is Black!");
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (Exception exception) {
			throw new CommonException("year must be a num!");
		}
		if (0 == i % 400)
			return true;
		if (0 == i % 100)
			return false;
		return 0 == i % 4;
	}

}
