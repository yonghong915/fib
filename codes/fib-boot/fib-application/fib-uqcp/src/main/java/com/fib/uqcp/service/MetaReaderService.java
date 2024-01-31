package com.fib.uqcp.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.fib.uqcp.topcheer.meta.bo.AppConfig;
import com.fib.uqcp.topcheer.meta.bo.SqlConfig;

import cn.hutool.core.lang.Assert;
import jakarta.annotation.PostConstruct;

//@Service
public class MetaReaderService implements ApplicationContextAware {
	@Autowired
	AppConfig appConfig;
	
	private SqlConfig sqlConfig;
	private Logger logger = LoggerFactory.getLogger(MetaReaderService.class);

	private ApplicationContext applicationContext;
	private Pattern lineBreakPattern = Pattern.compile("\\r\\n|\\n\\r");
	private String lineReplace = "\\\\n";

	// 启动运行
	@PostConstruct
	public void init() {
		logger.info("meta reader begin.. .");
		validConfig();
		/*
		 * 文件名称 PWMX_TABLE_COLUMN_[DATE].txt PWMX_VIEW_COLUMN_[DATE].txt
		 * PWMX_VIEW_[DATE].txt PWMX_TABLE_[DATE].txt
		 * 
		 */
		String yyyymmddTxt = new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt";
		String sysName = appConfig.getSysName();
		// 写入文件
		execSqlAndWriteFile(sqlConfig.getTableColumnSql(), sysName + "_TABLE_COLUMN_" + yyyymmddTxt);
		execSqlAndWriteFile(sqlConfig.getViewColumnSql(), sysName + "_VIEW_COLUMN_" + yyyymmddTxt);
		execSqlAndWriteFile(sqlConfig.getViewSql(), sysName + "_VIEW_" + yyyymmddTxt);
		execSqlAndWriteFile(sqlConfig.getTableSql(), sysName + "_TABLE_" + yyyymmddTxt);

		// 生成信号文件
		createFinishFile(sysName);

		logger.info("meta reader end...");
		System.exit(0);
	}

	/**
	 * 生成信号文件
	 * 
	 * @param sysName
	 */
	private void createFinishFile(String sysName) {
		String yyyymmdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String filename = yyyymmdd + "_" + sysName + "_META_START.finish";
		File fileOver = new File(appConfig.getFilePath(), filename);
		try (FileOutputStream fos = new FileOutputStream(fileOver)) {
			logger.info("meta reader execSql and write file: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		}

		File file = new File(appConfig.getFilePath(), "_TABLE_" + yyyymmdd + ".txt");
		if (file.exists()) {
			filename = yyyymmdd + "_" + sysName + "_TABLE_START.finish";
			fileOver = new File(appConfig.getFilePath(), filename);
			try (FileOutputStream fos = new FileOutputStream(fileOver)) {
				logger.info("meta reader execSql and write file: " + filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 配置验证
	 */
	private void validConfig() {
		logger.info("readed appConfig: " + appConfig);
		Assert.notNull(appConfig.getJdbcUrl(), "jdbcUrl");
		Assert.notNull(appConfig.getUsername(), "username");
		Assert.notNull(appConfig.getPassword(), "password");
		Assert.notNull(appConfig.getDataBaseType(), "databaseType");

		Assert.notNull(appConfig.getSysName(), "sysName");
		Assert.notNull(appConfig.getFilePath(), "filePath");

		File path = new File(appConfig.getFilePath());
		if (path.isFile() || !path.exists()) {
			path.mkdirs();
		}

		String dataBaseType = appConfig.getDataBaseType();
		sqlConfig = applicationContext.getBean(dataBaseType, SqlConfig.class);

		if (null == sqlConfig) {
			logger.error(dataBaseType + "'s sqlConfig must be setted.");
		}
	}

	/**
	 * 执行sq1语句并写入文件
	 * 
	 * @param sql
	 * @param fileName
	 */
	private void execSqlAndWriteFile(String sql, String fileName) {
		if (sql == null || "".equals(sql.trim())) {
			logger.warn("sql is not exist for fileName:" + fileName);
			return;
		}
		logger.info("meta reader execSql and write file: " + fileName);

		try (BufferedWriter bufw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(appConfig.getFilePath(), fileName)), appConfig.getCharsetName()));) {
			String databases = appConfig.getDatabases();
			if (databases == null || "".equals(databases.trim())) {
				execSqlAndWrite(appConfig.getJdbcUrl(), "", appConfig.getUsername(), appConfig.getPassword(), sql, bufw);
			} else {
				String[] dbs = databases.split(",");
				for (String db : dbs) {
					execSqlAndWrite(appConfig.getJdbcUrl(), db, appConfig.getUsername(), appConfig.getPassword(), sql, bufw);
				}
			}
		} catch (Exception e) {
			logger.error("file write error: ", e.getMessage());
		}
	}

	private void execSqlAndWrite(String jdbcUrl, String db, String username, String password, String sql, BufferedWriter bufw) {
		try (Connection conn = DriverManager.getConnection(jdbcUrl + db, username, password);) {
			conn.setReadOnly(true);

			try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rst = pstmt.executeQuery();) {
				int columnCount = rst.getMetaData().getColumnCount();
				while (rst.next()) {
					for (int i = 1; i <= columnCount; i++) {
						String value = rst.getString(i);
						String singleLine = singleLine(value);
						bufw.write(singleLine);
						bufw.write(appConfig.getSeperator());
					}
					bufw.newLine();
				}
				bufw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			logErrAndExit("database error: " + e.getMessage());
		}
	}

	private String singleLine(String value) {
		if (null == value) {
			return "";
		}
		return lineBreakPattern.matcher(value).replaceAll(lineReplace);
	}

	public void logErrAndExit(String msg) {
		logger.error(msg);
		System.exit(-1);

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}