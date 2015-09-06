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
		//动画切换的动画效果,必须在finish()方法后立即使用（注意使用位置）
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}

	@Override
	public void showPre() {

	}

}
