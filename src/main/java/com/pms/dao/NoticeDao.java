package com.pms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pms.model.NoticeBean;
import com.pms.model.PageBean;
import com.pms.util.DateUtil;
import com.pms.util.Log4jHelper;
import com.pms.util.StringUtil;

/**
 * 
 * @author Taowd
 * ��        �ܣ�������Ϣ���ݿ������
 * ��дʱ�䣺2017-4-8-����3:45:38
 */
public class NoticeDao {
	/**
	 * ���ܣ�����������ѯ������Ϣ
	 * @param con
	 * @param pageBean
	 * @param student
	 * @param bbirthday
	 * @param ebirthday
	 * @return
	 * @throws Exception
	 */
	public static ResultSet NoticeList(Connection con, PageBean pageBean,
			NoticeBean notice, String startDate, String endDate)
			throws Exception {
		StringBuffer sb = new StringBuffer(
				"SELECT notic.NOT_ID,notic.NOT_TITLE,notic.NOT_CONTENT,notic.NOT_DATE,admin.ADMIN_ID,admin.ADMIN_NAME,notic.EXT1,notic.EXT2,notic.EXT3 FROM pms.t_notice notic,pms.t_administrator admin WHERE notic.NOT_AUTHOR=admin.ADMIN_ID ");
		if (StringUtil.isNotEmpty(notice.getNot_Id())) {
			sb.append(" AND notic.NOT_ID like '%" + notice.getNot_Id() + "%'");
		}
		if (StringUtil.isNotEmpty(notice.getNot_Title())) {
			sb.append(" AND notic.NOT_TITLE like '%" + notice.getNot_Title()
					+ "%'");
		}
		if (StringUtil.isNotEmpty(notice.getNot_Content())) {
			sb.append(" AND notic.NOT_CONTENT like '%"
					+ notice.getNot_Content() + "%'");
		}

		if (StringUtil.isNotEmpty(startDate)) {
			sb.append(" AND TO_DAYS(notic.NOT_DATE) >= TO_DAYS('" + startDate
					+ "')");
		}
		if (StringUtil.isNotEmpty(endDate)) {
			sb.append(" AND TO_DAYS(notic.NOT_DATE) <= TO_DAYS('" + endDate
					+ "')");
		}
		if (StringUtil.isNotEmpty(notice.getNot_Author())) {
			sb.append(" AND admin.ADMIN_NAME like '%" + notice.getNot_Author()
					+ "%'");
		}
		if (StringUtil.isNotEmpty(notice.getNot_ext1())) {
			sb.append(" AND notic.EXT1 like '%" + notice.getNot_ext1() + "%'");
		}
		if (StringUtil.isNotEmpty(notice.getNot_ext2())) {
			sb.append(" AND notic.EXT2 like '%" + notice.getNot_ext2() + "%'");
		}
		if (StringUtil.isNotEmpty(notice.getNot_ext3())) {
			sb.append(" AND notic.EXT3 like '%" + notice.getNot_ext3() + "%'");
		}
		if (pageBean != null) {
			sb.append(" limit " + pageBean.getStart() + ","
					+ pageBean.getRows());
		}

		PreparedStatement pstmt = con.prepareStatement(sb.toString()
				.replaceFirst("and", "where"));

		Log4jHelper.info("��ѯ������Ϣ��" + sb.toString());
		return pstmt.executeQuery();
	}

	/**
	 * ���ܣ�����������ȡ������Ϣ����
	 * @param con
	 * @param student
	 * @param bbirthday
	 * @param ebirthday
	 * @return ������Ϣ����
	 * @throws Exception
	 */
	public static int NoticeCount(Connection con, NoticeBean notice,
			String startDate, String endDate) throws Exception {
		StringBuffer sb = new StringBuffer(
				"SELECT  count(*) as total FROM pms.t_notice ");
		if (StringUtil.isNotEmpty(notice.getNot_Id())) {
			sb.append(" and t_notice.NOT_ID like '%" + notice.getNot_Id()
					+ "%'");
		}
		if (StringUtil.isNotEmpty(notice.getNot_Title())) {
			sb.append(" and t_notice.NOT_TITLE like '%" + notice.getNot_Title()
					+ "%'");
		}
		if (StringUtil.isNotEmpty(notice.getNot_Content())) {
			sb.append(" and t_notice.NOT_CONTENT like '%"
					+ notice.getNot_Content() + "%'");
		}

		if (StringUtil.isNotEmpty(startDate)) {
			sb.append(" and TO_DAYS(t_notice.NOT_DATE) >= TO_DAYS('"
					+ startDate + "')");
		}
		if (StringUtil.isNotEmpty(endDate)) {
			sb.append(" and TO_DAYS(t_notice.NOT_DATE) <= TO_DAYS('" + endDate
					+ "')");
		}
		if (StringUtil.isNotEmpty(notice.getNot_Author())) {
			sb.append(" and t_notice.NOT_AUTHOR like '%"
					+ notice.getNot_Author() + "%'");
		}
		if (StringUtil.isNotEmpty(notice.getNot_ext1())) {
			sb.append(" and t_notice.EXT1 like '%" + notice.getNot_ext1()
					+ "%'");
		}
		if (StringUtil.isNotEmpty(notice.getNot_ext2())) {
			sb.append(" and t_notice.EXT2 like '%" + notice.getNot_ext2()
					+ "%'");
		}
		if (StringUtil.isNotEmpty(notice.getNot_ext3())) {
			sb.append(" and t_notice.EXT3 like '%" + notice.getNot_ext3()
					+ "%'");
		}

		Log4jHelper.info("��ѯ������" + sb.toString());
		PreparedStatement pstmt = con.prepareStatement(sb.toString()
				.replaceFirst("and", "where"));
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
	 * �������ڣ�2017-4-8-����4:18:22
	 * @param con
	 * @param delIds
	 * @return
	 * @throws Exception
	 */
	public static int NoticeDelete(Connection con, String delIds)
			throws Exception {
		String sql = "DELETE FROM pms.t_notice WHERE t_notice.NOT_ID IN("
				+ delIds + ")";
		PreparedStatement pstmt = con.prepareStatement(sql);

		Log4jHelper.info("ɾ��������Ϣ��" + pstmt.toString());
		return pstmt.executeUpdate();
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����һ��������Ϣ
	 * �������ڣ�2017-4-8-����4:19:45
	 * @param con
	 * @param notice
	 * @return
	 * @throws Exception
	 */
	public static int NoticeAdd(Connection con, NoticeBean notice)
			throws Exception {
		String sql = "INSERT INTO pms.t_notice VALUES(?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, notice.getNot_Id());
		pstmt.setString(2, notice.getNot_Title());
		pstmt.setString(3, notice.getNot_Content());
		pstmt.setString(4, DateUtil.getCurrentDateStr());
		pstmt.setString(5, notice.getNot_Author());
		pstmt.setString(6, notice.getNot_ext1());
		pstmt.setString(7, notice.getNot_ext2());
		pstmt.setString(8, notice.getNot_ext3());

		Log4jHelper.info("����������Ϣ��" + pstmt.toString());
		return pstmt.executeUpdate();
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ��޸Ĺ�����Ϣ
	 * �������ڣ�2017-4-8-����4:25:42
	 * @param con
	 * @param emp
	 * @return
	 * @throws Exception
	 */
	public static int NoticeModify(Connection con, NoticeBean notice)
			throws Exception {
		String sql = "UPDATE pms.t_notice SET t_notice.NOT_TITLE=?,t_notice.NOT_CONTENT=?,t_notice.NOT_DATE=?,t_notice.NOT_AUTHOR =?,t_notice.EXT1=?,t_notice.EXT2=?,t_notice.EXT3=? WHERE t_notice.NOT_ID=?";
		PreparedStatement pstmt = con.prepareStatement(sql);

		pstmt.setString(8, notice.getNot_Id());
		pstmt.setString(1, notice.getNot_Title());
		pstmt.setString(2, notice.getNot_Content());
		pstmt.setString(3, DateUtil.getCurrentDateStr());
		pstmt.setString(4, notice.getNot_Author());
		pstmt.setString(5, notice.getNot_ext1());
		pstmt.setString(6, notice.getNot_ext2());
		pstmt.setString(7, notice.getNot_ext3());
		// ��ӡִ�е�Sql���
		Log4jHelper.info("�޸Ĺ�����Ϣ��" + pstmt.toString());
		return pstmt.executeUpdate();
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���鹫��
	 * �������ڣ�2017-4-10-����9:24:35
	 * @param con
	 * @param dEP_ID
	 * @return
	 * @throws SQLException 
	 */
	public static boolean IsExistence(Connection con, String dEP_ID)
			throws SQLException {
		String sql = "SELECT * FROM pms.t_notice WHERE t_notice.NOT_ID=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, dEP_ID);
		Log4jHelper.info("��鹫���Ƿ��Ѿ����ڣ�" + pstmt.toString());
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

}
