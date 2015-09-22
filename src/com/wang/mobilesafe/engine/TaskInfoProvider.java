package com.wang.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.wang.mobilesafe.R;
import com.wang.mobilesafe.domain.TaskInfo;

public class TaskInfoProvider {

	/**
	 * 获取手机里面所有运行的进程信息
	 * @param context context
	 * @return 进程信息集合
	 */
	public static List<TaskInfo> getTaskInfos(Context context) {

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();

		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();

		for (RunningAppProcessInfo processInfo : processInfos) {

			TaskInfo taskInfo = new TaskInfo();

			String packName = processInfo.processName;
			taskInfo.setPackName(packName);

			try {
				ApplicationInfo applicationInfo = pm.getApplicationInfo(
						packName, 0);
				if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					taskInfo.setUserTask(true);
				} else {
					taskInfo.setUserTask(false);
				}
				taskInfo.setTaskIcon(applicationInfo.loadIcon(pm));
				taskInfo.setTaskName(applicationInfo.loadLabel(pm).toString());
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				taskInfo.setTaskIcon(context.getResources().getDrawable(
						R.drawable.ic_launcher));
				taskInfo.setTaskName(packName);
			}
			long memSize = am
					.getProcessMemoryInfo(new int[] { processInfo.pid })[0]
					.getTotalPrivateDirty() * 1024;
			taskInfo.setMenSize(memSize);
			taskInfos.add(taskInfo);
		}
		return taskInfos;
	}
}
