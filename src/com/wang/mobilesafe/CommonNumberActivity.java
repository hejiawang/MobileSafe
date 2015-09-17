package com.wang.mobilesafe;

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

		@Override
		public int getGroupCount() {
			return 10;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return groupPosition + 1;
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
			tv.setText("      我是第" + groupPosition + "个分组");
			return tv;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			
			TextView tv = new TextView(getApplicationContext());
			tv.setTextSize(18);
			tv.setTextColor(Color.BLUE);
			tv.setText("我是第" + groupPosition + "个分组的第"+ childPosition+"个孩子");
			return tv;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
	}
}
