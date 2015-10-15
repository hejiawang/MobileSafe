package com.wang.mobilesafe.utils;

import android.util.Log;

/**
 * log工具类
 * 
 * @author wang
 *
 */
public class LoggerUtil {

	private static final int VERBOSE = 5;
	private static final int DEBUG = 4;
	private static final int INFO = 3;
	private static final int WARN = 2;
	private static final int ERROR = 1;

	/**
	 * 通过改变这个数，控制LogCat打印的内容
	 * 内部测试阶段，所有信息都能打印到LogCat，LOG_LEVEL = 6 
	 * 测试版，预览版，LOG_LEVEL = 3
	 * 稳定版，LOG_LEVEL = 2 
	 */
	public static int LOG_LEVEL = 6;

	public static void v(String tag, String msg) {

		if (LOG_LEVEL > VERBOSE) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {

		if (LOG_LEVEL > DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {

		if (LOG_LEVEL > INFO) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {

		if (LOG_LEVEL > WARN) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {

		if (LOG_LEVEL > ERROR) {
			Log.e(tag, msg);
		}
	}
}
