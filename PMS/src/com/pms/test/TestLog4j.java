package com.pms.test;

import com.pms.util.Log4jHelper;

public class TestLog4j {
	/**
	 * Author:Taowd
	 * ���ܣ�����Log4j��־����
	 * �������ڣ�2017-4-21-����1:48:07
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * System.setProperty("log4j.configuration", "log4j.properties");
		 * 
		 * logger.debug("��¼Debug��Ϣ");
		 * 
		 * logger.info("���info��Ϣ");
		 * 
		 * logger.error("���error��Ϣ");
		 */

		Log4jHelper.info("����--");
		Log4jHelper.error("���Դ�����Ϣ--");

	}

}
