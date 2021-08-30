/**
 * 北京长信通信息技术有限公司
 * 2008-8-27 上午09:17:38
 */
package com.fib.msgconverter.commgateway.config;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;

/**
 * 通道配置
 * 
 * @author 刘恭亮
 * 
 */
public class ChannelMainConfig {
	/**
	 * 数据库专用：通道唯一索引
	 */
	private String databaseChannelId;
	/**
	 * 通道id
	 */
	private String id;

	/**
	 * 通道名
	 */
	private String name;

	/**
	 * 是否启动
	 */
	private boolean startup;

	/**
	 * 部署目录名
	 */
	private String deploy;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the startup
	 */
	public boolean isStartup() {
		return startup;
	}

	/**
	 * @param startup
	 *            the startup to set
	 */
	public void setStartup(boolean startup) {
		this.startup = startup;
	}

	/**
	 * @return the deploy
	 */
	public String getDeploy() {
		return deploy;
	}

	/**
	 * @param deploy
	 *            the deploy to set
	 */
	public void setDeploy(String deploy) {
		this.deploy = deploy;
	}

	public String getDatabaseChannelId() {
		return databaseChannelId;
	}

	public void setDatabaseChannelId(String databaseChannelId) {
		this.databaseChannelId = databaseChannelId;
	}

	public String toString() {

		// StringBuffer buf = new StringBuffer(128);
		// buf.append("Channel id[");
		// buf.append(id);
		// buf.append("] name[");
		// buf.append(name);
		// buf.append("] startup[");
		// buf.append(startup);
		// buf.append("] deploy[");
		// buf.append(deploy);
		// buf.append("]");
		// return buf.toString();
		return MultiLanguageResourceBundle.getInstance().getString(
				"ChannelMainConfig.toString",
				new String[] { id, name, "" + startup, deploy });

	}

}
