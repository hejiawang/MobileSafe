package com.wang.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public abstract class BaseSetupActivity extends Activity {
	
	protected SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		 
		initView();
		setupView();
	}
	
	/**
	 * 初始化View
	 */
	public abstract void initView();
	
	/**
	 * 设置view对象的响应事件
	 */
	public abstract void setupView();
	
	/**
	 * 下一个
	 */
	public abstract void showNext();
	
	/**
	 * 上一个
	 */
	public abstract void showPre();
	
	/**
	 * 下一个的点击事件
	 * @param view 视图
	 */
	public void next( View view ){
		showNext();
	}
	
	/**
	 * 上一个的点击事件
	 * @param view 视图
	 */
	public void pre( View view ){
		showPre();
	}
}
