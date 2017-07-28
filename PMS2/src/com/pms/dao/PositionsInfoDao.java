package com.pms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pms.model.PageBean;
import com.pms.model.PositionsInfoBean;
import com.pms.util.DateUtil;
import com.pms.util.Log4jHelper;
import com.pms.util.StringUtil;

/**
 * 
 * @author Taowd
 * ��        �ܣ���λ��Ϣ������
 * ��дʱ�䣺2017-4-8-����10:41:38
 */
public class PositionsInfoDao {

	/**
	 * ���ܣ���λ��Ϣ��ѯ�б�
	 * @param con
	 * @param pageBean
	 * @param grade
	 * @return
	 * @throws Exception
	 */
	public static ResultSet PositionsInfoList(Connection con,
			PageBean pageBean, PositionsInfoBean grade) throws Exception {
		Log4jHelper.info("��ѯ���ݣ�" + grade.toString());
		StringBuffer sb = new StringBuffer(
				"SELECT pi.POS_ID,pi.POS_NAME,dep.DEP_ID,DEP_NAME,dep.DEP_LEADER,pi.POS_CONTENT,pi.POS_SALARY,pi.POS_ALLOWANCE,pi.POS_PERQUISITES,pi.EXT1,pi.EXT2,pi.EXT3 FROM pms.t_positionsinfo pi,pms.t_department dep WHERE pi.DEP_ID = dep.DEP_ID ");
		if (grade != null && StringUtil.isNotEmpty(grade.getPos_Name())) {
			sb.append("and pi.POS_NAME like '%" + grade.getPos_Name() + "%'");
		}
		if (grade != null && StringUtil.isNotEmpty(grade.getDep_Id())) {
			sb.append("and dep.DEP_ID like '%" + grade.getDep_Id() + "%'");
		}
		if (pageBean != null) {
			sb.append(" limit " + pageBean.getStart() + ","
					+ pageBean.getRows());
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString());

		Log4jHelper.info("��ѯ��λ��Ϣ�б�" + pstmt.toString());
		return pstmt.executeQuery();
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�ȡ���ܼ�¼��
	 * �������ڣ�2017-4-8-����10:43:52
	 * @param con
	 * @param grade
	 * @return
	 * @throws Exception
	 */
	public static int PositionsInfoCount(Connection con, PositionsInfoBean grade)
			throws Exception {
		StringBuffer sb = new StringBuffer(
				"SELECT count(*) as total FROM pms.t_positionsinfo pi,pms.t_department dep WHERE pi.DEP_ID = dep.DEP_ID");
		if (StringUtil.isNotEmpty(grade.getPos_Name())) {
			sb.append(" and pi.POS_NAME like '%" + grade.getPos_Name() + "%'");
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString());

		Log4jHelper.info("��ѯ��λ��Ϣ�б���ȡ�ܼ�¼��" + pstmt.toString());
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
	 * ���ܣ�ɾ����λ��Ϣ
	 * �������ڣ�2017-4-8-����10:44:22
	 * @param con
	 * @param delIds
	 * @return
	 * @throws Exception
	 */
	public static int PositionsInfoDelete(Connection con, String delIds)
			throws Exception {
		String sql = "delete from pms.t_positionsinfo where t_positionsinfo.POS_ID in("
				+ delIds + ")";

		Log4jHelper.info("ɾ����λ��Ϣ" + sql);
		PreparedStatement pstmt = con.prepareStatement(sql);
		return pstmt.executeUpdate();
	}

	/**
	 * ���ܣ���ѯ�ø�λ�ĵ�Ա����Ϣ
	 * @param con
	 * @param gradeId
	 * @return
	 * @throws Exception
	 */
	public static boolean getEmpByPosId(Connection con, String pos_Id)
			throws Exception {
		Log4jHelper.info("�����ĸ�λ��ţ�" + pos_Id);
		String sql = "SELECT * FROM pms.t_inductioninfo WHERE t_inductioninfo.POS_ID=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, pos_Id);
		Log4jHelper.info("����λ���Ƿ���Ա����Ϣ��" + pstmt.toString());
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
	 * ���ܣ����Ӹ�λ��Ϣ
	 * �������ڣ�2017-4-8-����10:44:39
	 * @param con
	 * @param grade
	 * @return
	 * @throws Exception
	 */
	public static int PositionsInfoAdd(Connection con, PositionsInfoBean grade)
			throws Exception {
		String sql = "INSERT INTO pms.t_positionsinfo VALUES(?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, grade.getPos_Id());
		pstmt.setString(2, grade.getDep_Id());
		pstmt.setString(3, grade.getPos_Name());
		pstmt.setString(4, grade.getPos_Content());
		pstmt.setDouble(5, grade.getPos_Salary());
		pstmt.setDouble(6, grade.getPos_Allowance());
		pstmt.setDouble(7, grade.getPos_Perquisites());
		pstmt.setString(8, grade.getExt1());
		pstmt.setString(9, DateUtil.getCurrentDateStr());
		pstmt.setString(10, grade.getExt3());
		Log4jHelper.info("������λ��Ϣ��" + pstmt.toString());
		return pstmt.executeUpdate();
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ��޸ĸ�λ��Ϣ
	 * �������ڣ�2017-4-8-����10:54:27
	 * @param con
	 * @param grade
	 * @return
	 * @throws Exception
	 */
	public static int PositionsInfoModify(Connection con,
			PositionsInfoBean grade) throws Exception {
		String sql = "UPDATE pms.t_positionsinfo SET t_positionsinfo.DEP_ID=?,t_positionsinfo.POS_NAME=?,t_positionsinfo.POS_CONTENT=?,t_positionsinfo.POS_SALARY=?,t_positionsinfo.POS_ALLOWANCE=?,t_positionsinfo.POS_PERQUISITES=?,t_positionsinfo.EXT1=?,t_positionsinfo.EXT2=?,t_positionsinfo.EXT3=? WHERE t_positionsinfo.POS_ID=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(10, grade.getPos_Id());
		pstmt.setString(1, grade.getDep_Id());
		pstmt.setString(2, grade.getPos_Name());
		pstmt.setString(3, grade.getPos_Content());
		pstmt.setDouble(4, grade.getPos_Salary());
		pstmt.setDouble(5, grade.getPos_Allowance());
		pstmt.setDouble(6, grade.getPos_Perquisites());
		pstmt.setString(7, grade.getExt1());
		pstmt.setString(8, DateUtil.getCurrentDateStr());
		pstmt.setString(9, grade.getExt3());

		Log4jHelper.info("�޸ĸ�λ��Ϣ��" + pstmt.toString());
		return pstmt.executeUpdate();
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����λ�Ƿ����
	 * �������ڣ�2017-4-9-����12:04:22
	 * @param con
	 * @param pos_Id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean IsExistence(Connection con, String pos_Id)
			throws SQLException {
		String sql = "SELECT * FROM pms.t_positionsinfo WHERE t_positionsinfo.POS_ID = ? ";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, pos_Id);
		Log4jHelper.info("����λ�Ƿ���ڣ�" + pstmt.toString());
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}
}
