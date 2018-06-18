package com.pms.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.dbutils.ResultSetHandler;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * @author Taowd
 * ��        �ܣ����ݿ����ӵĹ�����
 * ��дʱ�䣺2017-5-4-����12:42:29
 */
public class DbUtils {

	/**
	 * ��ȡһ��ResultSetHandler���󷵻ؽ����ΪResultSet
	 */
	public static final ResultSetHandler<ResultSet> GetRSH = new ResultSetHandler<ResultSet>() {
		@Override
		public ResultSet handle(ResultSet rs) throws SQLException {
			return rs;
		}
	};
	/**
	 * ʹ��Ĭ�����û�ȡһ�����ݿ����Ӷ���
	 */
	private static ComboPooledDataSource dataSource = new ComboPooledDataSource(
			"mysql-config");

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ȡһ�����ݿ����Ӷ���
	 * �������ڣ�2017-5-4-����12:44:15
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ��ر����ݿ�����
	 * �������ڣ�2017-5-4-����12:52:36
	 * @param conn  Connection
	 * @throws SQLException
	 */
	public static void CloseConn(Connection conn) throws SQLException {
		if (conn != null) {
			conn.close();
		}
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����һ�����ӳض���
	 * �������ڣ�2017-5-4-����12:48:36
	 * @return
	 */

	public static DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����һ��UUID
	 * �������ڣ�2017-4-19-����8:20:01
	 * @return
	 * @throws Exception 
	 */
	public static String GetUUID() throws Exception {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ��������ݿ�����
	 * �������ڣ�2017-5-4-����12:53:53
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			DbUtils.getConnection();
			System.out.println("���ݿ����ӳɹ���");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
