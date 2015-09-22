package com.wang.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ShareActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		tv.setText("具有分享功能的activity");
		String text = getIntent().getStringExtra(Intent.EXTRA_TEXT);
		Toast.makeText(this, "分享" + text, 0).show();
		setContentView(tv);
	}
}
