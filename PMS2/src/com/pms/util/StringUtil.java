package com.pms.util;

import java.util.UUID;

/**
 * 
 * @author Taowd
 * ��        �ܣ�String������
 * ��дʱ�䣺2017-5-5-����2:26:44
 */
public class StringUtil {
	/**
	 * ���ܣ��ж��ַ���str�Ƿ�Ϊ�ջ���null
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if ("".equals(str) || str == null) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNotEmpty(String str) {
		if (!"".equals(str) && str != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����һ��UUID
	 * �������ڣ�2017-4-19-����8:20:01
	 * @return
	 * @throws Exception 
	 */
	public static String GetUUID() throws Exception {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ʽ��ɾ������ delIds
	 * �������ڣ�2017-4-19-����8:56:09
	 * @param delIds
	 * @return
	 */
	public static String FormatDeleteDelIds(String delIds) {
		String[] str = delIds.split(",");
		String deleteStr = "";
		for (int i = 0; i < str.length; i++) {
			if (i == str.length - 1) {
				deleteStr += "'" + str[i] + "'";
			} else {
				deleteStr += "'" + str[i] + "',";
			}
		}

		return deleteStr;
	}
}
