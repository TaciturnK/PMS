package com.pms.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.pms.dao.PositionsInfoDao;
import com.pms.model.PageBean;
import com.pms.model.PositionsInfoBean;
import com.pms.util.BaseServlet;
import com.pms.util.DbUtils;
import com.pms.util.JsonUtil;
import com.pms.util.Log4jHelper;
import com.pms.util.ResponseUtil;
import com.pms.util.StringUtil;

public class PositionsServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�������λ��Ϣ��Ϣ
	 * �������ڣ�2017-5-9-����1:15:27
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void AddPositions(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("������λ��Ϣ��Ϣ");

		request.setCharacterEncoding("utf-8");
		String pos_Id = request.getParameter("POS_ID");
		String dep_Id = request.getParameter("DEP_ID");
		String pos_Name = request.getParameter("POS_NAME");
		String pos_Content = request.getParameter("POS_CONTENT");
		String pos_Salary = request.getParameter("POS_SALARY");
		String pos_Allowance = request.getParameter("POS_ALLOWANCE");
		String pos_Perquisites = request.getParameter("POS_PERQUISITES");
		String ext1 = request.getParameter("EXT1");
		// String ext2 = request.getParameter("Ext2");
		// String ext3 = request.getParameter("Ext3");

		PositionsInfoBean pib = new PositionsInfoBean(pos_Id, dep_Id, pos_Name,
				pos_Content, Double.parseDouble(pos_Salary),
				Double.parseDouble(pos_Allowance),
				Double.parseDouble(pos_Perquisites), ext1, null, null);

		Connection con = null;
		try {
			con = DbUtils.getConnection();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			// ���������λ�Ƿ��Ѿ�����
			if (PositionsInfoDao.IsExistence(con, pos_Id)) {
				result.put("success", false);
				result.put("errorMsg", "��λ���Ѵ��ڣ�");
			} else {
				// ������λ�����سɹ�������
				saveNums = PositionsInfoDao.PositionsInfoAdd(con, pib);
				if (saveNums > 0) {
					result.put("success", true);
				} else {
					result.put("success", false);
					result.put("errorMsg", "������λ��Ϣʧ��");
				}
			}
			ResponseUtil.write(response, result);// ���͵��ͻ���
		} catch (Exception e) {
			Log4jHelper.exception(e);
		} finally {
			try {
				DbUtils.CloseConn(con);// �ر�����
			} catch (Exception e) {
				Log4jHelper.exception(e);
			}
		}

	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�ɾ����λ��Ϣ��Ϣ
	 * �������ڣ�2017-5-9-����1:17:09
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void DeletePositions(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("ɾ����λ��Ϣ��Ϣ");

		String delIds = request.getParameter("delIds");// ȡ��ɾ����id�ַ�������

		Connection con = null;
		try {
			con = DbUtils.getConnection();
			String[] str = delIds.split(",");
			JSONObject result = new JSONObject();
			for (int i = 0; i < str.length; i++) {
				boolean f = PositionsInfoDao.getEmpByPosId(con, str[i]);
				if (f) {
					result.put("success", false);
					result.put("errorIndex", i);
					result.put("errorMsg", "��λ����ְ��,����ɾ��");
					ResponseUtil.write(response, result);
					return;
				}
			}
			int delNums = PositionsInfoDao.PositionsInfoDelete(con,
					StringUtil.FormatDeleteDelIds(delIds));// ��������ɾ��������
			if (delNums > 0) {
				result.put("success", true);
				result.put("delNums", delNums);
			} else {
				result.put("success", false);
				result.put("errorMsg", "ɾ��ʧ��");
			}
			ResponseUtil.write(response, result);// ���͵��ͻ���
		} catch (Exception e) {
			Log4jHelper.exception(e);
		} finally {
			try {
				DbUtils.CloseConn(con);// �ر�����
			} catch (Exception e) {
				Log4jHelper.exception(e);
			}
		}

	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ��޸ĸ�λ��Ϣ��Ϣ
	 * �������ڣ�2017-5-9-����1:17:38
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void UpdatePositions(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("�޸ĸ�λ��Ϣ��Ϣ");

		request.setCharacterEncoding("utf-8");
		String pos_Id = request.getParameter("POS_ID");
		String dep_Id = request.getParameter("DEP_ID");
		String pos_Name = request.getParameter("POS_NAME");
		String pos_Content = request.getParameter("POS_CONTENT");
		String pos_Salary = request.getParameter("POS_SALARY");
		String pos_Allowance = request.getParameter("POS_ALLOWANCE");
		String pos_Perquisites = request.getParameter("POS_PERQUISITES");
		String ext1 = request.getParameter("EXT1");
		// String ext2 = request.getParameter("Ext2");
		// String ext3 = request.getParameter("Ext3");

		PositionsInfoBean pib = new PositionsInfoBean(pos_Id, dep_Id, pos_Name,
				pos_Content, Double.parseDouble(pos_Salary),
				Double.parseDouble(pos_Allowance),
				Double.parseDouble(pos_Perquisites), ext1, null, null);

		Connection con = null;
		try {
			con = DbUtils.getConnection();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			// ������޸ĵĻ��������޸ĳɹ�������
			saveNums = PositionsInfoDao.PositionsInfoModify(con, pib);
			if (saveNums > 0) {
				result.put("success", true);
			} else {
				result.put("success", false);
				result.put("errorMsg", "�޸ĸ�λ��Ϣʧ�ܣ�");
			}
			ResponseUtil.write(response, result);// ���͵��ͻ���
		} catch (Exception e) {
			Log4jHelper.exception(e);
		} finally {
			try {
				DbUtils.CloseConn(con);// �ر�����
			} catch (Exception e) {
				Log4jHelper.exception(e);
			}
		}

	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ѯ��λ��Ϣ��Ϣ�б�
	 * �������ڣ�2017-5-9-����1:18:49
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void PositionsListInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("��ѯ��λ��Ϣ��Ϣ�б�");

		request.setCharacterEncoding("utf-8");
		String page = request.getParameter("page");// ȡ������Ĳ���
		String rows = request.getParameter("rows");
		String pos_Id = request.getParameter("POS_ID");
		String pos_Name = request.getParameter("POS_NAME");
		String DEP_ID = request.getParameter("DEP_ID");

		PositionsInfoBean grade = new PositionsInfoBean();
		grade.setPos_Name(pos_Id);
		grade.setPos_Name(pos_Name);
		grade.setDep_Id(DEP_ID);

		PageBean pageBean = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(PositionsInfoDao
					.PositionsInfoList(con, pageBean, grade));// ȡ��json����
			int total = PositionsInfoDao.PositionsInfoCount(con, grade);// �ܼ�¼��
			result.put("rows", jsonArray);// ��װ����
			result.put("total", total);
			ResponseUtil.write(response, result);// ���͵��ͻ���
		} catch (Exception e) {
			Log4jHelper.exception(e);
		} finally {
			try {
				DbUtils.CloseConn(con);// �ر�����
			} catch (Exception e) {
				Log4jHelper.exception(e);
			}
		}

	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ѯ��λ��Ϣ��������Ϣ
	 * �������ڣ�2017-5-9-����1:19:22
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void PositionsComboboxInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("��ѯ��λ��Ϣ��������Ϣ");
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("POS_ID", "");
			jsonObject.put("POS_NAME", "��ѡ��...");
			jsonArray.add(jsonObject);
			// ������������
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(PositionsInfoDao
					.PositionsInfoList(con, null, new PositionsInfoBean())));// ȡ��json����
			ResponseUtil.write(response, jsonArray);// ���͵��ͻ���
		} catch (Exception e) {
			Log4jHelper.exception(e);
		} finally {
			try {
				DbUtils.CloseConn(con);// �ر�����
			} catch (Exception e) {
				Log4jHelper.exception(e);
			}
		}
	}

}
