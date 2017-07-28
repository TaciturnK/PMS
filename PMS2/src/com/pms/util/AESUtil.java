package com.pms.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author Taowd
 * ��        �ܣ�AES����  �ԳƼ���
 * ��дʱ�䣺2017-4-27-����8:57:52
 */
public class AESUtil {
	/**
	 * �ԳƼ�����Կ���̶�ֵ
	 */
	public static final String SKA = "mqqian0528";

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����
	 * �������ڣ�2017-4-27-����8:59:39
	 * @param content ��Ҫ���ܵ�����
	 * @param password ��������
	 * @return
	 */
	public static byte[] encrypt(String content) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(SKA.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// ����������
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// ��ʼ��
			byte[] result = cipher.doFinal(byteContent);
			return result; // ����
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����
	 * �������ڣ�2017-4-27-����9:00:11
	 * @param content  ����������
	 * @param password   ������Կ
	 * @return
	 */
	public static byte[] decrypt(byte[] content) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(SKA.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// ����������
			cipher.init(Cipher.DECRYPT_MODE, key);// ��ʼ��
			byte[] result = cipher.doFinal(content);
			return result; // ����
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����֮�������ת���ַ������������ݿ�洢����������ת����16���ƴ洢
	 * �������ڣ�2017-4-27-����9:03:04
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ������ݴ�תΪ��Ӧ�Ķ��������ģ� ��16����ת��Ϊ������
	 * �������ڣ�2017-4-27-����9:03:54
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static void main(String[] args) {
		// AES����
		/*
		 * String content = "asdhkjadhkjahdkjahdjsakhdkjahdjk"; String password
		 * = "12345678"; // ���� System.out.println("����ǰ��" + content); byte[]
		 * encryptResult = encrypt(content, password); System.out.println("���ģ�"
		 * + encryptResult.toString());
		 * System.out.println(encryptResult.toString().getBytes()); // ���� byte[]
		 * decryptResult = decrypt(encryptResult, password);
		 * System.out.println("���ܺ�" + new String(decryptResult));
		 */

		// AES ���ܲ���2  21232f297a57a5a743894a0e4a801fc3  21232f297a57a5a743894a0e4a801fc3
		String content = "admin";
		// ����
		System.out.println("����ǰ��" + content);
		byte[] encryptResult = encrypt(content);
		String encryptResultStr = parseByte2HexStr(encryptResult);
		System.out.println("���ܺ�" + encryptResultStr);
		// ����
		byte[] decryptResult = decrypt(parseHexStr2Byte(encryptResultStr));
		System.out.println("���ܺ�" + new String(decryptResult));
	}
}
