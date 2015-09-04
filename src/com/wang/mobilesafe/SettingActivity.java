package com.wang.mobilesafe;

import com.wang.mobilesafe.ui.SettingView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {

	private SettingView sv_setting_update;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
		sv_setting_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//判断是否被勾选
				sv_setting_update.setChecked(!sv_setting_update.isChecked());
			}
		});
	}
}
