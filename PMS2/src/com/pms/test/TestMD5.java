package com.pms.test;

import com.pms.util.MD5Utils;

public class TestMD5 {

	/**
	 * Author:Taowd
	 * ���ܣ�
	 * �������ڣ�2017-4-25-����7:37:07
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(MD5Utils.getStringMD5("admin"));

		System.out.println(MD5Utils.hash("1214210135"));
		
	

	}

}
