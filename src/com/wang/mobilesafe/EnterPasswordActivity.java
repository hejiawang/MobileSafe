package com.wang.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EnterPasswordActivity extends Activity {

	private TextView tv_name;
	private ImageView iv_icon;
	private EditText et_password;

	private String packname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_password);

		tv_name = (TextView) findViewById(R.id.tv_name);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		et_password = (EditText) findViewById(R.id.et_password);

		Intent intent = getIntent();
		packname = intent.getStringExtra("packName");

		try {
			PackageManager pm = getPackageManager();
			ApplicationInfo info = pm.getApplicationInfo(packname, 0);
			Drawable icon = info.loadIcon(pm);
			CharSequence name = info.loadLabel(pm);
			tv_name.setText(name);
			iv_icon.setImageDrawable(icon);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// 按返回键直接回到桌面
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.HOME");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addCategory("android.intent.category.MONKEY");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 确认密码的单击事件
	 * 
	 * @param view
	 */
	public void enter(View view) {

		String password = et_password.getText().toString().trim();
		if ("123".equals(password)) {
			finish();
		} else {
			Toast.makeText(this, "密码不正确", 0).show();
		}
	}
}
