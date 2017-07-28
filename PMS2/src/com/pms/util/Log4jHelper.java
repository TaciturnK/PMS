package com.pms.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * 
 * @author Taowd
 * ��        �ܣ�Log4j�ķ�װ��
 * ��дʱ�䣺2017-4-24-����8:18:04
 */
public class Log4jHelper {

	/**
	 * ����log4j�������ļ�:Ĭ�ϼ���srcĿ¼�µ�log4j.properties�����ļ�
	 */
	static {
		// DOMConfigurator.configure("/log4j.properties");
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ����������Ϣ
	 * �������ڣ�2017-4-25-����5:16:47
	 * @param msg
	 */
	public static void info(Object msg) {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		Logger logger = Logger.getLogger(stack[1].getClassName());
		logger.log(Log4jHelper.class.getName(), Level.INFO, msg, null);
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ����������־
	 * �������ڣ�2017-4-25-����5:16:33
	 * @param msg
	 */
	public static void error(Object msg) {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		Logger logger = Logger.getLogger(stack[1].getClassName());
		logger.log(Log4jHelper.class.getName(), Level.ERROR, msg, null);
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ����Debug��Ϣ
	 * �������ڣ�2017-4-25-����7:15:07
	 * @param msg
	 */
	public static void debug(Object msg) {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		Logger logger = Logger.getLogger(stack[1].getClassName());
		logger.log(Log4jHelper.class.getName(), Level.DEBUG, msg, null);
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ����������Ϣ
	 * �������ڣ�2017-4-25-����7:15:48
	 * @param msg
	 */
	public static void warn(Object msg) {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		Logger logger = Logger.getLogger(stack[1].getClassName());
		logger.log(Log4jHelper.class.getName(), Level.WARN, msg, null);
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ�����쳣��Ϣ
	 * �������ڣ�2017-4-25-����7:25:26
	 * @param e
	 */
	public static void exception(Exception e) {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		Logger logger = Logger.getLogger(stack[1].getClassName());
		logger.log(Log4jHelper.class.getName(), Level.ERROR, getSatckTrace(e),
				null);
	}

	/**
	 * 
	 * Author:Taowd
	 * ���ܣ���ȡ��ջ��Ϣ
	 * �������ڣ�2017-4-25-����7:23:29
	 * @param e
	 * @return
	 */
	private static String getSatckTrace(Exception e) {
		StringWriter write = new StringWriter();
		e.printStackTrace(new PrintWriter(write, true));
		return write.toString();
	}

}
