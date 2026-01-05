package com.fib.uqcp.batch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.StrUtil;

public class BatchImplUtil {
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String DEFAULT_DATA_FILE_SEPARATOR = "!^";
	@Autowired(required = false)
	private static JdbcTemplate jdbcTemplate;

	/**
	 * 分隔符处理
	 * 
	 * @param str
	 * @return
	 */
	public static String splitTools(String str) {
		String splitCode = "";
		if (str.length() > 1) {
			char[] strChars = str.toCharArray();
			for (int i = 0; i < strChars.length; i++) {
				splitCode = "\\" + strChars[i];
			}
		} else {
			splitCode = "\\" + str;
		}
		return splitCode;
	}

	public static void batchImplData2Table(String tableName, String[] colsName, String dataFilePath, String charsetName, String dataSeparater) {
		if (null == colsName || colsName.length == 0) {
			return;
		}

		if (StrUtil.isEmpty(tableName)) {
			return;
		}

		List<Map<String, Object>> userTabColsRet = getTableCols(tableName.trim());
		if (CollUtil.isEmpty(userTabColsRet)) {
			return;
		}

		if (StrUtil.isEmpty(charsetName)) {
			charsetName = DEFAULT_ENCODING;
		}

		if (StrUtil.isEmpty(dataSeparater)) {
			charsetName = DEFAULT_DATA_FILE_SEPARATOR;
		}
		String dataSql = buildSql(tableName, colsName);
		List<Map<String, Object>> rsMap = jdbcTemplate.queryForList(dataSql);

		jdbcTemplate.batchUpdate(dataSql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement pst, int i) throws SQLException {
				pst.setString(i, dataSql);

			}

			@Override
			public int getBatchSize() {
				return 10;
			}
		});

		
		try (BufferedReader bw = new BufferedReader(new InputStreamReader(new FileInputStream(dataFilePath), charsetName))) {
			String fileLine = null;
			while ((fileLine = bw.readLine()) != null) {
				String[] dataStr = fileLine.split(dataSeparater, -1);
				int dataLen = dataStr.length;
				dataLen = Math.min(colsName.length, dataLen);
				for (int i = 0; i < dataLen; i++) {
					String data = dataStr[i];
					data = (null == data) ? "" : data;
					String colName = colsName[i];
					String tblDataType = "";
					for (Map<String, Object> ret : userTabColsRet) {
						String tblColName = (String) ret.get("COLUMN_NAME");
						if (colName.trim().toUpperCase().equals(tblColName)) {
							tblDataType = (String) ret.get("DATA_TYPE");
							break;
						}
					}

					if ("VARCHAR".equals(tblDataType) || "VARCHAR2".equals(tblDataType) || "CHAR".equals(tblDataType)) {

					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String buildSql(String tableName, String[] colsName) {
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("insert into ").append(tableName).append("( ");
		int colLen = colsName.length;
		for (int i = 0; i < colLen; i++) {
			String colName = colsName[i];
			if (i == colLen - 1) {
				dataSql.append(colName);
			} else {
				dataSql.append(colName).append(",");
			}
		}

		dataSql.append(") values (");
		for (int i = 0; i < colLen; i++) {
			if (i == colLen - 1) {
				dataSql.append("?");
			} else {
				dataSql.append("?,");
			}
		}
		dataSql.append(" )");
		return dataSql.toString();
	}

	private static List<Map<String, Object>> getTableCols(String trim) {
		String sql = "select table_name,column_name,data_type from user_tab_cols where table_name = ? ";

		ClassLoaderUtil.getClassLoader();
		return null;
	}
}
