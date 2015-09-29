package com.wang.mobilesafe.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import com.wang.mobilesafe.EnterPasswordActivity;
import com.wang.mobilesafe.db.dao.AppLockDao;

public class WatchDogService extends Service {

	private static final String TAG = "WatchDogService";

	private ActivityManager am;
	private Intent intent;

	private AppLockDao dao;
	private boolean flag;
	private List<String> tempStopProtectPacknames;

	// private InnerReceiver receiver;
	private InnerLockScreenReceiver lockScreenReceiver;

	@Override
	public IBinder onBind(Intent intent) {

		return new MyBinder();
	}

	private class MyBinder extends Binder implements IService {

		@Override
		public void callMethodInService(String packname) {

			tempStopProtect(packname);
		}
	}

	/**
	 * 临时取消保护的应用程序
	 * 
	 * @param packname
	 *            应用程序的包名
	 */
	private void tempStopProtect(String packname) {

		tempStopProtectPacknames.add(packname);
	}

	@Override
	public void onCreate() {

		super.onCreate();
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		dao = new AppLockDao(this);
		intent = new Intent(this, EnterPasswordActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		tempStopProtectPacknames = new ArrayList<String>();

		// receiver = new InnerReceiver();
		// IntentFilter filter = new IntentFilter();
		// filter.addAction("com.wang.stopprotect");
		// registerReceiver(receiver, filter);

		lockScreenReceiver = new InnerLockScreenReceiver();
		IntentFilter lockScreenfilter = new IntentFilter();
		lockScreenfilter.addAction(Intent.ACTION_SCREEN_OFF);
		lockScreenfilter.setPriority(1000);
		registerReceiver(lockScreenReceiver, lockScreenfilter);
		// 开启看门狗，监视当前系统运行程序的信息
		new Thread() {
			public void run() {

				flag = true;

				while (flag) {

					List<RunningTaskInfo> infos = am.getRunningTasks(1);
					RunningTaskInfo taskInfo = infos.get(0); // 最新打开的任务栈
					ComponentName topActivity = taskInfo.topActivity; // 得到栈顶的activity
					String packName = topActivity.getPackageName();
					if (dao.find(packName)) {
						// 如果打开的这个软件是受保护的...
						// 判断当前包名是否要临时取消保护
						if (tempStopProtectPacknames.contains(packName)) {
							// 如果需要临时取消保护，什么也不做...
						} else {
							intent.putExtra("packName", packName);
							startActivity(intent);
						}
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
		// unregisterReceiver(receiver);
		// receiver = null;
		unregisterReceiver(lockScreenReceiver);
		lockScreenReceiver = null;
	}

	// private class InnerReceiver extends BroadcastReceiver {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// String packname = intent.getStringExtra("stoppackname");
	// tempStopProtectPacknames.add(packname);
	// }
	// }

	private class InnerLockScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			tempStopProtectPacknames.clear();
		}
	}

}
