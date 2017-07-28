package com.pms.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.pms.dao.AdministratorDao;
import com.pms.model.Administrator;
import com.pms.model.PageBean;
import com.pms.util.AESUtil;
import com.pms.util.BaseServlet;
import com.pms.util.DbUtils;
import com.pms.util.JsonUtil;
import com.pms.util.Log4jHelper;
import com.pms.util.ResponseUtil;

/**
 * 
 * @author Taowd
 * ��        �ܣ�����Ա���������������Ա���ӣ��޸ģ�ɾ������ȡ��Ϣ�б����ó�������Ա��ȡ����������Ա�ȹ���
 * ��дʱ�䣺2017-5-6-����12:02:06
 */
public class AdminServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	AdministratorDao adminDao = new AdministratorDao();
	// ������޸ĵĻ��������޸ĳɹ�������
	int saveNums = 0;

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ϸ����Ա��Ϣ
	 * �������ڣ�2017-5-6-����10:34:29
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void Update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Log4jHelper.info("���¹���Ա��Ϣ");
		request.setCharacterEncoding("utf-8");
		String ADMIN_NO = request.getParameter("ADMIN_NO");
		String ADMIN_ID = request.getParameter("ADMIN_ID");
		String ADMIN_PWD = request.getParameter("newPassword");
		String ADMIN_NAME = request.getParameter("ADMIN_NAME");
		String ADMIN_PHONE = request.getParameter("ADMIN_PHONE");
		String Ext1 = request.getParameter("Ext1");

		Administrator adminBean = new Administrator();
		adminBean.setAdmin_id(ADMIN_ID);
		adminBean.setAdmin_no(ADMIN_NO);
		if (!StringUtils.isEmpty(ADMIN_PWD)) {
			adminBean.setAdmin_pwd(AESUtil.parseByte2HexStr(AESUtil
					.encrypt(ADMIN_PWD)));
		}
		adminBean.setAdmin_name(ADMIN_NAME);
		adminBean.setAdmin_phone(ADMIN_PHONE);
		adminBean.setExt1(Ext1);

		try {
			saveNums = adminDao.AdminModify(adminBean);
			JSONObject result = new JSONObject();
			if (saveNums > 0) {
				result.put("success", true);
			} else {
				result.put("success", false);
				result.put("errorMsg", "�޸�Ա����Ϣʧ�ܣ�");
			}
			ResponseUtil.write(response, result);// ���͵��ͻ���
		} catch (Exception e) {
			Log4jHelper.exception(e);
		}

	}

	public void AdminPersionUpdate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		Log4jHelper.info("���¹���Ա������Ϣ");
		request.setCharacterEncoding("utf-8");
		String ADMIN_NO = request.getParameter("ADMIN_NO");
		String ADMIN_ID = request.getParameter("ADMIN_ID");
		String ADMIN_PWD = request.getParameter("newPassword");
		String ADMIN_NAME = request.getParameter("ADMIN_NAME");
		String ADMIN_PHONE = request.getParameter("ADMIN_PHONE");
		String Ext1 = request.getParameter("Ext1");

		Administrator adminBean = new Administrator();
		adminBean.setAdmin_id(ADMIN_ID);
		adminBean.setAdmin_no(ADMIN_NO);
		if (!StringUtils.isEmpty(ADMIN_PWD)) {
			adminBean.setAdmin_pwd(AESUtil.parseByte2HexStr(AESUtil
					.encrypt(ADMIN_PWD)));
		}
		adminBean.setAdmin_name(ADMIN_NAME);
		adminBean.setAdmin_phone(ADMIN_PHONE);
		adminBean.setExt1(Ext1);

		// ������޸ĵĻ��������޸ĳɹ�������
		try {
			saveNums = adminDao.AdminPersionModify(adminBean);
			JSONObject result = new JSONObject();
			if (saveNums > 0) {
				result.put("success", true);
			} else {
				result.put("success", false);
				result.put("errorMsg", "�޸�Ա����Ϣʧ�ܣ�");
			}
			ResponseUtil.write(response, result);// ���͵��ͻ���
		} catch (Exception e) {
			Log4jHelper.exception(e);
		}

	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����Ա��Ϣ����
	 * �������ڣ�2017-5-5-����8:42:45
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public void AdminAdd(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Log4jHelper.info("��������Ա������Ϣ");
		request.setCharacterEncoding("utf-8");
		String ADMIN_NO = request.getParameter("ADMIN_NO");
		String ADMIN_ID = request.getParameter("ADMIN_ID");
		String ADMIN_PWD = request.getParameter("newPassword");
		String ADMIN_NAME = request.getParameter("ADMIN_NAME");
		String ADMIN_PHONE = request.getParameter("ADMIN_PHONE");
		String Ext1 = request.getParameter("Ext1");

		Administrator adminBean = new Administrator();
		adminBean.setAdmin_id(ADMIN_ID);
		adminBean.setAdmin_no(ADMIN_NO);
		if (!StringUtils.isEmpty(ADMIN_PWD)) {
			adminBean.setAdmin_pwd(AESUtil.parseByte2HexStr(AESUtil
					.encrypt(ADMIN_PWD)));
		}
		adminBean.setAdmin_name(ADMIN_NAME);
		adminBean.setAdmin_phone(ADMIN_PHONE);
		adminBean.setExt1(Ext1);

		// ���stuNo�Ƿ��Ѿ�����
		boolean isExistenceFlag;
		JSONObject result = new JSONObject();
		try {
			isExistenceFlag = adminDao.IsExistence(ADMIN_NO);
			// �������Ա�����Ƿ��Ѿ�ע��
			if (isExistenceFlag) {
				result.put("success", false);
				result.put("errorMsg", "Ա�����Ѵ��ڣ�");
			} else {
				// ����Ա�������سɹ�������
				saveNums = adminDao.AdminAdd(adminBean);
				if (saveNums > 0) {
					result.put("success", true);
				} else {
					result.put("success", false);
					result.put("errorMsg", "����Ա����Ϣʧ��");
				}
			}
			ResponseUtil.write(response, result);// ���͵��ͻ���
		} catch (SQLException e) {
			Log4jHelper.exception(e);
		}

	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�ɾ������Ա��Ϣ
	 * �������ڣ�2017-5-6-����11:38:45
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void AdminDelete(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Log4jHelper.info("����ɾ������Ա�Ŀ�����");
		String delIds = request.getParameter("delIds");// ȡ��ɾ����id�ַ�������
		String[] str = delIds.split(",");
		JSONObject result = new JSONObject();
		for (int i = 0; i < str.length; i++) {
			int delNums = adminDao.AdminInfoDelete(str[i]);// ��������ɾ��������
			if (delNums > 0) {
				result.put("success", true);
				result.put("delNums", delNums);
			} else {
				result.put("success", false);
				result.put("errorMsg", "ɾ��ʧ��");
			}
		}

		ResponseUtil.write(response, result);// ���͵��ͻ���

	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ȡ����Ա��Ϣ�б�
	 * �������ڣ�2017-5-6-����11:51:20
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void AdminInfoList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Log4jHelper.info("��ȡ����Ա��Ϣ�б�");
		Administrator adminInfo = new Administrator();
		String admin_no = request.getParameter("ADMIN_NO");
		String admin_name = request.getParameter("ADMIN_NAME");
		String admin_phone = request.getParameter("ADMIN_PHONE");
		adminInfo.setAdmin_no(admin_no);
		adminInfo.setAdmin_name(admin_name);
		adminInfo.setAdmin_phone(admin_phone);

		String page = request.getParameter("page");// ȡ������Ĳ���
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(adminDao
					.GetAdminInfo(con, pageBean, adminInfo));// ȡ��json����
			int total = adminDao.GetAdminCount(adminInfo);// �ܼ�¼��
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
	 * ���ܣ�ȡ����������Ա
	 * �������ڣ�2017-5-6-����12:08:40
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void SetCancelSuperAdmin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");

		Log4jHelper.info("ȡ����������Ա������");
		String delIds = request.getParameter("delIds");// ȡ�����ó�������Ա�ĵ�id�ַ�������
		try {

			String[] str = delIds.split(",");
			JSONObject result = new JSONObject();
			int successSum = str.length;
			for (int i = 0; i < str.length; i++) {

				boolean f = adminDao.IsSuperAdmin(str[i]);
				if (!f) {
					result.put("success", false);
					result.put("errorIndex", i);
					result.put("errorMsg", "���ǳ�������Ա������ȡ��");
					ResponseUtil.write(response, result);
					return;
				}

				int delNums = adminDao.SetCancelSuperAdmin(str[i]);// ���������޸ĵ�����
				if (delNums > 0) {
					result.put("success", true);
					result.put("delNums", successSum);
				} else {
					successSum--;
					result.put("success", false);
					result.put("errorMsg", "����ʧ��");
				}
			}

			ResponseUtil.write(response, result);// ���͵��ͻ���
		} catch (Exception e) {
			Log4jHelper.exception(e);
		}
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ����ó�������Ա
	 * �������ڣ�2017-5-6-����12:09:13
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void SetSuperAdmin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Log4jHelper.info("���ó�������Ա");
		String delIds = request.getParameter("delIds");// ȡ�����ó�������Ա�ĵ�id�ַ�������
		try {
			String[] str = delIds.split(",");
			JSONObject result = new JSONObject();
			int successSum = str.length;
			for (int i = 0; i < str.length; i++) {

				boolean f = adminDao.IsSuperAdmin(str[i]);
				if (f) {
					result.put("success", false);
					result.put("errorIndex", i);
					result.put("errorMsg", "�Ѿ��ǳ�������Ա�������ظ�����");
					ResponseUtil.write(response, result);
					return;
				}

				int delNums = adminDao.SetSuperAdmin(str[i]);// ��������ɾ��������
				if (delNums > 0) {
					result.put("success", true);
					result.put("delNums", successSum);
				} else {
					successSum--;
					result.put("success", false);
					result.put("errorMsg", "����ʧ��");
				}
			}

			ResponseUtil.write(response, result);// ���͵��ͻ���
		} catch (Exception e) {
			Log4jHelper.exception(e);
		}
	}

}
