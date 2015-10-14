package com.wang.mobilesafe;

import java.io.File;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

/**
 * 缓存SD卡的activity
 * 
 * @author wang
 *
 */
public class CleanSDActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		tv.setTextSize(30);
		tv.setTextColor(Color.RED);
		tv.setText("SD卡清理");
		setContentView(tv);

		File file = Environment.getExternalStorageDirectory();
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				String dirName = f.getName();
				// 查询数据库，查询这个dirName是否在数据库里面，
				//如果存在，提示用户删除文件，
				//deleteFile(dirName);
			}
		}
	}

	/**
	 * 删除文件夹下的全部文件
	 * 
	 * @param file
	 */
	private void deleteFile(File file) {

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				deleteFile(f);
			}
		} else {
			file.delete();
		}
	}
}
