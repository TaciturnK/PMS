package com.pms.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.pms.dao.AdministratorDao;
import com.pms.dao.EmployeeDao;
import com.pms.model.Administrator;
import com.pms.model.Employee;
import com.pms.model.PageBean;
import com.pms.util.DbUtils;
import com.pms.util.JsonUtil;
import com.pms.util.Log4jHelper;
import com.pms.util.ResponseUtil;

/**
 * 
 * @author Taowd
 * ��        �ܣ���ȡ������Ϣ
 * ��дʱ�䣺2017-4-11-����9:09:20
 */
public class PersionInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	EmployeeDao empDao = new EmployeeDao();
	AdministratorDao adminDao = new AdministratorDao();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Log4jHelper.info("��ȡ������Ϣ----");
		HttpSession session = request.getSession();
		String ro = (String) session.getAttribute("userRole");

		if ("user".equals(ro)) {
			Employee userInfo = (Employee) session.getAttribute("currentUser");
			Employee emp = new Employee();
			emp.setEmp_no(userInfo.getEmp_no());
			String page = request.getParameter("page");// ȡ������Ĳ���
			String rows = request.getParameter("rows");
			PageBean pageBean = new PageBean(Integer.parseInt(page),
					Integer.parseInt(rows));
			Connection con = null;
			try {
				con = DbUtils.getConnection();
				JSONObject result = new JSONObject();
				JSONArray jsonArray = JsonUtil.formatRsToJsonArray(EmployeeDao
						.EmployeetPersionInfo(con, pageBean, emp));// ȡ��json����
				int total = EmployeeDao.EmployeeCount(con, emp, null, null);// �ܼ�¼��
				result.put("rows", jsonArray);// ��װ����
				result.put("total", total);
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
		} else if ("admin".equals(ro) || "superAdmin".equals(ro)) {
			Administrator adminInfo = (Administrator) session
					.getAttribute("currentUser");
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
				e.printStackTrace();
			} finally {
				try {
					DbUtils.CloseConn(con);// �ر�����
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

}
