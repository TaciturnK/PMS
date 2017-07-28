package com.pms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.lang.StringUtils;

import com.pms.model.InductionInfoBean;
import com.pms.model.PageBean;
import com.pms.util.DateUtil;
import com.pms.util.DbUtils;
import com.pms.util.Log4jHelper;
import com.pms.util.StringUtil;

/**
 * 
 * @author Taowd
 * ��        �ܣ���ְ��Ϣ
 * ��дʱ�䣺2017-4-11-����8:38:05
 */
public class InductionInfoDao {
	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ѯ����Ա������ְ��Ϣ--����ԱȨ�޲ſ���--����ְ����ʱ�Ľ���
	 * �������ڣ�2017-4-11-����8:39:05
	 * @param con
	 * @param pageBean
	 * @param demp
	 * @return
	 * @throws Exception
	 */
	public static ResultSet InductionInfoList(Connection con,
			PageBean pageBean, InductionInfoBean demp, String startDate,
			String endDate) throws Exception {
		StringBuffer sb = new StringBuffer(
				"SELECT ii.IND_ID,emp.EMP_NO,emp.EMP_NAME,pos.POS_ID,pos.POS_NAME,ii.IND_DATE,ii.IND_STATE AS stateCode,(case WHEN ii.IND_STATE='1'then '��ְ' WHEN ii.IND_STATE='0'then '��ְ' END ) as stateName ,ii.IND_ENDDATE,ii.IND_Reasons,ii.EXT1 as approveState,(case WHEN ii.EXT1='11'then '������ͨ��' WHEN ii.EXT1='00'then '������'  WHEN ii.EXT1='22'then '����δͨ��' WHEN ii.EXT1='33'then 'δ�ύ' END ) as approveName ,ii.EXT2,ii.EXT3,(case WHEN ii.EXT3='IN'then '��ְ����' WHEN ii.EXT3='OUT'then '��ְ����' END ) as typeName  FROM pms.t_inductioninfo ii,pms.t_employee emp,pms.t_positionsinfo pos WHERE ii.EMP_NO=emp.EMP_NO and ii.POS_ID=pos.POS_ID ");
		if (demp != null && StringUtil.isNotEmpty(demp.getInd_Id())) {
			sb.append(" and ii.IND_ID like '%" + demp.getInd_Id() + "%'");
		}
		// Ա����
		if (demp != null && StringUtil.isNotEmpty(demp.getEmp_No())) {
			sb.append(" and emp.EMP_NO like '%" + demp.getEmp_No() + "%'");
		}
		// ����״̬
		if (demp != null && StringUtil.isNotEmpty(demp.getExt1())) {
			sb.append(" and ii.EXT1 like '%" + demp.getExt1() + "%'");
		}
		// ��ְ״̬
		if (demp != null && StringUtil.isNotEmpty(demp.getInd_State())) {
			sb.append(" and ii.IND_STATE like '%" + demp.getInd_State() + "%'");
		}
		// ��λ���
		if (demp != null && StringUtil.isNotEmpty(demp.getPos_Name())) {
			sb.append(" and pos.POS_NAME like '%" + demp.getPos_Name() + "%'");
		}
		// ��������
		if (StringUtil.isNotEmpty(startDate)) {
			sb.append(" and TO_DAYS(ii.IND_DATE) >= TO_DAYS('" + startDate
					+ "')");
		}
		if (StringUtil.isNotEmpty(endDate)) {
			sb.append(" and TO_DAYS(ii.IND_DATE) <= TO_DAYS('" + endDate + "')");
		}

		if (pageBean != null) {
			sb.append(" limit " + pageBean.getStart() + ","
					+ pageBean.getRows());
		}

		PreparedStatement pstmt = con.prepareStatement(sb.toString());

		Log4jHelper.info("��ѯԱ������ְ��Ϣ��" + pstmt.toString());
		return pstmt.executeQuery();
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���������ְλ���õķ���
	 * �������ڣ�2017-4-13-����9:05:21
	 * @param con
	 * @param induction
	 * @return
	 * @throws Exception 
	 */
	public static int ApplyInduction(InductionInfoBean induction)
			throws Exception {
		// ְλ����:
		// ����ȫ��Ψһ��UUID upper(replace(uuid(),'-',''))
		// ����QueryRunner����Ҫ�ṩ���ݿ����ӳض���
		QueryRunner qr = new QueryRunner(DbUtils.getDataSource());
		// ����sqlģ��
		String sql = "INSERT INTO t_inductioninfo() VALUES(upper(replace(uuid(),'-','')),?,?,?,?,?,?,?,?,?) ";
		// ��������
		Object[] par = { induction.getEmp_No(), induction.getPos_Id(),
				DateUtil.getCurrentDateStr(), induction.getInd_State(),
				induction.getInd_Enddate(), induction.getInd_Reasons(),
				induction.getExt1(), induction.getExt2(), induction.getExt3() };
		// ִ��Sql����ȡ����ֵ
		return qr.update(sql, par);
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ȡ��Ϣ����
	 * �������ڣ�2017-4-13-����8:18:42
	 * @param con
	 * @param inductionBean
	 * @return
	 * @throws SQLException 
	 */
	public static int InductionInfoCount(Connection con,
			InductionInfoBean inductionBean) throws SQLException {
		Log4jHelper.info("������InductionInfoBean" + inductionBean.toString());
		StringBuffer sb = new StringBuffer(
				"SELECT count(*) as total FROM t_inductioninfo ii,t_employee emp,t_positionsinfo pos WHERE ii.EMP_NO=emp.EMP_NO and ii.POS_ID=pos.POS_ID ");
		if (inductionBean != null
				&& StringUtil.isNotEmpty(inductionBean.getInd_Id())) {
			sb.append(" and ii.IND_ID like '%" + inductionBean.getInd_Id()
					+ "%'");
		}
		if (inductionBean != null
				&& StringUtil.isNotEmpty(inductionBean.getEmp_No())) {
			sb.append(" and emp.EMP_NO like '%" + inductionBean.getEmp_No()
					+ "%'");
		}
		if (inductionBean != null
				&& StringUtil.isNotEmpty(inductionBean.getPos_Id())) {
			sb.append(" and pos.POS_ID like '%" + inductionBean.getPos_Id()
					+ "%'");
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
	 * 
	 * Author:Taowd
	 * ���ܣ����ò����Ƿ��Ѿ�������ˣ����������ˣ��Ͳ������ٴ�����
	 * �������ڣ�2017-4-14-����1:18:28
	 * @param con
	 * @param pos_Id
	 * @return  true-�Ѿ��������  false-û�������
	 * @throws SQLException 
	 */
	public static boolean IsExistence(InductionInfoBean inductionBean)
			throws SQLException {
		// ����QueryRunner����Ҫ�ṩ���ݿ����ӳض���
		QueryRunner qr = new QueryRunner(DbUtils.getDataSource());
		// ����sqlģ��
		String sql = "SELECT * FROM t_inductioninfo WHERE t_inductioninfo.EMP_NO=? AND t_inductioninfo.POS_ID=? AND t_inductioninfo.EXT3=?";
		// ��������
		Object[] par = { inductionBean.getEmp_No(), inductionBean.getPos_Id(),
				inductionBean.getExt3() };
		// ִ��Sql����ȡ����ֵ
		InductionInfoBean admin = qr.query(sql,
				new BeanHandler<InductionInfoBean>(InductionInfoBean.class),
				par);
		if (admin != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ��޸��Ѿ����룬����û���ύ����Ϣ
	 * �������ڣ�2017-4-14-����1:23:49
	 * @param con
	 * @param induction
	 * @return
	 * @throws SQLException 
	 */
	public static int ApplyInductionModify(InductionInfoBean induction)
			throws SQLException {
		// ����QueryRunner����Ҫ�ṩ���ݿ����ӳض���
		QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
		// ����sqlģ��
		String sql = "UPDATE t_inductioninfo SET t_inductioninfo.POS_ID=?  WHERE t_inductioninfo.IND_ID=? ";
		// ��������
		Object[] params = { induction.getPos_Id(), induction.getInd_Id() };
		// ִ��Sql����ȡ����ֵ
		return queryRun.update(sql, params);
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ��ύ������߼�
	 * �������ڣ�2017-4-14-����8:25:31
	 * @param con
	 * @param induction
	 * @return
	 * @throws SQLException
	 */
	public static int ApplyInductionApprove(InductionInfoBean induction)
			throws SQLException {
		Log4jHelper.info("��������Ϣ��" + induction.toString());

		// ����QueryRunner����Ҫ�ṩ���ݿ����ӳض���
		QueryRunner qr = new QueryRunner(DbUtils.getDataSource());
		// ����sqlģ��
		String sql1 = "select EXT3 from t_inductioninfo where ind_id =? ";
		// ��������
		Object[] par = { induction.getInd_Id() };
		// ִ��Sql����ȡ����ֵ
		InductionInfoBean admin = qr.query(sql1,
				new BeanHandler<InductionInfoBean>(InductionInfoBean.class),
				par);
		if (admin != null) {
			if ("11".equals(induction.getExt1()))// ������ְͨ��
			{
				if (!StringUtils.isEmpty(admin.getExt3())
						&& "IN".equals(admin.getExt3())) {
					// ��ְ
					induction.setInd_State("1");
				} else {
					// ��ְ
					induction.setInd_State("0");
				}
			} else if ("22".equals(induction.getExt1()))// ������ͨ��
			{
				induction.setInd_State("");
			}

			// ����QueryRunner����Ҫ�ṩ���ݿ����ӳض���
			QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
			// ����sqlģ��
			String sql = "UPDATE t_inductioninfo SET t_inductioninfo.EXT1=?,t_inductioninfo.EXT2=?,t_inductioninfo.IND_STATE=? WHERE t_inductioninfo.IND_ID=? ";
			// ��������
			Object[] params = { induction.getExt1(), induction.getExt2(),
					induction.getInd_State(), induction.getInd_Id() };
			// ִ��Sql����ȡ����ֵ
			return queryRun.update(sql, params);
		} else {
			return 0;
		}

	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ��������״̬�Ƿ���޸ģ���ext1=="33"??
	 * �������ڣ�2017-4-14-����1:58:58
	 * @param con
	 * @param induction
	 * @return
	 * @throws SQLException 
	 */
	public boolean CheckApproveState(Connection con, InductionInfoBean induction)
			throws SQLException {
		String sql = "SELECT * FROM t_inductioninfo WHERE t_inductioninfo.IND_ID=? and t_inductioninfo.EXT1 ='33' ";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, induction.getInd_Id());
		Log4jHelper.info("��鲿���Ƿ��Ѿ��ύ��" + pstmt.toString());
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
	 * ���ܣ���鵱ǰ״̬
	 * �������ڣ�2017-4-14-����2:29:09
	 * @param con
	 * @param string
	 * @return
	 * @throws SQLException 
	 */
	public static boolean CheckApproveState(String IND_ID) throws SQLException {
		// ����QueryRunner����Ҫ�ṩ���ݿ����ӳض���
		QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
		// ����sqlģ��
		String sql = "SELECT * FROM t_inductioninfo WHERE t_inductioninfo.IND_ID=? and t_inductioninfo.EXT1 ='33' ";
		// ��������
		Object[] params = { IND_ID };
		// ִ��Sql����ȡ����ֵ
		InductionInfoBean admin = queryRun.query(sql,
				new BeanHandler<InductionInfoBean>(InductionInfoBean.class),
				params);
		if (admin != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�ɾ������
	 * �������ڣ�2017-4-14-����2:31:26
	 * @param con
	 * @param delIds
	 * @return
	 * @throws SQLException 
	 */
	public static int InductionDelete(String delIds) throws SQLException {
		// ����QueryRunner����Ҫ�ṩ���ݿ����ӳض���
		QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
		// ����sqlģ��
		String sql = "delete from t_inductioninfo where t_inductioninfo.IND_ID in ( "
				+ delIds + ")";
		// ִ��Sql����ȡ����ֵ
		return queryRun.update(sql);
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����Ƿ�Ϊ������״̬--��ext1=="00"
	 * �������ڣ�2017-4-17-����1:05:04
	 * @param con
	 * @param string
	 * @return
	 * @throws SQLException 
	 */
	public static boolean IsApproveState(String IND_ID) throws SQLException {
		// ����QueryRunner����Ҫ�ṩ���ݿ����ӳض���
		QueryRunner queryRun = new QueryRunner(DbUtils.getDataSource());
		// ����sqlģ��
		String sql = "SELECT * FROM t_inductioninfo WHERE t_inductioninfo.IND_ID=? and t_inductioninfo.EXT1 ='00' ";
		// ��������
		Object[] params = { IND_ID };
		// ִ��Sql����ȡ����ֵ
		InductionInfoBean admin = queryRun.query(sql,
				new BeanHandler<InductionInfoBean>(InductionInfoBean.class),
				params);
		if (admin != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����Ա��ѯ������Ϣ--���⴦���ų�δ�ύ������
	 * �������ڣ�2017-4-17-����1:36:37
	 * @param con
	 * @param pageBean
	 * @param inductionBean
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws SQLException 
	 */
	public static ResultSet AdminInductionInfoList(Connection con,
			PageBean pageBean, InductionInfoBean demp, String startDate,
			String endDate) throws SQLException {
		Log4jHelper.info("��ѯ������" + demp.toString());
		StringBuffer sb = new StringBuffer(
				"SELECT ii.IND_ID,emp.EMP_NO,emp.EMP_NAME,pos.POS_ID,pos.POS_NAME,ii.IND_DATE,ii.IND_STATE AS stateCode,(case WHEN ii.IND_STATE='1'then '��ְ' WHEN ii.IND_STATE='0'then '��ְ' END ) as stateName ,ii.IND_ENDDATE,ii.IND_Reasons,ii.EXT1 as approveState,(case WHEN ii.EXT1='11'then '������ͨ��' WHEN ii.EXT1='00'then '������'  WHEN ii.EXT1='22'then '����δͨ��' WHEN ii.EXT1='33'then 'δ�ύ' END ) as approveName ,ii.EXT2,ii.EXT3,(case WHEN ii.EXT3='IN'then '��ְ����' WHEN ii.EXT3='OUT'then '��ְ����' END ) as typeName  FROM t_inductioninfo ii,t_employee emp,t_positionsinfo pos WHERE ii.EMP_NO=emp.EMP_NO and ii.POS_ID=pos.POS_ID AND ii.EXT1!='33' ");
		if (demp != null && StringUtil.isNotEmpty(demp.getInd_Id())) {
			sb.append(" and ii.IND_ID like '%" + demp.getInd_Id() + "%'");
		}
		// Ա����
		if (demp != null && StringUtil.isNotEmpty(demp.getEmp_No())) {
			sb.append(" and emp.EMP_NO like '%" + demp.getEmp_No() + "%'");
		}
		// ����״̬
		if (demp != null && StringUtil.isNotEmpty(demp.getExt1())) {
			sb.append(" and ii.EXT1 like '%" + demp.getExt1() + "%'");
		}
		// ��ְ״̬
		if (demp != null && StringUtil.isNotEmpty(demp.getInd_State())) {
			sb.append(" and ii.IND_STATE like '%" + demp.getInd_State() + "%'");
		}
		// ��λ���
		if (demp != null && StringUtil.isNotEmpty(demp.getPos_Name())) {
			sb.append(" and pos.POS_NAME like '%" + demp.getPos_Name() + "%'");
		}
		// ��������
		if (StringUtil.isNotEmpty(startDate)) {
			sb.append(" and TO_DAYS(ii.IND_DATE) >= TO_DAYS('" + startDate
					+ "')");
		}
		if (StringUtil.isNotEmpty(endDate)) {
			sb.append(" and TO_DAYS(ii.IND_DATE) <= TO_DAYS('" + endDate + "')");
		}

		if (pageBean != null) {
			sb.append(" limit " + pageBean.getStart() + ","
					+ pageBean.getRows());
		}

		PreparedStatement pstmt = con.prepareStatement(sb.toString());

		Log4jHelper.info("��ѯԱ������ְ��Ϣ��" + pstmt.toString());
		return pstmt.executeQuery();
	}

}
