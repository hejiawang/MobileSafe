package com.wang.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.mobilesafe.domain.AppInfo;
import com.wang.mobilesafe.engine.AppInfoProvider;
import com.wang.mobilesafe.utils.MyAsyncTask;

public class AppLockActiviry extends Activity implements OnClickListener {

	private TextView tv_unlock;
	private TextView tv_locked;
	private LinearLayout ll_unlock;
	private LinearLayout ll_locked;
	private ListView lv_unlock;
	private ListView lv_locked;
	private ProgressBar pd_loading;
	private TextView tv_unlock_status;
	private TextView tv_locked_status;

	private List<AppInfo> appInfos;

	private AppInfoAdapter unlockAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applock);

		tv_unlock = (TextView) findViewById(R.id.tv_unlock);
		tv_locked = (TextView) findViewById(R.id.tv_locked);
		ll_unlock = (LinearLayout) findViewById(R.id.ll_unlock);
		ll_locked = (LinearLayout) findViewById(R.id.ll_locked);
		lv_unlock = (ListView) findViewById(R.id.lv_unlock);
		lv_locked = (ListView) findViewById(R.id.lv_locked);
		pd_loading = (ProgressBar) findViewById(R.id.pd_loading);
		tv_unlock_status = (TextView) findViewById(R.id.tv_unlock_status);
		tv_locked_status = (TextView) findViewById(R.id.tv_locked_status);

		tv_unlock.setOnClickListener(this);
		tv_locked.setOnClickListener(this);

		new MyAsyncTask() {

			@Override
			public void onPreExecute() {
				pd_loading.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPostExecute() {
				unlockAdapter = new AppInfoAdapter(appInfos, true);
				lv_unlock.setAdapter(unlockAdapter);
				pd_loading.setVisibility(View.INVISIBLE);
			}

			@Override
			public void doInBackground() {

				appInfos = AppInfoProvider.getAppInfos(getApplicationContext());

			}
		}.execute();
		;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_unlock: // 未加锁
			tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
			tv_locked.setBackgroundResource(R.drawable.tab_right_default);
			ll_unlock.setVisibility(View.VISIBLE);
			ll_locked.setVisibility(View.INVISIBLE);
			break;

		case R.id.tv_locked: // 已加锁
			tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
			tv_locked.setBackgroundResource(R.drawable.tab_right_pressed);
			ll_unlock.setVisibility(View.INVISIBLE);
			ll_locked.setVisibility(View.VISIBLE);
			break;
		}
	}

	private class AppInfoAdapter extends BaseAdapter {

		private List<AppInfo> showAppInfos;
		private boolean unlockflag;

		public AppInfoAdapter(List<AppInfo> showAppInfos, boolean unlockflag) {
			this.showAppInfos = showAppInfos;
			this.unlockflag = unlockflag;
		}

		@Override
		public int getCount() {
			
			if( unlockflag ){
				tv_unlock_status.setText("未加锁软件(" + showAppInfos.size() + ")个");
			} else {
				tv_locked_status.setText("已加锁软件(" + showAppInfos.size() + ")个");
			}
			return showAppInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return showAppInfos.get(position);
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
				if (unlockflag) {
					view = View.inflate(getApplicationContext(),
							R.layout.list_appunlock_item, null);
				} else {
					view = View.inflate(getApplicationContext(),
							R.layout.list_applocked_item, null);
				}
				holder = new ViewHolder();
				holder.tv_applock_name = (TextView) view
						.findViewById(R.id.tv_applock_name);
				holder.iv_applock_icon = (ImageView) view
						.findViewById(R.id.iv_applock_icon);
				view.setTag(holder);
			}
			AppInfo appInfo = showAppInfos.get(position);
			holder.tv_applock_name.setText(appInfo.getAppName());
			holder.iv_applock_icon.setImageDrawable(appInfo.getAppIcon());
			return view;
		}
	}

	static class ViewHolder {
		TextView tv_applock_name;
		ImageView iv_applock_icon;
	}
}
