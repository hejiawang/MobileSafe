package com.wang.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.wang.mobilesafe.service.ShowLocationService;
import com.wang.mobilesafe.ui.SettingView;

public class SettingActivity extends Activity {

	private SettingView sv_setting_update;
	private SettingView sv_setting_showaddress;
	
	private SharedPreferences sp;
	private Intent showAddressService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);

//自动更新设置——start
		sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
		//默认开启自动更新
		//sp.getBoolean("update", true);——若"update"没有存储过，就为默认的true，
		boolean isupdate = sp.getBoolean("update", true);
		sv_setting_update.setChecked(isupdate);
		
		sv_setting_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Editor editor = sp.edit();
				
				//判断是否被勾选
				if ( sv_setting_update.isChecked() ) {
					sv_setting_update.setChecked(false);
					editor.putBoolean("update", false); 
				} else {
					sv_setting_update.setChecked(true);
					editor.putBoolean("update", true); 
				}
				
				//不要忘记commit
				editor.commit();
				
			}
		});
//自动更新设置——end
		
//号码归属地设置——start		
		sv_setting_showaddress = (SettingView) findViewById(R.id.sv_setting_showaddress);
		showAddressService = new Intent(this, ShowLocationService.class);
		sv_setting_showaddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (sv_setting_showaddress.isChecked()) {
					sv_setting_showaddress.setChecked(false);
					stopService(showAddressService);
				} else {
					sv_setting_showaddress.setChecked(true);
					startService(showAddressService);
				}
			}
		});
//号码归属地设置——end		
	}
}
