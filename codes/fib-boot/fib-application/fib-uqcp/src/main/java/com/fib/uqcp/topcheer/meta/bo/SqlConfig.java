package com.fib.uqcp.topcheer.meta.bo;

public class SqlConfig {
	private String tableSql;
	private String tableColumnSql;
	private String viewSql;
	private String viewColumnSql;

	public String getTableSql() {
		return tableSql;
	}

	public void setTableSql(String tableSql) {
		this.tableSql = tableSql;
	}

	public String getTableColumnSql() {
		return tableColumnSql;
	}

	public void setTableColumnSql(String tableColumnSql) {
		this.tableColumnSql = tableColumnSql;
	}

	public String getViewSql() {
		return viewSql;
	}

	public void setViewSql(String viewSql) {
		this.viewSql = viewSql;
	}

	public String getViewColumnSql() {
		return viewColumnSql;
	}

	public void setViewColumnSql(String viewColumnSql) {
		this.viewColumnSql = viewColumnSql;
	}

	@Override
	public String toString() {
		return "SqlConfig [tableSql=" + tableSql + ",tableColumnSql=" + tableColumnSql + ",viewSql=" + viewSql + ",viewColumnSql=" + viewColumnSql
				+ "]";
	}

}
