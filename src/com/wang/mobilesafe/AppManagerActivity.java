package com.wang.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.mobilesafe.domain.AppInfo;
import com.wang.mobilesafe.engine.AppInfoProvider;
import com.wang.mobilesafe.utils.AvailSDAndERomUtil;
import com.wang.mobilesafe.utils.MyAsyncTask;

public class AppManagerActivity extends Activity {

	private TextView tv_free_mem;
	private TextView tv_free_sd;
	private ListView lv_app_manager;
	private View loading;
	private TextView tv_appmanager_status;

	private List<AppInfo> appInfos;
	private List<AppInfo> userAppInfos;
	private List<AppInfo> systemAppInfos;

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

		lv_app_manager.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if (userAppInfos != null && systemAppInfos != null) {
					if (firstVisibleItem >= (userAppInfos.size() + 1)) {
						tv_appmanager_status.setText("系统程序("
								+ systemAppInfos.size() + ")");
					} else {
						tv_appmanager_status.setText("用户程序("
								+ userAppInfos.size() + ")");
					}
				}
//				int[] location = new int[2];
//				View topView = lv_app_manager.getChildAt(0);
//				if (topView != null) {
//					topView.getLocationInWindow(location);
//					System.out.println("y=" + location[1]);
//				}
			}
		});

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
		public int getCount() {
			return userAppInfos.size() + systemAppInfos.size() + 2;
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
			return view;
		}
	}

	static class ViewHolder {
		TextView tv_app_name;
		TextView tv_app_location;
		TextView tv_app_version;
		ImageView iv_app_icon;
	}
}
