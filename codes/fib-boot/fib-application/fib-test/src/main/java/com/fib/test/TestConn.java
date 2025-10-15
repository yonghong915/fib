package com.fib.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestConn {
	public static void main(String[] args) {
		String url = "jdbc:mysql://192.168.56.11:3306/uppdb";
		String user = "uppapp";
		String psd = "uppapp1234";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// 获得连接
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, user, psd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 执行SQL语句
		String sql = "select * from resource_lock where id = ?";
		// statement 用于执行静态 SQL 语句并返回其生成的结果的对象
		try {
			PreparedStatement pst = connection.prepareStatement(sql);
			pst.setInt(1, 1);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				System.out.println("11" + "   " + rs.getString(2));

			}
			// 关闭资源
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
