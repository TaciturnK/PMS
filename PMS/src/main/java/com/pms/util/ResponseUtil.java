package com.pms.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author ��ΰ��
 * TODO����װjson���ݷ��������� �ͻ���
 * ��дʱ�䣺����9:53:45
 */
public class ResponseUtil {

	/**
	 * ��װjson���ݷ��������� �ͻ���
	 * @param response
	 * @param o
	 * @throws Exception
	 */
	public static void write(HttpServletResponse response, Object o)
			throws Exception {
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		System.out.println("����ҳ���JSON���ݣ�" + o.toString());
		out.println(o.toString());
		out.flush();
		out.close();
	}
}
