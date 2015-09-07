package com.wang.mobilesafe.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * GPS位置提供类(单例)
 * 
 * @author HeJW
 *
 */
public class GPSInfoProvider {

	private static GPSInfoProvider myGPSInfoProvider;
	private static SharedPreferences sp;
	private static LocationManager lm;
	private static MyListener listener;

	private GPSInfoProvider() {

	}

	public static synchronized GPSInfoProvider getInstance(Context context) {

		if (myGPSInfoProvider == null) {

			lm = (LocationManager) context
					.getSystemService(context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);	//获取精确的位置
			criteria.setAltitudeRequired(true);
			criteria.setBearingRequired(true);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_HIGH);
			criteria.setSpeedRequired(true);
			
			String provider = lm.getBestProvider(criteria, true);
			myGPSInfoProvider = new GPSInfoProvider();
			listener = myGPSInfoProvider.new MyListener();//内部类new对象的形式
			sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
			lm.requestLocationUpdates(provider, 0, 0, listener);
			
		}

		return myGPSInfoProvider;
	}
	
	/**
	 * 获取手机最后一次更新到的位置
	 * @return
	 */
	public String getLastLocation(){
		return sp.getString("lastlocation", "");
	}
	
	private class MyListener implements LocationListener{

		/**
		 * 当前位置发生变化时调用
		 */
		@Override
		public void onLocationChanged(Location location) {
			
			String longitude = "j: " + location.getLongitude();
			String latitude = "j: " + location.getLatitude();
			String dx = "dx: " + location.getAccuracy();
			
			Editor editor = sp.edit();
			editor.putString("lastlocation", longitude+latitude+dx);
			editor.commit();
			
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		
	}

}
