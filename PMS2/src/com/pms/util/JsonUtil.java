package com.pms.util;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author Taowd
 * ��        �ܣ������ݼ�ת��JSON��
 * ��дʱ�䣺2017-4-25-����7:31:34
 */
public class JsonUtil {

	/**
	 * ���ܣ����ݼ���װ��json������
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public static JSONArray formatRsToJsonArray(ResultSet rs) throws Exception {
		ResultSetMetaData md = rs.getMetaData();// �õ�ResultSet�����������ͺ�������Ϣ����
		int num = md.getColumnCount();// �õ�������
		JSONArray array = new JSONArray();// �õ�json�������
		while (rs.next()) {
			JSONObject mapOfColValues = new JSONObject();// Json����
			for (int i = 1; i <= num; i++) {
				Object o = rs.getObject(i);
				if (o instanceof Date) {
					mapOfColValues.put(md.getColumnName(i),
							DateUtil.formatDate((Date) o, "yyyy-MM-dd"));
				} else {
					mapOfColValues.put(md.getColumnName(i), rs.getObject(i));
				}
			}
			array.add(mapOfColValues);// json�������json����
		}

		Log4jHelper.info("����ҳ���JSON���ݸ�ʽ��" + array.toString());

		return array;
	}
}
