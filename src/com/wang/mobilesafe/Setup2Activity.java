package com.wang.mobilesafe;

import com.wang.mobilesafe.utils.ActivityUtil;

public class Setup2Activity extends BaseSetupActivity {

	@Override
	public void initView() {
		
		setContentView(R.layout.activity_setup2);
	}

	@Override
	public void setupView() {

	}

	@Override
	public void showNext() {
		
		ActivityUtil.startActivityAndFinish(this, Setup3Activity.class);
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}

	@Override
	public void showPre() {
		
		ActivityUtil.startActivityAndFinish(this, Setup1Activity.class);
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

}
