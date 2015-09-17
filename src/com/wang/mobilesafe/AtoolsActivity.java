package com.wang.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wang.mobilesafe.utils.ActivityUtil;

public class AtoolsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}

	/**
	 * 号码归属地查询的点击事件
	 * 
	 * @param view
	 */
	public void numberAddressQuery(View view) {

		ActivityUtil.startActivity(this, NumberQueryActivity.class);

		// 如果用startActivityAndFinish开启“号码归属地查询页面”，也就是将“高级工具”finish了，
		// 那么返回后是home主页面，就不是“高级工具”页面了
		// ActivityUtil.startActivityAndFinish(this, NumberQueryActivity.class);
	}

	/**
	 * 创建桌面快捷图标
	 * 
	 * @param view
	 */
	public void createIcon(View view) {
	
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		
		Intent shortCutIntent = new Intent();
		shortCutIntent.setAction("com.wang.xxx");
		shortCutIntent.addCategory(Intent.CATEGORY_DEFAULT);
		
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutIntent);
    	intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "手机卫士");
    	intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
    	sendBroadcast(intent);
    	
    	Toast.makeText(this, "创建成功", 1).show();
	}
}
