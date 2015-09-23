package com.wang.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TaskSettingActivity extends Activity {

	private CheckBox cb_task_setting_showsystem;

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_setting);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean showsystem = sp.getBoolean("showsystem", false);

		cb_task_setting_showsystem = (CheckBox) findViewById(R.id.cb_task_setting_showsystem);
		cb_task_setting_showsystem.setChecked(showsystem);

		cb_task_setting_showsystem
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						Editor editor = sp.edit();
						editor.putBoolean("showsystem", isChecked);
						editor.commit();
						
						setResult(200);
					}
				});
	}
}
