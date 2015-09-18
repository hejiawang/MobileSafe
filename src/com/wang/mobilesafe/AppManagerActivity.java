package com.wang.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

	private List<AppInfo> appInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);

		tv_free_mem = (TextView) findViewById(R.id.tv_free_mem);
		tv_free_sd = (TextView) findViewById(R.id.tv_free_sd);
		lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);
		loading = (View) findViewById(R.id.loading);

		tv_free_sd.setText("可用SD卡:" + AvailSDAndERomUtil.getAvailSD(this));
		tv_free_mem.setText("可用内存:" + AvailSDAndERomUtil.getAvailRom(this));

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
			}
		}.execute();
	}

	private class AppInfoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return appInfos.size();
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

			AppInfo appInfo = appInfos.get(position);
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
