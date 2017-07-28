package com.pms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pms.model.Employee;
import com.pms.model.PageBean;
import com.pms.util.DateUtil;
import com.pms.util.Log4jHelper;
import com.pms.util.StringUtil;

public class EmployeeDao {
	/**
	 * ���ܣ�����û�������
	 * @param con
	 * @param emp
	 * @return  ��ѯ�����û�ʱ�����ظ��û�����Ϣʵ�壬û�в鵽ʱ����null
	 * @throws Exception
	 */
	public Employee CheckPwd(Connection con, Employee emp) {
		Employee resultUser = null;
		String sql = "SELECT * FROM pms.t_employee WHERE t_employee.EMP_NO =? and t_employee.EMP_PWD=?";
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, emp.getEmp_no());
			pstmt.setString(2, emp.getEmp_pwd());
			Log4jHelper.info("��ͨ�û�������룺" + pstmt.toString());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				resultUser = new Employee();
				resultUser.setEmp_no(rs.getString(("EMP_NO")));
				resultUser.setEmp_pwd(rs.getString("EMP_PWD"));
				resultUser.setEmp_name(rs.getString("EMP_NAME"));
				resultUser.setEmp_sex(rs.getString("EMP_SEX"));
				resultUser.setEmp_birthday(rs.getDate("EMP_Birthday"));
				resultUser.setPs_id(rs.getString("PS_ID"));
				resultUser.setEmp_phone(rs.getString("EMP_Phone"));
				resultUser.setEmp_address(rs.getString("EMP_Address"));
			}
		} catch (SQLException e) {
			Log4jHelper.exception(e);
		}

		return resultUser;
	}

	/**
	 * ���ܣ�����������ѯԱ����Ϣ
	 * @param con
	 * @param pageBean
	 * @param emp
	 * @param bbirthday
	 * @param ebirthday
	 * @return
	 * @throws Exception
	 */
	public static ResultSet EmployeetList(Connection con, PageBean pageBean,
			Employee emp, String bbirthday, String ebirthday) throws Exception {
		StringBuffer sb = new StringBuffer(
				"SELECT  emp.EMP_NO,EMP_PWD,EMP_NAME,emp.emp_sex, (case WHEN emp_sex='F'then 'Ů' WHEN emp_sex='M'then '��' END ) as sexName,emp.EMP_Birthday,zzmm.PS_TYPE,zzmm.PS_Name,EMP_Phone, emp.EMP_Address, emp.ext1,emp.ext2 FROM pms.t_politicalstatus zzmm, pms.t_employee emp WHERE zzmm.PS_TYPE = emp.PS_ID ");
		if (emp != null && StringUtil.isNotEmpty(emp.getEmp_name())) {
			sb.append(" AND emp.emp_name LIKE '%" + emp.getEmp_name() + "%'");
		}
		if (emp != null && StringUtil.isNotEmpty(emp.getEmp_no())) {
			sb.append("AND emp.emp_no LIKE '%" + emp.getEmp_no() + "%'");
		}
		if (emp != null && StringUtil.isNotEmpty(emp.getEmp_sex())) {
			sb.append("AND emp.emp_sex LIKE '%" + emp.getEmp_sex() + "%'");
		}
		if (emp != null && StringUtil.isNotEmpty(emp.getEmp_phone())) {
			sb.append("AND emp.EMP_Phone LIKE '%" + emp.getEmp_phone() + "%'");
		}
		if (emp != null && StringUtil.isNotEmpty(emp.getEmp_address())) {
			sb.append("AND emp.EMP_Address LIKE '%" + emp.getEmp_address()
					+ "%'");
		}
		// and zzmm.PS_TYPE = '102'
		if (emp != null && StringUtil.isNotEmpty(emp.getPs_id())) {
			sb.append("AND zzmm.PS_TYPE LIKE '%" + emp.getPs_id() + "%'");
		}
		if (emp != null && StringUtil.isNotEmpty(bbirthday)) {
			sb.append(" and TO_DAYS(emp.EMP_Birthday) >= TO_DAYS('" + bbirthday
					+ "')");
		}
		if (emp != null && StringUtil.isNotEmpty(ebirthday)) {
			sb.append(" and TO_DAYS(emp.EMP_Birthday) <= TO_DAYS('" + ebirthday
					+ "')");
		}
		if (emp != null && pageBean != null) {
			sb.append(" limit " + pageBean.getStart() + ","
					+ pageBean.getRows());
		}
		Log4jHelper.info("��ѯԱ����Ϣ��" + sb.toString());
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		return pstmt.executeQuery();
	}

	/**
	 * ���ܣ�����������ȡԱ������
	 * @param con
	 * @param student
	 * @param bbirthday
	 * @param ebirthday
	 * @return Ա������
	 * @throws Exception
	 */
	public static int EmployeeCount(Connection con, Employee student,
			String bbirthday, String ebirthday) throws Exception {
		Log4jHelper.info("������emp" + student.toString());
		StringBuffer sb = new StringBuffer(
				"SELECT  count(*) as total FROM pms.t_politicalstatus zzmm, pms.t_employee emp WHERE zzmm.PS_TYPE = emp.PS_ID ");
		if (StringUtil.isNotEmpty(student.getEmp_name())) {
			sb.append(" AND emp.emp_name LIKE '%" + student.getEmp_name()
					+ "%'");
		}
		if (StringUtil.isNotEmpty(student.getEmp_no())) {
			sb.append("AND emp.emp_no LIKE '%" + student.getEmp_no() + "%'");
		}
		if (StringUtil.isNotEmpty(student.getEmp_sex())) {
			sb.append("AND emp.emp_sex LIKE '%" + student.getEmp_sex() + "%'");
		}
		if (StringUtil.isNotEmpty(student.getEmp_phone())) {
			sb.append("AND emp.EMP_Phone LIKE '%" + student.getEmp_phone()
					+ "%'");
		}
		if (StringUtil.isNotEmpty(student.getEmp_address())) {
			sb.append("AND emp.EMP_Address LIKE '%" + student.getEmp_address()
					+ "%'");
		}
		if (StringUtil.isNotEmpty(bbirthday)) {
			sb.append(" and TO_DAYS(emp.EMP_Birthday) >= TO_DAYS('" + bbirthday
					+ "')");
		}
		if (StringUtil.isNotEmpty(ebirthday)) {
			sb.append(" and TO_DAYS(emp.EMP_Birthday) <= TO_DAYS('" + ebirthday
					+ "')");
		}
		Log4jHelper.info(sb.toString());
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return rs.getInt("total");
		} else {
			return 0;
		}
	}

	/**
	 * ���ܣ�ɾ��Ա����Ϣ
	 * @param con
	 * @param delIds
	 * @return ɾ���ɹ�������
	 * @throws Exception
	 */
	public static int EmlopyeeDelete(Connection con, String delIds)
			throws Exception {

		// ���t_induction �����Ƿ����delIds�û�����Ϣ��������������ɾ��
		String querySql = "select * from t_inductioninfo where EMP_NO in ( "
				+ delIds + ")";
		PreparedStatement queryPstmt = con.prepareStatement(querySql);
		Log4jHelper.info("���t_induction���Ƿ���delIds�û�����Ϣ��"
				+ queryPstmt.toString());
		ResultSet rs = queryPstmt.executeQuery();
		if (rs.next()) {
			// ɾ��t_induction����û�������
			// ɾ���������͵����ݣ� ������ְ����δ�ύ����ְ�����ύ����δͨ��
			String sql1 = "delete from pms.t_inductioninfo where t_inductioninfo.EMP_NO in ( "
					+ delIds + ")";
			Log4jHelper.info("ɾ��������Ϣ:" + sql1);
			PreparedStatement pstmt1 = con.prepareStatement(sql1);
			if (pstmt1.executeUpdate() > 0) {
				String sql = "DELETE FROM pms.t_employee WHERE t_employee.EMP_NO IN("
						+ delIds + ")";
				PreparedStatement pstmt = con.prepareStatement(sql);
				Log4jHelper.info("ɾ��Ա����Ϣ��" + pstmt.toString());
				return pstmt.executeUpdate();
			} else {

				return 0;
			}
		} else {
			// ���t_induction���в����ڸ��û�����Ϣ����ֱ��ɾ��
			String sql = "DELETE FROM pms.t_employee WHERE t_employee.EMP_NO IN("
					+ delIds + ")";
			PreparedStatement pstmt = con.prepareStatement(sql);
			Log4jHelper.info("ɾ��Ա����Ϣ��" + pstmt.toString());
			return pstmt.executeUpdate();
		}

	}

	/**
	 * ���ܣ������û���Ϣ
	 * @param con
	 * @param student
	 * @return
	 * @throws Exception
	 */
	public static int EmployeeAdd(Connection con, Employee emp)
			throws Exception {
		String sql = "INSERT INTO pms.t_employee() VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, emp.getEmp_no());
		pstmt.setString(2, emp.getEmp_pwd());
		pstmt.setString(3, emp.getEmp_name());
		pstmt.setString(4, emp.getEmp_sex());
		pstmt.setString(5,
				DateUtil.formatDate(emp.getEmp_birthday(), "yyyy-MM-dd"));
		pstmt.setString(6, emp.getPs_id());
		pstmt.setString(7, emp.getEmp_phone());
		pstmt.setString(8, emp.getEmp_address());
		pstmt.setString(9, emp.getExt1());
		pstmt.setString(10, DateUtil.getCurrentDateStr());
		pstmt.setString(11, emp.getExt3());

		Log4jHelper.info("����Ա����Ϣ��" + pstmt.toString());
		return pstmt.executeUpdate();
	}

	/**
	 * ���ܣ�ְ��ע��
	 * @param con
	 * @param student
	 * @return
	 * @throws Exception
	 */
	public static int EmployeeRegister(Connection con, Employee emp)
			throws Exception {
		String sql = "INSERT INTO pms.t_employee() VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);

		pstmt.setString(1, emp.getEmp_no());
		// Ա��ע��ʱ��ʹ��ע�������
		pstmt.setString(2, emp.getEmp_pwd());
		pstmt.setString(3, emp.getEmp_name());
		pstmt.setString(4, emp.getEmp_sex());
		pstmt.setString(5,
				DateUtil.formatDate(emp.getEmp_birthday(), "yyyy-MM-dd"));
		pstmt.setString(6, emp.getPs_id());
		pstmt.setString(7, emp.getEmp_phone());
		pstmt.setString(8, emp.getEmp_address());
		pstmt.setString(9, emp.getExt1());
		// ��������
		pstmt.setString(10, DateUtil.getCurrentDateStr());
		pstmt.setString(11, emp.getExt3());
		Log4jHelper.info("Ա��ע�᣺" + pstmt.toString());
		return pstmt.executeUpdate();
	}

	/**
	 * ���ܣ��޸��û���Ϣ
	 * @param con
	 * @param student
	 * @return �޸ĳɹ���������
	 * @throws Exception
	 */
	public static int EmployeeModify(Connection con, Employee emp) {
		Log4jHelper.info("���²�����" + emp.toString());
		try {
			if (emp != null && StringUtil.isNotEmpty(emp.getEmp_pwd())) {
				String sql = "UPDATE pms.t_employee SET t_employee.EMP_NAME=?,EMP_PWD=?,t_employee.EMP_SEX=?,t_employee.EMP_Birthday =?,t_employee.PS_ID=?,t_employee.EMP_Phone=?,t_employee.EMP_Address=?,t_employee.ext1=?,t_employee.ext2=?,t_employee.ext3=? WHERE t_employee.EMP_NO=?";
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, emp.getEmp_name());
				pstmt.setString(2, emp.getEmp_pwd());
				pstmt.setString(3, emp.getEmp_sex());
				pstmt.setString(4, DateUtil.formatDate(emp.getEmp_birthday(),
						"yyyy-MM-dd"));
				pstmt.setString(5, emp.getPs_id());
				pstmt.setString(6, emp.getEmp_phone());
				pstmt.setString(7, emp.getEmp_address());
				pstmt.setString(8, emp.getExt1());
				pstmt.setString(9, DateUtil.getCurrentDateStr());
				pstmt.setString(10, emp.getExt3());
				// �޸�����
				pstmt.setString(11, emp.getEmp_no());
				// ��ӡִ�е�Sql���
				Log4jHelper.info("�޸ĵ�Sql��䣺" + pstmt.toString());
				return pstmt.executeUpdate();
			} else {
				String sql = "UPDATE pms.t_employee SET t_employee.EMP_NAME=?,t_employee.EMP_SEX=?,t_employee.EMP_Birthday =?,t_employee.PS_ID=?,t_employee.EMP_Phone=?,t_employee.EMP_Address=?,t_employee.ext1=?,t_employee.ext2=?,t_employee.ext3=? WHERE t_employee.EMP_NO=?";
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, emp.getEmp_name());
				pstmt.setString(2, emp.getEmp_sex());
				pstmt.setString(3, DateUtil.formatDate(emp.getEmp_birthday(),
						"yyyy-MM-dd"));
				pstmt.setString(4, emp.getPs_id());
				pstmt.setString(5, emp.getEmp_phone());
				pstmt.setString(6, emp.getEmp_address());
				pstmt.setString(7, emp.getExt1());
				pstmt.setString(8, DateUtil.getCurrentDateStr());
				pstmt.setString(9, emp.getExt3());
				// �޸�����
				pstmt.setString(10, emp.getEmp_no());
				// ��ӡִ�е�Sql���
				Log4jHelper.info("�޸ĵ�Sql��䣺" + pstmt.toString());
				return pstmt.executeUpdate();
			}

		} catch (Exception e) {
			Log4jHelper.exception(e);
			return 0;
		}
	}

	/**
	 * ���ܣ����ע���û��Ƿ��Ѿ�ע��
	 * @param con 
	 * @param stuNo  Ա������
	 * @return ������ ����false ���ڷ���true
	 * @throws SQLException 
	 */
	public static boolean IsExistence(Connection con, String empNo)
			throws SQLException {
		String sql = "SELECT * FROM pms.t_employee WHERE t_employee.EMP_NO=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, empNo);
		Log4jHelper.info("���Ա���ţ�" + pstmt.toString());
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
	 * ���ܣ�����Ƿ���ְ t_induction���н��м��
	 * �������ڣ�2017-4-25-����1:26:26
	 * @param con
	 * @param string
	 * @return
	 * @throws SQLException 
	 */
	public static boolean IsInduction(Connection con, String emp_no)
			throws SQLException {
		// ��飺��ְ����������ͨ����������ɾ��
		String sql = "SELECT * FROM t_inductioninfo WHERE EMP_NO =? and EXT3='IN' and EXT1='11'";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, emp_no);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean IsLeader(Connection con, String emp_no)
			throws SQLException {
		// ��飺���û��ǲ��ǲ����쵼
		String sql = "SELECT * FROM t_department WHERE DEP_LEADER =?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, emp_no);
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
	 * ���ܣ���ȡ��ͨԱ��������Ϣ
	 * �������ڣ�2017-5-7-����8:09:40
	 * @param con
	 * @param pageBean
	 * @param emp
	 * @param object
	 * @param object2
	 * @return
	 * @throws SQLException 
	 */
	public static ResultSet EmployeetPersionInfo(Connection con,
			PageBean pageBean, Employee emp) throws SQLException {
		StringBuffer sb = new StringBuffer(
				"SELECT  emp.EMP_NO,EMP_PWD,EMP_NAME,emp.emp_sex, (case WHEN emp_sex='F'then 'Ů' WHEN emp_sex='M'then '��' END ) as sexName,emp.EMP_Birthday,zzmm.PS_TYPE,zzmm.PS_Name,EMP_Phone, emp.EMP_Address, emp.ext1,emp.ext2 FROM pms.t_politicalstatus zzmm, pms.t_employee emp WHERE zzmm.PS_TYPE = emp.PS_ID ");

		if (emp != null && StringUtil.isNotEmpty(emp.getEmp_no())) {
			sb.append("AND emp.emp_no =" + emp.getEmp_no() + "");
		}

		Log4jHelper.info("��ѯԱ��������Ϣ��" + sb.toString());
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		return pstmt.executeQuery();
	}

}
