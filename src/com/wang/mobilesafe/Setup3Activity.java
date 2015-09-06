package com.wang.mobilesafe;

import com.wang.mobilesafe.utils.ActivityUtil;

public class Setup3Activity extends BaseSetupActivity {

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup3);
	}

	@Override
	public void setupView() {

	}

	@Override
	public void showNext() {
		
		ActivityUtil.startActivityAndFinish(this, Setup4Activity.class);
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}

	@Override
	public void showPre() {
		
		ActivityUtil.startActivityAndFinish(this, Setup2Activity.class);
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

}
