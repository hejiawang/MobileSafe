package com.wang.mobilesafe;

import com.wang.mobilesafe.db.dao.CommonNumDao;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class CommonNumberActivity extends Activity {

	private ExpandableListView elv;
	private MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_number);

		elv = (ExpandableListView) findViewById(R.id.elv);
		adapter = new MyAdapter();

		elv.setAdapter(adapter);
	}

	private class MyAdapter extends BaseExpandableListAdapter {

		// 返回一共多少个分组
		@Override
		public int getGroupCount() {
			return CommonNumDao.getGroupCount();
		}

		// 每个分组有多少个孩子
		@Override
		public int getChildrenCount(int groupPosition) {
			return CommonNumDao.getChildrenCount(groupPosition);
		}

		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {

			TextView tv = new TextView(getApplicationContext());
			tv.setTextSize(25);
			tv.setTextColor(Color.RED);
			tv.setText("      " + CommonNumDao.getGroupName(groupPosition));
			return tv;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			TextView tv = new TextView(getApplicationContext());
			tv.setTextSize(18);
			tv.setTextColor(Color.BLUE);
			tv.setText(CommonNumDao.getChildInfoByPositon(groupPosition,
					childPosition));
			return tv;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
	}
}
