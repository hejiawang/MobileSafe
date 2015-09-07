package com.wang.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * 判断手机是否被盗;
 * <p/>
 * 只要监听手机重启，SIM卡串号改变，就认为，手机被盗了;
 * <p/>
 * 没有进行测试;
 * @author HeJW
 *
 */
public class BootCompleteRecevier extends BroadcastReceiver {

	private static final String TAG = "BootCompleteRecevier";

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.i(TAG, "手机重启了");
		Toast.makeText(context, "手机重启了", 0).show();
		
		SharedPreferences sp = context.getSharedPreferences("config",
				context.MODE_PRIVATE);

		boolean protecting = sp.getBoolean("protecting", false);
		if (protecting) {

			// 1. 获取当前手机卡串号
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(context.TELEPHONY_SERVICE);
			String realSim = tm.getSimSerialNumber();

			// 2. 获取到绑定的SIM卡串号
			String savedSim = sp.getString("sim", "");

			// 3. 比较SIM卡串号是否相同
			if (!realSim.equals(savedSim)) {
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage(sp.getString("safenumber", ""), null,
						"SIM changed", null, null);

			}
		}
	}

}
