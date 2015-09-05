package com.wang.mobilesafe;

import com.wang.mobilesafe.utils.ActivityUtil;

public class Setup1Activity extends BaseSetupActivity {

	@Override
	public void initView() {
		
		setContentView(R.layout.activity_setup1);
	}

	@Override
	public void setupView() {

	}

	@Override
	public void showNext() {
		
		ActivityUtil.startActivityAndFinish(this, Setup2Activity.class);
	}

	@Override
	public void showPre() {

	}

}
