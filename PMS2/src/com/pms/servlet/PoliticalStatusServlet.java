package com.pms.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.pms.dao.PoliticalStatusDao;
import com.pms.model.PageBean;
import com.pms.model.PoliticalStatusBean;
import com.pms.util.BaseServlet;
import com.pms.util.DbUtils;
import com.pms.util.JsonUtil;
import com.pms.util.Log4jHelper;
import com.pms.util.ResponseUtil;
import com.pms.util.StringUtil;

public class PoliticalStatusServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����������ò��Ϣ
	 * �������ڣ�2017-5-9-����1:15:27
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void AddPoliticalStatus(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("����������ò��Ϣ");
		request.setCharacterEncoding("utf-8");
		String ps_Type = request.getParameter("PS_TYPE");
		String ps_Name = request.getParameter("PS_Name");
		String ext1 = request.getParameter("ext1");
		String ext2 = request.getParameter("Ext2");
		String ext3 = request.getParameter("ext3");

		PoliticalStatusBean psb = new PoliticalStatusBean(ps_Type, ps_Name,
				ext1, ext2, ext3);
		Log4jHelper.info("������Ϣ��" + psb.toString());

		Connection con = null;
		try {
			con = DbUtils.getConnection();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			// �������Ա�����Ƿ��Ѿ�ע��
			if (PoliticalStatusDao.IsExistence(con, ps_Type)) {
				result.put("success", false);
				result.put("errorMsg", "�������Ѵ��ڣ�");
			} else {
				// ����Ա�������سɹ�������
				saveNums = PoliticalStatusDao.PoliticalStatusAdd(con, psb);
				if (saveNums > 0) {
					result.put("success", true);
				} else {
					result.put("success", false);
					result.put("errorMsg", "����������ò��Ϣʧ��");
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
	 * ���ܣ�ɾ��������ò��Ϣ
	 * �������ڣ�2017-5-9-����1:17:09
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void DeletePoliticalStatus(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("ɾ��������ò��Ϣ");

		request.setCharacterEncoding("utf-8");
		String delIds = request.getParameter("delIds");// ȡ��ɾ����id�ַ�������
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			String[] str = delIds.split(",");
			JSONObject result = new JSONObject();
			for (int i = 0; i < str.length; i++) {
				boolean f = PoliticalStatusDao.getEmpByPsType(con, str[i]);
				if (f) {
					result.put("success", false);
					result.put("errorIndex", i);
					result.put("errorMsg", "�ý�ɫ����ְ��,����ɾ��");
					ResponseUtil.write(response, result);
					return;
				}
			}
			int delNums = PoliticalStatusDao.PoliticalStatusDelete(con,
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
	 * ���ܣ��޸�������ò��Ϣ
	 * �������ڣ�2017-5-9-����1:17:38
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void UpdatePoliticalStatus(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("�޸�������ò��Ϣ");

		request.setCharacterEncoding("utf-8");
		String ps_Type = request.getParameter("PS_TYPE");
		String ps_Name = request.getParameter("PS_Name");
		String ext1 = request.getParameter("ext1");
		String ext2 = request.getParameter("Ext2");
		String ext3 = request.getParameter("ext3");

		PoliticalStatusBean psb = new PoliticalStatusBean(ps_Type, ps_Name,
				ext1, ext2, ext3);
		Log4jHelper.info("�޸���Ϣ��" + psb.toString());

		Connection con = null;
		try {
			con = DbUtils.getConnection();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			// ������޸ĵĻ��������޸ĳɹ�������
			saveNums = PoliticalStatusDao.PoliticalStatusModify(con, psb);
			if (saveNums > 0) {
				result.put("success", true);
			} else {
				result.put("success", false);
				result.put("errorMsg", "�޸�������ò��Ϣʧ�ܣ�");
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
	 * ���ܣ���ѯ������ò��Ϣ�б�
	 * �������ڣ�2017-5-9-����1:18:49
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void PoliticalStatusListInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("��ѯ������ò��Ϣ�б�");
		String page = request.getParameter("page");// ȡ������Ĳ���
		String rows = request.getParameter("rows");
		String PS_Name = request.getParameter("PS_Name");
		String PS_TYPE = request.getParameter("PS_TYPE");
		String Ext1 = request.getParameter("Ext1");
		String Ext2 = request.getParameter("Ext2");
		String Ext3 = request.getParameter("Ext3");
		if (PS_Name == null) {
			PS_Name = "";
		}
		PoliticalStatusBean grade = new PoliticalStatusBean();
		grade.setPs_name(PS_Name);
		grade.setPs_type(PS_TYPE);
		grade.setExt1(Ext1);
		grade.setExt2(Ext2);
		grade.setExt3(Ext3);
		PageBean pageBean = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil
					.formatRsToJsonArray(PoliticalStatusDao
							.politicalStatusList(con, pageBean, grade));// ȡ��json����
			int total = PoliticalStatusDao.PoliticalStatusCount(con, grade);// �ܼ�¼��
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
	 * ���ܣ���ѯ������ò��������Ϣ
	 * �������ڣ�2017-5-9-����1:19:22
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void PoliticalStatusComboboxInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("��ѯ������ò��������Ϣ");

		Connection con = null;
		try {
			con = DbUtils.getConnection();
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("PS_TYPE", "");
			jsonObject.put("PS_Name", "��ѡ��...");
			jsonArray.add(jsonObject);
			// ������������
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(PoliticalStatusDao
					.politicalStatusList(con, null, null)));// ȡ��json����
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
