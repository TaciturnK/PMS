package com.pms.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.pms.dao.NoticeDao;
import com.pms.model.Administrator;
import com.pms.model.NoticeBean;
import com.pms.model.PageBean;
import com.pms.util.BaseServlet;
import com.pms.util.DbUtils;
import com.pms.util.JsonUtil;
import com.pms.util.Log4jHelper;
import com.pms.util.ResponseUtil;
import com.pms.util.StringUtil;

/**
 * 
 * @author Taowd
 * ��        �ܣ�������Ϣ������
 * ��дʱ�䣺2017-5-9-����8:25:18
 */
public class NoticeServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����������Ϣ
	 * �������ڣ�2017-5-9-����8:25:01
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void AddNotice(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Log4jHelper.info("���빫����Ϣ�����Ĳ���");

		request.setCharacterEncoding("utf-8");
		String NOT_ID = request.getParameter("NOT_ID");
		// String NOT_AUTHOR = request.getParameter("NOT_AUTHOR");
		String NOT_TITLE = request.getParameter("NOT_TITLE");
		String NOT_CONTENT = request.getParameter("NOT_CONTENT");

		HttpSession session = request.getSession();
		Administrator adminInfo = (Administrator) session
				.getAttribute("currentUser");

		NoticeBean db = new NoticeBean(NOT_ID, NOT_TITLE, NOT_CONTENT, null,
				adminInfo.getAdmin_id(), null, null, null);
		Log4jHelper.info("������Ϣ��" + db.toString());

		Connection con = null;
		try {
			con = DbUtils.getConnection();
			int saveNums = 0;
			// ���stuNo�Ƿ��Ѿ�����
			boolean isExistenceFlag = NoticeDao.IsExistence(con, NOT_ID);
			JSONObject result = new JSONObject();

			// �������Ա�����Ƿ��Ѿ�ע��
			if (isExistenceFlag) {
				result.put("success", false);
				result.put("errorMsg", "�ù���ID�Ѵ��ڣ�");
			} else {
				db.setNot_Id(StringUtil.GetUUID());
				// ����Ա�������سɹ�������
				saveNums = NoticeDao.NoticeAdd(con, db);
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
	 * �������ڣ�2017-5-9-����8:24:51
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void DeleteNotice(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log4jHelper.info("ɾ��������Ϣ");
		String delIds = request.getParameter("delIds");// ȡ��ɾ����id�ַ�������

		Connection con = null;
		try {
			con = DbUtils.getConnection();
			JSONObject result = new JSONObject();
			int delNums = NoticeDao.NoticeDelete(con,
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
	 * ���ܣ��޸Ĺ�����Ϣ
	 * �������ڣ�2017-5-9-����8:24:41
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void UpdateNotice(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Log4jHelper.info("���빫����Ϣ�޸ĵĲ���");

		request.setCharacterEncoding("utf-8");
		String NOT_ID = request.getParameter("NOT_ID");
		String NOT_TITLE = request.getParameter("NOT_TITLE");
		String NOT_CONTENT = request.getParameter("NOT_CONTENT");

		HttpSession session = request.getSession();
		Administrator adminInfo = (Administrator) session
				.getAttribute("currentUser");

		NoticeBean db = new NoticeBean(NOT_ID, NOT_TITLE, NOT_CONTENT, null,
				adminInfo.getAdmin_id(), null, null, null);
		Log4jHelper.info("�޸���Ϣ��" + db.toString());

		Connection con = null;
		try {
			con = DbUtils.getConnection();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			// ������޸ĵĻ��������޸ĳɹ�������
			saveNums = NoticeDao.NoticeModify(con, db);
			if (saveNums > 0) {
				result.put("success", true);
			} else {
				result.put("success", false);
				result.put("errorMsg", "�޸Ĺ�����Ϣʧ�ܣ�");
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
	 * �������ڣ�2017-5-9-����8:24:20
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void NoticeListInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String page = request.getParameter("page");// ȡ������Ĳ���
		String rows = request.getParameter("rows");
		String not_id = request.getParameter("NOT_ID");
		String not_title = request.getParameter("NOT_TITLE");
		String not_author = request.getParameter("ADMIN_NAME");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String Ext1 = request.getParameter("Ext1");
		String Ext2 = request.getParameter("Ext2");
		String Ext3 = request.getParameter("Ext3");

		NoticeBean notice = new NoticeBean();
		notice.setNot_Id(not_id);
		notice.setNot_Title(not_title);
		notice.setNot_Author(not_author);
		notice.setNot_ext1(Ext1);
		notice.setNot_ext2(Ext2);
		notice.setNot_ext3(Ext3);

		PageBean pageBean = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(NoticeDao
					.NoticeList(con, pageBean, notice, startDate, endDate));// ȡ��json����
			int total = NoticeDao.NoticeCount(con, notice, startDate, endDate);// �ܼ�¼��
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
