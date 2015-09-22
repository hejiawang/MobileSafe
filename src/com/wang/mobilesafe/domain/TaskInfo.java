package com.wang.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class TaskInfo {

	private Drawable taskIcon;
	private String taskName;
	private String packName;
	private long menSize;
	private boolean checked;
	private boolean userTask;

	@Override
	public String toString() {
		return "TaskInfo [taskName=" + taskName + ", packName=" + packName
				+ ", menSize=" + menSize + ", userTask=" + userTask + "]";
	}

	public Drawable getTaskIcon() {
		return taskIcon;
	}

	public void setTaskIcon(Drawable taskIcon) {
		this.taskIcon = taskIcon;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public long getMenSize() {
		return menSize;
	}

	public void setMenSize(long menSize) {
		this.menSize = menSize;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isUserTask() {
		return userTask;
	}

	public void setUserTask(boolean userTask) {
		this.userTask = userTask;
	}
}
