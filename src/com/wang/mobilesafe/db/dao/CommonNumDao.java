package com.wang.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonNumDao {

	private static final String path = "/data/data/com.wang.mobilesafe/files/commonnum.db";

	/**
	 * 返回一共有多少个分组
	 * 
	 * @return 分组数
	 */
	public static int getGroupCount() {

		int count = 0;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select * from classlist", null);
		count = cursor.getCount();
		cursor.close();
		db.close();
		return count;
	}

	/**
	 * 返回某个分组里面有多少个孩子
	 * 
	 * @param groupPosition
	 *            分组位置
	 * @return 孩子数
	 */
	public static int getChildrenCount(int groupPosition) {

		int count = 0;
		int newPosition = groupPosition + 1;
		String sql = "select * from table" + newPosition;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery(sql, null);
		count = cursor.getCount();
		cursor.close();
		db.close();
		return count;
	}

	/**
	 * 获取某个分组的名字
	 * 
	 * @param groupPosition
	 *            分组位置
	 * @return 分组的名字
	 */
	public static String getGroupName(int groupPosition) {

		String name = "";
		int newPosition = groupPosition + 1;
		String sql = "select name from classlist where idx = ?";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery(sql, new String[] { newPosition + "" });
		if (cursor.moveToNext()) {
			name = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return name;
	}

	/**
	 * 获取某个分组里面某个孩子的信息
	 * 
	 * @param groupPosition
	 *            分组位置
	 * @param childPosition
	 *            孩子位置
	 * @return
	 */
	public static String getChildInfoByPositon(int groupPosition,
			int childPosition) {

		String result = "";
		int newPosition = groupPosition + 1;
		int newChildPosition = childPosition + 1;
		String sql = "select name, number from table" + newPosition
				+ " where _id = ?";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db
				.rawQuery(sql, new String[] { newChildPosition + "" });
		if (cursor.moveToNext()) {
			String name = cursor.getString(0);
			String number = cursor.getString(1);
			result = name + "\n" + number;
		}
		cursor.close();
		db.close();
		return result;
	}
}
