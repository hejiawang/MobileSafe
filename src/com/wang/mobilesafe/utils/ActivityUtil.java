package com.wang.mobilesafe.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * activity类相关的工具类
 * @author HeJW
 *
 */
public class ActivityUtil {
	
	/**
	 * 开始新的activity，并且关闭自己
	 */
	public static void startActivityAndFinish( Activity context, Class<?> cls  ){

		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
		context.finish();
	}
}
