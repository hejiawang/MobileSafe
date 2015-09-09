package com.wang.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 服务状态类
 * @author HeJW
 *
 */
public class ServiceStatusUtil {
	
	/**
	 * 服务是否开启
	 * @param serviceName 服务名
	 * @param context 上下文环境
	 * @return 服务开启返回true,否则返回false
	 */
	public static boolean isServiceRuuing(String serviceName, Context context){
		
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos = am.getRunningServices(100);
		for ( RunningServiceInfo info : infos ) {
			
			if ( serviceName.equals(info.service.getClassName()) ) {
				
				return true;	//只要在得到的服务名中，有与serviceName名相同的，就是服务开启的。
			}
		}
		return false;
	}
}
