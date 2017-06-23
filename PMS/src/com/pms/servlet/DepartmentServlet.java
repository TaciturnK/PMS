package com.pms.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.pms.dao.DepartmentDao;
import com.pms.model.DepartmentBean;
import com.pms.model.PageBean;
import com.pms.util.BaseServlet;
import com.pms.util.DbUtils;
import com.pms.util.JsonUtil;
import com.pms.util.Log4jHelper;
import com.pms.util.ResponseUtil;
import com.pms.util.StringUtil;

public class DepartmentServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����������Ϣ
	 * �������ڣ�2017-5-8-����5:17:13
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void AddDepartment(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("���벿����Ϣ�����Ĳ���");

		request.setCharacterEncoding("utf-8");
		String DEP_ID = request.getParameter("DEP_ID");
		String DEP_NAME = request.getParameter("DEP_NAME");
		String DEP_LEADER = request.getParameter("DEP_LEADER");

		DepartmentBean db = new DepartmentBean(DEP_ID, DEP_NAME, DEP_LEADER);

		if (DEP_LEADER == null || DEP_LEADER.equals("")) {
			db.setDep_Leader(null);
		}

		Connection con = null;
		try {
			con = DbUtils.getConnection();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			// ��鲿����Ϣ�Ƿ��Ѿ����ӣ���Ҫ�Ǹ��ݲ���ID���м��
			if (DepartmentDao.IsExistence(con, DEP_ID)) {
				result.put("success", false);
				result.put("errorMsg", "�ò�����Ϣ�Ѵ��ڣ�");
			} else {
				db.setDep_Id("POS" + StringUtil.GetUUID());
				// ����������Ϣ�����سɹ�������
				saveNums = DepartmentDao.DepartmentAdd(con, db);
				if (saveNums > 0) {
					result.put("success", true);
				} else {
					result.put("success", false);
					result.put("errorMsg", "����������Ϣʧ��");
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
	 * ���ܣ�ɾ��������Ϣ
	 * �������ڣ�2017-5-8-����5:18:27
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void DeleteDepartment(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String delIds = request.getParameter("delIds");// ȡ��ɾ����id�ַ�������
		Log4jHelper.info("����ɾ��������");
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			String[] str = delIds.split(",");
			JSONObject result = new JSONObject();
			for (int i = 0; i < str.length; i++) {
				boolean f = DepartmentDao.getPosInfoByDepId(con, str[i]);
				if (f) {
					result.put("success", false);
					result.put("errorIndex", i);
					result.put("errorMsg", "���Ŵ��ڸ�λ��Ϣ������ɾ����");
					ResponseUtil.write(response, result);
					return;
				}
			}
			int delNums = DepartmentDao.DepartmentDelete(con,
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
			e.printStackTrace();
		} finally {
			try {
				DbUtils.CloseConn(con);// �ر�����
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ��޸Ĳ�����Ϣ
	 * �������ڣ�2017-5-8-����5:17:33
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void UpdateDepartment(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("���벿����Ϣ���µĲ���");

		request.setCharacterEncoding("utf-8");
		String DEP_ID = request.getParameter("DEP_ID");
		String DEP_NAME = request.getParameter("DEP_NAME");
		String DEP_LEADER = request.getParameter("DEP_LEADER");

		DepartmentBean db = new DepartmentBean(DEP_ID, DEP_NAME, DEP_LEADER);

		if (DEP_LEADER == null || DEP_LEADER.equals("")) {
			db.setDep_Leader(null);
		}

		Connection con = null;
		try {
			con = DbUtils.getConnection();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			// ������޸ĵĻ��������޸ĳɹ�������
			saveNums = DepartmentDao.DepartmentModify(con, db);
			if (saveNums > 0) {
				result.put("success", true);
			} else {
				result.put("success", false);
				result.put("errorMsg", "�޸Ĳ�����Ϣʧ�ܣ�");
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
	 * ���ܣ�������Ϣ�б�
	 * �������ڣ�2017-5-8-����5:19:01
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void DepartmentListInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("�����ȡ������Ϣ�б�Ĳ���");
		request.setCharacterEncoding("utf-8");
		String page = request.getParameter("page");// ȡ������Ĳ���
		String rows = request.getParameter("rows");
		String dep_Id = request.getParameter("DEP_ID");
		String dep_Name = request.getParameter("DEP_NAME");
		String dep_Leader = request.getParameter("DEP_LEADER");

		DepartmentBean grade = new DepartmentBean();
		grade.setDep_Id(dep_Id);
		grade.setDep_Name(dep_Name);
		if (dep_Leader != null && !dep_Leader.equals("��ѡ��...")) {
			grade.setDep_Leader(dep_Leader);
		}

		PageBean pageBean = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(DepartmentDao
					.DepartmentList(con, pageBean, grade));// ȡ��json����
			int total = DepartmentDao.DepartmentCount(con, grade);// �ܼ�¼��
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
	 * ���ܣ���ȡ����������Ϣ
	 * �������ڣ�2017-5-8-����5:19:36
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void DepartmentComboboxInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Log4jHelper.info("�����ȡ�������������ݵĲ���");
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("DEP_ID", "");
			jsonObject.put("DEP_NAME", "��ѡ��...");
			jsonArray.add(jsonObject);
			// ������������
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(DepartmentDao
					.DepartmentList(con, null, null)));// ȡ��json����
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