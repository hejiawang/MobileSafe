package com.wang.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Date;

import android.os.Build;

/**
 * 处理程序全局异常
 * 
 * @author wang
 *
 */
public class MyUncaughtExceptionHandler implements UncaughtExceptionHandler {

	@SuppressWarnings("deprecation")
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			StringBuffer sb = new StringBuffer();
			
			//异常发生时间
			long time = System.currentTimeMillis();
			Date date = new Date(time);
			String timeStr = date.toGMTString();
			sb.append(timeStr);
			sb.append("\n");

			//用户使用的手机型号等信息
			Field[] fields = Build.class.getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				String values = field.get(null).toString();
				sb.append(name + " : " + values);
				sb.append("\n");
			}

			//发生异常的具体情况
			StringWriter sw = new StringWriter();
			PrintWriter write = new PrintWriter(sw);
			ex.printStackTrace(write);
			String errorlog = sw.toString();

			File log = new File("/sdcard/log.txt");
			FileOutputStream fos;

			fos = new FileOutputStream(log);
			fos.write(sb.toString().getBytes());
			fos.write(errorlog.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 绕过生命周期的顺序,强制关闭.
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
