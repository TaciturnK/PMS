package com.util.test;

import org.junit.Test;

import com.pms.util.Log4jHelper;

public class TestLog4j {
	/**
	 * Author:Taowd
	 * ���ܣ�����Log4j��־����
	 * �������ڣ�2017-4-21-����1:48:07
	 * @param args
	 */
	@Test
	public void testLog4j() {

		Log4jHelper.info("����--");
		Log4jHelper.error("���Դ�����Ϣ--");
	}

}
