package com.wang.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 查询手机号码归属地
 * 
 * @author HeJW
 *
 */
public class AddressDao {

	public static final String path = "/data/data/com.wang.mobilesafe/files/address.db";

	/**
	 * 获取某个电话话码的归属地信息
	 * 
	 * @param number
	 *            电话号码
	 * @return 归属地
	 */
	public static String getAddress(String number) {

		String address = number; // 如果没查询到，就把电话号码返回去
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);

		// 使用正则表达式判断number是否为手机号码
		if (number.matches("^1[34578]\\d{9}$")) {

			Cursor cursor = db
					.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?)",
							new String[] { number.substring(0, 7) });
			if (cursor.moveToNext()) {

				address = cursor.getString(0);
			}
			cursor.close();
		} else {

			// 非手机号码
			switch (number.length()) {
			case 3:
				address = "特殊号码";
				break;
			case 4:
				address = "模拟器";
				break;
			case 5:
				address = "特殊号码";
				break;
			case 7:
				address = "本地号码";
				break;
			case 8:
				address = "本地号码";
				break;
			default:
				if (number.length() >= 10) {
					
					//查前三位
					Cursor cursor = db.rawQuery(
							"select location from data2 where area=?",
							new String[] { number.substring(1, 3) });
					if (cursor.moveToNext()) {
						String text = cursor.getString(0);
						address = text.substring(0, text.length()-2);
					}
					
					//查前四位
					cursor = db.rawQuery(
							"select location from data2 where area=?",
							new String[] { number.substring(1, 4) });
					if (cursor.moveToNext()) {
						String text = cursor.getString(0);
						address = text.substring(0, text.length()-2);
					}
					
					cursor.close();
				}

				break;
			}
		}
		db.close();
		return address;
	}
}
