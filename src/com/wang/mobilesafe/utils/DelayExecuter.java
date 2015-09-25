package com.wang.mobilesafe.utils;

import android.os.Handler;

/**
 * 延时执行的工具类
 * 
 * @author HeJW
 *
 */
public abstract class DelayExecuter {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			onPostExecute();
		};
	};

	/**
	 * 延时之后执行的方法
	 */
	public abstract void onPostExecute();

	/**
	 * 执行延时
	 * 
	 * @param delayTime
	 *            延时的毫秒值
	 */
	public void execute(final long delayTime) {

		new Thread() {
			public void run() {
				try {
					Thread.sleep(delayTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			};
		}.start();
	}
}
