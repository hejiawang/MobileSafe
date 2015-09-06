package com.wang.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wang.mobilesafe.utils.ActivityUtil;

public class Setup3Activity extends BaseSetupActivity {

	private EditText et_safenumber;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_setup3);
	}

	@Override
	public void setupView() {
		
		et_safenumber = (EditText) findViewById(R.id.et_safenumber);
		et_safenumber.setText(sp.getString("safenumber", ""));
	}

	@Override
	public void showNext() {
		
		//显示下一个界面之前，必须选择安全号码，并持久化
		String safenumber = et_safenumber.getText().toString().trim();
		if ( TextUtils.isEmpty(safenumber) ) {
			
			Toast.makeText(this, "安全号码不能为空", 0).show();
			return ;
		}
		Editor editor = sp.edit();
		editor.putString("safenumber", safenumber);
		editor.commit();
		
		ActivityUtil.startActivityAndFinish(this, Setup4Activity.class);
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}

	@Override
	public void showPre() {
		
		ActivityUtil.startActivityAndFinish(this, Setup2Activity.class);
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
	
	/**
	 * 选择联系人的点击事件
	 * @param view
	 */
	public void selectContact( View view ){
		Intent intent = new Intent(this, SelectContactActivity.class);
		startActivityForResult(intent, 0);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		if ( data != null ) {
			
			String phnoe = data.getStringExtra("phone");
			et_safenumber.setText(phnoe);
		}
	}

}
