package com.pms.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.pms.dao.AdministratorDao;
import com.pms.dao.EmployeeDao;
import com.pms.model.Administrator;
import com.pms.model.Employee;
import com.pms.util.AESUtil;
import com.pms.util.DbUtils;
import com.pms.util.Log4jHelper;
import com.pms.util.ResponseUtil;

/**
 * 
 * @author Taowd
 * ��        �ܣ��޸��û���������߼�
 * ��дʱ�䣺2017-4-12-����1:38:24
 */
public class PasswordModifyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	EmployeeDao empDao = new EmployeeDao();
	AdministratorDao adminDao = new AdministratorDao();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Log4jHelper.info("--------------------�޸����������-----------------------");
		String passwd = request.getParameter("newPassword");
		HttpSession session = request.getSession();
		String ro = (String) session.getAttribute("userRole");

		int saveNums = 0;
		if ("user".equals(ro))// ��ͨ�û��޸�����
		{
			Employee userInfo = (Employee) session.getAttribute("currentUser");
			userInfo.setEmp_pwd(AESUtil.parseByte2HexStr(AESUtil
					.encrypt(passwd)));

			Connection con = null;
			JSONObject result = new JSONObject();
			try {
				con = DbUtils.getConnection();
				saveNums = EmployeeDao.EmployeeModify(con, userInfo);
				if (saveNums > 0) {
					result.put("success", true);
				} else {
					result.put("success", false);
					result.put("errorMsg", "�޸�����ʧ�ܣ�");
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
			Log4jHelper.info("��ͨ�û���Ϣ��" + userInfo.toString());

		} else // ����Ա�޸������߼�
		{
			Administrator adminInfo = (Administrator) session
					.getAttribute("currentUser");
			adminInfo.setAdmin_pwd(AESUtil.parseByte2HexStr(AESUtil
					.encrypt(passwd)));
			Connection con = null;
			JSONObject result = new JSONObject();
			try {
				con = DbUtils.getConnection();
				saveNums = adminDao.AdminModifyPasswd(adminInfo);
				if (saveNums > 0) {
					result.put("success", true);
				} else {
					result.put("success", false);
					result.put("errorMsg", "�޸�����ʧ�ܣ�");
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
			Log4jHelper.info("����Ա�û���Ϣ��" + adminInfo.toString());
		}
	}
}
