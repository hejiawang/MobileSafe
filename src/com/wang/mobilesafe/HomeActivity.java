package com.wang.mobilesafe;

import com.wang.mobilesafe.adapter.HomeAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class HomeActivity extends Activity {
	
	private GridView gv_home;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		gv_home = (GridView)findViewById(R.id.gv_home);
		gv_home.setAdapter(new HomeAdapter(this));
		
	}
}
