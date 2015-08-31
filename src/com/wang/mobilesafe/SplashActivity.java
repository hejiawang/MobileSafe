package com.wang.mobilesafe;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.wang.mobilesafe.domain.UpdateInfo;
import com.wang.mobilesafe.engine.UpdateInfoParser;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	public static final int PARSE_XML_ERROR = 10;
	public static final int PARSE_XML_SUCCESS = 11;
	public static final int SERVER_ERROR = 12;
	public static final int URL_ERROR = 13;
	public static final int NETWORK_ERROR = 14;
	protected static final String TAG = "SplashActivity";
	
	private TextView tv_splash_version;
	private UpdateInfo updateInfo;
	
	private Handler handler = new Handler(){
		
		public void handleMessage( Message msg ) {
			
			switch ( msg.what ) {
			
			case PARSE_XML_ERROR :
				Toast.makeText(getApplicationContext(), "解析XML失败", 0).show();
				//进入主线程界面
				loadMainUI();
				break;
				
			case SERVER_ERROR :
				Toast.makeText(getApplicationContext(), "服务器异常", 0).show();
				loadMainUI();
				break;
				
			case URL_ERROR :
				Toast.makeText(getApplicationContext(), "URL错误", 0).show();
				loadMainUI();
				break;
				
			case NETWORK_ERROR :
				Toast.makeText(getApplicationContext(), "网络异常", 0).show();
				loadMainUI();
				break;
				
			case PARSE_XML_SUCCESS :
				if ( getAppVersion().equals(updateInfo.getVersion()) ) {
					Log.i(TAG, "版本号相同，进入主界面");
					loadMainUI();
				} else {
					Log.i(TAG, "版本号不相同，弹出升级提示对话框");
				}
				break;
			}
			
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_splash_version.setText("版本：" + this.getAppVersion());
        
        //链接服务器检查服务更新
        new Thread(new CheckVersionTask()).start();
    }
    
    /**
     * 进入主界面 
     */
    private void loadMainUI(){
    	
    	Intent intent = new Intent(this, HomeActivity.class);
    	startActivity(intent);
    	//将当前splash页面结束，否则用户返回桌面在回到应用后，还会显示splash
    	finish();
    }
    
    /**
     * 获取应用程序版本号
     * @return
     */
    private String getAppVersion(){
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
    
    private class CheckVersionTask implements Runnable{

		@Override
		public void run() {
			
			//obtain()返回一个已存在的Message，提高效率
			Message msg = Message.obtain();
			
			try {
				
				URL url = new URL(getResources().getString(R.string.serverurl));
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				
				int code  = conn.getResponseCode();
				if( code == 200 ){
					InputStream is = conn.getInputStream();
					updateInfo = UpdateInfoParser.getUpdateInfo(is);
					
					if ( updateInfo == null ) {
						//解析xml失败
						msg.what = PARSE_XML_ERROR;
					} else { 
						//解析成功
						msg.what = PARSE_XML_SUCCESS;
					}
				} else {
					//服务器内部错误
					msg.what = SERVER_ERROR;
				}
			} catch (MalformedURLException e) {
				msg.what = URL_ERROR;
				e.printStackTrace();
			} catch (IOException e) {
				msg.what = NETWORK_ERROR;
				e.printStackTrace();
			} finally {
				handler.sendMessage(msg);
			}
		}
    }
}
