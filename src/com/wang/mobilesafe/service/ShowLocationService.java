package com.wang.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.wang.mobilesafe.R;
import com.wang.mobilesafe.db.dao.AddressDao;

public class ShowLocationService extends Service {

	public static final String TAG = "ShowLocationService";
	//"半透明","活力橙","卫士蓝","金属灰","苹果绿"
	private static final int[] bgs = {R.drawable.call_locate_white,
		R.drawable.call_locate_orange,
		R.drawable.call_locate_blue,
		R.drawable.call_locate_gray,
		R.drawable.call_locate_green};
	
	private SharedPreferences sp;
	private TelephonyManager tm;
	private myPhoneListener listener;
	private InnerOutCallReceiver receiver;
	private WindowManager wm;
	private View view;

	private final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
	
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
		
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		sp = getSharedPreferences("config", MODE_PRIVATE);
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

	/**
	 * 显示归属地信息
	 * @param incomingNumber 电话号码
	 */
	public void showAddressInfo( String incomingNumber ){
		
//		tv = new TextView(this);
//		tv.setTextSize(25);
//		tv.setTextColor(Color.RED);
		
		view = View.inflate(this, R.layout.ui_toast, null);
		TextView tv = (TextView) view.findViewById(R.id.tv_toast_address);
		tv.setText(AddressDao.getAddress(incomingNumber));
		int which = sp.getInt("which", 0);
		view.setBackgroundResource(bgs[which]);
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        //params.windowAnimations = com.android.internal.R.style.Animation_Toast;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		
		wm.addView(view, params);
	}
	
	private class myPhoneListener extends PhoneStateListener {

		/**
		 * 当电话呼叫状态发生改变时调用的方法
		 */
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: // 空闲状态,挂断.....
				//不打电话时，移除归属地信息
				if ( view != null ) {
					wm.removeView(view);
					view = null;
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING: // 响铃状态
				//Log.i(TAG, AddressDao.getAddress(incomingNumber));
				//Toast.makeText(getApplicationContext(), AddressDao.getAddress(incomingNumber), 1).show();
				showAddressInfo(incomingNumber);
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
			//Toast.makeText(context, AddressDao.getAddress(getResultData()), 1).show();
			//如果没有SIM卡
			String simnumber = tm.getSimSerialNumber();
			if ( !TextUtils.isEmpty(simnumber) ) {
				//Toast.makeText(getApplicationContext(), "手机中没有SIM卡", 0).show();
				showAddressInfo(getResultData());
			}
		}
	}
	
}
