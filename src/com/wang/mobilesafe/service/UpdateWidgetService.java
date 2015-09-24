package com.wang.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.wang.mobilesafe.R;
import com.wang.mobilesafe.receiver.MyWidget;
import com.wang.mobilesafe.utils.TaskUtil;

public class UpdateWidgetService extends Service {
	private Timer timer;
	private TimerTask task;
	private AppWidgetManager awm;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		awm = AppWidgetManager.getInstance(getApplicationContext());
		// 开启定期执行的任务.
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				// 更新widget
				ComponentName provider = new ComponentName(
						getApplicationContext(), MyWidget.class);
				RemoteViews views = new RemoteViews(getPackageName(),
						R.layout.process_widget);
				views.setTextViewText(
						R.id.process_count,
						"正在运行的软件:"
								+ TaskUtil
										.getRunningProcessCount(getApplicationContext())
								+ "个");
				views.setTextViewText(
						R.id.process_memory,
						"可用内存:"
								+ Formatter.formatFileSize(
										getApplicationContext(),
										TaskUtil.getAvailRam(getApplicationContext())));
				Intent intent = new Intent();// 自定义的广播事件.
				intent.setAction("com.wang.killbgprocess");
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						getApplicationContext(), 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				awm.updateAppWidget(provider, views);

			}
		};
		timer.schedule(task, 1000, 2000);

		super.onCreate();
	}

	public void onDestroy() {
		timer.cancel();
		task = null;
		timer = null;
	};

}
