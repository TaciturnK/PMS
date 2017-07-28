package com.pms.util;

import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Taowd
 * ��        �ܣ�Servlet��һ���������
 * ��дʱ�䣺2017-5-3-����2:05:21
 */
public abstract class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		/**
		 * 1����ȡ����������ʶ���û��������Ǹ�������
		 * 2��Ȼ���ж��Ǹ�����ʹ��������õģ����ý��д���
		 */
		String methodName = request.getParameter("method");
		if (methodName == null || methodName.trim().isEmpty()) {
			throw new RuntimeException("��û�д���method�������޷�ȷ������Ҫ���õķ�����");
		}

		/**
		 * �õ����������Ƿ�ͨ��������÷���
		 */
		Class<? extends BaseServlet> c = this.getClass();
		Method method = null;
		try {
			method = c.getMethod(methodName, HttpServletRequest.class,
					HttpServletResponse.class);
		} catch (Exception e) {
			throw new RuntimeException("��Ҫ���õķ���:" + methodName + ":������");
		}

		try {
			Log4jHelper
					.info("�������Servlet�����࣬����ɹ����ɣ�[" + methodName + "]��������ִ��");
			// ���з������
			String result = (String) method.invoke(this, request, response);

			if (result == null || result.trim().isEmpty()) {
				return;
			}

			if (result.contains(":")) {
				int index = result.indexOf(":");
				String s = result.substring(0, index);// ȡ��ð��ǰ׺
				String path = result.substring(index + 1);// ȡ��ð�ź�׺
				if ("r".equals(s)) {
					response.sendRedirect(request.getContextPath() + path);
				} else if ("f".equals(s)) {
					request.getRequestDispatcher(path).forward(request,
							response);
				} else {
					throw new RuntimeException("�ݲ�֧��");
				}

			} else// û��ð�ţ�Ĭ��ת��
			{
				request.getRequestDispatcher(result).forward(request, response);
			}
		} catch (Exception e) {
			System.out.println("��Ҫ���õķ���:" + methodName + ":�ڲ��쳣");
			throw new RuntimeException(e);
		}

	}
}
