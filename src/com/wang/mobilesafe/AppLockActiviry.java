package com.wang.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.mobilesafe.db.dao.AppLockDao;
import com.wang.mobilesafe.domain.AppInfo;
import com.wang.mobilesafe.engine.AppInfoProvider;
import com.wang.mobilesafe.ui.MyToast;
import com.wang.mobilesafe.utils.DelayExecuter;
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
	private List<AppInfo> appLockedInfos; // 存放已加锁程序信息
	private List<AppInfo> appunLockInfos; // 存放未加锁程序信息

	private AppInfoAdapter unlockAdapter;
	private AppInfoAdapter lockedAdapter;

	private AppLockDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applock);

		dao = new AppLockDao(this);

		appLockedInfos = new ArrayList<AppInfo>();
		appunLockInfos = new ArrayList<AppInfo>();

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

		// 未加锁条目的点击事件
		lv_unlock.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				AppInfo appInfo = (AppInfo) lv_unlock
						.getItemAtPosition(position);
				String packname = appInfo.getPackName();
				dao.add(packname); // 添加锁定的程序包名到数据库
				appLockedInfos.add(appInfo); // 添加被锁定的程序信息到内存集合
				appunLockInfos.remove(appInfo); // 把被锁定的程序从未锁定的内存集合中移除

				TranslateAnimation ta = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 1.0f,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0);
				ta.setDuration(800);
				view.setAnimation(ta);

				new DelayExecuter() {

					@Override
					public void onPostExecute() {
						unlockAdapter.notifyDataSetChanged();
						lockedAdapter.notifyDataSetChanged();
					}
				}.execute(800);
			}
		});

		// 已加锁条目的点击事件
		lv_locked.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				AppInfo appInfo = (AppInfo) lv_locked
						.getItemAtPosition(position);
				String packname = appInfo.getPackName();
				dao.delete(packname);
				appLockedInfos.remove(appInfo);
				appunLockInfos.add(appInfo);
				TranslateAnimation ta = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, -1.0f,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0);
				ta.setDuration(800);
				view.setAnimation(ta);

				new DelayExecuter() {

					@Override
					public void onPostExecute() {
						unlockAdapter.notifyDataSetChanged();
						lockedAdapter.notifyDataSetChanged();
					}
				}.execute(800);
			}
		});

		new MyAsyncTask() {

			@Override
			public void onPreExecute() {
				pd_loading.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPostExecute() {
				unlockAdapter = new AppInfoAdapter(appunLockInfos, true);
				lv_unlock.setAdapter(unlockAdapter);

				lockedAdapter = new AppInfoAdapter(appLockedInfos, false);
				lv_locked.setAdapter(lockedAdapter);

				pd_loading.setVisibility(View.INVISIBLE);
			}

			@Override
			public void doInBackground() {

				appInfos = AppInfoProvider.getAppInfos(getApplicationContext());
				for (AppInfo appInfo : appInfos) {
					if (dao.find(appInfo.getPackName())) {
						appLockedInfos.add(appInfo);
					} else {
						appunLockInfos.add(appInfo);
					}
				}
			}
		}.execute();
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

			if (unlockflag) {
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
