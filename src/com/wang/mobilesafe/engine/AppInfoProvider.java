package com.wang.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.wang.mobilesafe.domain.AppInfo;

/**
 * 软件信息提供类
 * 
 * @author HeJW
 *
 */
public class AppInfoProvider {

	/**
	 * 获取手机上的所有软件的相关信息
	 * 
	 * @param context
	 *            context
	 * @return 手机上所有软件信息的集合
	 */
	public static List<AppInfo> getAppInfos(Context context) {

		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packInfos = pm
				.getInstalledPackages(PackageManager.GET_PERMISSIONS);
		for (PackageInfo packInfo : packInfos) {

			AppInfo appInfo = new AppInfo();

			String version = packInfo.versionName;
			appInfo.setVersion(version);

			String packName = packInfo.packageName;
			appInfo.setPackName(packName);

			String appName = packInfo.applicationInfo.loadLabel(pm).toString();
			appInfo.setAppName(appName);

			Drawable appIcon = packInfo.applicationInfo.loadIcon(pm);
			appInfo.setAppIcon(appIcon);

			if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				appInfo.setUserApp(true); // 用户应用
			} else {
				appInfo.setUserApp(false); // 系统应用
			}

			if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
				appInfo.setInRom(true); // 存储在手机内存
			} else {
				appInfo.setInRom(false); // 存储在SD卡上
			}

			String[] permissions = packInfo.requestedPermissions;
			if (permissions != null && permissions.length > 0) {
				for (String permission : permissions) {
					if ("android.permission.INTERNET".equals(permission)) {
						appInfo.setUseNet(true);
					} else if ("android.permission.READ_CONTACTS"
							.equals(permission)) {
						appInfo.setUseContact(true);
					} else if ("android.permission.SEND_SMS".equals(permission)) {
						appInfo.setUseSms(true);
					} else if ("android.permission.ACCESS_FINE_LOCATION"
							.equals(permission)) {
						appInfo.setUseGps(true);
					}
				}
			}

			appInfos.add(appInfo);
		}
		return appInfos;
	}
}
