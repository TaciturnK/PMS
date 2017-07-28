package com.util.test;

import org.junit.Test;

import com.pms.util.PinYinUtils;

public class TestPinyin {
	@Test
	public void TestPinyin1() {

		System.out.println("תƴ����" + PinYinUtils.getPingYin("���� ����       tom 12345 %^%^   �ǫ  ������"));

		System.out.println("תƴ����" + PinYinUtils.getFirstSpell("���� ����           tom 12345 %^%^             �츣԰ ������"));

		System.out.println("תƴ����" + PinYinUtils.getFullSpell("���� ����             tom 12345 %^%^           �ǫ  ������"));
	}
}
