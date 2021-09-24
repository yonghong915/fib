package com.fib.upp;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;

public class StrTest {

	public static void main(String[] args) {
		// . , - _ ( ) / = + ? ! & * ; @ # : % [ ] \n \r \t
//		char dot = StrUtil.C_DOT;// 点
//		char comma = StrUtil.C_COMMA;// 逗号
//		String dashed = StrUtil.DASHED;// 减号
//		String UNDERLINE = StrUtil.UNDERLINE;// 下划线

		String orgStr = "ab已哥啊哥&23";
		orgStr.toCharArray();
		for (int i = 0; i < orgStr.length(); i++) {
			char orgiChar = orgStr.charAt(i);
			boolean flag = CharUtil.isLetterOrNumber(orgiChar);
			boolean isCh = isChinese(orgiChar);
			boolean isNum = isNumber(orgiChar);
			boolean isSpe = isSpecialChar(orgiChar);
			System.out.println("flag=" + flag + "  isCh=" + isCh + "  isNum=" + isNum + " isSpe=" + isSpe);
		}

	}

	public static boolean isSpecialChar(final char ch) {
		for (char speciCh : SPECIAL_CHAR_ARR) {
			if (CharUtil.equals(speciCh, ch, false)) {
				return true;
			}
		}
		return false;
	}

	static char[] SPECIAL_CHAR_ARR = { CharPool.DOT, StrUtil.C_COMMA, CharPool.DASHED, StrUtil.C_UNDERLINE, '(', ')',
			StrUtil.C_SLASH, '=', '+', '?', '!', '&', '*', ';', StrUtil.C_AT, '#', StrUtil.C_COLON, '%',
			StrUtil.C_BRACKET_START, StrUtil.C_BRACKET_END, StrUtil.C_LF, StrUtil.C_CR, StrUtil.C_TAB };

	/**
	 * 判定输入的是否是汉字
	 *
	 * @param c 被校验的字符
	 * @return true代表是汉字
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	public static boolean isLetter(char ch) {
		return isLetterUpper(ch) || isLetterLower(ch);
	}

	public static boolean isLetterUpper(final char ch) {
		return ch >= 'A' && ch <= 'Z';
	}

	public static boolean isLetterLower(final char ch) {
		return ch >= 'a' && ch <= 'z';
	}

	public static boolean isNumber(char ch) {
		return ch >= '0' && ch <= '9';
	}
}
