package com.wang.mobilesafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.wang.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 黑名单号码短信拦截的服务类
 * 
 * @author HeJW
 *
 */
public class CallSmsFireWallSerivce extends Service {

	private static final String Tag = "CallSmsFireWallSerivce";
	private BlackNumberDao dao;
	private InnerSmsReceiver receiver;
	private TelephonyManager tm;
	private innerTeleStateListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {

		super.onCreate();
		dao = new BlackNumberDao(this);

		receiver = new InnerSmsReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(receiver, filter);

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new innerTeleStateListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {

		unregisterReceiver(receiver);
		receiver = null;

		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		super.onDestroy();
	}

	private class InnerSmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {

				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				String sender = smsMessage.getOriginatingAddress();
				// 判断sender是否在黑名单库里，判断sender的拦截模式.
				String mode = dao.findMode(sender);
				if ("1".equals(mode) || "3".equals(mode)) {

					Log.i(Tag, "拦截到黑名单号码的短信...");
					abortBroadcast(); // 不要收到短信
				}

				// 拦截特殊字符
				String body = smsMessage.getMessageBody();
				if (body.contains("teshu")) {

					Log.i(Tag, "拦截到特殊字符的短信...");
					abortBroadcast(); // 不要收到短信
				}
			}
		}
	}

	private class innerTeleStateListener extends PhoneStateListener {

		/**
		 * 当电话呼叫状态发生改变时调用的方法
		 */
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING: // 响铃状态
				String mode = dao.findMode(incomingNumber);
				if ("1".equals(mode) || "2".equals(mode)) {

					Log.i(Tag, "挂断黑名单电话...");
					endcall(incomingNumber);
					// 当呼叫记录产生后删除
					//deleteCallLog(incomingNumber);
					getContentResolver().registerContentObserver(
							CallLog.Calls.CONTENT_URI, true,
							new MyObserver(new Handler(), incomingNumber));
				}
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

	/**
	 * 挂断电话
	 * 
	 * @param incomingNumber
	 *            电话号码
	 */
	private void endcall(String incomingNumber) {
		try {
			Method method = Class.forName("android.os.ServiceManager")
					.getMethod("getService", String.class);
			IBinder binder = (IBinder) method.invoke(null,
					new Object[] { TELEPHONY_SERVICE });
			ITelephony telephony = ITelephony.Stub.asInterface(binder);
			telephony.endCall();// 挂断电话.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除黑名单呼叫记录
	 * 
	 * @param incomingNumber
	 *            电话号码
	 */
	private void deleteCallLog(String incomingNumber) {

		Uri uri = Uri.parse("content://call_log/calls");
		getContentResolver().delete(uri, "number=?",
				new String[] { incomingNumber });
	}

	private class MyObserver extends ContentObserver {

		private String incomingNumber;

		public MyObserver(Handler handler, String incomingNumber) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}

		// 当内容发生变化时调用
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			deleteCallLog(incomingNumber);
			// 取消内容观察者
			getContentResolver().unregisterContentObserver(this);
		}
	}

}
