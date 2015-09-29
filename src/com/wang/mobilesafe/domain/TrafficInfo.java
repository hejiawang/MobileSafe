package com.wang.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class TrafficInfo {

	private Drawable appIcon;
	private String appName;
	private String trafficUp;
	private String trafficDown;
	private String trafficTotal;

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getTrafficUp() {
		return trafficUp;
	}

	public void setTrafficUp(String trafficUp) {
		this.trafficUp = trafficUp;
	}

	public String getTrafficDown() {
		return trafficDown;
	}

	public void setTrafficDown(String trafficDown) {
		this.trafficDown = trafficDown;
	}

	public String getTrafficTotal() {
		return trafficTotal;
	}

	public void setTrafficTotal(String trafficTotal) {
		this.trafficTotal = trafficTotal;
	}
}
