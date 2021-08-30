package com.fib.msgconverter.commgateway.job.expression;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeSet;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;

public class TimeExpressionParser implements  Cloneable {
	/**
	 * 类型：秒
	 */
	protected static final int SECOND = 0;
	/**
	 * 类型：分钟
	 */
	protected static final int MINUTE = 1;
	/**
	 * 类型：小时
	 */
	protected static final int HOUR = 2;
	/**
	 * 类型：一月中的一天
	 */
	protected static final int DAY_OF_MONTH = 3;
	/**
	 * 类型：月份
	 */
	protected static final int MONTH = 4;
	/**
	 * 类型：一周中的一天
	 */
	protected static final int DAY_OF_WEEK = 5;
	/**
	 * 类型：年
	 */
	protected static final int YEAR = 6;

	protected static final int ALL_SPEC_INT = 99;

	protected static final int NO_SPEC_INT = 98;

	protected static final Integer ALL_SPEC =  Integer.valueOf(99);

	protected static final Integer NO_SPEC =  Integer.valueOf(98);
	/**
	 * 存放所有月份的容器，如JAN,FEB等
	 */
	protected static Map monthMap;
	/**
	 * 存放SUN,MON等一周中每天名称的容器
	 */
	protected static Map dayMap;
	/**
	 * 表达式
	 */
	private String expression;
	/**
	 * 时区
	 */
	private TimeZone timeZone;

	protected transient TreeSet seconds;

	protected transient TreeSet minutes;

	protected transient TreeSet hours;

	protected transient TreeSet daysOfMonth;

	protected transient TreeSet months;

	protected transient TreeSet daysOfWeek;

	protected transient TreeSet years;

	protected transient boolean lastdayOfWeek;

	protected transient int nthdayOfWeek;

	protected transient boolean lastdayOfMonth;

	protected transient boolean nearestWeekday;

	protected transient boolean expressionParsed;

	static {
		monthMap = new HashMap(20);
		dayMap = new HashMap(60);
		monthMap.put("JAN", Integer.valueOf(0));
		monthMap.put("FEB",  Integer.valueOf(1));
		monthMap.put("MAR",  Integer.valueOf(2));
		monthMap.put("APR",  Integer.valueOf(3));
		monthMap.put("MAY",  Integer.valueOf(4));
		monthMap.put("JUN",  Integer.valueOf(5));
		monthMap.put("JUL",  Integer.valueOf(6));
		monthMap.put("AUG",  Integer.valueOf(7));
		monthMap.put("SEP",  Integer.valueOf(8));
		monthMap.put("OCT",  Integer.valueOf(9));
		monthMap.put("NOV",  Integer.valueOf(10));
		monthMap.put("DEC",  Integer.valueOf(11));
		dayMap.put("SUN",  Integer.valueOf(1));
		dayMap.put("MON",  Integer.valueOf(2));
		dayMap.put("TUE",  Integer.valueOf(3));
		dayMap.put("WED",  Integer.valueOf(4));
		dayMap.put("THU",  Integer.valueOf(5));
		dayMap.put("FRI",  Integer.valueOf(6));
		dayMap.put("SAT",  Integer.valueOf(7));
	}

	public TimeExpressionParser(String expression) throws ParseException {

		this.expression = null;
		timeZone = null;
		lastdayOfWeek = false;
		nthdayOfWeek = 0;
		lastdayOfMonth = false;
		nearestWeekday = false;
		expressionParsed = false;
		if (expression == null) {
			// throw new IllegalArgumentException("expression cannot be null");
			throw new IllegalArgumentException(MultiLanguageResourceBundle
					.getInstance().getString("inputParameter.null"));
		} else {
			this.expression = expression.toUpperCase(Locale.US);
			buildExpression(this.expression);
			return;
		}
	}

	/**
	 * 获得从当前时间开始的下次执行时间
	 * 
	 * @return
	 */
	public long caculateNextFireTimeFromNow() {
		Date tmp = getTimeAfter(new Date());
		if (null == tmp) {
			return 0;
		} else {
			return tmp.getTime();
		}
	}

	/**
	 * 获得从给定时间开始的下次执行时间
	 * 
	 * @param date
	 * @return
	 */
	public Date getNextValidTimeAfter(Date date) {
		return getTimeAfter(date);
	}

	/**
	 * 获得当前时区
	 * 
	 * @return
	 */
	public TimeZone getTimeZone() {
		if (timeZone == null) {
			timeZone = TimeZone.getDefault();
		}
		return timeZone;
	}

	/**
	 * 设定时区
	 * 
	 * @param timeZone
	 */
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public String toString() {
		return expression;
	}

	/**
	 * 检查是否是有效的时间表达式
	 * 
	 * @param expression
	 * @return
	 */
	public static boolean isValidExpression(String expression) {
		try {
			new TimeExpressionParser(expression);
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	/**
	 * 分解表达式
	 * 
	 * @param expression
	 * @throws ParseException
	 */
	protected void buildExpression(String expression) throws ParseException {
		expressionParsed = true;
		try {
			if (seconds == null) {
				seconds = new TreeSet();
			}
			if (minutes == null) {
				minutes = new TreeSet();
			}
			if (hours == null) {
				hours = new TreeSet();
			}
			if (daysOfMonth == null) {
				daysOfMonth = new TreeSet();
			}
			if (months == null) {
				months = new TreeSet();
			}
			if (daysOfWeek == null) {
				daysOfWeek = new TreeSet();
			}
			if (years == null) {
				years = new TreeSet();
			}
			int exprOn = 0;
			for (StringTokenizer exprsTok = new StringTokenizer(expression,
					" \t", false); exprsTok.hasMoreTokens() && exprOn <= 6; exprOn++) {
				String expr = exprsTok.nextToken().trim();
				if (exprOn == 3 && expr.indexOf('L') != -1 && expr.length() > 1
						&& expr.indexOf(",") >= 0) {
					// throw new ParseException(
					// "Support for specifying 'L' and 'LW' with other days of the month is not implemented",
					// -1);
					throw new ParseException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"TimeExpressionParser.buildExpression.parseMonth.error"),
							-1);
				}
				if (exprOn == 5 && expr.indexOf('L') != -1 && expr.length() > 1
						&& expr.indexOf(",") >= 0) {
					// throw new ParseException(
					// "Support for specifying 'L' with other days of the week is not implemented",
					// -1);
					throw new ParseException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"TimeExpressionParser.buildExpression.parseWeek.error"),
							-1);
				}
				String v;
				for (StringTokenizer vTok = new StringTokenizer(expr, ","); vTok
						.hasMoreTokens(); storeExpressionVals(0, v, exprOn)) {
					v = vTok.nextToken();
				}
			}

			if (exprOn <= 5) {
				// throw new ParseException("Unexpected end of expression.",
				// expression.length());
				throw new ParseException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.buildExpression.expression.unexpectedEnd"),
						expression.length());
			}
			if (exprOn <= 6) {
				storeExpressionVals(0, "*", 6);
			}
			TreeSet dow = getSet(5);
			TreeSet dom = getSet(3);
			boolean dayOfMSpec = !dom.contains(NO_SPEC);
			boolean dayOfWSpec = !dow.contains(NO_SPEC);
			if ((!dayOfMSpec || dayOfWSpec) && (!dayOfWSpec || dayOfMSpec)) {
				// throw new ParseException(
				// "Support for specifying both a day-of-week AND a day-of-month parameter is not implemented.",
				// 0);
				throw new ParseException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.buildExpression.parseMonthOrWeek.error"),
						0);
			}
		} catch (ParseException pe) {
			throw pe;
		} catch (Exception e) {
			// throw new ParseException("Illegal time expression format["
			// + e.toString() + "]", 0);
			throw new ParseException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"TimeExpressionParser.buildExpression.expression.illegalFormat",
									new String[] { e.toString() }), 0);
		}
	}

	/**
	 * 储存表达式的值
	 * 
	 * @param pos
	 * @param s
	 * @param type
	 * @return
	 * @throws ParseException
	 */
	protected int storeExpressionVals(int pos, String s, int type)
			throws ParseException {
		int incr = 0;
		int i = skipWhiteSpace(pos, s);
		if (i >= s.length()) {
			return i;
		}
		char c = s.charAt(i);
		if (c >= 'A' && c <= 'Z' && !s.equals("L") && !s.equals("LW")) {
			String sub = s.substring(i, i + 3);
			int sval = -1;
			int eval = -1;
			if (type == MONTH) {
				sval = getMonthNumber(sub) + 1;
				if (sval <= 0) {
					// throw new ParseException("Invalid Month value[" + sub
					// + "]", i);
					throw new ParseException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"TimeExpressionParser.storeExpressionVals.monthValue.invalid",
											new String[] { sub }), i);
				}
				if (s.length() > i + 3) {
					c = s.charAt(i + 3);
					if (c == '-') {
						i += 4;
						sub = s.substring(i, i + 3);
						eval = getMonthNumber(sub) + 1;
						if (eval <= 0) {
							// throw new ParseException("Invalid Month value["
							// + sub + "]", i);
							throw new ParseException(
									MultiLanguageResourceBundle
											.getInstance()
											.getString(
													"TimeExpressionParser.storeExpressionVals.monthValue.invalid",
													new String[] { sub }), i);
						}
					}
				}
			} else if (type == DAY_OF_WEEK) {
				sval = getDayOfWeekNumber(sub);
				if (sval < 0) {
					// throw new ParseException("Invalid Day-of-Week value[" +
					// sub
					// + "]", i);
					throw new ParseException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"TimeExpressionParser.storeExpressionVals.dayOfWeek.invalid",
											new String[] { sub }), i);
				}
				if (s.length() > i + 3) {
					c = s.charAt(i + 3);
					if (c == '-') {
						i += 4;
						sub = s.substring(i, i + 3);
						eval = getDayOfWeekNumber(sub);
						if (eval < 0) {
							// throw new ParseException(
							// "Invalid Day-of-Week value[" + sub + "]",
							// i);
							throw new ParseException(
									MultiLanguageResourceBundle
											.getInstance()
											.getString(
													"TimeExpressionParser.storeExpressionVals.dayOfWeek.invalid",
													new String[] { sub }), i);
						}
					} else if (c == '#') {
						try {
							i += 4;
							nthdayOfWeek = Integer.parseInt(s.substring(i));
							if (nthdayOfWeek < 1 || nthdayOfWeek > 5) {
								throw new Exception();
							}
						} catch (Exception e) {
							// throw new ParseException(
							// "A numeric value between 1 and 5 must follow the '#' option",
							// i);
							throw new ParseException(
									MultiLanguageResourceBundle
											.getInstance()
											.getString(
													"TimeExpressionParser.numbericValue.mustFollow"),
									i);
						}
					} else if (c == 'L') {
						lastdayOfWeek = true;
						i++;
					}
				}
			} else {
				// throw new ParseException(
				// "Illegal characters for this position[" + sub + "]", i);
				throw new ParseException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.storeExpressionVals.illegalCharacter.1",
										new String[] { sub }), i);
			}
			if (eval != -1) {
				incr = 1;
			}
			addToSet(sval, eval, incr, type);
			return i + 3;
		}
		if (c == '?') {
			if (++i + 1 < s.length() && s.charAt(i) != ' '
					&& s.charAt(i + 1) != '\t') {
				// throw new ParseException("Illegal character after '?': "
				// + s.charAt(i), i);
				throw new ParseException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.storeExpressionVals.illegalCharacter.2",
										new String[] { "" + s.charAt(i) }), i);
			}
			if (type != DAY_OF_WEEK && type != DAY_OF_MONTH) {
				// throw new ParseException(
				// "'?' can only be specified for Day-of-Month or Day-of-Week.",
				// i);
				throw new ParseException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.storeExpressionVals.specialSymbol.1"),
						i);
			}
			if (type == DAY_OF_WEEK && !lastdayOfMonth) {
				int val = ((Integer) daysOfMonth.last()).intValue();
				if (val == NO_SPEC_INT) {
					// throw new ParseException(
					// "'?' can only be specified for Day-of-Month or Day-of-Week.",
					// i);
					throw new ParseException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"TimeExpressionParser.storeExpressionVals.specialSymbol.1"),
							i);
				}
			}
			addToSet(NO_SPEC_INT, -1, 0, type);
			return i;
		}
		if (c == '*' || c == '/') {
			if (c == '*' && i + 1 >= s.length()) {
				addToSet(99, -1, incr, type);
				return i + 1;
			}
			if (c == '/'
					&& (i + 1 >= s.length() || s.charAt(i + 1) == ' ' || s
							.charAt(i + 1) == '\t')) {
				// throw new
				// ParseException("'/' must be followed by an integer.",
				// i);
				throw new ParseException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.storeExpressionVals.specialSymbol.2"),
						i);
			}
			if (c == '*') {
				i++;
			}
			c = s.charAt(i);
			if (c == '/') {
				if (++i >= s.length()) {
					// throw new ParseException("Unexpected end of string.", i);
					throw new ParseException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"TimeExpressionParser.storeExpressionVals.unexpectedEnd"),
							i);
				}
				incr = getNumericValue(s, i);
				i++;
				if (incr > 10) {
					i++;
				}
				if (incr > 59 && (type == SECOND || type == MINUTE)) {
					// throw new ParseException("Increment[" + incr + "] > 60",
					// i);
					throw new ParseException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"TimeExpressionParser.storeExpressionVals.increment.greaterThan60",
											new String[] { "" + incr }), i);
				}
				if (incr > 23 && type == HOUR) {
					// throw new ParseException("Increment[" + incr + "] > 24",
					// i);
					throw new ParseException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"TimeExpressionParser.storeExpressionVals.increment.greaterThan24",
											new String[] { "" + incr }), i);
				}
				if (incr > 31 && type == DAY_OF_MONTH) {
					// throw new ParseException("Increment[" + incr + "] > 31",
					// i);
					throw new ParseException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"TimeExpressionParser.storeExpressionVals.increment.greaterThan31",
											new String[] { "" + incr }), i);
				}
				if (incr > 7 && type == DAY_OF_WEEK) {
					// throw new ParseException("Increment[" + incr + "] > 7",
					// i);
					throw new ParseException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"TimeExpressionParser.storeExpressionVals.increment.greaterThan7",
											new String[] { "" + incr }), i);
				}
				if (incr > 12 && type == MONTH) {
					// throw new ParseException("Increment[" + incr + "] > 12",
					// i);
					throw new ParseException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"TimeExpressionParser.storeExpressionVals.increment.greaterThan12",
											new String[] { "" + incr }), i);
				}
			} else {
				incr = 1;
			}
			addToSet(ALL_SPEC_INT, -1, incr, type);
			return i;
		}
		if (c == 'L') {
			i++;
			if (type == DAY_OF_MONTH) {
				lastdayOfMonth = true;
			}
			if (type == DAY_OF_WEEK) {
				addToSet(7, 7, 0, type);
			}
			if (type == DAY_OF_MONTH && s.length() > i) {
				c = s.charAt(i);
				if (c == 'W') {
					nearestWeekday = true;
					i++;
				}
			}
			return i;
		}
		if (c >= '0' && c <= '9') {
			int val = Integer.parseInt(String.valueOf(c));
			if (++i >= s.length()) {
				addToSet(val, -1, -1, type);
			} else {
				c = s.charAt(i);
				if (c >= '0' && c <= '9') {
					ValueSet vs = getValue(val, s, i);
					val = vs.value;
					i = vs.pos;
				}
				i = checkNext(i, s, val, type);
				return i;
			}
		} else {
			// throw new ParseException("Unexpected character: " + c, i);
			throw new ParseException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"TimeExpressionParser.storeExpressionVals.character.unexpected"),
					i);
		}
		return i;
	}

	protected int checkNext(int pos, String s, int val, int type)
			throws ParseException {
		int end = -1;
		int i = pos;
		if (i >= s.length()) {
			addToSet(val, end, -1, type);
			return i;
		}
		char c = s.charAt(pos);
		if (c == 'L') {
			if (type == DAY_OF_WEEK) {
				lastdayOfWeek = true;
			} else {
				// throw new
				// ParseException("'L' option is not valid here. index = "
				// + i , i);
				throw new ParseException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.checkNext.LOption.notValid",
										new String[] { "" + i }), i);
			}
			TreeSet set = getSet(type);
			set.add( Integer.valueOf(val));
			return ++i;
		}
		if (c == 'W') {
			if (type == DAY_OF_MONTH) {
				nearestWeekday = true;
			} else {
				// throw new ParseException(
				// "'W' option is not valid here. index = " + i, i);
				throw new ParseException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.checkNext.WOption.notValid",
										new String[] { "" + i }), i);
			}
			TreeSet set = getSet(type);
			set.add( Integer.valueOf(val));
			return ++i;
		}
		if (c == '#') {
			if (type != DAY_OF_WEEK) {
				// throw new ParseException(
				// "'#' option is not valid here. index = " + i, i);
				throw new ParseException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.checkNext.Option.notValid",
										new String[] { "" + i }), i);
			}
			i++;
			try {
				nthdayOfWeek = Integer.parseInt(s.substring(i));
				if (nthdayOfWeek < 1 || nthdayOfWeek > 5) {
					throw new RuntimeException();
				}
			} catch (Exception e) {
				// throw new ParseException(
				// "A numeric value between 1 and 5 must follow the '#' option",
				// i);
				throw new ParseException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.numbericValue.mustFollow"),
						i);
			}
			TreeSet set = getSet(type);
			set.add( Integer.valueOf(val));
			return ++i;
		}
		if (c == '-') {
			i++;
			c = s.charAt(i);
			int v = Integer.parseInt(String.valueOf(c));
			end = v;
			if (++i >= s.length()) {
				addToSet(val, end, 1, type);
				return i;
			}
			c = s.charAt(i);
			if (c >= '0' && c <= '9') {
				ValueSet vs = getValue(v, s, i);
				int v1 = vs.value;
				end = v1;
				i = vs.pos;
			}
			if (i < s.length() && (c = s.charAt(i)) == '/') {
				i++;
				c = s.charAt(i);
				int v2 = Integer.parseInt(String.valueOf(c));
				if (++i >= s.length()) {
					addToSet(val, end, v2, type);
					return i;
				}
				c = s.charAt(i);
				if (c >= '0' && c <= '9') {
					ValueSet vs = getValue(v2, s, i);
					int v3 = vs.value;
					addToSet(val, end, v3, type);
					i = vs.pos;
					return i;
				} else {
					addToSet(val, end, v2, type);
					return i;
				}
			} else {
				addToSet(val, end, 1, type);
				return i;
			}
		}
		if (c == '/') {
			i++;
			c = s.charAt(i);
			int v2 = Integer.parseInt(String.valueOf(c));
			if (++i >= s.length()) {
				addToSet(val, end, v2, type);
				return i;
			}
			c = s.charAt(i);
			if (c >= '0' && c <= '9') {
				ValueSet vs = getValue(v2, s, i);
				int v3 = vs.value;
				addToSet(val, end, v3, type);
				i = vs.pos;
				return i;
			} else {
				// throw new ParseException("Unexpected character '" + c
				// + "' after '/'", i);
				throw new ParseException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.checkNext.character.unexpected",
										new String[] { "" + c }), i);
			}
		} else {
			addToSet(val, end, 0, type);
			return ++i;
		}
	}

	/**
	 * 获得表达式的值
	 * 
	 * @return
	 */
	public String getExpression() {
		return expression;
	}

	protected int skipWhiteSpace(int i, String s) {
		while (i < s.length() && (s.charAt(i) == ' ' || s.charAt(i) == '\t')) {
			i++;
		}
		return i;
	}

	protected int findNextWhiteSpace(int i, String s) {
		while (i < s.length() && (s.charAt(i) != ' ' || s.charAt(i) != '\t')) {
			i++;
		}
		return i;
	}

	protected void addToSet(int val, int end, int incr, int type)
			throws ParseException {
		TreeSet set = getSet(type);
		if (type == SECOND || type == MINUTE) {
			if ((val < 0 || val > 59 || end > 59) && val != ALL_SPEC_INT) {
				// throw new ParseException(
				// "Minute and Second values must be between 0 and 59", -1);
				throw new ParseException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.addToSet.minuteAndSecondValue.between0And59"),
						-1);
			}
		} else if (type == HOUR) {
			if ((val < 0 || val > 23 || end > 23) && val != ALL_SPEC_INT) {
				// throw new ParseException(
				// "Hour values must be between 0 and 23", -1);
				throw new ParseException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.addToSet.hourValue.between0And23"),
						-1);
			}
		} else if (type == DAY_OF_MONTH) {
			if ((val < 1 || val > 31 || end > 31) && val != ALL_SPEC_INT
					&& val != NO_SPEC_INT) {
				// throw new ParseException(
				// "Day of month values must be between 1 and 31", -1);
				throw new ParseException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.addToSet.dayOfMonth.between1And31"),
						-1);
			}
		} else if (type == MONTH) {
			if ((val < 1 || val > 12 || end > 12) && val != ALL_SPEC_INT) {
				// throw new ParseException(
				// "Month values must be between 1 and 12", -1);
				throw new ParseException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.addToSet.monthValue.between1And12"),
						-1);
			}
		} else if (type == DAY_OF_WEEK && (val == 0 || val > 7 || end > 7)
				&& val != ALL_SPEC_INT && val != NO_SPEC_INT) {
			// throw new ParseException(
			// "Day-of-Week values must be between 1 and 7", -1);
			throw new ParseException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"TimeExpressionParser.addToSet.dayOfWeek.between1And7"),
					-1);
		}
		if ((incr == 0 || incr == -1) && val != ALL_SPEC_INT) {
			if (val != -1) {
				set.add( Integer.valueOf(val));
			} else {
				set.add(NO_SPEC);
			}
			return;
		}
		int startAt = val;
		int stopAt = end;
		if (val == ALL_SPEC_INT && incr <= 0) {
			incr = 1;
			set.add(ALL_SPEC);
		}
		if (type == SECOND || type == MINUTE) {
			if (stopAt == -1) {
				stopAt = 59;
			}
			if (startAt == -1 || startAt == ALL_SPEC_INT) {
				startAt = 0;
			}
		} else if (type == HOUR) {
			if (stopAt == -1) {
				stopAt = 23;
			}
			if (startAt == -1 || startAt == ALL_SPEC_INT) {
				startAt = 0;
			}
		} else if (type == DAY_OF_MONTH) {
			if (stopAt == -1) {
				stopAt = 31;
			}
			if (startAt == -1 || startAt == ALL_SPEC_INT) {
				startAt = 1;
			}
		} else if (type == MONTH) {
			if (stopAt == -1) {
				stopAt = 12;
			}
			if (startAt == -1 || startAt == ALL_SPEC_INT) {
				startAt = 1;
			}
		} else if (type == DAY_OF_WEEK) {
			if (stopAt == -1) {
				stopAt = 7;
			}
			if (startAt == -1 || startAt == ALL_SPEC_INT) {
				startAt = 1;
			}
		} else if (type == YEAR) {
			if (stopAt == -1) {
				stopAt = 2099;
			}
			if (startAt == -1 || startAt == ALL_SPEC_INT) {
				startAt = 1970;
			}
		}
		int max = -1;
		if (stopAt < startAt) {
			switch (type) {
			case SECOND:
				max = 60;
				break;
			case MINUTE:
				max = 60;
				break;
			case HOUR:
				max = 24;
				break;
			case DAY_OF_MONTH:
				max = 31;
				break;
			case MONTH:
				max = 12;
				break;
			case DAY_OF_WEEK:
				max = 7;
				break;
			case YEAR:
				// throw new IllegalArgumentException(
				// "Start year must be less than stop year");
				throw new IllegalArgumentException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.addToSet.startYear.mustLessThan.stopYear"));
			default:
				// throw new IllegalArgumentException(
				// "Unexpected type encountered");
				throw new IllegalArgumentException(MultiLanguageResourceBundle
						.getInstance().getString(
								"TimeExpressionParser.addToSet.unexpectedType"));
			}
			stopAt += max;
		}
		for (int i = startAt; i <= stopAt; i += incr) {
			if (max == -1) {
				set.add( Integer.valueOf(i));
				continue;
			}
			int i2 = i % max;
			if (i2 == 0
					&& (type == MONTH || type == DAY_OF_WEEK || type == DAY_OF_MONTH)) {
				i2 = max;
			}
			set.add( Integer.valueOf(i2));
		}

	}

	/**
	 * 根据类型获得其值的集合
	 * 
	 * @param type
	 * @return
	 */
	protected TreeSet getSet(int type) {
		switch (type) {
		case SECOND:
			return seconds;
		case MINUTE:
			return minutes;
		case HOUR:
			return hours;
		case DAY_OF_MONTH:
			return daysOfMonth;
		case MONTH:
			return months;
		case DAY_OF_WEEK:
			return daysOfWeek;
		case YEAR:
			return years;
		}
		return null;
	}

	protected ValueSet getValue(int v, String s, int i) {
		char c = s.charAt(i);
		String s1 = String.valueOf(v);
		do {
			if (c < '0' || c > '9') {
				break;
			}
			s1 = s1 + c;
			if (++i >= s.length()) {
				break;
			}
			c = s.charAt(i);
		} while (true);

		ValueSet val = new ValueSet();
		val.pos = i >= s.length() ? i + 1 : i;
		val.value = Integer.parseInt(s1);
		return val;
	}

	protected int getNumericValue(String s, int i) {
		int endOfVal = findNextWhiteSpace(i, s);
		String val = s.substring(i, endOfVal);
		return Integer.parseInt(val);
	}

	protected int getMonthNumber(String s) {
		Integer integer = (Integer) monthMap.get(s);
		if (integer == null) {
			return -1;
		} else {
			return integer.intValue();
		}
	}

	protected int getDayOfWeekNumber(String s) {
		Integer integer = (Integer) dayMap.get(s);
		if (integer == null) {
			return -1;
		} else {
			return integer.intValue();
		}
	}

	protected Date getTimeAfter(Date afterTime) {
		Calendar cl = Calendar.getInstance(getTimeZone());
		afterTime = new Date(afterTime.getTime() + 1000L);
		cl.setTime(afterTime);
		cl.set(14, 0);
		boolean gotOne = false;
		do {
			if (gotOne) {
				break;
			}
			if (cl.get(1) > 2999) {
				return null;
			}
			SortedSet st = null;
			int t = 0;
			int sec = cl.get(13);
			int min = cl.get(12);
			st = seconds.tailSet( Integer.valueOf(sec));
			if (st != null && st.size() != 0) {
				sec = ((Integer) st.first()).intValue();
			} else {
				sec = ((Integer) seconds.first()).intValue();
				min++;
				cl.set(12, min);
			}
			cl.set(13, sec);
			min = cl.get(12);
			int hr = cl.get(11);
			t = -1;
			st = minutes.tailSet( Integer.valueOf(min));
			if (st != null && st.size() != 0) {
				t = min;
				min = ((Integer) st.first()).intValue();
			} else {
				min = ((Integer) minutes.first()).intValue();
				hr++;
			}
			if (min != t) {
				cl.set(13, 0);
				cl.set(12, min);
				setCalendarHour(cl, hr);
				continue;
			}
			cl.set(12, min);
			hr = cl.get(11);
			int day = cl.get(5);
			t = -1;
			st = hours.tailSet( Integer.valueOf(hr));
			if (st != null && st.size() != 0) {
				t = hr;
				hr = ((Integer) st.first()).intValue();
			} else {
				hr = ((Integer) hours.first()).intValue();
				day++;
			}
			if (hr != t) {
				cl.set(13, 0);
				cl.set(12, 0);
				cl.set(5, day);
				setCalendarHour(cl, hr);
				continue;
			}
			cl.set(11, hr);
			day = cl.get(5);
			int mon = cl.get(2) + 1;
			t = -1;
			int tmon = mon;
			boolean dayOfMSpec = !daysOfMonth.contains(NO_SPEC);
			boolean dayOfWSpec = !daysOfWeek.contains(NO_SPEC);
			if (dayOfMSpec && !dayOfWSpec) {
				st = daysOfMonth.tailSet( Integer.valueOf(day));
				if (lastdayOfMonth) {
					if (!nearestWeekday) {
						t = day;
						day = getLastDayOfMonth(mon, cl.get(1));
					} else {
						t = day;
						day = getLastDayOfMonth(mon, cl.get(1));
						Calendar tcal = Calendar.getInstance(getTimeZone());
						tcal.set(13, 0);
						tcal.set(12, 0);
						tcal.set(11, 0);
						tcal.set(5, day);
						tcal.set(2, mon - 1);
						tcal.set(1, cl.get(1));
						int ldom = getLastDayOfMonth(mon, cl.get(1));
						int dow = tcal.get(7);
						if (dow == 7 && day == 1) {
							day += 2;
						} else if (dow == 7) {
							day--;
						} else if (dow == 1 && day == ldom) {
							day -= 2;
						} else if (dow == 1) {
							day++;
						}
						tcal.set(13, sec);
						tcal.set(12, min);
						tcal.set(11, hr);
						tcal.set(5, day);
						tcal.set(2, mon - 1);
						Date nTime = tcal.getTime();
						if (nTime.before(afterTime)) {
							day = 1;
							mon++;
						}
					}
				} else if (nearestWeekday) {
					t = day;
					day = ((Integer) daysOfMonth.first()).intValue();
					Calendar tcal = Calendar.getInstance(getTimeZone());
					tcal.set(13, 0);
					tcal.set(12, 0);
					tcal.set(11, 0);
					tcal.set(5, day);
					tcal.set(2, mon - 1);
					tcal.set(1, cl.get(1));
					int ldom = getLastDayOfMonth(mon, cl.get(1));
					int dow = tcal.get(7);
					if (dow == 7 && day == 1) {
						day += 2;
					} else if (dow == 7) {
						day--;
					} else if (dow == 1 && day == ldom) {
						day -= 2;
					} else if (dow == 1) {
						day++;
					}
					tcal.set(13, sec);
					tcal.set(12, min);
					tcal.set(11, hr);
					tcal.set(5, day);
					tcal.set(2, mon - 1);
					Date nTime = tcal.getTime();
					if (nTime.before(afterTime)) {
						day = ((Integer) daysOfMonth.first()).intValue();
						mon++;
					}
				} else if (st != null && st.size() != 0) {
					t = day;
					day = ((Integer) st.first()).intValue();
					int lastDay = getLastDayOfMonth(mon, cl.get(1));
					if (day > lastDay) {
						day = ((Integer) daysOfMonth.first()).intValue();
						mon++;
					}
				} else {
					day = ((Integer) daysOfMonth.first()).intValue();
					mon++;
				}
				if (day != t || mon != tmon) {
					cl.set(13, 0);
					cl.set(12, 0);
					cl.set(11, 0);
					cl.set(5, day);
					cl.set(2, mon - 1);
					continue;
				}
			} else if (dayOfWSpec && !dayOfMSpec) {
				if (lastdayOfWeek) {
					int dow = ((Integer) daysOfWeek.first()).intValue();
					int cDow = cl.get(7);
					int daysToAdd = 0;
					if (cDow < dow) {
						daysToAdd = dow - cDow;
					}
					if (cDow > dow) {
						daysToAdd = dow + (7 - cDow);
					}
					int lDay = getLastDayOfMonth(mon, cl.get(1));
					if (day + daysToAdd > lDay) {
						cl.set(13, 0);
						cl.set(12, 0);
						cl.set(11, 0);
						cl.set(5, 1);
						cl.set(2, mon);
						continue;
					}
					while (day + daysToAdd + 7 <= lDay) {
						daysToAdd += 7;
					}
					day += daysToAdd;
					if (daysToAdd > 0) {
						cl.set(13, 0);
						cl.set(12, 0);
						cl.set(11, 0);
						cl.set(5, day);
						cl.set(2, mon - 1);
						continue;
					}
				} else if (nthdayOfWeek != 0) {
					int dow = ((Integer) daysOfWeek.first()).intValue();
					int cDow = cl.get(7);
					int daysToAdd = 0;
					if (cDow < dow) {
						daysToAdd = dow - cDow;
					} else if (cDow > dow) {
						daysToAdd = dow + (7 - cDow);
					}
					boolean dayShifted = false;
					if (daysToAdd > 0) {
						dayShifted = true;
					}
					day += daysToAdd;
					int weekOfMonth = day / 7;
					if (day % 7 > 0) {
						weekOfMonth++;
					}
					daysToAdd = (nthdayOfWeek - weekOfMonth) * 7;
					day += daysToAdd;
					if (daysToAdd < 0
							|| day > getLastDayOfMonth(mon, cl.get(1))) {
						cl.set(13, 0);
						cl.set(12, 0);
						cl.set(11, 0);
						cl.set(5, 1);
						cl.set(2, mon);
						continue;
					}
					if (daysToAdd > 0 || dayShifted) {
						cl.set(13, 0);
						cl.set(12, 0);
						cl.set(11, 0);
						cl.set(5, day);
						cl.set(2, mon - 1);
						continue;
					}
				} else {
					int cDow = cl.get(7);
					int dow = ((Integer) daysOfWeek.first()).intValue();
					st = daysOfWeek.tailSet( Integer.valueOf(cDow));
					if (st != null && st.size() > 0) {
						dow = ((Integer) st.first()).intValue();
					}
					int daysToAdd = 0;
					if (cDow < dow) {
						daysToAdd = dow - cDow;
					}
					if (cDow > dow) {
						daysToAdd = dow + (7 - cDow);
					}
					int lDay = getLastDayOfMonth(mon, cl.get(1));
					if (day + daysToAdd > lDay) {
						cl.set(13, 0);
						cl.set(12, 0);
						cl.set(11, 0);
						cl.set(5, 1);
						cl.set(2, mon);
						continue;
					}
					if (daysToAdd > 0) {
						cl.set(13, 0);
						cl.set(12, 0);
						cl.set(11, 0);
						cl.set(5, day + daysToAdd);
						cl.set(2, mon - 1);
						continue;
					}
				}
			} else {
				// throw new UnsupportedOperationException(
				// "Support for specifying both a day-of-week AND a day-of-month parameter is not implemented.");
				throw new UnsupportedOperationException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"TimeExpressionParser.getTimeAfter.operation.unsupport"));
			}
			cl.set(5, day);
			mon = cl.get(2) + 1;
			int year = cl.get(1);
			t = -1;
			if (year > 2099) {
				return null;
			}
			st = months.tailSet( Integer.valueOf(mon));
			if (st != null && st.size() != 0) {
				t = mon;
				mon = ((Integer) st.first()).intValue();
			} else {
				mon = ((Integer) months.first()).intValue();
				year++;
			}
			if (mon != t) {
				cl.set(13, 0);
				cl.set(12, 0);
				cl.set(11, 0);
				cl.set(5, 1);
				cl.set(2, mon - 1);
				cl.set(1, year);
			} else {
				cl.set(2, mon - 1);
				year = cl.get(1);
				t = -1;
				st = years.tailSet( Integer.valueOf(year));
				if (st != null && st.size() != 0) {
					t = year;
					year = ((Integer) st.first()).intValue();
				} else {
					return null;
				}
				if (year != t) {
					cl.set(13, 0);
					cl.set(12, 0);
					cl.set(11, 0);
					cl.set(5, 1);
					cl.set(2, 0);
					cl.set(1, year);
				} else {
					cl.set(1, year);
					gotOne = true;
				}
			}
		} while (true);

		return cl.getTime();
	}

	protected void setCalendarHour(Calendar cal, int hour) {
		cal.set(11, hour);
		if (cal.get(11) != hour && hour != 24) {
			cal.set(11, hour + 1);
		}
	}

	/**
	 * 判断是否是闰年
	 * 
	 * @param year
	 * @return
	 */
	protected boolean isLeapYear(int year) {
		return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
	}

	/**
	 * 获得每月的最后一天的日期，31,29,30
	 * 
	 * @param monthNum
	 * @param year
	 * @return
	 */
	protected int getLastDayOfMonth(int monthNum, int year) {
		switch (monthNum) {
		case 1:
			return 31;
		case 2:
			return isLeapYear(year) ? 29 : 28;
		case 3:
			return 31;
		case 4:
			return 30;
		case 5:
			return 31;
		case 6:
			return 30;
		case 7:
			return 31;
		case 8:
			return 31;
		case 9:
			return 30;
		case 10:
			return 31;
		case 11:
			return 30;
		case 12:
			return 31;
		default:
			// throw new IllegalArgumentException("Illegal month number: "
			// + monthNum);
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"TimeExpressionParser.getLastDayOfMonth.month.illegal",
									new String[] { "" + monthNum }));
		}
	}

	public Object clone() {
		TimeExpressionParser copy = null;
		try {
			copy = new TimeExpressionParser(getExpression());
			copy.setTimeZone(getTimeZone());
		} catch (ParseException ex) {
			// throw new IncompatibleClassChangeError("Not Cloneable.");
			throw new IncompatibleClassChangeError(MultiLanguageResourceBundle
					.getInstance().getString(
							"TimeExpressionParser.clone.notCloneable"));
		}
		return copy;
	}

	private class ValueSet {
		public int value;
		public int pos;
	}

}
