package com.wang.mobilesafe;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class SystemOptActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_opt);

		TabHost tabHost = getTabHost(); // 获得最外层的容器

		TabSpec tabSpec1 = tabHost.newTabSpec("缓存清理");
		tabSpec1.setIndicator(this.getTabView(R.drawable.tab1, "缓存清理"));
		tabSpec1.setContent(new Intent(this, CleanCacheActivity.class));
		tabHost.addTab(tabSpec1);

		TabSpec tabSpec2 = tabHost.newTabSpec("SD卡清理");
		tabSpec2.setIndicator(this.getTabView(R.drawable.tab2, "SD卡清理"));
		tabSpec2.setContent(new Intent(this, CleanSDActivity.class));
		tabHost.addTab(tabSpec2);

		TabSpec tabSpec3 = tabHost.newTabSpec("启动项清理");
		tabSpec3.setIndicator(this.getTabView(R.drawable.tab3, "启动项清理"));
		tabSpec3.setContent(new Intent(this, CleanStartupActivity.class));
		tabHost.addTab(tabSpec3);
	}

	private View getTabView(int icon, String text) {

		View view = View.inflate(this, R.layout.tab_system_opt, null);
		ImageView iv_tab = (ImageView) view.findViewById(R.id.iv_tab);
		TextView tv_tab = (TextView) view.findViewById(R.id.tv_tab);

		iv_tab.setImageResource(icon);
		tv_tab.setText(text);

		return view;
	}
}
