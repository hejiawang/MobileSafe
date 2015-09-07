package com.wang.mobilesafe.receiver;

import com.wang.mobilesafe.engine.GPSInfoProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {

		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for (Object pdu : pdus) {

			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
			String sender = smsMessage.getOriginatingAddress();
			String body = smsMessage.getMessageBody();
			if ("#*location*#".equals(body)) {
				Log.i(TAG, "返回手机位置....");
				String location = GPSInfoProvider.getInstance(context).getLastLocation();
				if ( !TextUtils.isEmpty(location) ) {
				
					SmsManager sm = SmsManager.getDefault();
					sm.sendTextMessage(sender, null, location, null, null);
				}
				//这样就收不到短信了
				abortBroadcast();
			} else if ("#*alarm*#".equals(body)) {
				Log.i(TAG, "播放报警音乐....");
				abortBroadcast();
			} else if ("#*wipedata*#".equals(body)) {
				Log.i(TAG, "清空数据....");
				abortBroadcast();
			} else if ("#*lockscreen*#".equals(body)) {
				Log.i(TAG, "锁屏....");
				abortBroadcast();
			}
		}
	}
}
