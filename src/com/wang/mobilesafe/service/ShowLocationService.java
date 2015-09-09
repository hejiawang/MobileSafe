package com.wang.mobilesafe.service;

import com.wang.mobilesafe.db.dao.AddressDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class ShowLocationService extends Service {

	public static final String TAG = "ShowLocationService";
	
	private TelephonyManager tm;
	private myPhoneListener listener;
	private InnerOutCallReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {

		// 监听电话状态的改变
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new myPhoneListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		//初始化监听来电的广播接受者
		receiver = new InnerOutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		//在代码中，动态注册广播接收者，是这个广播接受者与service联系在一起
		registerReceiver(receiver, filter);
		
		super.onCreate();
	}

	@Override
	public void onDestroy() {

		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		//取消广播接受者
		unregisterReceiver(receiver);
		receiver = null;
		super.onDestroy();
	}

	private class myPhoneListener extends PhoneStateListener {

		/**
		 * 当电话呼叫状态发生改变时调用的方法
		 */
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: // 空闲状态

				break;
			case TelephonyManager.CALL_STATE_RINGING: // 响铃状态
				
				Log.i(TAG, AddressDao.getAddress(incomingNumber));
				Toast.makeText(getApplicationContext(), AddressDao.getAddress(incomingNumber), 1).show();
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK: // 通话状态

				break;
			}

			super.onCallStateChanged(state, incomingNumber);
		}

	}

	private class InnerOutCallReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			Log.i(TAG, "发现外拨电话");
		}
	}
	
}
