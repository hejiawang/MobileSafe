package com.wang.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.mobilesafe.db.dao.BlackNumberDao;
import com.wang.mobilesafe.domain.BlackNumberInfo;
import com.wang.mobilesafe.utils.MyAsyncTask;

public class CallSmsSafeActivity extends Activity {

	public static final String TAG = "CallSmsSafeActivity";
	
	private ListView lv_call_sms;
	private View loading;
	
	private BlackNumberDao dao;
	private List<BlackNumberInfo> infos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms);
		
		lv_call_sms = (ListView)findViewById(R.id.lv_call_sms);
		loading = findViewById(R.id.loading);
		
		dao = new BlackNumberDao(this);
		
		MyAsyncTask task = new MyAsyncTask() {

			@Override
			public void onPreExecute() {

				loading.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPostExecute() {

				lv_call_sms.setAdapter(new CallSmsAdapter());
				loading.setVisibility(View.INVISIBLE);
			}

			@Override
			public void doInBackground() {

				infos = dao.findAll();	// 耗时操作
			}
		};
		task.execute();
	}
	
	private class CallSmsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			BlackNumberInfo info = infos.get(position);
			String mode = info.getMode();
			
			ViewHolder holder;
			
			View view ;
			if ( convertView!=null && convertView instanceof RelativeLayout ) {
				
				view = convertView;	//复用历史缓存的View对象
				//Log.i(TAG, "使用缓存View : " + position);
				holder = (ViewHolder)view.getTag();
			} else {
				
				view = View.inflate(getApplicationContext(), R.layout.list_callsms_item, null);
				//Log.i(TAG, "创建新的View : " + position);
				//寻找到孩子引用，把引用存起来
				holder = new ViewHolder();
				holder.tv_call_sms_mode = (TextView) view.findViewById(R.id.tv_call_sms_mode);
				holder.tv_call_sms_number = (TextView) view.findViewById(R.id.tv_call_sms_number);
				view.setTag(holder);
				
			}
		
			//View view = View.inflate(getApplicationContext(), R.layout.list_callsms_item, null);
			//TextView tv_call_sms_number = (TextView) view.findViewById(R.id.tv_call_sms_number);
			//TextView tv_call_sms_mode = (TextView) view.findViewById(R.id.tv_call_sms_mode);
			holder.tv_call_sms_number.setText(info.getNumber());
			
			if ( mode.equals("1") ) {
				holder.tv_call_sms_mode.setText("全部拦截");
			} else if ( mode.equals("2") ) {
				holder.tv_call_sms_mode.setText("电话拦截");
			} else if ( mode.equals("3") ) {
				holder.tv_call_sms_mode.setText("短信拦截");
			}
			return view;
		}
	}
	
	/**
	 * View对象孩子引用的容器.
	 * @author HeJW
	 *
	 */
	static class ViewHolder{
		TextView tv_call_sms_number;
		TextView tv_call_sms_mode;
	}
}
