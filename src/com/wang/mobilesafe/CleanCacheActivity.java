package com.wang.mobilesafe;

import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 缓存清理的activity
 * 
 * @author wang
 *
 */
public class CleanCacheActivity extends Activity {
	private PackageManager pm;
	private LinearLayout ll_container;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			MyPackageInfo info = (MyPackageInfo) msg.obj;

			if (info.cachesize > 0) {

				View view = View.inflate(getApplicationContext(),
						R.layout.list_clean_cache, null);
				ImageView iv = (ImageView) view.findViewById(R.id.imageView1);
				TextView tv_name = (TextView) view.findViewById(R.id.textView1);
				TextView tv_codesize = (TextView) view
						.findViewById(R.id.textView2);

				try {
					iv.setImageDrawable(pm.getApplicationInfo(info.packname, 0)
							.loadIcon(pm));
					tv_name.setText(pm.getApplicationInfo(info.packname, 0)
							.loadLabel(pm));
					tv_codesize.setText("缓存大小:"
							+ Formatter.formatFileSize(getApplicationContext(),
									info.cachesize));
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				ll_container.addView(view, 0);
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clean_cache);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);

		pm = getPackageManager();
		new Thread() {
			public void run() {
				List<PackageInfo> infos = pm.getInstalledPackages(0);
				for (PackageInfo info : infos) {
					String packname = info.packageName;
					getPackageSize(packname);
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	private void getPackageSize(String packname) {
		try {
			Method method = PackageManager.class.getMethod(
					"getPackageSizeInfo", new Class[] { String.class,
							IPackageStatsObserver.class });

			method.invoke(pm,
					new Object[] { packname, new MyObserver(packname) });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class MyObserver extends IPackageStatsObserver.Stub {
		private String packname;

		public MyObserver(String packname) {
			this.packname = packname;
		}

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			long cachesize = pStats.cacheSize;
			long codesize = pStats.codeSize;
			//long datasize = pStats.dataSize;

			MyPackageInfo mypackinfo = new MyPackageInfo();
			mypackinfo.codesize = codesize;
			mypackinfo.cachesize = cachesize;
			mypackinfo.packname = packname;
			Message msg = Message.obtain();
			msg.obj = mypackinfo;
			handler.sendMessage(msg);
		}

	}

	class MyPackageInfo {
		long codesize;
		long cachesize;
		String packname;
	}
}
