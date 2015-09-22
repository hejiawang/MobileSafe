package com.wang.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.mobilesafe.domain.AppInfo;
import com.wang.mobilesafe.engine.AppInfoProvider;
import com.wang.mobilesafe.utils.AvailSDAndERomUtil;
import com.wang.mobilesafe.utils.MyAsyncTask;

public class AppManagerActivity extends Activity implements OnClickListener {

	private static final String TAG = "AppManagerActivity";

	private TextView tv_free_mem;
	private TextView tv_free_sd;
	private ListView lv_app_manager;
	private View loading;
	private TextView tv_appmanager_status;
	private LinearLayout ll_uninstall;
	private LinearLayout ll_start;
	private LinearLayout ll_share;

	private List<AppInfo> appInfos;
	private List<AppInfo> userAppInfos;
	private List<AppInfo> systemAppInfos;

	private PopupWindow popwindow;

	private AppInfo selectedAppInfo; // 被点击条目的appInfo对象

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);

		tv_free_mem = (TextView) findViewById(R.id.tv_free_mem);
		tv_free_sd = (TextView) findViewById(R.id.tv_free_sd);
		lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);
		loading = (View) findViewById(R.id.loading);
		tv_appmanager_status = (TextView) findViewById(R.id.tv_appmanager_status);

		tv_free_sd.setText("可用SD卡:" + AvailSDAndERomUtil.getAvailSD(this));
		tv_free_mem.setText("可用内存:" + AvailSDAndERomUtil.getAvailRom(this));

		lv_app_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Object obj = lv_app_manager.getItemAtPosition(position);
				if (obj instanceof AppInfo) {

					// 如果当前界面存在了弹出窗体，关闭它。
					dismissPopupWindow();

					selectedAppInfo = (AppInfo) obj;
					// Log.i(TAG, "packname:" + appInfo.getPackName());

					View contentView = View.inflate(getApplicationContext(),
							R.layout.ui_popupwindow_app, null);
					ll_uninstall = (LinearLayout) contentView
							.findViewById(R.id.ll_uninstall);
					ll_start = (LinearLayout) contentView
							.findViewById(R.id.ll_start);
					ll_share = (LinearLayout) contentView
							.findViewById(R.id.ll_share);

					ll_uninstall.setOnClickListener(AppManagerActivity.this);
					ll_start.setOnClickListener(AppManagerActivity.this);
					ll_share.setOnClickListener(AppManagerActivity.this);

					popwindow = new PopupWindow(contentView,
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT);
					popwindow.setBackgroundDrawable(new ColorDrawable(
							Color.TRANSPARENT));

					int[] location = new int[2];
					view.getLocationInWindow(location);
					popwindow.showAtLocation(parent,
							Gravity.TOP | Gravity.LEFT, location[0] + 120,
							location[1]);

					// 弹出popupwindow的动画效果.
					ScaleAnimation sa = new ScaleAnimation(0.2f, 1.0f, 0.2f,
							1.0f, 0.5f, 0.5f);
					sa.setDuration(600);
					TranslateAnimation ta = new TranslateAnimation(
							Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 0.1f,
							Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 0);
					ta.setDuration(600);
					AnimationSet set = new AnimationSet(false);
					set.addAnimation(sa);
					set.addAnimation(ta);
					contentView.startAnimation(set);
				}
			}

		});

		lv_app_manager.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				// 如果当前界面存在了弹出窗体，关闭它。
				dismissPopupWindow();

				if (userAppInfos != null && systemAppInfos != null) {
					if (firstVisibleItem >= (userAppInfos.size() + 1)) {
						tv_appmanager_status.setText("系统程序("
								+ systemAppInfos.size() + ")");
					} else {
						tv_appmanager_status.setText("用户程序("
								+ userAppInfos.size() + ")");
					}
				}
				// int[] location = new int[2];
				// View topView = lv_app_manager.getChildAt(0);
				// if (topView != null) {
				// topView.getLocationInWindow(location);
				// System.out.println("y=" + location[1]);
				// }
			}
		});

		fillData();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ll_uninstall:
			Log.i(TAG, "卸载" + selectedAppInfo.getAppName());
			// 如果应用程序是系统级的，提示用户，不能卸载
			if (selectedAppInfo.isUserApp()) {
				uninstallApk();
			} else {
				Toast.makeText(this, "系统级程序，不能卸载", 0).show();
			}
			break;
		case R.id.ll_start:
			Log.i(TAG, "启动" + selectedAppInfo.getAppName());
			startApk();
			break;
		case R.id.ll_share:
			Log.i(TAG, "分享" + selectedAppInfo.getAppName());
			shareApk();
			break;
		}
		dismissPopupWindow();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 10) {
			// 刷新数据
			fillData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 卸载软件
	 */
	private void uninstallApk() {

		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + selectedAppInfo.getPackName()));
		startActivityForResult(intent, 10);
	}

	/**
	 * 分享软件
	 */
	private void shareApk() {

		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "share a applitaion"
				+ selectedAppInfo.getAppName());
		startActivity(intent);
	}

	/**
	 * 启动软件
	 */
	private void startApk() {

		// 开启这个应用程序里面的第一个activity
		String packName = selectedAppInfo.getPackName();
		try {
			PackageInfo packInfo = getPackageManager().getPackageInfo(packName,
					PackageManager.GET_ACTIVITIES);
			ActivityInfo[] activityInfos = packInfo.activities;
			if (activityInfos != null && activityInfos.length > 0) {
				ActivityInfo activityInfo = activityInfos[0];
				String className = activityInfo.name;
				Intent intent = new Intent();
				intent.setClassName(packName, className);
				startActivity(intent);
			} else {
				Toast.makeText(this, "无法启动应用程序!!!", 0).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "无法启动应用程序", 0).show();
		}
	}

	/**
	 * 使弹出窗体消失.
	 */
	private void dismissPopupWindow() {
		if (popwindow != null && popwindow.isShowing()) {
			popwindow.dismiss();
			popwindow = null;
		}
	}

	/**
	 * 向listView中填充数据
	 */
	private void fillData() {
		new MyAsyncTask() {

			@Override
			public void onPreExecute() {
				loading.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPostExecute() {
				lv_app_manager.setAdapter(new AppInfoAdapter());
				loading.setVisibility(View.INVISIBLE);
			}

			@Override
			public void doInBackground() {
				appInfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);
				// 在ListView中分组显示
				userAppInfos = new ArrayList<AppInfo>();
				systemAppInfos = new ArrayList<AppInfo>();
				for (AppInfo appInfo : appInfos) {
					if (appInfo.isUserApp()) {
						userAppInfos.add(appInfo);
					} else {
						systemAppInfos.add(appInfo);
					}
				}

			}
		}.execute();
	}

	private class AppInfoAdapter extends BaseAdapter {

		@Override
		public boolean isEnabled(int position) {
			if (position == 0) {
				return false;
			} else if (position == userAppInfos.size() + 1) {
				return false;
			}
			return super.isEnabled(position);
		}

		@Override
		public int getCount() {
			return userAppInfos.size() + systemAppInfos.size() + 2;
		}

		@Override
		public Object getItem(int position) {

			if (position == 0) {
				return position;
			} else if (position == userAppInfos.size() + 1) {
				return position;
			} else if (position <= userAppInfos.size()) { // 用户程序
				int newPosition = position - 1;
				return userAppInfos.get(newPosition);
			} else { // 系统程序
				int newPosition = position - userAppInfos.size() - 2;
				return systemAppInfos.get(newPosition);
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
						R.layout.list_app_item, null);
				holder = new ViewHolder();
				holder.tv_app_name = (TextView) view
						.findViewById(R.id.tv_app_name);
				holder.tv_app_location = (TextView) view
						.findViewById(R.id.tv_app_location);
				holder.tv_app_version = (TextView) view
						.findViewById(R.id.tv_app_version);
				holder.iv_app_icon = (ImageView) view
						.findViewById(R.id.iv_app_icon);
				holder.ll_appstatus_container = (LinearLayout) view
						.findViewById(R.id.ll_appstatus_container);
				view.setTag(holder);
			}

			// 在ListView中分组显示内容
			AppInfo appInfo;
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setTextSize(18);
				tv.setTextColor(Color.BLUE);
				tv.setBackgroundResource(R.color.gray);
				tv.setText("用户程序(" + userAppInfos.size() + ")");
				return tv;
			} else if (position == userAppInfos.size() + 1) {
				TextView tv = new TextView(getApplicationContext());
				tv.setTextSize(18);
				tv.setTextColor(Color.BLUE);
				tv.setBackgroundResource(R.color.gray);
				tv.setText("系统程序(" + systemAppInfos.size() + ")");
				return tv;
			} else if (position <= userAppInfos.size()) { // 用户程序
				int newPosition = position - 1;
				appInfo = userAppInfos.get(newPosition);
			} else { // 系统程序
				int newPosition = position - userAppInfos.size() - 2;
				appInfo = systemAppInfos.get(newPosition);
			}

			holder.tv_app_name.setText(appInfo.getAppName());
			holder.tv_app_version.setText(appInfo.getVersion());
			holder.iv_app_icon.setImageDrawable(appInfo.getAppIcon());
			if (appInfo.isInRom()) {
				holder.tv_app_location.setText("手机内存" + appInfo.isUserApp());
			} else {
				holder.tv_app_location.setText("外部内存" + appInfo.isUserApp());
			}
			//清空前一个的图标
			holder.ll_appstatus_container.removeAllViews();
			if (appInfo.isUseContact()) {
				ImageView iv = new ImageView(getApplicationContext());
				iv.setImageResource(R.drawable.contact);
				holder.ll_appstatus_container.addView(iv, 60, 60);
			}
			if (appInfo.isUseSms()) {
				ImageView iv = new ImageView(getApplicationContext());
				iv.setImageResource(R.drawable.sms);
				holder.ll_appstatus_container.addView(iv, 60, 60);
			}
			if (appInfo.isUseNet()) {
				ImageView iv = new ImageView(getApplicationContext());
				iv.setImageResource(R.drawable.net);
				holder.ll_appstatus_container.addView(iv, 60, 60);
			}
			if (appInfo.isUseGps()) {
				ImageView iv = new ImageView(getApplicationContext());
				iv.setImageResource(R.drawable.gps);
				holder.ll_appstatus_container.addView(iv, 60, 60);
			}
			return view;
		}
	}

	static class ViewHolder {
		TextView tv_app_name;
		TextView tv_app_location;
		TextView tv_app_version;
		ImageView iv_app_icon;
		LinearLayout ll_appstatus_container;
	}
}
