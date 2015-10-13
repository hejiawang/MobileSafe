package com.wang.mobilesafe;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 缓存SD卡的activity
 * 
 * @author wang
 *
 */
public class CleanSDActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		tv.setTextSize(30);
		tv.setTextColor(Color.RED);
		tv.setText("SD卡清理");
		setContentView(tv);
	}
}
