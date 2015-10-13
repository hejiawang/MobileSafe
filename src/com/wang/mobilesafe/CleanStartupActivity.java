package com.wang.mobilesafe;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 缓存启动项的activity
 * 
 * @author wang
 *
 */
public class CleanStartupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		tv.setTextSize(30);
		tv.setTextColor(Color.RED);
		tv.setText("启动项清理");
		setContentView(tv);
	}
}
