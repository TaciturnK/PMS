package com.pms.util;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * 
 * @author Taowd
 * ��        �ܣ����ڹ�����
 * ��дʱ�䣺2017-4-11-����2:27:37
 */
public class DateUtil {
	/**
	 * һ��ĺ�����
	 */
	public static final long SECOND_OF_DAY = 86400000L;

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�������ת��ָ����ʽ���ַ���
	 * �������ڣ�2017-4-11-����2:27:52
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String formatDate(Date date, String format) throws Exception {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (date != null) {
			result = sdf.format(date);
		}
		return result;
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ָ����ʽ���ַ���ת������
	 * �������ڣ�2017-4-11-����2:28:29
	 * @param str
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static Date formatString(String str, String format) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(str);
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ȡ��ǰϵͳʱ��
	 * �������ڣ�2017-4-11-����9:49:50
	 * @return ��ǰ���ڵ��ַ���
	 * @throws Exception
	 */
	public static String getCurrentDateStr() throws Exception {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ������������ڼ��������������ʱ����
	 * �������ڣ�2017-5-8-����10:37:25
	 * @param dateStart ��ʼ����
	 * @param dateEnd ��������
	 * @return �������
	 */
	public static int dayLength(Date dateStart, Date dateEnd) {
		int sign = 1;
		// �жϿ�ʼʱ�����Ƿ����ڽ�������
		if (dateStart.after(dateEnd)) {
			Date dateTemp = dateStart;
			dateStart = dateEnd;
			dateEnd = dateTemp;
			sign = -1;
		}
		Calendar calendarStart = Calendar.getInstance();
		calendarStart.setTime(dateStart);
		calendarStart.set(Calendar.HOUR_OF_DAY, 1);
		calendarStart.set(Calendar.MINUTE, 0);
		calendarStart.set(Calendar.SECOND, 0);

		Calendar calendarEnd = Calendar.getInstance();
		calendarEnd.setTime(dateEnd);
		calendarEnd.set(Calendar.HOUR_OF_DAY, 2);
		calendarEnd.set(Calendar.MINUTE, 0);
		calendarEnd.set(Calendar.SECOND, 0);

		long diffScond = calendarEnd.getTimeInMillis()
				- calendarStart.getTimeInMillis();
		long diffDays = diffScond / SECOND_OF_DAY;

		return (int) diffDays * sign;

	}
}
