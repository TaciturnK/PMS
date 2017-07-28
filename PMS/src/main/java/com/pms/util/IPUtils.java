package com.pms.util;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 
 * @author Taowd
 * ��        �ܣ���ȡIP������
 * ��дʱ�䣺2017-5-5-����2:26:12
 */
public class IPUtils {

	private static String LOCAL_IP_STAR_STR = "192.168.";

	static {
		String ip = null;
		String hostName = null;
		try {
			hostName = InetAddress.getLocalHost().getHostName();
			InetAddress ipAddr[] = InetAddress.getAllByName(hostName);
			for (int i = 0; i < ipAddr.length; i++) {
				ip = ipAddr[i].getHostAddress();
				if (ip.startsWith(LOCAL_IP_STAR_STR)) {
					break;
				}
			}
			if (ip == null) {
				ip = ipAddr[0].getHostAddress();
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		LOCAL_IP = ip;
		HOST_NAME = hostName;

	}

	/**
	 * ϵͳ�ı���IP��ַ
	 */
	public static final String LOCAL_IP;

	/**
	 * ϵͳ�ı��ط�������
	 */
	public static final String HOST_NAME;

	/**
	 * <p>
	 * ��ȡ�ͻ��˵�IP��ַ�ķ����ǣ�request.getRemoteAddr()�����ַ����ڴ󲿷�����¶�����Ч�ġ�
	 * ������ͨ����Apache,Squid�ȷ����������Ͳ��ܻ�ȡ���ͻ��˵���ʵIP��ַ�ˣ����ͨ���˶༶�������Ļ���
	 * X-Forwarded-For��ֵ����ֹһ��������һ��IPֵ�� �����ĸ������������û��˵���ʵIP�أ�
	 * ����ȡX-Forwarded-For�е�һ����unknown����ЧIP�ַ�����
	 * ���磺X-Forwarded-For��192.168.1.110, 192.168.1.120,
	 * 192.168.1.130, 192.168.1.100 �û���ʵIPΪ�� 192.168.1.110
	 * </p>
	 *
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if (ip.equals("127.0.0.1")) {
				/** ��������ȡ�������õ�IP */
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
					ip = inet.getHostAddress();
				} catch (UnknownHostException e) {

				}
			}
		}
		/**
		 * ����ͨ��������������� ��һ��IPΪ�ͻ�����ʵIP,���IP����','�ָ� "***.***.***.***".length() =
		 * 15
		 */
		if (ip != null && ip.length() > 15) {
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}
		return ip;
	}
}
