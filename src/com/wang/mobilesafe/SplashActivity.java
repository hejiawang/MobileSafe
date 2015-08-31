package com.wang.mobilesafe;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends Activity {

	private TextView tv_splash_version;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_splash_version.setText("版本：" + this.getVersion());
        
    }
    
    /**
     * 获取应用程序版本号
     * @return
     */
    private String getVersion(){
    	PackageManager pm = getPackageManager();
    	try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			//如果抛出异常，返回空字符串
			return "";
		}
    }

}
