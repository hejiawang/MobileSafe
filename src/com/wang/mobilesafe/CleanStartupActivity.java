package com.wang.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 缓存启动项的activity
 * 
 * @author wang
 *
 */
public class CleanStartupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		tv.setTextSize(30);
		tv.setTextColor(Color.RED);
		tv.setText("启动项清理——逻辑实现");
		setContentView(tv);

		// 查询手机里面的应用程序，看那个程序有android.intent.action.BOOT_COMPLETED
		PackageManager pm = getPackageManager();
		Intent intent = new Intent("android.intent.action.BOOT_COMPLETED");
		List<ResolveInfo> infos = pm.queryBroadcastReceivers(intent,
				PackageManager.GET_INTENT_FILTERS);
		for (ResolveInfo info : infos) {

			String receiverName = info.activityInfo.name;
			String packname = info.activityInfo.packageName;
			System.out.println("receiver:" + receiverName + "---" + packname);
			//其他业务逻辑
		}
	}
}
