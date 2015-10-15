package com.wang.mobilesafe;

import com.wang.mobilesafe.domain.BlackNumberInfo;

import android.app.Application;

/**
 * 在组件之间传递参数的类
 * 
 * @author HeJW
 *
 */
public class MobileSafeApplication extends Application {

	public BlackNumberInfo blackNumberInfo;

	@Override
	public void onCreate() {

		super.onCreate();
		// 处理程序全局异常
		Thread.currentThread().setUncaughtExceptionHandler(
				new MyUncaughtExceptionHandler());
	}
}
