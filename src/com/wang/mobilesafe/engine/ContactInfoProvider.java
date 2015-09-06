package com.wang.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.wang.mobilesafe.domain.ContactInfo;

/**
 * 提供手机联系人信息的类
 * 
 * @author HeJW
 *
 */
public class ContactInfoProvider {

	/**
	 * 获取手机系统里面所有联系人信息
	 * 
	 * @return 所有联系人信息的集合
	 */
	public static List<ContactInfo> getContactInfos(Context context) {

		List<ContactInfo> infos = new ArrayList<ContactInfo>();
		
		ContentResolver resolver = context.getContentResolver();

		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri = Uri.parse("content://com.android.contacts/data");

		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);
		while (cursor.moveToNext()) {

			String id = cursor.getString(0);
			if (id != null) {

				ContactInfo info = new ContactInfo();
				
				Cursor dataCursor = resolver.query(dataUri, new String[] {
						"mimetype", "data1" }, "raw_contact_id=?",
						new String[] { id }, null);
				while (dataCursor.moveToNext()) {

					String mime = dataCursor.getString(0);
					String data1 = dataCursor.getString(1);
					if ( "vnd.android.cursor.item/name".equals(mime) ) {
						info.setName(data1);
					} else if ( "vnd.android.cursor.item/phone_v2".equals(mime) ) {
						info.setPhono(data1);
					}
				}
				infos.add(info);
				dataCursor.close();
			}
		}
		cursor.close();

		return infos;
	}
}
