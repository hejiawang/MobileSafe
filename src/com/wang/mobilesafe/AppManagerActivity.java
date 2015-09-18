package com.wang.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.wang.mobilesafe.utils.AvailSDAndERomUtil;

public class AppManagerActivity extends Activity {

	private TextView tv_free_mem;
	private TextView tv_free_sd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);

		tv_free_mem = (TextView) findViewById(R.id.tv_free_mem);
		tv_free_sd = (TextView) findViewById(R.id.tv_free_sd);

		tv_free_sd.setText("可用SD卡:" + AvailSDAndERomUtil.getAvailSD(this));
		tv_free_mem.setText("可用内存:" + AvailSDAndERomUtil.getAvailRom(this));
	}

}
