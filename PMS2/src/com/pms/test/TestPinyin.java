package com.pms.test;

import com.pms.util.PinYinUtils;

public class TestPinyin {
	public static void main(String[] args) {

		System.out.println("תƴ����"
				+ PinYinUtils
						.getPingYin("���� ����       tom 12345 %^%^   �ǫ  ������"));

		System.out
				.println("תƴ����"
						+ PinYinUtils
								.getFirstSpell("���� ����           tom 12345 %^%^             �ǫ  ������"));

		System.out
				.println("תƴ����"
						+ PinYinUtils
								.getFullSpell("���� ����             tom 12345 %^%^           �ǫ  ������"));
	}
}
