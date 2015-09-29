package com.wang.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.text.format.Formatter;

import com.wang.mobilesafe.domain.TrafficInfo;

/**
 * 流量使用情况信息提供类
 * 
 * @author HeJW
 *
 */
public class TrafficInfoProvider {

	/**
	 * 获取手机上所有使用网络的应用程序的流量信息
	 * 
	 * @param context
	 *            context
	 * @return 流量信息集合
	 */
	public static List<TrafficInfo> getTrafficInfos(Context context) {

		List<TrafficInfo> trafficInfos = new ArrayList<TrafficInfo>();
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packInfos = pm
				.getInstalledPackages(PackageManager.GET_PERMISSIONS);
		for (PackageInfo packInfo : packInfos) {
			String[] permissions = packInfo.requestedPermissions;
			if (permissions != null && permissions.length > 0) {
				for (String permission : permissions) {
					if ("android.permission.INTERNET".equals(permission)) {
						TrafficInfo trafficInfo = new TrafficInfo();

						String appName = packInfo.applicationInfo.loadLabel(pm)
								.toString();
						trafficInfo.setAppName(appName);

						Drawable appIcon = packInfo.applicationInfo
								.loadIcon(pm);
						trafficInfo.setAppIcon(appIcon);

						int uid = packInfo.applicationInfo.uid;

						long trafficDownLong = TrafficStats.getUidRxBytes(uid); // 总接收量
						long trafficUpLong = TrafficStats.getUidTxBytes(uid); // 总发送量
						long trafficTotalLong = trafficDownLong + trafficUpLong;

						String trafficDown = Formatter.formatFileSize(context,
								trafficDownLong);
						if(trafficDown.equals("-1.00B")  ){
							trafficDown = "0.00B";
						}
						String trafficUp = Formatter.formatFileSize(context,
								trafficUpLong);
						if(trafficUp.equals("-1.00B")  ){
							trafficUp = "0.00B";
						}
						String trafficTotal = Formatter.formatFileSize(context,
								trafficTotalLong);
						if(trafficTotal.equals("-2.00B")  ){
							trafficTotal = "0.00B";
						}
						trafficInfo.setTrafficUp(trafficUp);
						trafficInfo.setTrafficDown(trafficDown);
						trafficInfo.setTrafficTotal(trafficTotal);

						trafficInfos.add(trafficInfo);
					}
				}
			}
		}
		return trafficInfos;
	}
}
