package com.pms.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.pms.dao.AdministratorDao;
import com.pms.dao.EmployeeDao;
import com.pms.model.Administrator;
import com.pms.model.Employee;
import com.pms.util.AESUtil;
import com.pms.util.DbUtils;
import com.pms.util.Log4jHelper;

/**
 * 登录的业务处理逻辑
 * @author Taowd
 * @version 2018年8月31日
 * @see LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 管理员操作
	 */
	private AdministratorDao adminDao = new AdministratorDao();
	/**
	 * 职工管理
	 */
	private EmployeeDao empDao = new EmployeeDao();

	/**
	 * get请求
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * Post请求
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String empNO = request.getParameter("userName");
		String password = request.getParameter("password");
		// 角色
		String role = request.getParameter("role");
		Log4jHelper.info("--登录用户为：" + empNO + "--角色：" + role);

		// 根据角色不同进行校验
		// 如果是普通用户
		if ("user".equals(role)) {

			if (StringUtils.isEmpty(empNO) || StringUtils.isEmpty(password)) {
				request.setAttribute("error", "用户名或密码为空");
				request.getRequestDispatcher("index.jsp").forward(request, response);
				return;
			}

			Employee emp = new Employee();
			emp.setEmp_no(empNO);
			emp.setEmp_pwd(AESUtil.parseByte2HexStr(AESUtil.encrypt(password)));
			Connection conn = null;
			try {
				conn = DbUtils.getConnection();
				// 检查密码
				Employee currentUser = empDao.CheckPwd(conn, emp);
				if (currentUser == null) {
					request.setAttribute("error", "用户名或密码错误");
					request.getRequestDispatcher("index.jsp").forward(request, response);
				} else {
					// 验证成功之后，登录主页面之后传过去的值
					request.setAttribute("userName", empNO);
					request.setAttribute("password", password);
					HttpSession session = request.getSession();
					// 当前登录用户的信息存放在Session中
					session.setAttribute("currentUser", currentUser);
					// 登录用户的角色
					session.setAttribute("userRole", role);
					// 成功之后 制定返回页面
					response.sendRedirect("main.jsp");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					DbUtils.CloseConn(conn);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else if ("admin".equals(role) || "superAdmin".equals(role))// 管理员登录 superAdmin
		{
			if (StringUtils.isEmpty(empNO) || StringUtils.isEmpty(password)) {
				request.setAttribute("error", "用户名或密码为空");
				request.getRequestDispatcher("index.jsp").forward(request, response);
				return;
			}

			Administrator user = new Administrator(null, empNO,
					AESUtil.parseByte2HexStr(AESUtil.encrypt(password)), null, null, null, null,
					role);
			Connection conn = null;
			try {
				conn = DbUtils.getConnection();
				Administrator currentUser = adminDao.login(user);
				if (currentUser == null) {
					System.out.println("管理员用户验证：用户名或密码错误");
					request.setAttribute("error", "用户名或密码错误");
					request.getRequestDispatcher("index.jsp").forward(request, response);
				} else {
					// 设置返回页面的值
					request.setAttribute("userName", empNO);
					request.setAttribute("password", password);
					HttpSession session = request.getSession();
					session.setAttribute("currentUser", currentUser);
					session.setAttribute("userRole", role);
					// 成功之后 制定返回页面
					response.sendRedirect("main.jsp");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					DbUtils.CloseConn(conn);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else {
			System.out.println("管理员用户验证：角色不存在");
			request.setAttribute("error", "角色不存在");
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}

	}
}
