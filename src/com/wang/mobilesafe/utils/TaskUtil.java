package com.wang.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

public class TaskUtil {

	/**
	 * 得到当前系统中运行的总进程数
	 * 
	 * @param context
	 *            context
	 * @return 进程数量
	 */
	public static int getRunningProcessCount(Context context) {

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}

	/**
	 * 获取手机的可用内存
	 * 
	 * @param context
	 *            context
	 * @return long类型手机可用内存
	 */
	public static long getAvailRam(Context context) {

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}

	/**
	 * 获取手机的总内存
	 * 
	 * @return long类型的手机总内存
	 */
	public static long getTotalRam() {

		try {
			File file = new File("/proc/meminfo");
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String result = br.readLine();
			StringBuffer sb = new StringBuffer();
			char[] chars = result.toCharArray();
			for (char c : chars) {
				if (c >= '0' && c <= '9') {
					sb.append(c);
				}
			}
			return Long.parseLong(sb.toString()) * 1024;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
