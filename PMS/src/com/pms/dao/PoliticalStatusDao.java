package com.pms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pms.model.PageBean;
import com.pms.model.PoliticalStatusBean;
import com.pms.util.DateUtil;
import com.pms.util.Log4jHelper;
import com.pms.util.StringUtil;

public class PoliticalStatusDao {

	/**
	 * ���ܣ�������ò��ѯ�б�
	 * @param con
	 * @param pageBean
	 * @param grade
	 * @return
	 * @throws Exception
	 */
	public static ResultSet politicalStatusList(Connection con,
			PageBean pageBean, PoliticalStatusBean ps) throws Exception {
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM pms.t_politicalstatus");
		if (ps != null && StringUtil.isNotEmpty(ps.getPs_type())) {
			sb.append(" and t_politicalstatus.PS_TYPE like '%"
					+ ps.getPs_type() + "%'");
		}
		if (ps != null && StringUtil.isNotEmpty(ps.getPs_name())) {
			sb.append(" and t_politicalstatus.PS_Name like '%"
					+ ps.getPs_name() + "%'");
		}
		if (pageBean != null) {
			sb.append(" limit " + pageBean.getStart() + ","
					+ pageBean.getRows());
		}

		PreparedStatement pstmt = con.prepareStatement(sb.toString()
				.replaceFirst("and", "where"));

		Log4jHelper.info("��ѯ������ò��" + pstmt.toString());
		return pstmt.executeQuery();
	}

	/**
	 * ���ܣ���ȡ�ܼ�¼��
	 * @param con
	 * @param grade
	 * @return
	 * @throws Exception
	 */
	public static int PoliticalStatusCount(Connection con,
			PoliticalStatusBean ps) throws Exception {
		StringBuffer sb = new StringBuffer(
				"select count(*) as total from pms.t_politicalstatus");
		if (ps != null && StringUtil.isNotEmpty(ps.getPs_type())) {
			sb.append(" and t_politicalstatus.PS_TYPE like '%"
					+ ps.getPs_type() + "%'");
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
	 * ���ܣ�ɾ��������ò��¼
	 * @param con
	 * @param delIds
	 * @return
	 * @throws Exception
	 */
	public static int PoliticalStatusDelete(Connection con, String delIds)
			throws Exception {
		String sql = "delete from pms.t_politicalstatus where t_politicalstatus.PS_TYPE in("
				+ delIds + ")";
		PreparedStatement pstmt = con.prepareStatement(sql);
		Log4jHelper.info("ɾ��������ò��¼��" + pstmt.toString());
		return pstmt.executeUpdate();
	}

	/**
	 * ���ܣ�����һ��������ò����
	 * @param con
	 * @param grade
	 * @return
	 * @throws Exception
	 */
	public static int PoliticalStatusAdd(Connection con, PoliticalStatusBean ps)
			throws Exception {
		String sql = "INSERT INTO pms.t_politicalstatus VALUES(?,?,?,?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, ps.getPs_type());
		pstmt.setString(2, ps.getPs_name());
		// �����ֶΣ��˴���������ʱ�䴦��
		pstmt.setString(3, DateUtil.getCurrentDateStr());
		pstmt.setString(4, ps.getExt2());
		pstmt.setString(5, ps.getExt3());

		Log4jHelper.info("����һ��������ò����" + pstmt.toString());
		return pstmt.executeUpdate();
	}

	/**
	 * ���ܣ��޸�������ò����
	 * @param con
	 * @param grade
	 * @return
	 * @throws Exception
	 */
	public static int PoliticalStatusModify(Connection con,
			PoliticalStatusBean ps) throws Exception {
		String sql = "UPDATE pms.t_politicalstatus SET t_politicalstatus.PS_Name=?,t_politicalstatus.Ext1=?,t_politicalstatus.Ext2=?,t_politicalstatus.Ext3=? WHERE t_politicalstatus.PS_TYPE=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(5, ps.getPs_type());
		pstmt.setString(1, ps.getPs_name());
		pstmt.setString(2, DateUtil.getCurrentDateStr());
		pstmt.setString(3, ps.getExt2());
		pstmt.setString(4, ps.getExt3());
		Log4jHelper.info("�޸�������ò����" + pstmt.toString());
		return pstmt.executeUpdate();
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ��жϸ����ͱ�ŵ��Ƿ����
	 * �������ڣ�2017-4-9-����11:01:46
	 * @param con
	 * @param psb
	 * @return
	 * @throws SQLException 
	 */
	public static boolean IsExistence(Connection con, String ps_Type)
			throws SQLException {
		String sql = "SELECT * FROM pms.t_politicalstatus WHERE t_politicalstatus.PS_TYPE =?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, ps_Type);
		Log4jHelper.info("������ͱ�ţ�" + pstmt.toString());
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
	 * ���ܣ���ѯ�ý�ɫ�µ�Ա����Ϣ
	 * �������ڣ�2017-4-10-����1:36:10
	 * @param con
	 * @param string
	 * @return
	 * @throws SQLException 
	 */
	public static boolean getEmpByPsType(Connection con, String psType)
			throws SQLException {
		String sql = "SELECT * FROM pms.t_employee WHERE PS_ID =?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, psType);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}
}
