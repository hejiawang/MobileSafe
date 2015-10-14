package com.wang.mobilesafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.wang.mobilesafe.domain.UpdateInfo;
import com.wang.mobilesafe.engine.UpdateInfoParser;
import com.wang.mobilesafe.utils.CopyFileUtil;
import com.wang.mobilesafe.utils.DownLoadUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	public static final int PARSE_XML_ERROR = 10;
	public static final int PARSE_XML_SUCCESS = 11;
	public static final int SERVER_ERROR = 12;
	public static final int URL_ERROR = 13;
	public static final int NETWORK_ERROR = 14;
	private static final int DOWNLOAD_SUCCESS = 15;
	private static final int DOWNLOAD_ERROR = 16;
	private static final int COPY_ERROR = 17;

	protected static final String TAG = "SplashActivity";

	private TextView tv_splash_version;
	private UpdateInfo updateInfo;

	private ProgressDialog pd; // 下载进度的对话框

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {

			case PARSE_XML_ERROR:
				Toast.makeText(getApplicationContext(), "解析XML失败", 0).show();
				// 进入主线程界面
				loadMainUI();
				break;

			case SERVER_ERROR:
				Toast.makeText(getApplicationContext(), "服务器异常", 0).show();
				loadMainUI();
				break;

			case URL_ERROR:
				Toast.makeText(getApplicationContext(), "URL错误", 0).show();
				loadMainUI();
				break;

			case NETWORK_ERROR:
				Toast.makeText(getApplicationContext(), "网络异常", 0).show();
				loadMainUI();
				break;

			case PARSE_XML_SUCCESS:
				if (getAppVersion().equals(updateInfo.getVersion())) {
					// Log.i(TAG, "版本号相同，进入主界面");
					loadMainUI();
				} else {
					// Log.i(TAG, "版本号不相同，弹出升级提示对话框");
					showUpdateDialog();
				}
				break;
			case DOWNLOAD_ERROR:
				Toast.makeText(getApplicationContext(), "下载失败", 0).show();
				loadMainUI();
				break;
			case DOWNLOAD_SUCCESS:
				File file = (File) msg.obj;
				installApk(file);
				System.out.println("安装apk" + file.getAbsolutePath());
				finish();
				break;
			case COPY_ERROR:
				Toast.makeText(getApplicationContext(), "Copy数据库异常", 0).show();
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("版本：" + this.getAppVersion());

		// 链接服务器检查服务更新
		new Thread(new CheckVersionTask()).start();

		// splash页面淡出 2秒钟
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(2000);
		findViewById(R.id.rl_splash).startAnimation(aa);

		// 拷贝关键文件到系统目录,耗时操作,要新起线程
		// 1. 查询手机号码归属地的数据库文件
		copyAddressDB();
		// 2. 查询常用号码的数据库文件
		copyCommonNumDB();
		// 3.病毒数据库
		copyVirusDB();
	}

	/**
	 * 释放电话号码归属地数据库文件到系统目录
	 */
	private void copyAddressDB() {

		final File file = new File(getFilesDir(), "address.db");
		if (file.exists() && file.length() > 0) {
			// ...
		} else {

			new Thread() {

				public void run() {

					Message msg = Message.obtain();
					try {

						InputStream is = getAssets().open("address.db");
						File f = CopyFileUtil.copyFile(is,
								file.getAbsolutePath());
						if (f != null) {
							// ...
						} else {

							msg.what = COPY_ERROR;
						}
					} catch (IOException e) {

						e.printStackTrace();
						msg.what = COPY_ERROR;
					} finally {

						handler.sendMessage(msg);
					}
				};
			}.start();
		}
	}

	/**
	 * 释放常用号码数据库文件到系统目录
	 */
	private void copyCommonNumDB() {

		final File file = new File(getFilesDir(), "commonnum.db");
		if (file.exists() && file.length() > 0) {
			// ...
		} else {

			new Thread() {

				public void run() {

					Message msg = Message.obtain();
					try {

						InputStream is = getAssets().open("commonnum.db");
						File f = CopyFileUtil.copyFile(is,
								file.getAbsolutePath());
						if (f != null) {
							// ...
						} else {

							msg.what = COPY_ERROR;
						}
					} catch (IOException e) {

						e.printStackTrace();
						msg.what = COPY_ERROR;
					} finally {

						handler.sendMessage(msg);
					}
				};
			}.start();
		}
	}

	/**
	 * 释放病毒数据库文件到系统目录
	 */
	private void copyVirusDB() {

		final File file = new File(getFilesDir(), "antivirus.db");
		if (file.exists() && file.length() > 0) {
			// ...
		} else {

			new Thread() {

				public void run() {

					Message msg = Message.obtain();
					try {

						InputStream is = getAssets().open("antivirus.db");
						File f = CopyFileUtil.copyFile(is,
								file.getAbsolutePath());
						if (f != null) {
							// ...
						} else {

							msg.what = COPY_ERROR;
						}
					} catch (IOException e) {

						e.printStackTrace();
						msg.what = COPY_ERROR;
					} finally {

						handler.sendMessage(msg);
					}
				};
			}.start();
		}
	}

	/**
	 * 安装apk文件
	 * 
	 * @param file
	 *            要安装的apk文件
	 */
	private void installApk(File file) {

		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		// intent.setData(Uri.fromFile(file));
		// intent.setType("application/vnd.android.package-archive");
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");

		startActivity(intent);
	}

	/**
	 * 自动升级的对话框提醒
	 */
	private void showUpdateDialog() {

		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("升级提醒");
		builder.setMessage(updateInfo.getDescription());

		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				// 下载进度条设置
				pd = new ProgressDialog(SplashActivity.this);
				pd.setTitle("升级操作");
				pd.setMessage("正在下载...");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.show();

				// 在匿内部类中使用，要加final
				final String apkUrl = updateInfo.getApkurl();
				final File file = new File(Environment
						.getExternalStorageDirectory(), DownLoadUtil
						.getFileName(apkUrl));

				// 存储路径在SD上，所以要先判断SD卡是否可用
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {

					// 下载文件
					new Thread() {

						public void run() {

							// 对于下载进度，当然是下载的方法最了解
							File savedFile = DownLoadUtil.download(apkUrl,
									file.getAbsolutePath(), pd);
							Message msg = Message.obtain();

							if (savedFile != null) {
								// 下载成功
								msg.what = DOWNLOAD_SUCCESS;
								msg.obj = savedFile;
							} else {
								// 下载失败
								msg.what = DOWNLOAD_ERROR;
							}

							handler.sendMessage(msg);
							// 解散进度对话框
							pd.dismiss();
						}
					}.start();
				} else {

					Toast.makeText(getApplicationContext(), "SD卡不可用", 0).show();
					loadMainUI();
				}
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				loadMainUI();
			}
		});

		builder.show();
	}

	/**
	 * 进入主界面
	 */
	private void loadMainUI() {

		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		// 将当前splash页面结束，否则用户返回桌面在回到应用后，还会显示splash
		finish();
	}

	/**
	 * 获取应用程序版本号
	 * 
	 * @return
	 */
	private String getAppVersion() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// 如果抛出异常，返回空字符串
			return "";
		}
	}

	private class CheckVersionTask implements Runnable {

		@Override
		public void run() {

			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			boolean isupdate = sp.getBoolean("update", true);

			// 如果关闭自动更新。。。。。
			if (!isupdate) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				loadMainUI();
				return;
			}

			long startTime = System.currentTimeMillis();

			// obtain()返回一个已存在的Message，提高效率
			Message msg = Message.obtain();

			try {

				URL url = new URL(getResources().getString(R.string.serverurl));
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("GET");
				// conn.setConnectTimeout(5000);
				// 为了测试速度，时间少点
				conn.setConnectTimeout(1500);

				int code = conn.getResponseCode();
				if (code == 200) {
					InputStream is = conn.getInputStream();
					updateInfo = UpdateInfoParser.getUpdateInfo(is);

					if (updateInfo == null) {
						// 解析xml失败
						msg.what = PARSE_XML_ERROR;
					} else {
						// 解析成功
						msg.what = PARSE_XML_SUCCESS;
					}
				} else {
					// 服务器内部错误
					msg.what = SERVER_ERROR;
				}
			} catch (MalformedURLException e) {
				msg.what = URL_ERROR;
				e.printStackTrace();
			} catch (IOException e) {
				msg.what = NETWORK_ERROR;
				e.printStackTrace();
			} finally {

				long endTime = System.currentTimeMillis();
				long dTime = endTime - startTime;

				// 使splash页面停够2秒钟
				if (dTime < 2000) {
					try {
						Thread.sleep(2000 - dTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				handler.sendMessage(msg);
			}
		}
	}
}
