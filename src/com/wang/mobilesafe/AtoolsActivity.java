package com.wang.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.wang.mobilesafe.engine.SmsProvider;
import com.wang.mobilesafe.utils.ActivityUtil;

public class AtoolsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}

	/**
	 * 号码归属地查询的点击事件
	 * 
	 * @param view
	 */
	public void numberAddressQuery(View view) {

		ActivityUtil.startActivity(this, NumberQueryActivity.class);

		// 如果用startActivityAndFinish开启“号码归属地查询页面”，也就是将“高级工具”finish了，
		// 那么返回后是home主页面，就不是“高级工具”页面了
		// ActivityUtil.startActivityAndFinish(this, NumberQueryActivity.class);
	}

	/**
	 * 创建桌面快捷图标
	 * 
	 * @param view
	 */
	public void createIcon(View view) {

		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		// 不允许重复创建
		intent.putExtra("duplicate", false);

		Intent shortCutIntent = new Intent();
		shortCutIntent.setAction("com.wang.xxx");
		shortCutIntent.addCategory(Intent.CATEGORY_DEFAULT);

		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "手机卫士");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory
				.decodeResource(getResources(), R.drawable.ic_launcher));
		sendBroadcast(intent);

		Toast.makeText(this, "创建成功", 1).show();
	}

	/**
	 * 查询常用号码
	 * 
	 * @param view
	 */
	public void commonNumberQuery(View view) {

		ActivityUtil.startActivity(this, CommonNumberActivity.class);
	}

	private ProgressDialog pd;

	/**
	 * 短信备份的单击事件
	 * 
	 * @param view
	 */
	public void backUpSms(View view) {

		try {
			File file = new File(Environment.getExternalStorageDirectory(),
					"smsbackup.xml");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);

			new AsyncTask<OutputStream, Integer, Boolean>() {

				@Override
				protected Boolean doInBackground(OutputStream... params) {

					try {
						SmsProvider smsProvider = new SmsProvider(
								getApplicationContext());
						smsProvider.backUpSms(params[0],
								new SmsProvider.BackUpProcessListener() {

									@Override
									public void onProcessUpdate(int process) {
										pd.setProgress(process);
									}

									@Override
									public void beforeBackUp(int max) {
										pd.setMax(max);
									}
								});
						return true;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}

				}

				@Override
				protected void onPreExecute() {

					super.onPreExecute();
					pd = new ProgressDialog(AtoolsActivity.this);
					pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					pd.setTitle("提示:");
					pd.setMessage("正在备份短信...");
					pd.show();
				}

				@Override
				protected void onPostExecute(Boolean result) {

					super.onPostExecute(result);
					pd.dismiss();
					if (result) {
						Toast.makeText(getApplicationContext(), "备份完成", 1)
								.show();
					} else {
						Toast.makeText(getApplicationContext(), "备份失败", 1)
								.show();
					}
				}

				@Override
				protected void onProgressUpdate(Integer... values) {
					super.onProgressUpdate(values);
				}

			}.execute(fos);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "备份失败", 1).show();
		}
	}

	/**
	 * 短信还原单击事件
	 * 
	 * @param view
	 */
	public void restoreSms(View view) {
		SmsProvider smsProvider = new SmsProvider(getApplicationContext());
		smsProvider.restoreSms();
	}

	/**
	 * 程序锁的单击事件
	 * 
	 * @param view
	 */
	public void appLock(View view) {
		ActivityUtil.startActivity(this, AppLockActiviry.class);
	}

	/**
	 * SlidingDrawer测试
	 * 
	 * @param view
	 */
	public void slidingTest(View view) {
		ActivityUtil.startActivity(this, Test_Sliding_Drawer.class);
	}
}
