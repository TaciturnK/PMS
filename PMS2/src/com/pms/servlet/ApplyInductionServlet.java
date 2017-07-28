package com.pms.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.pms.dao.InductionInfoDao;
import com.pms.model.Employee;
import com.pms.model.InductionInfoBean;
import com.pms.model.PageBean;
import com.pms.util.BaseServlet;
import com.pms.util.DateUtil;
import com.pms.util.DbUtils;
import com.pms.util.JsonUtil;
import com.pms.util.Log4jHelper;
import com.pms.util.ResponseUtil;
import com.pms.util.StringUtil;

/**
 * 
 * @author Taowd
 * ��        �ܣ���λ����Ĵ��������������ְλ���룬�޸ģ�ɾ�����ύ�������Ȳ���
 * ��дʱ�䣺2017-5-7-����2:34:57
 */
public class ApplyInductionServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	InductionInfoDao inductionDao = new InductionInfoDao();

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ѯ����Ա��Ϣ-ֻ��ѯ�Ѿ��ύ����Ϣ
	 * �������ڣ�2017-5-7-����7:09:49
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void AdminApproveInfoList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		String page = request.getParameter("page");// ȡ������Ĳ���
		String rows = request.getParameter("rows");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String EMP_NO = request.getParameter("EMP_NO");
		String approveState = request.getParameter("approveState");
		String IND_STATE = request.getParameter("IND_STATE");
		String POS_NAME = request.getParameter("POS_NAME");

		InductionInfoBean inductionBean = new InductionInfoBean();

		inductionBean.setEmp_No(EMP_NO);
		inductionBean.setExt1(approveState);
		inductionBean.setInd_State(IND_STATE);
		inductionBean.setPos_Name(POS_NAME);

		// ����Ա��������ʱ��
		inductionBean.setExt1(approveState);
		PageBean pageBean = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(InductionInfoDao
					.AdminInductionInfoList(con, pageBean, inductionBean,
							startDate, endDate));// ȡ��json����
			int total = InductionInfoDao.InductionInfoCount(con, inductionBean);// �ܼ�¼��
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
	 * ���ܣ����˻�ȡ��Ϣ�б�
	 * �������ڣ�2017-5-7-����7:02:23
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void UserApproveInfoList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		String page = request.getParameter("page");// ȡ������Ĳ���
		String rows = request.getParameter("rows");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String EMP_NO = request.getParameter("EMP_NO");
		String approveState = request.getParameter("approveState");
		String IND_STATE = request.getParameter("IND_STATE");
		String POS_NAME = request.getParameter("POS_NAME");

		HttpSession session = request.getSession();

		InductionInfoBean inductionBean = new InductionInfoBean();

		inductionBean.setEmp_No(EMP_NO);
		inductionBean.setExt1(approveState);
		inductionBean.setInd_State(IND_STATE);
		inductionBean.setPos_Name(POS_NAME);

		Employee userInfo = (Employee) session.getAttribute("currentUser");

		inductionBean.setEmp_No(userInfo.getEmp_no());
		PageBean pageBean = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(InductionInfoDao
					.InductionInfoList(con, pageBean, inductionBean, startDate,
							endDate));// ȡ��json����
			int total = InductionInfoDao.InductionInfoCount(con, inductionBean);// �ܼ�¼��
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
	 * ���ܣ���ͨ�û��ύ������Ϣ
	 * �������ڣ�2017-5-7-����2:38:35
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void UserApprove(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("��ͨ�û��ύ������Ϣ");
		request.setCharacterEncoding("utf-8");
		InductionInfoBean induction = new InductionInfoBean();
		String delIds = request.getParameter("delIds");// ȡ��ɾ����id�ַ�������
		try {
			String[] str = delIds.split(",");
			JSONObject result = new JSONObject();
			int intData = str.length;
			for (int i = 0; i < str.length; i++) {
				// Ϊtrue��ɾ��
				boolean f = InductionInfoDao.CheckApproveState(str[i]);
				if (!f) {
					result.put("success", false);
					result.put("errorIndex", i);
					result.put("errorMsg", "���������ύ,�����ظ��ύ");
					intData--;
				} else {
					induction.setInd_Id(str[i]);
					// 00-���ύ
					induction.setExt1("00");
					int delNums = InductionInfoDao
							.ApplyInductionApprove(induction);// ��������ɾ��������
					if (delNums > 0) {
						result.put("success", true);
						result.put("delNums", intData);
					} else {
						result.put("success", false);
						result.put("errorMsg", "�ύʧ��");
						intData--;
					}
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
	 * ���ܣ�ɾ�����˵�������Ϣ
	 * �������ڣ�2017-5-7-����3:46:49
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void DeleteUserApplyInduction(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("***************����ɾ��������***********************");
		request.setCharacterEncoding("utf-8");
		String delIds = request.getParameter("delIds");// ȡ��ɾ����id�ַ�������
		try {

			String[] str = delIds.split(",");
			JSONObject result = new JSONObject();
			for (int i = 0; i < str.length; i++) {
				// Ϊtrue��ɾ��
				boolean f = InductionInfoDao.CheckApproveState(str[i]);
				if (!f) {
					result.put("success", false);
					result.put("errorIndex", i);
					result.put("errorMsg", "�������ύ,����ɾ��");
					ResponseUtil.write(response, result);
					return;

				}
			}
			int delNums = InductionInfoDao.InductionDelete(StringUtil
					.FormatDeleteDelIds(delIds));// ��������ɾ��������
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
		}

	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����Աɾ��������Ϣ
	 * �������ڣ�2017-5-7-����4:04:02
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void DeleteAdminApplyInduction(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String delIds = request.getParameter("delIds");// ȡ��ɾ����id�ַ�������
		try {
			JSONObject result = new JSONObject();
			int delNums = InductionInfoDao.InductionDelete(StringUtil
					.FormatDeleteDelIds(delIds));// ��������ɾ��������
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
		}
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����ְλ��Ϣ����--��������ְ����
	 * �������ڣ�2017-5-7-����5:17:19
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void AddUserApplyInduction(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("����ְλ������Ϣ�������߼�");
		// DEP_ID
		request.setCharacterEncoding("utf-8");
		String pos_Id = request.getParameter("POS_ID");
		String ind_ID = request.getParameter("IND_ID");
		String applyStyle = request.getParameter("EXT3");

		InductionInfoBean induction = new InductionInfoBean();
		induction.setPos_Id(pos_Id);
		induction.setInd_Id(ind_ID);
		HttpSession session = request.getSession();
		Employee userInfo = (Employee) session.getAttribute("currentUser");
		induction.setEmp_No(userInfo.getEmp_no());
		// ����ʱ����״̬Ϊ��33-δ�ύ
		induction.setExt1("33");
		// ��ְ���IN-��ְ����
		if (applyStyle.equals("IN")) {
			induction.setExt3("IN");
		} else {
			induction.setExt3("OUT");
		}

		Log4jHelper.info("������Ϣ��" + induction.toString());

		try {
			int saveNums = 0;
			boolean isExistenceFlag = false;
			if (StringUtil.isNotEmpty(induction.getInd_Id())) {
				// ���ò����Ƿ��Ѿ�������ˣ����������ˣ��Ͳ������ٴ�����
				isExistenceFlag = InductionInfoDao.IsExistence(induction);
			}

			JSONObject result = new JSONObject();
			// �������Ա�����Ƿ��Ѿ�ע��
			if (isExistenceFlag) {
				result.put("success", false);
				result.put("errorMsg", "�ò��Ŵ���������Ѿ����룬�����ظ����룡");
			} else {
				// ����Ա�������سɹ�������
				saveNums = InductionInfoDao.ApplyInduction(induction);
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
		}

	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ������û���������Ϣ-ָδ�ύǰ�ɽ����޸�
	 * �������ڣ�2017-5-7-����4:51:40
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void UpdateUserApplyApprove(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("����ְλ������Ϣ���޸��߼�");
		// DEP_ID
		request.setCharacterEncoding("utf-8");
		String pos_Id = request.getParameter("POS_ID");
		String ind_ID = request.getParameter("IND_ID");
		String applyStyle = request.getParameter("EXT3");

		InductionInfoBean induction = new InductionInfoBean();
		induction.setPos_Id(pos_Id);
		induction.setInd_Id(ind_ID);
		HttpSession session = request.getSession();
		Employee userInfo = (Employee) session.getAttribute("currentUser");
		induction.setEmp_No(userInfo.getEmp_no());
		// ����ʱ����״̬Ϊ��33-δ�ύ
		induction.setExt1("33");
		// ��ְ���IN-��ְ����
		if (applyStyle.equals("IN")) {
			induction.setExt3("IN");
		} else {
			induction.setExt3("OUT");
		}

		Log4jHelper.info("������Ϣ��" + induction.toString());

		try {

			int saveNums = 0;
			// ���ò����Ƿ��Ѿ�������ˣ����������ˣ��Ͳ������ٴ�����
			boolean isExistenceFlag = InductionInfoDao.IsExistence(induction);
			JSONObject result = new JSONObject();
			// �������״̬�Ƿ���޸ģ���ext1=="33"??
			if (!InductionInfoDao.CheckApproveState(induction.getInd_Id())) {
				result.put("success", false);
				result.put("errorMsg", "�������Ѿ��ύ�������޸ģ�");

			} else {
				if (isExistenceFlag) {
					result.put("success", false);
					result.put("errorMsg", "�ò��Ŵ���������Ѿ����룬�����ظ����룡");

				} else {
					// ������޸ĵĻ��������޸ĳɹ�������
					saveNums = InductionInfoDao.ApplyInductionModify(induction);
					if (saveNums > 0) {
						result.put("success", true);
					} else {
						result.put("success", false);
						result.put("errorMsg", "������Ϣ�޸�ʧ�ܣ�");
					}
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
	 * ���ܣ�����Ա�޸�������Ϣ
	 * �������ڣ�2017-5-7-����4:39:24
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void AdminUpdateApplyApprove(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("����Ա�޸�������Ϣ");
		request.setCharacterEncoding("utf-8");
		InductionInfoBean induction = new InductionInfoBean();
		String delIds = request.getParameter("delIds");
		String approvalComments = request.getParameter("EXT1");

		try {

			String[] str = delIds.split(",");
			JSONObject result = new JSONObject();
			for (int i = 0; i < str.length; i++) {

				induction.setInd_Id(str[i]);
				induction.setExt1(approvalComments);
				induction.setExt2(DateUtil.getCurrentDateStr());
				// �޸�ʱ ����Ҫ��� �Ƿ���Խ�����������
				// ����Ա������������
				int delNums = InductionInfoDao.ApplyInductionApprove(induction);
				if (delNums > 0) {
					result.put("success", true);
					result.put("delNums", delNums);
				} else {
					result.put("success", false);
					result.put("errorMsg", "�ύʧ��");
				}

			}
			ResponseUtil.write(response, result);// ���͵��ͻ���
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����Ա��������
	 * �������ڣ�2017-5-7-����3:05:43
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void AdminApplyApprove(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		InductionInfoBean induction = new InductionInfoBean();
		String delIds = request.getParameter("delIds");
		String approvalComments = request.getParameter("EXT1");
		// ������޸������յ� update ����������յ��κ��ַ���
		String saveFlag = request.getParameter("saveFlag");

		try {

			String[] str = delIds.split(",");
			JSONObject result = new JSONObject();
			for (int i = 0; i < str.length; i++) {
				// Ϊtrue�ɽ�����������
				boolean f = InductionInfoDao.IsApproveState(str[i]);
				if (!f && !"update".equals(saveFlag)) {
					result.put("success", false);
					result.put("errorIndex", i);
					result.put("errorMsg", "������������,�����ظ�����");
				} else {
					induction.setInd_Id(str[i]);
					induction.setExt1(approvalComments);
					induction.setExt2(DateUtil.getCurrentDateStr());
					// ����Ա������������
					int delNums = InductionInfoDao
							.ApplyInductionApprove(induction);
					if (delNums > 0) {
						result.put("success", true);
						result.put("delNums", delNums);
					} else {
						result.put("success", false);
						result.put("errorMsg", "�ύʧ��");
					}
				}
			}
			ResponseUtil.write(response, result);// ���͵��ͻ���
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
