package com.wang.mobilesafe.service;

import java.util.List;

import com.wang.mobilesafe.EnterPasswordActivity;
import com.wang.mobilesafe.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class WatchDogService extends Service {

	private static final String TAG = "WatchDogService";
	private ActivityManager am;
	private Intent intent;

	private AppLockDao dao;

	private boolean flag;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {

		super.onCreate();
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		dao = new AppLockDao(this);
		intent = new Intent(this, EnterPasswordActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// 开启看门狗，监视当前系统运行程序的信息
		new Thread() {
			public void run() {

				flag = true;

				while (flag) {

					List<RunningTaskInfo> infos = am.getRunningTasks(1);
					RunningTaskInfo taskInfo = infos.get(0); // 最新打开的任务栈
					ComponentName topActivity = taskInfo.topActivity; // 得到栈顶的activity
					String packName = topActivity.getPackageName();
					Log.i(TAG, "packName : " + packName);
					if (dao.find(packName)) {
						// 如果打开的这个软件是受保护的...
						intent.putExtra("packName", packName);
						startActivity(intent);
					}

					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		flag = false;
	}
}
