package com.wang.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wang.mobilesafe.domain.ContactInfo;
import com.wang.mobilesafe.engine.ContactInfoProvider;

public class SelectContactActivity extends Activity {
	
	private ListView lv_select_contact;
	
	private List<ContactInfo> infos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);
		
		lv_select_contact = (ListView) findViewById(R.id.lv_select_contact);
		infos = ContactInfoProvider.getContactInfos(this);
		lv_select_contact.setAdapter( new ContactAdapter() );
		
		lv_select_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				ContactInfo info = infos.get(position);
				String phone = info.getPhono();
				
				Intent data = new Intent();
				data.putExtra("phone", phone);
				setResult(0,data);
				
				finish();
			}
		});
	}
	
	private class ContactAdapter extends BaseAdapter {

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
			
			View view = View.inflate(getApplicationContext(), R.layout.list_contact_item, null);
			TextView tv_contact_name = (TextView)view.findViewById(R.id.tv_contact_name);
			TextView tv_contact_phone = (TextView)view.findViewById(R.id.tv_contact_phone);
			ContactInfo info = infos.get(position);
			tv_contact_name.setText(info.getName());
			tv_contact_phone.setText(info.getPhono());
			
			return view;
		}
	}
}
