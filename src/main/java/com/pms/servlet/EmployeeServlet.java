package com.pms.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.pms.dao.EmployeeDao;
import com.pms.model.Employee;
import com.pms.model.PageBean;
import com.pms.util.AESUtil;
import com.pms.util.BaseServlet;
import com.pms.util.DateUtil;
import com.pms.util.DbUtils;
import com.pms.util.JsonUtil;
import com.pms.util.Log4jHelper;
import com.pms.util.ResponseUtil;
import com.pms.util.StringUtil;

public class EmployeeServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�������ͨԱ����Ϣ
	 * �������ڣ�2017-5-7-����7:25:11
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void AddEmployee(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Log4jHelper.info("-----------���뱣��Ա����Ϣ�Ŀ�����-----------");
		request.setCharacterEncoding("utf-8");
		String stuNo = request.getParameter("EMP_NO");
		String passwd = request.getParameter("passwd");
		String stuName = request.getParameter("EMP_NAME");
		String sex = request.getParameter("EMP_SEX");
		String birthday = request.getParameter("EMP_Birthday");
		String ps_id = request.getParameter("PS_TYPE");
		String emp_phone = request.getParameter("EMP_Phone");
		String emp_address = request.getParameter("EMP_Address");
		String ext1 = request.getParameter("ext1");
		String ext2 = request.getParameter("ext2");
		String ext3 = request.getParameter("ext3");

		String encyPasswd = "";
		if (!StringUtils.isEmpty(passwd)) {
			encyPasswd = AESUtil.parseByte2HexStr(AESUtil.encrypt(passwd));
		}

		Employee empBean = null;
		try {
			empBean = new Employee(stuNo, encyPasswd, stuName, sex,
					DateUtil.formatString(birthday, "yyyy-MM-dd"), ps_id,
					emp_phone, emp_address, ext1, ext2, ext3);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Connection con = null;
		try {
			con = DbUtils.getConnection();
			int saveNums = 0;
			// ���stuNo�Ƿ��Ѿ�����
			boolean isExistenceFlag = EmployeeDao.IsExistence(con, stuNo);
			JSONObject result = new JSONObject();

			if (isExistenceFlag) {
				result.put("success", false);
				result.put("errorMsg", "Ա�����Ѵ��ڣ�");
			} else {
				// ����Ա�������سɹ�������
				saveNums = EmployeeDao.EmployeeAdd(con, empBean);
				if (saveNums > 0) {
					result.put("success", true);
				} else {
					result.put("success", false);
					result.put("errorMsg", "����Ա����Ϣʧ��");
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
	 * ���ܣ���ͨԱ��ע����Ϣ
	 * �������ڣ�2017-5-7-����7:26:00
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void RegisterEmployee(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		String empNo = request.getParameter("stuNo");
		String passwd = request.getParameter("passwd");
		String stuName = request.getParameter("stuName");
		String sex = request.getParameter("sex");
		String birthday = request.getParameter("birthday");
		String ps_id = request.getParameter("ps_id");
		String emp_phone = request.getParameter("phone");
		String emp_address = request.getParameter("address");
		String ext1 = request.getParameter("ext1");
		String ext2 = request.getParameter("ext2");
		String ext3 = request.getParameter("ext3");

		Employee Emp = null;
		try {
			// 2017��4��25��19:33:33���������ܣ�����ʹ��MD5���м���
			Emp = new Employee(empNo, AESUtil.parseByte2HexStr(AESUtil
					.encrypt(passwd)), stuName, sex, DateUtil.formatString(
					birthday, "yyyy-MM-dd"), ps_id, emp_phone, emp_address,
					ext1, ext2, ext3);
		} catch (Exception e) {
			Log4jHelper.exception(e);
		}

		Connection con = null;
		try {
			con = DbUtils.getConnection();
			boolean isExistenceFlag = false;
			JSONObject result = new JSONObject();
			isExistenceFlag = EmployeeDao.IsExistence(con, empNo);// ���ز���ɹ�������
			if (!isExistenceFlag) {
				// ���Խ���ע��
				int saveNums = 0;
				saveNums = EmployeeDao.EmployeeRegister(con, Emp);// ���ز���ɹ�������
				if (saveNums > 0) {
					result.put("success", true);
				} else {
					result.put("success", false);
					result.put("errorMsg", "����ʧ��");
				}
				ResponseUtil.write(response, result);// ���͵��ͻ���
			} else {
				// �����Խ���ע�ᣬ���ر���
				result.put("success", false);
				result.put("errorMsg", "���û��Ѵ���");
				ResponseUtil.write(response, result);// ���͵��ͻ���
			}

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
	 * ���ܣ�ɾ����ͨԱ����Ϣ
	 * �������ڣ�2017-5-7-����7:27:05
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void DeleteEmployee(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String delIds = request.getParameter("delIds");// ȡ��ɾ����id�ַ�������

		Log4jHelper.info("ɾ����Ա�����ţ�" + delIds);
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			String[] str = delIds.split(",");
			JSONObject result = new JSONObject();
			for (int i = 0; i < str.length; i++) {
				// ����Ƿ���ְ�����������ľͿ���ɾ�������磺��������ְ����δ�ύ������Ա��ǿ��ɾ��
				boolean f = EmployeeDao.IsInduction(con, str[i]);
				if (f) {
					result.put("success", false);
					result.put("errorIndex", i);
					result.put("errorMsg", "��ְ����������ͨ����������ɾ��");
					ResponseUtil.write(response, result);
					return;
				}
				// ����Ƿ�Ϊ�����쵼
				boolean f2 = EmployeeDao.IsLeader(con, str[i]);
				if (f2) {
					result.put("success", false);
					result.put("errorIndex", i);
					result.put("errorMsg", "��Ա��Ϊ�����쵼��������ɾ��");
					ResponseUtil.write(response, result);
					return;
				}
			}

			int delNums = EmployeeDao.EmlopyeeDelete(con,
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
	 * ���ܣ�������ͨԱ����Ϣ
	 * �������ڣ�2017-5-7-����7:26:51
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void UpdateEmployee(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Log4jHelper.info("-----------���뱣��Ա����Ϣ�Ŀ�����-----------");
		request.setCharacterEncoding("utf-8");
		String stuNo = request.getParameter("EMP_NO");
		String passwd = request.getParameter("passwd");
		String stuName = request.getParameter("EMP_NAME");
		String sex = request.getParameter("EMP_SEX");
		String birthday = request.getParameter("EMP_Birthday");
		String ps_id = request.getParameter("PS_TYPE");
		String emp_phone = request.getParameter("EMP_Phone");
		String emp_address = request.getParameter("EMP_Address");
		String ext1 = request.getParameter("ext1");
		String ext2 = request.getParameter("ext2");
		String ext3 = request.getParameter("ext3");

		String encyPasswd = "";
		if (!StringUtils.isEmpty(passwd)) {
			encyPasswd = AESUtil.parseByte2HexStr(AESUtil.encrypt(passwd));
		}

		Employee empBean = null;
		try {
			empBean = new Employee(stuNo, encyPasswd, stuName, sex,
					DateUtil.formatString(birthday, "yyyy-MM-dd"), ps_id,
					emp_phone, emp_address, ext1, ext2, ext3);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Connection con = null;
		try {
			con = DbUtils.getConnection();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			// ������޸ĵĻ��������޸ĳɹ�������
			saveNums = EmployeeDao.EmployeeModify(con, empBean);
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
	 * ���ܣ���ȡ��ͨԱ�������б�--ֻ��ȡԱ���ź�Ա������
	 * �������ڣ�2017-5-7-����7:27:47
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void ComboListEmployee(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Log4jHelper.info("���룬��ȡ��ȡ�����쵼�������ݵĴ����߼�");
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("EMP_NO", "");
			jsonObject.put("EMP_NAME", "��ѡ��...");
			jsonArray.add(jsonObject);
			// ������������
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(EmployeeDao
					.EmployeetList(con, null, null, null, null)));// ȡ��json����
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

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ȡԱ���б���Ϣ
	 * �������ڣ�2017-5-7-����7:29:11
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void EmployeeListInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		String stuNo = request.getParameter("EMP_NO");
		String stuName = request.getParameter("EMP_NAME");
		String sex = request.getParameter("EMP_SEX");
		String bbirthday = request.getParameter("bbirthday");
		String ebirthday = request.getParameter("ebirthday");
		String gradeId = request.getParameter("PS_TYPE");
		// String emp_phone = request.getParameter("EMP_Phone");
		// String emp_address = request.getParameter("EMP_Address");

		Employee emp = new Employee();
		if (gradeId != null) {
			emp.setEmp_no(stuNo);
			emp.setEmp_name(stuName);
			emp.setEmp_sex(sex);
			if (StringUtil.isNotEmpty(gradeId)) {
				emp.setPs_id(gradeId);
			}
		}

		String page = request.getParameter("page");// ȡ������Ĳ���
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(EmployeeDao
					.EmployeetList(con, pageBean, emp, bbirthday, ebirthday));// ȡ��json����
			int total = EmployeeDao.EmployeeCount(con, emp, bbirthday,
					ebirthday);// �ܼ�¼��
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
}
