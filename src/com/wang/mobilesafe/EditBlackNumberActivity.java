package com.wang.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wang.mobilesafe.db.dao.BlackNumberDao;
import com.wang.mobilesafe.domain.BlackNumberInfo;

public class EditBlackNumberActivity extends Activity {

	private TextView tv_title_name;
	private EditText ed_blacknumber;
	private Button bt_ok, bt_cancle;
	private RadioButton rb_all, rb_phone, rb_sms;
	private RadioGroup rg_mode;
	
	private BlackNumberDao dao;
	
	private BlackNumberInfo info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_add_blacknumber);

		dao = new BlackNumberDao(this);
		
		MobileSafeApplication app = (MobileSafeApplication) getApplication();
		info = app.blackNumberInfo;
		app.blackNumberInfo = null;
		
		tv_title_name = (TextView) findViewById(R.id.tv_title_name);
		ed_blacknumber = (EditText) findViewById(R.id.ed_blacknumber);
		rb_all = (RadioButton) findViewById(R.id.rb_all);
		rb_phone = (RadioButton) findViewById(R.id.rb_phone);
		rb_sms = (RadioButton) findViewById(R.id.rb_sms);
		bt_ok = (Button) findViewById(R.id.bt_ok);
		bt_cancle = (Button) findViewById(R.id.bt_cancle);
		rg_mode = (RadioGroup) findViewById(R.id.rg_mode);
		
		tv_title_name.setText("更改黑名单拦截模式");
		
		String number = info.getNumber();
		ed_blacknumber.setEnabled(false);
		ed_blacknumber.setText(number);
		String mode = info.getMode();
		if ("1".equals(mode)) {
			rb_all.setChecked(true);
		} else if ("2".equals(mode)) {
			rb_phone.setChecked(true);
		} else if ("3".equals(mode)) {
			rb_sms.setChecked(true);
		}

		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				int id = rg_mode.getCheckedRadioButtonId();
				String newMode = "";
				switch (id) {
				case R.id.rb_all:
					newMode = "1";
					break;
				case R.id.rb_phone:
					newMode = "2";
					break;
				case R.id.rb_sms:
					newMode = "3";
					break;
				}
				dao.update(info.getNumber(), newMode);
				//设置新的拦截模式到info对象
				info.setMode(newMode);
				setResult(200);
				finish();
			}
		});

		bt_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(404);
				finish();
			}
		});
	}
}
