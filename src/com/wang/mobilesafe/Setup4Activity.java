package com.wang.mobilesafe;

import android.content.SharedPreferences.Editor;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.wang.mobilesafe.utils.ActivityUtil;

public class Setup4Activity extends BaseSetupActivity {

	private CheckBox cb_setup4_satus;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_setup4);
	}

	@Override
	public void setupView() {
		
		cb_setup4_satus = (CheckBox) findViewById(R.id.cb_setup4_satus);
		
		boolean protecting = sp.getBoolean("protecting", false);
		if ( protecting ) {
			cb_setup4_satus.setText("防盗保护已经开启");
			cb_setup4_satus.setChecked(true);
		} else {
			cb_setup4_satus.setText("防盗保护还未开启");
			cb_setup4_satus.setChecked(false);
		}
		
		cb_setup4_satus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				Editor editor = sp.edit();
				if ( isChecked ) {
					cb_setup4_satus.setText("防盗保护已经开启");
				} else {
					cb_setup4_satus.setText("防盗保护还未开启");
				}
				editor.putBoolean("protecting", isChecked);
				editor.commit();
			}
		});
		
	}

	@Override
	public void showNext() {
		
		//完成设置向导
		Editor editor = sp.edit();
		editor.putBoolean("setup", true);
		editor.commit();
		
		ActivityUtil.startActivityAndFinish(this, LostFindActivity.class);
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}

	@Override
	public void showPre() {
		
		ActivityUtil.startActivityAndFinish(this, Setup3Activity.class);
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

}
