package com.wang.mobilesafe;

import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wang.mobilesafe.utils.ActivityUtil;

public class Setup2Activity extends BaseSetupActivity {

	private TelephonyManager tm;	//获取手机里面与电话相关的内容
	
	private ImageView iv_setup2_bind;
	
	@Override
	public void initView() {
		
		setContentView(R.layout.activity_setup2);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
	}

	@Override
	public void setupView() {
		
		iv_setup2_bind = (ImageView) findViewById(R.id.iv_setup2_bind);
		
		//根据配置的信息，初始化一下状态(显示的图片样式)
		String sim = sp.getString("sim", "");
		if ( TextUtils.isEmpty(sim) ) {
			
			iv_setup2_bind.setImageResource(R.drawable.unlock);
		} else {
			
			iv_setup2_bind.setImageResource(R.drawable.lock);
		}
	}

	@Override
	public void showNext() {
		
		String sim = sp.getString("sim", "");
		if ( TextUtils.isEmpty(sim) ) {
			Toast.makeText(this, "请绑定手机卡", 0).show();
			return ;
		} 
		
		ActivityUtil.startActivityAndFinish(this, Setup3Activity.class);
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}

	@Override
	public void showPre() {
		
		ActivityUtil.startActivityAndFinish(this, Setup1Activity.class);
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
	
	/**
	 * 绑定SIM的点击事件
	 * @param view
	 */
	public void bindSim( View view ){
		
		//判断sp里面是否保存了sim卡的串号（SIM卡的身份证）
		String sim = sp.getString("sim", "");
		if ( TextUtils.isEmpty(sim) ) {
			
			String simnumber = tm.getSimSerialNumber();
			Editor editor = sp.edit();
			editor.putString("sim", simnumber);
			editor.commit();
			iv_setup2_bind.setImageResource(R.drawable.lock);
		} else {
			
			Editor editor = sp.edit();
			editor.putString("sim", null);
			editor.commit();
			iv_setup2_bind.setImageResource(R.drawable.unlock);
		}
	}
}
