package com.wang.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.mobilesafe.domain.TaskInfo;
import com.wang.mobilesafe.engine.TaskInfoProvider;
import com.wang.mobilesafe.ui.MyToast;
import com.wang.mobilesafe.utils.MyAsyncTask;
import com.wang.mobilesafe.utils.TaskUtil;

public class TaskManagerActivity extends Activity {

	private TextView tv_task_count; // 总进程数
	private TextView tv_mem_info; // 可用/总内存
	private ListView lv_task_manager;
	private View loading;
	private Button bt_select_all;

	private int runningProcessCount;
	private long availRom;
	private long totalRom;

	private List<TaskInfo> taskInfos;
	private List<TaskInfo> userTaskInfos;
	private List<TaskInfo> systemTaskInfos;

	private TaskAdapter taskAdapter;

	private SharedPreferences sp;

	private boolean selectAll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);

		sp = getSharedPreferences("config", MODE_PRIVATE);

		tv_task_count = (TextView) findViewById(R.id.tv_task_count);
		tv_mem_info = (TextView) findViewById(R.id.tv_mem_info);
		lv_task_manager = (ListView) findViewById(R.id.lv_task_manager);
		loading = findViewById(R.id.loading);
		bt_select_all = (Button) findViewById(R.id.bt_select_all);

		runningProcessCount = TaskUtil.getRunningProcessCount(this);
		availRom = TaskUtil.getAvailRam(this);
		totalRom = TaskUtil.getTotalRam();

		tv_task_count.setText("运行中进程:" + runningProcessCount + "个");
		tv_mem_info.setText("可用/总内存:"
				+ Formatter.formatFileSize(this, availRom) + "/"
				+ Formatter.formatFileSize(this, totalRom));

		lv_task_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Object obj = lv_task_manager.getItemAtPosition(position);
				if (obj != null) {
					TaskInfo taskInfo = (TaskInfo) obj;
					if (getPackageName().equals(taskInfo.getPackName())) { // 本程序
						return;
					}
					if (taskInfo.isChecked()) {
						taskInfo.setChecked(false);
					} else {
						taskInfo.setChecked(true);
					}
					taskAdapter.notifyDataSetChanged();
				}
			}
		});

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
				taskAdapter = new TaskAdapter();
				lv_task_manager.setAdapter(taskAdapter);
				loading.setVisibility(View.INVISIBLE);
			}

			@Override
			public void doInBackground() {
				taskInfos = TaskInfoProvider
						.getTaskInfos(getApplicationContext());
				userTaskInfos = new ArrayList<TaskInfo>();
				systemTaskInfos = new ArrayList<TaskInfo>();
				for (TaskInfo taskInfo : taskInfos) {
					if (taskInfo.isUserTask()) {
						userTaskInfos.add(taskInfo);
					} else {
						systemTaskInfos.add(taskInfo);
					}
				}

			}
		}.execute();
	}

	/**
	 * 一键清理的单击事件
	 * 
	 * @param view
	 */
	public void killAll(View view) {

		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		int count = 0;
		long savedMem = 0;
		List<TaskInfo> killedTasks = new ArrayList<TaskInfo>();
		for (TaskInfo taskInfo : userTaskInfos) {
			if (taskInfo.isChecked()) {
				am.killBackgroundProcesses(taskInfo.getPackName());
				count++;
				savedMem += taskInfo.getMenSize();
				killedTasks.add(taskInfo);
			}
		}
		for (TaskInfo taskInfo : systemTaskInfos) {
			if (taskInfo.isChecked()) {
				am.killBackgroundProcesses(taskInfo.getPackName());
				count++;
				savedMem += taskInfo.getMenSize();
				killedTasks.add(taskInfo);
			}
		}
		String memStr = Formatter.formatFileSize(this, savedMem);
		// Toast.makeText(this, "杀死了" + count + "个进程,共释放" + memStr + "内存", 0)
		// .show();
		// 使用自定义toast
		MyToast.show(R.drawable.notification, "杀死了" + count + "个进程,共释放"
				+ memStr + "内存", this);
		// 把杀死的条目从界面移除
		for (TaskInfo taskInfo : killedTasks) {
			if (taskInfo.isUserTask()) {
				userTaskInfos.remove(taskInfo);
			} else {
				systemTaskInfos.remove(taskInfo);
			}
		}
		taskAdapter.notifyDataSetChanged();
		runningProcessCount -= count;
		tv_task_count.setText("运行中进程:" + runningProcessCount + "个");
		availRom += savedMem;
		tv_mem_info.setText("可用/总内存:"
				+ Formatter.formatFileSize(this, availRom) + "/"
				+ Formatter.formatFileSize(this, totalRom));
	}

	/**
	 * 全选的单击事件
	 * 
	 * @param view
	 */
	public void selectAll(View view) {

		if (selectAll) { // 已经全选了
			for (TaskInfo taskInfo : userTaskInfos) {
				taskInfo.setChecked(false);
			}
			for (TaskInfo taskInfo : systemTaskInfos) {
				taskInfo.setChecked(false);
			}
			taskAdapter.notifyDataSetChanged();
			selectAll = false;
			bt_select_all.setText("全选");
		} else {
			for (TaskInfo taskInfo : userTaskInfos) {
				taskInfo.setChecked(true);
				if (getPackageName().equals(taskInfo.getPackName())) { // 本程序
					taskInfo.setChecked(false);
				}
			}
			for (TaskInfo taskInfo : systemTaskInfos) {
				taskInfo.setChecked(true);
			}
			taskAdapter.notifyDataSetChanged();
			selectAll = true;
			bt_select_all.setText("全不选");
		}
	}

	/**
	 * 设置的单击事件
	 * 
	 * @param view
	 */
	public void setting(View view) {
		Intent intent = new Intent(this, TaskSettingActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 200) {
			taskAdapter.notifyDataSetChanged();
		}
	}

	private class TaskAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			boolean showsystem = sp.getBoolean("showsystem", false);
			if (showsystem) {
				return userTaskInfos.size() + systemTaskInfos.size() + 2;
			} else {
				return userTaskInfos.size() + 1;
			}
		}

		@Override
		public Object getItem(int position) {
			if (position == 0) {
				return null;
			} else if (position == userTaskInfos.size() + 1) {
				return null;
			} else if (position <= userTaskInfos.size()) { // 用户程序
				int newPosition = position - 1;
				return userTaskInfos.get(newPosition);
			} else { // 系统程序
				int newPosition = position - userTaskInfos.size() - 2;
				return systemTaskInfos.get(newPosition);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view;
			ViewHolder holder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.list_task_item, null);
				holder = new ViewHolder();
				holder.tv_task_name = (TextView) view
						.findViewById(R.id.tv_task_name);
				holder.tv_task_men = (TextView) view
						.findViewById(R.id.tv_task_men);
				holder.cb_task_status = (CheckBox) view
						.findViewById(R.id.cb_task_status);
				holder.iv_task_icon = (ImageView) view
						.findViewById(R.id.iv_task_icon);
				view.setTag(holder);
			}
			// 在ListView中分组显示内容
			TaskInfo taskInfo;
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setTextSize(18);
				tv.setTextColor(Color.BLUE);
				tv.setBackgroundResource(R.color.gray);
				tv.setText("用户进程(" + userTaskInfos.size() + ")");
				return tv;
			} else if (position == userTaskInfos.size() + 1) {
				TextView tv = new TextView(getApplicationContext());
				tv.setTextSize(18);
				tv.setTextColor(Color.BLUE);
				tv.setBackgroundResource(R.color.gray);
				tv.setText("系统进程(" + systemTaskInfos.size() + ")");
				return tv;
			} else if (position <= userTaskInfos.size()) { // 用户程序
				int newPosition = position - 1;
				taskInfo = userTaskInfos.get(newPosition);
			} else { // 系统程序
				int newPosition = position - userTaskInfos.size() - 2;
				taskInfo = systemTaskInfos.get(newPosition);
			}
			holder.iv_task_icon.setImageDrawable(taskInfo.getTaskIcon());
			holder.tv_task_name.setText(taskInfo.getTaskName());
			holder.tv_task_men.setText("内存占用:"
					+ Formatter.formatFileSize(getApplicationContext(),
							taskInfo.getMenSize()));
			holder.cb_task_status.setChecked(taskInfo.isChecked());
			if (getPackageName().equals(taskInfo.getPackName())) { // 本程序
				holder.cb_task_status.setVisibility(View.INVISIBLE);
			} else {
				holder.cb_task_status.setVisibility(View.VISIBLE);
			}
			return view;
		}
	}

	static class ViewHolder {
		TextView tv_task_name;
		TextView tv_task_men;
		CheckBox cb_task_status;
		ImageView iv_task_icon;
	}
}
