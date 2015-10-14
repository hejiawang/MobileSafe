package com.wang.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 查询病毒
 * 
 * @author HeJW
 *
 */
public class AntivirusDao {

	private static final String path = "/data/data/com.wang.mobilesafe/files/antivirus.db";

	/**
	 * 获取是否是病毒
	 * 
	 * @param md5
	 * @return 不是病毒返回空
	 */
	public static String findVirus(String md5) {

		String desc = null;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);

		Cursor cursor = db.rawQuery("select desc from datable where md5=?",
				new String[] { md5 });
		if (cursor.moveToNext()) {
			desc = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return desc;
	}
}
