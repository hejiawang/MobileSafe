package com.wang.mobilesafe.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

/**
 * 获取SD卡或手机内存的可用量的工具类
 * 
 * @author HeJW
 *
 */
public class AvailSDAndERomUtil {

	/**
	 * 可用SD卡内存
	 * 
	 * @return 可用SD卡内存容量
	 */
	public static String getAvailSD(Context context) {

		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		// long totalBlocks = stat.getBlockCount();
		long availableBlocks = stat.getAvailableBlocks();
		// return blockSize * availableBlocks + "";
		return Formatter.formatFileSize(context, blockSize * availableBlocks);
	}

	/**
	 * 手机可用内存
	 * 
	 * @return 手机可用内存容量
	 */
	public static String getAvailRom(Context context) {

		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		// return blockSize * availableBlocks + "";
		return Formatter.formatFileSize(context, blockSize * availableBlocks);
	}
}
