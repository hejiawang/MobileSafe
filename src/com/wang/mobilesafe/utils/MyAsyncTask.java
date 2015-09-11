package com.wang.mobilesafe.utils;

import android.os.Handler;

/**
 * 模拟android的AsyncTask类
 * </br>
 * 一个异步任务的工具类
 * </br>
 * 模板设计模式
 * @author HeJW
 *
 */
public abstract class MyAsyncTask {
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			onPostExecute();
		};
	};
	
	/**
	 * 耗时任务开始之前执行的方法
	 */
	public abstract void onPreExecute();
	
	
	/**
	 * 耗时任务执行之后调用的方法
	 */
	public abstract void onPostExecute();
	
	/**
	 * 执行的耗时任务，运行在子线程中
	 */
	public abstract void doInBackground();
	
	/**
	 * 执行任务
	 */
	public void execute(){
		
		onPreExecute();
		new Thread(){
			
			public void run() {
				
				doInBackground();
				handler.sendEmptyMessage(0);
			};
		}.start();
		
	}
}
