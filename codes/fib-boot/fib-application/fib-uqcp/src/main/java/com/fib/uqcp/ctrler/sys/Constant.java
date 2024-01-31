package com.fib.uqcp.ctrler.sys;

public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;
	/** 数据权限过滤 */
	public static final String SQL_FILTER = "sql_filter";

	/**
	 * 菜单类型
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @date 2024-01-08 15:53:22
	 */
	public enum MenuType {
		/**
		 * 目录
		 */
		CATALOG(0),
		/**
		 * 菜单
		 */
		MENU(1),
		/**
		 * 按钮
		 */
		BUTTON(2);

		private int value;

		MenuType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
