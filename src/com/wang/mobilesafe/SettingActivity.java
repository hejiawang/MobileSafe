package com.wang.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.mobilesafe.service.CallSmsFireWallSerivce;
import com.wang.mobilesafe.service.ShowLocationService;
import com.wang.mobilesafe.service.WatchDogService;
import com.wang.mobilesafe.ui.SettingView;
import com.wang.mobilesafe.utils.ActivityUtil;
import com.wang.mobilesafe.utils.ServiceStatusUtil;

public class SettingActivity extends Activity implements OnClickListener {

	private SettingView sv_setting_update;
	private SettingView sv_setting_showaddress;
	private RelativeLayout rl_setting_changebg;
	private RelativeLayout rl_setting_change_location;
	private TextView tv_setting_addressbg_color;
	private SettingView sv_setting_callsmsfirewall;
	private SettingView sv_setting_applock;

	private SharedPreferences sp;
	private Intent showAddressService;
	private Intent callSmsFireWallIntent;
	private Intent appLockServiceIntent;

	String[] items = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		sp = getSharedPreferences("config", MODE_PRIVATE);

		// 自动更新设置——start
		sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
		// 默认开启自动更新
		// sp.getBoolean("update", true);——若"update"没有存储过，就为默认的true，
		boolean isupdate = sp.getBoolean("update", true);
		sv_setting_update.setChecked(isupdate);

		sv_setting_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Editor editor = sp.edit();

				// 判断是否被勾选
				if (sv_setting_update.isChecked()) {
					sv_setting_update.setChecked(false);
					editor.putBoolean("update", false);
				} else {
					sv_setting_update.setChecked(true);
					editor.putBoolean("update", true);
				}

				// 不要忘记commit
				editor.commit();

			}
		});
		// 自动更新设置——end

		// 号码归属地设置——start
		sv_setting_showaddress = (SettingView) findViewById(R.id.sv_setting_showaddress);
		showAddressService = new Intent(this, ShowLocationService.class);
		sv_setting_showaddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (sv_setting_showaddress.isChecked()) {
					sv_setting_showaddress.setChecked(false);
					stopService(showAddressService);
				} else {
					sv_setting_showaddress.setChecked(true);
					startService(showAddressService);
				}
			}
		});
		// 号码归属地设置——end

		// 归属地提示风格start
		rl_setting_changebg = (RelativeLayout) findViewById(R.id.rl_setting_changebg);
		rl_setting_changebg.setOnClickListener(this);
		tv_setting_addressbg_color = (TextView) findViewById(R.id.tv_setting_addressbg_color);
		int which = sp.getInt("which", 0);
		tv_setting_addressbg_color.setText(items[which]);
		// 归属地提示风格end

		// 归属地提示位置start
		rl_setting_change_location = (RelativeLayout) findViewById(R.id.rl_setting_change_location);
		rl_setting_change_location.setOnClickListener(this);
		// 归属地提示位置end

		// 通讯防火墙设置start
		sv_setting_callsmsfirewall = (SettingView) findViewById(R.id.sv_setting_callsmsfirewall);
		callSmsFireWallIntent = new Intent(this, CallSmsFireWallSerivce.class);
		sv_setting_callsmsfirewall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (sv_setting_callsmsfirewall.isChecked()) {
					sv_setting_callsmsfirewall.setChecked(false);
					stopService(callSmsFireWallIntent);
				} else {
					sv_setting_callsmsfirewall.setChecked(true);
					startService(callSmsFireWallIntent);
				}
			}
		});
		// 通讯防火墙设置end

		// 程序锁设置start
		sv_setting_applock = (SettingView) findViewById(R.id.sv_setting_applock);
		appLockServiceIntent = new Intent(this, WatchDogService.class);
		sv_setting_applock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (sv_setting_applock.isChecked()) {
					sv_setting_applock.setChecked(false);
					stopService(appLockServiceIntent);
				} else {
					sv_setting_applock.setChecked(true);
					startService(appLockServiceIntent);
				}
			}
		});
		// 程序锁设置end
	}

	/**
	 * 当页面重新可见时执行
	 */
	@Override
	protected void onStart() {

		// 重新初始化服务的状态
		if (ServiceStatusUtil.isServiceRuuing(
				"com.wang.mobilesafe.service.ShowLocationService", this)) {
			sv_setting_showaddress.setChecked(true);
		} else {
			sv_setting_showaddress.setChecked(false);
		}

		if (ServiceStatusUtil.isServiceRuuing(
				"com.wang.mobilesafe.service.CallSmsFireWallSerivce", this)) {
			sv_setting_callsmsfirewall.setChecked(true);
		} else {
			sv_setting_callsmsfirewall.setChecked(false);
		}

		if (ServiceStatusUtil.isServiceRuuing(
				"com.wang.mobilesafe.service.WatchDogService", this)) {
			sv_setting_applock.setChecked(true);
		} else {
			sv_setting_applock.setChecked(false);
		}

		super.onStart();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rl_setting_changebg:
			showChangeBgDialog();
			break;
		case R.id.rl_setting_change_location:
			ActivityUtil.startActivity(this, DragViewActivity.class);
			break;
		}
	}

	/**
	 * 显示归属地提示风格的Dialog
	 */
	private void showChangeBgDialog() {

		int which = sp.getInt("which", 0);
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("归属地提示框风格");
		builder.setIcon(R.drawable.notification);
		builder.setSingleChoiceItems(items, which,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Editor editor = sp.edit();
						editor.putInt("which", which);
						editor.commit();
						dialog.dismiss();
						tv_setting_addressbg_color.setText(items[which]);
					}
				});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// do nothing
			}
		});
		builder.show();
	}

}
