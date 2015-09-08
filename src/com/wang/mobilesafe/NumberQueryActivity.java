package com.wang.mobilesafe;

import com.wang.mobilesafe.db.dao.AddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumberQueryActivity extends Activity {
	
	private EditText et_query_number;
	private TextView tv_query_address;
	private Vibrator vibrator;	//手机震动
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_query);
		
		et_query_number = (EditText) findViewById(R.id.et_query_number);
		tv_query_address = (TextView) findViewById(R.id.tv_query_address);
		
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		
		//文本内容发生改变时监听的事件
		et_query_number.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				String number = s.toString();
				String address = AddressDao.getAddress(number);
				tv_query_address.setText( "归属地为:" + address);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		
	}
	
	/**
	 * 查询电话号码归属地
	 * @param view
	 */
	public void query(View view){
		
		String number = et_query_number.getText().toString().trim();
		if ( TextUtils.isEmpty(number) ) {
			
			tv_query_address.setText( "归属地:");
			Toast.makeText(this, "请输入手机号码", 1).show();
			
			//文本框晃动
			Animation snake = AnimationUtils.loadAnimation(this, R.anim.shake);
			et_query_number.startAnimation(snake);
		
			//手机震动频率
			vibrator.vibrate(200);	
			
			return;
		}
		String address = AddressDao.getAddress(number);
		tv_query_address.setText( "归属地为:" + address);
		
	}
}
