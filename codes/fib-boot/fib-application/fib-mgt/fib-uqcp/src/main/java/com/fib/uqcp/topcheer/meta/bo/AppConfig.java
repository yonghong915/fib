package com.fib.uqcp.topcheer.meta.bo;


/**
 * 应用程序配置
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-05 15:14:45
 */
public class AppConfig {
	private String jdbcUrl;
	private String username;
	private String password;
	private String databases;
	private String dataBaseType;
	private String seperator = "|%|";
	private String version = "v1.0";

	private String sysName;
	private String filePath;
	private String charsetName = "UTF-8";

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDatabases() {
		return databases;
	}

	public void setDatabases(String databases) {
		this.databases = databases;
	}

	public String getDataBaseType() {
		return dataBaseType;
	}

	public void setDataBaseType(String dataBaseType) {
		this.dataBaseType = dataBaseType;
	}

	public String getSeperator() {
		return seperator;
	}

	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getCharsetName() {
		return charsetName;
	}

	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}
}
