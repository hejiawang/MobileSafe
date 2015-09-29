package com.wang.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.mobilesafe.domain.TrafficInfo;
import com.wang.mobilesafe.engine.TrafficInfoProvider;
import com.wang.mobilesafe.utils.MyAsyncTask;

public class TrafficManagerActivity extends Activity {

	private ListView lv_traffic_manager;
	private View loading;

	private MyAdapter adapter;

	private List<TrafficInfo> trafficInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_maneget);

		lv_traffic_manager = (ListView) findViewById(R.id.lv_traffic_manager);
		loading = (View) findViewById(R.id.loading);

		fillData();
	}

	/**
	 * 填充数据
	 */
	private void fillData() {

		new MyAsyncTask() {

			@Override
			public void onPreExecute() {
				loading.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPostExecute() {
				adapter = new MyAdapter();
				lv_traffic_manager.setAdapter(adapter);
				loading.setVisibility(View.INVISIBLE);
			}

			@Override
			public void doInBackground() {
				trafficInfos = TrafficInfoProvider
						.getTrafficInfos(TrafficManagerActivity.this);
			}
		}.execute();
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return trafficInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view;
			ViewHolder holder;

			if (convertView != null && convertView instanceof RelativeLayout) {

				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {

				view = View.inflate(getApplicationContext(),
						R.layout.list_traffic_item, null);
				holder = new ViewHolder();
				holder.iv_traffic_icon = (ImageView) view
						.findViewById(R.id.iv_traffic_icon);
				holder.tv_traffic_name = (TextView) view
						.findViewById(R.id.tv_traffic_name);
				holder.tv_traffic_up = (TextView) view
						.findViewById(R.id.tv_traffic_up);
				holder.tv_traffic_down = (TextView) view
						.findViewById(R.id.tv_traffic_down);
				holder.tv_traffic_total = (TextView) view
						.findViewById(R.id.tv_traffic_total);
				view.setTag(holder);
			}

			TrafficInfo trafficInfo = trafficInfos.get(position);
			holder.iv_traffic_icon.setImageDrawable(trafficInfo.getAppIcon());
			holder.tv_traffic_name.setText(trafficInfo.getAppName());
			holder.tv_traffic_up.setText("上传:" + trafficInfo.getTrafficUp());
			holder.tv_traffic_down.setText("下载:"
					+ trafficInfo.getTrafficDown());
			holder.tv_traffic_total.setText("总共:"
					+ trafficInfo.getTrafficTotal());
			return view;
		}
	}

	static class ViewHolder {
		TextView tv_traffic_name;
		ImageView iv_traffic_icon;
		TextView tv_traffic_up;
		TextView tv_traffic_down;
		TextView tv_traffic_total;
	}
}
