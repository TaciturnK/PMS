package com.pms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pms.model.DepartmentBean;
import com.pms.model.PageBean;
import com.pms.util.Log4jHelper;
import com.pms.util.StringUtil;

/**
 * 
 * @author ��ΰ��
 * ���ܣ����ű������
 * ��дʱ�䣺����8:11:16
 */
public class DepartmentDao {

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ѯ������Ϣ
	 * �������ڣ�2017-4-8-����5:46:53
	 * @param con
	 * @param pageBean
	 * @param demp
	 * @return
	 * @throws Exception
	 */
	public static ResultSet DepartmentList(Connection con, PageBean pageBean,
			DepartmentBean demp) throws Exception {
		StringBuffer sb = new StringBuffer(
				"SELECT dep.DEP_ID,dep.DEP_NAME,DEP_LEADER,emp.EMP_NAME,emp.EMP_Phone FROM pms.t_department dep LEFT JOIN pms.t_employee emp ON dep.DEP_LEADER = emp.EMP_NO ");
		if (demp != null && StringUtil.isNotEmpty(demp.getDep_Id())) {
			sb.append(" and dep.DEP_ID like '%" + demp.getDep_Id() + "%'");
		}
		if (demp != null && StringUtil.isNotEmpty(demp.getDep_Name())) {
			sb.append(" and dep.DEP_NAME like '%" + demp.getDep_Name() + "%'");
		}
		if (demp != null && StringUtil.isNotEmpty(demp.getDep_Leader())) {
			sb.append(" and emp.EMP_NAME like '%" + demp.getDep_Leader() + "%'");
		}
		if (pageBean != null) {
			sb.append(" limit " + pageBean.getStart() + ","
					+ pageBean.getRows());
		}

		PreparedStatement pstmt = con.prepareStatement(sb.toString()
				.replaceFirst("and", "where"));

		Log4jHelper.info("��ѯ������Ϣ��" + pstmt.toString());
		return pstmt.executeQuery();
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ѯ������Ϣ�ܼ�¼
	 * �������ڣ�2017-4-8-����5:47:07
	 * @param con
	 * @param demp
	 * @return
	 * @throws Exception
	 */
	public static int DepartmentCount(Connection con, DepartmentBean demp)
			throws Exception {
		StringBuffer sb = new StringBuffer(
				"select count(*) as total from pms.t_department ");
		if (demp != null && StringUtil.isNotEmpty(demp.getDep_Id())) {
			sb.append(" and t_department.DEP_ID like '%" + demp.getDep_Id()
					+ "%'");
		}
		if (demp != null && StringUtil.isNotEmpty(demp.getDep_Name())) {
			sb.append(" and t_department.DEP_NAME like '%" + demp.getDep_Name()
					+ "%'");
		}
		if (demp != null && StringUtil.isNotEmpty(demp.getDep_Leader())) {
			sb.append(" and t_department.DEP_LEADER like '%"
					+ demp.getDep_Leader() + "%'");
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString()
				.replaceFirst("and", "where"));

		Log4jHelper.info("��ȡ�ܼ�¼����" + pstmt.toString());
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return rs.getInt("total");
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�ɾ��������Ϣ
	 * �������ڣ�2017-4-10-����8:19:25
	 * @param con
	 * @param delIds
	 * @return
	 * @throws SQLException 
	 */
	public static int DepartmentDelete(Connection con, String delIds)
			throws SQLException {
		String sql = "DELETE FROM pms.t_department WHERE t_department.DEP_ID IN("
				+ delIds + ")";
		PreparedStatement pstmt = con.prepareStatement(sql);

		Log4jHelper.info("ɾ��������Ϣ��" + pstmt.toString());
		return pstmt.executeUpdate();
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ������Ƿ��Ѿ�����
	 * �������ڣ�2017-4-10-����9:16:43
	 * @param con
	 * @param ps_Type
	 * @return
	 * @throws SQLException
	 */
	public static boolean IsExistence(Connection con, String ps_Type)
			throws SQLException {
		String sql = "SELECT * FROM pms.t_department WHERE t_department.DEP_ID=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, ps_Type);
		Log4jHelper.info("��鲿���Ƿ��Ѿ����ڣ�" + pstmt.toString());
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ��޸Ĳ�����Ϣ
	 * �������ڣ�2017-4-10-����9:17:39
	 * @param con
	 * @param db
	 * @return
	 * @throws SQLException 
	 */
	public static int DepartmentModify(Connection con, DepartmentBean db)
			throws SQLException {
		String sql = "UPDATE pms.t_department SET t_department.DEP_NAME=?,t_department.DEP_LEADER=? WHERE t_department.DEP_ID=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, db.getDep_Name());
		pstmt.setString(2, db.getDep_Leader());
		// �޸�����
		pstmt.setString(3, db.getDep_Id());
		// ��ӡִ�е�Sql���
		Log4jHelper.info("�޸ĵ�Sql��䣺" + pstmt.toString());
		return pstmt.executeUpdate();
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����������Ϣ
	 * �������ڣ�2017-4-10-����9:21:16
	 * @param con
	 * @param db
	 * @return
	 * @throws SQLException 
	 */
	public static int DepartmentAdd(Connection con, DepartmentBean db)
			throws SQLException {
		String sql = "INSERT INTO pms.t_department() VALUES(?,?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(2, db.getDep_Name());
		pstmt.setString(3, db.getDep_Leader());
		// �޸�����
		pstmt.setString(1, db.getDep_Id());
		Log4jHelper.info("Ա��ע�᣺" + pstmt.toString());
		return pstmt.executeUpdate();
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ��鿴�ò����Ƿ��и�λ��Ϣ
	 * �������ڣ�2017-4-19-����9:04:59
	 * @param con
	 * @param string
	 * @return
	 * @throws SQLException 
	 */
	public static boolean getPosInfoByDepId(Connection con, String DepId)
			throws SQLException {
		Log4jHelper.info("�ò����Ƿ��и�λ��Ϣ��" + DepId);
		String sql = "SELECT * FROM pms.t_positionsinfo WHERE t_positionsinfo.DEP_ID=? ";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, DepId);
		Log4jHelper.info("�ò����Ƿ��и�λ��Ϣ��" + pstmt.toString());
		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

}
