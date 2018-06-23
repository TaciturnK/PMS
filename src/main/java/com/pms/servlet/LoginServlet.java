package com.pms.servlet;

import com.pms.dao.AdministratorDao;
import com.pms.dao.EmployeeDao;
import com.pms.model.Administrator;
import com.pms.model.Employee;
import com.pms.util.AESUtil;
import com.pms.util.DbUtils;
import com.pms.util.Log4jHelper;
import com.pms.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

/**
 * @author ��ΰ��
 * TODO����¼��ҵ�����߼�
 * ��дʱ�䣺����10:00:40
 */
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    AdministratorDao adminDao = new AdministratorDao();
    EmployeeDao EmpDao = new EmployeeDao();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String empNO = request.getParameter("userName");
        String password = request.getParameter("password");
        // ��ɫ
        String role = request.getParameter("role");
        Log4jHelper.info("--��¼�û�Ϊ��" + empNO + "--��ɫ��" + role);

        // ���ݽ�ɫ��ͬ����У��
        // �������ͨ�û�
        if ("user".equals(role)) {

            if (StringUtil.isEmpty(empNO) || StringUtil.isEmpty(password)) {
                request.setAttribute("error", "�û���������Ϊ��");
                request.getRequestDispatcher("index.jsp").forward(request,
                        response);
                return;
            }

            Employee emp = new Employee();
            emp.setEmp_no(empNO);
            emp.setEmp_pwd(AESUtil.parseByte2HexStr(AESUtil.encrypt(password)));
            Connection conn = null;
            try {
                conn = DbUtils.getConnection();
                // �������
                Employee currentUser = EmpDao.CheckPwd(conn, emp);
                if (currentUser == null) {
                    request.setAttribute("error", "�û������������");
                    request.getRequestDispatcher("index.jsp").forward(request,
                            response);
                } else {
                    // ��֤�ɹ�֮�󣬵�¼��ҳ��֮�󴫹�ȥ��ֵ
                    request.setAttribute("userName", empNO);
                    request.setAttribute("password", password);
                    HttpSession session = request.getSession();
                    // ��ǰ��¼�û�����Ϣ�����Session��
                    session.setAttribute("currentUser", currentUser);
                    // ��¼�û��Ľ�ɫ
                    session.setAttribute("userRole", role);
                    // �ɹ�֮�� �ƶ�����ҳ��
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

        } else if ("admin".equals(role) || "superAdmin".equals(role))// ����Ա��¼ superAdmin
        {
            if (StringUtil.isEmpty(empNO) || StringUtil.isEmpty(password)) {
                request.setAttribute("error", "�û���������Ϊ��");
                request.getRequestDispatcher("index.jsp").forward(request,
                        response);
                return;
            }

            Administrator user = new Administrator(null, empNO,
                    AESUtil.parseByte2HexStr(AESUtil.encrypt(password)), null,
                    null, null, null, role);
            Connection conn = null;
            try {
                conn = DbUtils.getConnection();
                Administrator currentUser = adminDao.login(user);
                if (currentUser == null) {
                    System.out.println("����Ա�û���֤���û������������");
                    request.setAttribute("error", "�û������������");
                    request.getRequestDispatcher("index.jsp").forward(request,
                            response);
                } else {
                    // ���÷���ҳ���ֵ
                    request.setAttribute("userName", empNO);
                    request.setAttribute("password", password);
                    HttpSession session = request.getSession();
                    session.setAttribute("currentUser", currentUser);
                    session.setAttribute("userRole", role);
                    // �ɹ�֮�� �ƶ�����ҳ��
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
            System.out.println("����Ա�û���֤����ɫ������");
            request.setAttribute("error", "��ɫ������");
            request.getRequestDispatcher("index.jsp").forward(request,
                    response);
        }

    }
}
