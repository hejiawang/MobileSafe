package com.wang.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wang.mobilesafe.domain.TaskInfo;
import com.wang.mobilesafe.engine.TaskInfoProvider;
import com.wang.mobilesafe.utils.MyAsyncTask;
import com.wang.mobilesafe.utils.TaskUtil;

public class TaskManagerActivity extends Activity {

	private TextView tv_task_count; // 总进程数
	private TextView tv_mem_info; // 可用/总内存
	private ListView lv_task_manager;
	private View loading;

	private int runningProcessCount;
	private long availRom;
	private long totalRom;

	private List<TaskInfo> taskInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);

		tv_task_count = (TextView) findViewById(R.id.tv_task_count);
		tv_mem_info = (TextView) findViewById(R.id.tv_mem_info);
		lv_task_manager = (ListView) findViewById(R.id.lv_task_manager);
		loading = findViewById(R.id.loading);

		runningProcessCount = TaskUtil.getRunningProcessCount(this);
		availRom = TaskUtil.getAvailRam(this);
		totalRom = TaskUtil.getTotalRam();

		tv_task_count.setText("运行中进程:" + runningProcessCount + "个");
		tv_mem_info.setText("可用/总内存:"
				+ Formatter.formatFileSize(this, availRom) + "/"
				+ Formatter.formatFileSize(this, totalRom));

		fillData();
	}

	/**
	 * 填充数据
	 */
	private void fillData() {
		new MyAsyncTask() {

			@Override
			public void onPreExecute() {
				loading.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPostExecute() {
				lv_task_manager.setAdapter(new TaskAdapter());
				loading.setVisibility(View.INVISIBLE);
			}

			@Override
			public void doInBackground() {
				taskInfos = TaskInfoProvider
						.getTaskInfos(getApplicationContext());
			}
		}.execute();
	}

	private class TaskAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return taskInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(getApplicationContext());
			tv.setTextSize(20);
			tv.setTextColor(Color.RED);
			tv.setText(taskInfos.get(position).toString());
			return tv;
		}
	}
}
