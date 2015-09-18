package com.wang.mobilesafe.test;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.test.AndroidTestCase;

public class TestSmsProvider extends AndroidTestCase {
	
	/**
	 * 插入短信
	 * @throws Exception
	 */
	public void testInserSms() throws Exception {
		
		Uri uri = Uri.parse("content://sms/");

		ContentValues values = new ContentValues();
		values.put("address", "13333333333");
		values.put("date", System.currentTimeMillis()+"");
		values.put("tpye", "1");
		values.put("body", "ws");
		//values.put("subject", "subject");
		
		ContentResolver resolver = getContext().getContentResolver();
		resolver.insert(uri, values);
		
	}
}
