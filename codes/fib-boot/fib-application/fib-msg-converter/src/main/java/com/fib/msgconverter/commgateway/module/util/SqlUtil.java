package com.fib.msgconverter.commgateway.module.util;

import java.util.Map;

import com.fib.msgconverter.commgateway.module.constants.Constants;

public class SqlUtil {
	public static String combineSearchSecurityWhereClause(Map map) {
		boolean isAppend = false;
		StringBuffer buf = new StringBuffer();
		String tmp = (String) map
				.get(Constants.SECURITY_RULE_BUSS_RELATION_ORG_ID);
		if (null != tmp) {
			buf.append("org_id='");
			buf.append(tmp);
			buf.append("'");

			isAppend = true;
		}

		tmp = (String) map
				.get(Constants.SECURITY_RULE_BUSS_RELATION_PRODUCT_ID);
		if (null != tmp) {
			if (isAppend) {
				buf.append(" and ");
			}
			buf.append("product_id='");
			buf.append(tmp);
			buf.append("'");
		}

		if (0 == buf.capacity()) {
			tmp = (String) map
					.get(Constants.SECURITY_RULE_BUSS_RELATION_RULE_ID);
			if (null != tmp) {
				buf.append("rule_id='");
				buf.append(tmp);
				buf.append("'");
			}
		}

		if (0 == buf.capacity()) {
			return null;
		} else {
			return buf.toString();
		}
	}

	public static String combineSearchCommLogWhereClause(Map map) {
		boolean isAppend = false;
		StringBuffer buf = new StringBuffer();
		String tmp = (String) map.get(Constants.COMM_LOG_PARAMS_ID);
		if (null != tmp) {
			buf.append("id='");
			buf.append(tmp);
			buf.append("'");

			return buf.toString();
		}

		tmp = (String) map.get(Constants.COMM_LOG_PARAMS_STATE);
		if (null != tmp) {
			buf.append(combineCondition("state", tmp, isAppend));
			isAppend = true;
		}

		tmp = (String) map.get(Constants.COMM_LOG_PARAMS_START_DATE);
		if (null != tmp) {
			buf.append(combineCondition("start_date", tmp, isAppend));
			isAppend = true;
		}

		tmp = (String) map.get(Constants.COMM_LOG_PARAMS_END_DATE);
		if (null != tmp) {
			buf.append(combineCondition("end_date", tmp, isAppend));
			isAppend = true;
		}

		tmp = (String) map.get(Constants.COMM_LOG_PARAMS_TYPE);
		if (null != tmp) {
			buf.append(combineCondition("type", tmp, isAppend));
			isAppend = true;
		}

		tmp = (String) map.get(Constants.COMM_LOG_PARAMS_ERR_TYP);
		if (null != tmp) {
			buf.append(combineCondition("error_type", tmp, isAppend));
			isAppend = true;
		}

		tmp = (String) map.get(Constants.COMM_LOG_PARAMS_GW_ID);
		if (null != tmp) {
			buf.append(combineCondition("gateway_id", tmp, isAppend));
			isAppend = true;
		}

		tmp = (String) map.get(Constants.COMM_LOG_PARAMS_START_TIME);
		if (null != tmp) {
			buf.append(combineCondition("start_time", tmp, isAppend));
			isAppend = true;
		}

		tmp = (String) map.get(Constants.COMM_LOG_PARAMS_END_TIME);
		if (null != tmp) {
			buf.append(combineCondition("end_time", tmp, isAppend));
			isAppend = true;
		}

		tmp = (String) map.get(Constants.COMM_LOG_PARAMS_SRC_CHANNEL_ID);
		if (null != tmp) {
			buf.append(combineCondition("source_channel_id", tmp, isAppend));
			isAppend = true;
		}

		tmp = (String) map.get(Constants.COMM_LOG_PARAMS_DEST_CHANNEL_ID);
		if (null != tmp) {
			buf.append(combineCondition("dest_channel_id", tmp, isAppend));
			isAppend = true;
		}

		tmp = (String) map.get(Constants.COMM_LOG_PARAMS_END_DATE_FROM);
		if (null != tmp) {
			if (isAppend) {
				buf.append(" and ");
			} else {
				isAppend = true;
			}
			buf.append("end_date>='");
			buf.append(tmp);
			buf.append("'");
		}

		tmp = (String) map.get(Constants.COMM_LOG_PARAMS_END_DATE_TO);
		if (null != tmp) {
			if (isAppend) {
				buf.append(" and ");
			} else {
				isAppend = true;
			}
			buf.append("end_date<='");
			buf.append(tmp);
			buf.append("'");
		}

		tmp = (String) map.get(Constants.COMM_LOG_PARAMS_START_DATE_FROM);
		if (null != tmp) {
			if (isAppend) {
				buf.append(" and ");
			} else {
				isAppend = true;
			}
			buf.append("start_date>='");
			buf.append(tmp);
			buf.append("'");
		}

		tmp = (String) map.get(Constants.COMM_LOG_PARAMS_START_DATE_TO);
		if (null != tmp) {
			if (isAppend) {
				buf.append(" and ");
			} else {
				isAppend = true;
			}
			buf.append("start_date<='");
			buf.append(tmp);
			buf.append("'");
		}

		if (0 == buf.capacity()) {
			return null;
		} else {
			return buf.toString();
		}
	}

	private static String combineCondition(String columnName,
			String columnValue, boolean isAppend) {
		StringBuffer buf = new StringBuffer();
		if (isAppend) {
			buf.append(" and ");
		}
		buf.append(columnName);
		buf.append("='");
		buf.append(columnValue);
		buf.append("'");

		return buf.toString();
	}
}
