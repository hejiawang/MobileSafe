package com.wang.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {

	private String appName;
	private String packName;
	private String version;
	private Drawable appIcon;
	private boolean inRom;
	private boolean userApp;

	private boolean useSms;
	private boolean useGps;
	private boolean useContact;
	private boolean useNet;

	public boolean isUseSms() {
		return useSms;
	}

	public void setUseSms(boolean useSms) {
		this.useSms = useSms;
	}

	public boolean isUseGps() {
		return useGps;
	}

	public void setUseGps(boolean useGps) {
		this.useGps = useGps;
	}

	public boolean isUseContact() {
		return useContact;
	}

	public void setUseContact(boolean useContact) {
		this.useContact = useContact;
	}

	public boolean isUseNet() {
		return useNet;
	}

	public void setUseNet(boolean useNet) {
		this.useNet = useNet;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public boolean isInRom() {
		return inRom;
	}

	public void setInRom(boolean inRom) {
		this.inRom = inRom;
	}

	public boolean isUserApp() {
		return userApp;
	}

	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}
}
