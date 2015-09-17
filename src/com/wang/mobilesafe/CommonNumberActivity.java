package com.wang.mobilesafe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.wang.mobilesafe.db.dao.CommonNumDao;

public class CommonNumberActivity extends Activity {

	private ExpandableListView elv;
	private MyAdapter adapter;

	//使用内存缓存优化程序效率
	private List<String> groupNames;
	private Map<Integer, List<String>> childerCacheInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_number);

		childerCacheInfos = new HashMap<Integer, List<String>>();
		elv = (ExpandableListView) findViewById(R.id.elv);
		adapter = new MyAdapter();
		elv.setAdapter(adapter);
	}

	private class MyAdapter extends BaseExpandableListAdapter {

		// 返回一共多少个分组
		@Override
		public int getGroupCount() {
			// return CommonNumDao.getGroupCount();
			groupNames = CommonNumDao.getGroupInfos();
			return groupNames.size();
		}

		// 每个分组有多少个孩子
		@Override
		public int getChildrenCount(int groupPosition) {
			// return CommonNumDao.getChildrenCount(groupPosition);
			List<String> childrenInfos;
			if (childerCacheInfos.containsKey(groupPosition)) {
				childrenInfos = childerCacheInfos.get(groupPosition);
			} else {
				childrenInfos = CommonNumDao
						.getChildrenInfosByPosition(groupPosition);
				childerCacheInfos.put(groupPosition, childrenInfos);
			}
			return childrenInfos.size();
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

			TextView tv;
			if (convertView != null && convertView instanceof TextView) {
				tv = (TextView) convertView;
			} else {
				tv = new TextView(getApplicationContext());
			}
			tv.setTextSize(25);
			tv.setTextColor(Color.RED);
			// tv.setText("      " + CommonNumDao.getGroupName(groupPosition));
			tv.setText("      " + groupNames.get(groupPosition));
			return tv;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			TextView tv;
			if (convertView != null && convertView instanceof TextView) {
				tv = (TextView) convertView;
			} else {
				tv = new TextView(getApplicationContext());
			}
			tv.setTextSize(18);
			tv.setTextColor(Color.BLUE);
			// tv.setText(CommonNumDao.getChildInfoByPositon(groupPosition,
			// childPosition));
			tv.setText(childerCacheInfos.get(groupPosition).get(childPosition));
			return tv;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
	}
}
