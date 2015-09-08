package com.wang.mobilesafe;

import com.wang.mobilesafe.utils.ActivityUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class AtoolsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	
	/**
	 * 号码归属地查询的点击事件
	 * @param view
	 */
	public void numberAddressQuery( View view ){
		
		ActivityUtil.startActivity(this, NumberQueryActivity.class);
		
		//如果用startActivityAndFinish开启“号码归属地查询页面”，也就是将“高级工具”finish了，
		//那么返回后是home主页面，就不是“高级工具”页面了
		//ActivityUtil.startActivityAndFinish(this, NumberQueryActivity.class);
	}
}
