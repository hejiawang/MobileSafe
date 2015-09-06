package com.wang.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseSetupActivity extends Activity {

	protected static final String TAG = "BaseSetupActivity";
	
	protected SharedPreferences sp;
	// 1、定义一个手势识别器
	private GestureDetector myGestureDetector;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);

		// 2、初始化手势识别器
		myGestureDetector = new GestureDetector(
				new GestureDetector.SimpleOnGestureListener() {
					
					//当手指在屏幕上滑动时调用的方法
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						
						if ( Math.abs(velocityX) < 200 ) {
							Log.i(TAG, "移动太慢，动作不合法");
							//当返回true时，停止，不再往下执行
							return true;
						}
						
						if ( Math.abs(e1.getRawY() - e2.getRawY()) > 100 ) {
							Log.i(TAG, "竖直方向移动距离过大，动作不合法");
							return true;
						}
						
						if ( (e1.getRawX() - e2.getRawX()) > 200 ) {
							showNext();
							return true;
						}
						
						if ( (e2.getRawX() - e1.getRawX()) > 200 ) {
							showPre();
							return true;
						}
						
						
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});

		initView();
		setupView();

	}
	
	//3、让手势识别器生效
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		myGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
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
	 * 
	 * @param view
	 *            视图
	 */
	public void next(View view) {
		showNext();
	}

	/**
	 * 上一个的点击事件
	 * 
	 * @param view
	 *            视图
	 */
	public void pre(View view) {
		showPre();
	}
}
