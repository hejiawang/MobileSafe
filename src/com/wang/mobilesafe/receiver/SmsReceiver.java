package com.wang.mobilesafe.receiver;

import com.wang.mobilesafe.R;
import com.wang.mobilesafe.engine.GPSInfoProvider;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";
	private SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {

		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for (Object pdu : pdus) {

			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
			String sender = smsMessage.getOriginatingAddress();
			String body = smsMessage.getMessageBody();
			if ("#*location*#".equals(body)
					&& sp.getString("safenumber", "").equals(sender)) {

				Log.i(TAG, "返回手机位置....");
				String location = GPSInfoProvider.getInstance(context)
						.getLastLocation();
				if (!TextUtils.isEmpty(location)) {

					SmsManager sm = SmsManager.getDefault();
					sm.sendTextMessage(sender, null, location, null, null);
				}
				// 这样就收不到短信了
				abortBroadcast();
			} else if ("#*alarm*#".equals(body)) {

				Log.i(TAG, "播放报警音乐....");
				MediaPlayer mp = MediaPlayer.create(context, R.raw.ylzs);
				mp.setVolume(1.0f, 1.0f);
				mp.start();
				abortBroadcast();
			} else if ("#*wipedata*#".equals(body)) {

				Log.i(TAG, "清空数据....");
				DevicePolicyManager dpm = (DevicePolicyManager) context
						.getSystemService(Context.DEVICE_POLICY_SERVICE);
				ComponentName who = new ComponentName(context, MyAdmin.class);
				if (dpm.isAdminActive(who)) {

					dpm.wipeData(0);
				}
				abortBroadcast();
			} else if ("#*lockscreen*#".equals(body)) {

				Log.i(TAG, "锁屏....");
				DevicePolicyManager dpm = (DevicePolicyManager) context
						.getSystemService(Context.DEVICE_POLICY_SERVICE);
				ComponentName who = new ComponentName(context, MyAdmin.class);
				if (dpm.isAdminActive(who)) {

					dpm.resetPassword("321", 0); // 重置屏幕密码
					dpm.lockNow();
				}
				abortBroadcast();
			}
		}
	}
}
