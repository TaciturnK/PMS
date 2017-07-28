package com.pms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.pms.model.Administrator;
import com.pms.model.PageBean;
import com.pms.util.DateUtil;
import com.pms.util.DbUtils;
import com.pms.util.Log4jHelper;
import com.pms.util.StringUtil;

/**
 * 
 * @author Taowd
 * ��        �ܣ�����Ա������
 * ��дʱ�䣺2017-4-25-����2:35:08
 */
public class AdministratorDao {

	/**
	 * ���ܣ�����Ա��¼��֤
	 * @param con
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Administrator login(Administrator admin) throws Exception {

		// ����QueryRunner����Ҫ�ṩ���ݿ����ӳض���
		QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
		// ����sqlģ��
		String sql = "SELECT * from pms.t_administrator WHERE t_administrator.ADMIN_NO = ? and t_administrator.ADMIN_PWD =? and t_administrator.Ext3=? ";
		// ��������
		Object[] params = { admin.getAdmin_no(), admin.getAdmin_pwd(),
				admin.getExt3() };
		// ִ��Sql����ȡ����ֵ
		return queryRun.query(sql, new BeanHandler<Administrator>(
				Administrator.class), params);

	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ȡ����Ա��Ϣ
	 * �������ڣ�2017-4-11-����9:54:43
	 * @param con
	 * @param pageBean
	 * @param adminInfo
	 * @return
	 * @throws SQLException 
	 */
	public ResultSet GetAdminInfo(Connection con, PageBean pageBean,
			Administrator adminInfo) throws SQLException {
		Log4jHelper.info("��ѯ������" + adminInfo.toString());
		StringBuffer sb = new StringBuffer(
				"SELECT ADMIN_ID,ADMIN_NO, ADMIN_PWD,ADMIN_NAME,ADMIN_PHONE,Ext1 ,Ext2,Ext3 as roleCode,(case WHEN Ext3='admin'then '����Ա' WHEN Ext3='superAdmin'then '��������Ա' END ) as roleName from pms.t_administrator  ");
		if (adminInfo != null && StringUtil.isNotEmpty(adminInfo.getAdmin_no())) {
			sb.append(" and t_administrator.ADMIN_NO like '%"
					+ adminInfo.getAdmin_no() + "%'");
		}
		if (adminInfo != null
				&& StringUtil.isNotEmpty(adminInfo.getAdmin_name())) {
			sb.append(" and t_administrator.ADMIN_NAME like '%"
					+ adminInfo.getAdmin_name() + "%'");
		}
		if (adminInfo != null
				&& StringUtil.isNotEmpty(adminInfo.getAdmin_phone())) {
			sb.append(" and t_administrator.ADMIN_PHONE like '%"
					+ adminInfo.getAdmin_phone() + "%'");
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString()
				.replaceFirst("and", "where"));
		Log4jHelper.info("��ȡ����Ա��Ϣ��" + pstmt.toString());
		return pstmt.executeQuery();
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ȡ����Ա����
	 * �������ڣ�2017-4-11-����9:55:02
	 * @param con
	 * @param adminInfo
	 * @return
	 * @throws SQLException 
	 */
	public int GetAdminCount(Administrator adminInfo)
			throws SQLException {
		// ����QueryRunner����Ҫ�ṩ���ݿ����ӳض���
		QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
		StringBuffer sql = new StringBuffer(
				"SELECT count(*) as total from pms.t_administrator  ");
		if (adminInfo != null && StringUtil.isNotEmpty(adminInfo.getAdmin_no())) {
			sql.append(" and t_administrator.ADMIN_NO like '%"
					+ adminInfo.getAdmin_no() + "%'");
		}
		if (adminInfo != null
				&& StringUtil.isNotEmpty(adminInfo.getAdmin_name())) {
			sql.append(" and t_administrator.ADMIN_NAME like '%"
					+ adminInfo.getAdmin_name() + "%'");
		}
		if (adminInfo != null
				&& StringUtil.isNotEmpty(adminInfo.getAdmin_phone())) {
			sql.append(" and t_administrator.ADMIN_PHONE like '%"
					+ adminInfo.getAdmin_phone() + "%'");
		}
		// ��ѯ-���ص��е��н��
		Long number = (Long) queryRun.query(
				sql.toString().replaceFirst("and", "where"),
				new ScalarHandler());
		return number.intValue();

	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ��޸Ĺ���Ա��Ϣ
	 * �������ڣ�2017-4-12-����1:58:39
	 * @param con
	 * @param adminInfo
	 * @return
	 * @throws Exception 
	 */
	public int AdminModify( Administrator adminInfo)
			throws Exception {

		if (adminInfo != null
				&& StringUtil.isNotEmpty(adminInfo.getAdmin_pwd())) {
			QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
			String sql = "UPDATE pms.t_administrator SET t_administrator.ADMIN_NO =?,ADMIN_PWD =?, t_administrator.ADMIN_NAME=? , t_administrator.ADMIN_PHONE=? , t_administrator.Ext1 = ? , t_administrator.Ext2=?  WHERE t_administrator.ADMIN_ID=? ";
			Object[] params = { adminInfo.getAdmin_no(),
					adminInfo.getAdmin_pwd(), adminInfo.getAdmin_name(),
					adminInfo.getAdmin_phone(), adminInfo.getExt1(),
					DateUtil.getCurrentDateStr(), adminInfo.getAdmin_id() };
			return queryRun.update(sql, params);
		} else {

			QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
			String sql = "UPDATE pms.t_administrator SET t_administrator.ADMIN_NO =?, t_administrator.ADMIN_NAME=? , t_administrator.ADMIN_PHONE=? , t_administrator.Ext1 = ? , t_administrator.Ext2=?  WHERE t_administrator.ADMIN_ID=? ";
			Object[] params = { adminInfo.getAdmin_no(),
					adminInfo.getAdmin_name(), adminInfo.getAdmin_phone(),
					adminInfo.getExt1(), DateUtil.getCurrentDateStr(),
					adminInfo.getAdmin_id() };

			return queryRun.update(sql, params);
		}
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����Ա�޸ĸ�����Ϣ�������ڴ��޸�����
	 * �������ڣ�2017-4-27-����2:20:33
	 * @param con
	 * @param adminInfo
	 * @return
	 * @throws Exception
	 */
	public int AdminPersionModify(Administrator adminInfo)
			throws Exception {
		QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
		String sql = "UPDATE pms.t_administrator SET t_administrator.ADMIN_NO =?, t_administrator.ADMIN_NAME=? , t_administrator.ADMIN_PHONE=? , t_administrator.Ext1 = ? , t_administrator.Ext2=?  WHERE t_administrator.ADMIN_ID=? ";
		Object[] params = { adminInfo.getAdmin_no(), adminInfo.getAdmin_name(),
				adminInfo.getAdmin_phone(), adminInfo.getExt1(),
				DateUtil.getCurrentDateStr(), adminInfo.getAdmin_id() };

		return queryRun.update(sql, params);
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����Ա�޸�����
	 * �������ڣ�2017-4-19-����1:43:04
	 * @param con
	 * @param adminInfo
	 * @return
	 * @throws Exception
	 */
	public int AdminModifyPasswd(Administrator adminInfo)
			throws Exception {
		QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
		String sql = "UPDATE pms.t_administrator SET t_administrator.ADMIN_PWD =? WHERE t_administrator.ADMIN_ID=? ";
		Object[] params = { adminInfo.getAdmin_pwd(), adminInfo.getAdmin_id() };
		return queryRun.update(sql, params);
	}

	/**
	 * 
	 * Author:Taowd 
	 * ���ܣ�������Աadmin_no�Ƿ��Ѿ�����
	 * �������ڣ�2017-4-18-����12:59:55
	 * @param con
	 * @param aDMIN_NO
	 * @return
	 * @throws SQLException 
	 */
	public boolean IsExistence(String admin_no)
			throws SQLException {
		QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
		String sql = "SELECT * FROM pms.t_administrator WHERE admin_no =? ";
		Object[] params = { admin_no };
		// ��ѯ-���ؽ��ΪJavaBean���󣬲�ѯ����ʱ����Ϊnull
		Administrator admin = queryRun.query(sql,
				new BeanHandler<Administrator>(Administrator.class), params);
		if (admin != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ����ӹ���Ա
	 * �������ڣ�2017-4-18-����1:04:06
	 * @param con
	 * @param adminBean
	 * @return
	 * @throws Exception 
	 */
	public int AdminAdd(Administrator adminBean)
			throws Exception {

		QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
		String sql = "INSERT INTO pms.t_administrator VALUES(?,?,?,?,?,?,?,?) ";
		Object[] params = {
				DateUtil.formatDate(new Date(), "yyyyMMdd")
						+ adminBean.getAdmin_no(), adminBean.getAdmin_no(),
				adminBean.getAdmin_pwd(), adminBean.getAdmin_name(),
				adminBean.getAdmin_phone(), adminBean.getExt1(),
				DateUtil.getCurrentDateStr(), "admin" };
		return queryRun.update(sql, params);
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�ɾ������Ա��Ϣ
	 * �������ڣ�2017-4-19-����1:20:28
	 * @param con
	 * @param delIds
	 * @return
	 * @throws SQLException 
	 */
	public int AdminInfoDelete(String delIds)
			throws SQLException {
		int SussessFlag = 0;
		QueryRunner qr = new QueryRunner(DbUtils.getDataSource());
		String sql = "SELECT count(*) FROM pms.t_notice WHERE NOT_AUTHOR = ? ";
		Object[] params = { delIds };
		Long countNum = (Long) qr.query(sql, new ScalarHandler(), params);

		if (countNum.intValue() > 0) {
			// ��ɾ���ù���Ա���ӵĹ�����Ϣ
			QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
			String sql1 = "DELETE FROM pms.t_notice WHERE t_notice.NOT_AUTHOR IN ("
					+ delIds + ")";

			SussessFlag = queryRun.update(sql1);
			if (SussessFlag > 0) {
				// ɾ������Ա
				String sql2 = "DELETE FROM pms.t_administrator WHERE t_administrator.ADMIN_ID IN ("
						+ delIds + ")";
				SussessFlag = queryRun.update(sql2);
			}

		} else {
			// ɾ������Ա
			QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
			String sql4 = "DELETE FROM pms.t_administrator WHERE t_administrator.ADMIN_ID IN ("
					+ delIds + ")";
			SussessFlag = queryRun.update(sql4);
		}

		return SussessFlag;
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ����ó�������Ա
	 * �������ڣ�2017-4-24-����8:02:31
	 * @param con
	 * @param string
	 * @return
	 * @throws SQLException 
	 * @throws Exception 
	 */
	public int SetSuperAdmin(String delIds) throws SQLException {
		// ɾ������Ա
		QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
		String sql = "UPDATE pms.t_administrator SET t_administrator.Ext3 = 'superAdmin' WHERE t_administrator.ADMIN_ID=? ";
		Object[] params = { delIds };
		return queryRun.update(sql, params);
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�ȡ����������Ա������
	 * �������ڣ�2017-4-25-����12:43:50
	 * @param string
	 * @return
	 * @throws SQLException 
	 */
	public int SetCancelSuperAdmin(String delIds) throws SQLException {
		// ɾ������Ա
		QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
		String sql = "UPDATE pms.t_administrator SET t_administrator.Ext3 = 'admin' WHERE t_administrator.ADMIN_ID=? ";
		Object[] params = { delIds };
		return queryRun.update(sql, params);

	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����Ƿ�Ϊ��������Ա
	 * �������ڣ�2017-4-25-����1:55:16
	 * @param string
	 * @return true �ǳ�������Ա
	 * @throws SQLException 
	 */
	public boolean IsSuperAdmin(String delIds) throws SQLException {
		// ɾ������Ա
		QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
		String sql = "SELECT * FROM pms.t_administrator WHERE t_administrator.Ext3='superAdmin' and t_administrator.ADMIN_ID=? ";
		Object[] params = { delIds };
		Administrator admin = queryRun.query(sql,
				new BeanHandler<Administrator>(Administrator.class), params);
		if (admin != null) {
			return true;
		} else {
			return false;
		}

	}
}
