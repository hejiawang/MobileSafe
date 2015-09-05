package com.wang.mobilesafe.receiver;

import com.wang.mobilesafe.LostFindActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 外拨电话广播接收者;
 * 要注册;
 * 要设置优先级;
 * @author HeJW
 *
 */
public class OutCallReceiver extends BroadcastReceiver {

	private static final String TAG = "OutCallReceiver";

	@Override
	public void onReceive(Context context, Intent arg1) {
		
		Log.i(TAG , "手机外拨电话了");
		String number = getResultData();	//获得外拨电话所对应的电话号码
		
		if ( "20182018".equals(number)) {
			Intent intent = new Intent(context, LostFindActivity.class);
			//告诉intent，将界面放在自己的栈里面
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			//拦截掉拨打的号码（停止拨号）
			setResultData(null);
			
		}
	}

}
