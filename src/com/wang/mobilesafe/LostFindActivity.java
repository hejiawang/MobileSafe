package com.wang.mobilesafe;

import com.wang.mobilesafe.utils.ActivityUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {

	private SharedPreferences sp;
	
	private TextView tv_lostfind_number;
	private ImageView iv_lostfind_status;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		setContentView(R.layout.activity_lost_find);
		
		//显示设置过的安全号码
		tv_lostfind_number = (TextView) findViewById(R.id.tv_lostfind_number);
		tv_lostfind_number.setText( sp.getString("safenumber", "") );
		
		//显示设置过的防盗保护是否开启的图标
		iv_lostfind_status = (ImageView) findViewById(R.id.iv_lostfind_status);
		boolean protecting = sp.getBoolean("protecting", false);
		if ( protecting ) {
			iv_lostfind_status.setImageResource(R.drawable.lock);
		} else {
			iv_lostfind_status.setImageResource(R.drawable.unlock);
		}
		
		//判断用户是否进行过设置向导
		if ( !isSetup() ) {
			//进入设置向导
			Intent intent = new Intent(this,Setup1Activity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.lost_find_menu, menu);
		return true;
	}

	/**
	 * 更改防盗名称的menu选择事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.item_change_name:
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("更改名称");
			builder.setIcon(R.drawable.notification);
			final EditText et = new EditText(this);
			et.setHint("请输入新的名称");
			builder.setView(et);
		
			builder.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String newName = et.getText().toString().trim();
					Editor editor = sp.edit();
					editor.putString("newname", newName);
					editor.commit();
				}
			});
			
			builder.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * 判断用户是否设置过设置向导
	 * @return
	 */
	private boolean isSetup(){
		
		return sp.getBoolean("setup", false);
	}
	
	/**
	 * 重新进入设置向导的单价事件
	 * @param view
	 */
	public void reEntrySetup( View view ){
		
		ActivityUtil.startActivityAndFinish(this, Setup1Activity.class);
		
	}
}
