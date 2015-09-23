package com.wang.mobilesafe.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class AutoKillService extends Service {

	private static final String TAG = "AutoKillService";

	private LockScreenReceiver receiver;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		
		super.onCreate();
		receiver = new LockScreenReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(1000);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(receiver, filter);
		Log.i(TAG , "自动清理进程开启...");
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		receiver = null;
		Log.i(TAG , "自动清理进程关闭...");
		super.onDestroy();
	}
	
	private class LockScreenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {

			Log.i(TAG , "屏幕锁屏了...");
			
			ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
			for( RunningAppProcessInfo info : infos ){
				am.killBackgroundProcesses(info.processName);
			}
		}
	}
}
