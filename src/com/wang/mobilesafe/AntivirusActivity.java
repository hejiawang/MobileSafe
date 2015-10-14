package com.wang.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wang.mobilesafe.db.dao.AntivirusDao;
import com.wang.mobilesafe.utils.MD5Util;

/**
 * 杀毒界面的activity
 * 
 * @author wang
 *
 */
public class AntivirusActivity extends Activity {

	private ImageView iv_scan;
	private TextView tv_scan_status;
	private ProgressBar progressBar1;
	private TextView tv_scan_total;
	private LinearLayout ll_scan_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anti_virus);

		iv_scan = (ImageView) findViewById(R.id.iv_scan);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		tv_scan_total = (TextView) findViewById(R.id.tv_scan_total);
		ll_scan_status = (LinearLayout) findViewById(R.id.ll_scan_status);

		this.scan();

		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				1.0f);
		ra.setDuration(500);
		ra.setRepeatCount(Animation.INFINITE);
		iv_scan.startAnimation(ra);
	}

	/**
	 * 扫描系统
	 */
	private void scan() {

		new AsyncTask<Void, Object, Void>() {

			List<PackageInfo> virusInfos;

			@Override
			protected Void doInBackground(Void... params) {

				try {

					Thread.sleep(500);
					publishProgress("正在扫描列表...");
					// tv_scan_status.setText("正在扫描列表...");
					// 检查系统中的每一个程序的签名信息，判断该签名信息是否在病毒库。
					PackageManager pm = getPackageManager();
					List<PackageInfo> packinfos = pm
							.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
									| PackageManager.GET_SIGNATURES);
					progressBar1.setMax(packinfos.size());
					int total = 0;
					for (PackageInfo packinfo : packinfos) {

						Signature[] signatures = packinfo.signatures;
						String signstr = signatures[0].toCharsString();
						String md5 = MD5Util.encode(signstr);
						String desc = AntivirusDao.findVirus(md5);
						if (desc != null) { // 病毒
							publishProgress(null, packinfo, true);
						} else {
							publishProgress(null, packinfo, false);
						}
						total++;
						progressBar1.setProgress(total);
						Thread.sleep(50);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				tv_scan_status.setText("正在初始化...");
				virusInfos = new ArrayList<PackageInfo>();
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				tv_scan_status.setText("扫描完毕...");
				iv_scan.clearAnimation();
				tv_scan_total.setText("扫描完毕...");
				if (virusInfos.size() > 0) {
					AlertDialog.Builder builder = new Builder(AntivirusActivity.this);
					builder.setTitle("警告");
					builder.setMessage("发现病毒，是否立即清理");
					builder.setPositiveButton("清理", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							for( PackageInfo info :virusInfos ){
								Intent intent = new Intent();
								intent.setAction(Intent.CATEGORY_DEFAULT);
								intent.setData(Uri.parse("package:"+ info.packageName));
								startActivity(intent);
							}
						}
					});
					builder.setNegativeButton("取消", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					builder.show();
				}
			}

			@Override
			protected void onProgressUpdate(Object... values) {

				super.onProgressUpdate(values);

				if (values.length == 1) {
					String text = (String) values[0];
					tv_scan_status.setText(text);
				} else {

					tv_scan_total.setText("正在扫描第" + progressBar1.getProgress()
							+ "个程序");
					PackageInfo packinfo = (PackageInfo) values[1];
					boolean result = (Boolean) values[2];

					TextView tv = new TextView(getApplicationContext());
					tv.setTextSize(20);

					if (result) {
						tv.setTextColor(Color.RED);
						tv.setText("发现病毒" + packinfo.packageName);
						virusInfos.add(packinfo);
					} else {
						tv.setTextColor(Color.BLUE);
						tv.setText("扫描安全" + packinfo.packageName);
					}
					ll_scan_status.addView(tv,0);
				}
			}
		}.execute();
	}
}
