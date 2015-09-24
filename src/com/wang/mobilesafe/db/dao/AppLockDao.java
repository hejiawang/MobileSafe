package com.wang.mobilesafe.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wang.mobilesafe.db.AppLockDBOpenHelper;

public class AppLockDao {

	private AppLockDBOpenHelper helper;

	public AppLockDao(Context context) {
		helper = new AppLockDBOpenHelper(context);
	}

	/**
	 * 查找包名是否锁定
	 * 
	 * @param packname
	 *            包名
	 * @return true or false
	 */
	public boolean find(String packname) {

		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from applock where packname=?",
				new String[] { packname });
		if (cursor.moveToNext()) {

			result = true;
		}
		cursor.close();
		db.close();

		return result;
	}

	/**
	 * 添加锁定包名
	 * 
	 * @param packname
	 *            锁定包名
	 */
	public void add(String packname) {

		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("insert into applock ( packname ) values( ? )",
				new Object[] { packname });
		db.close();
	}


	/**
	 * 删除锁定包名
	 * 
	 * @param packname
	 *            锁定包名
	 * @return true or false
	 */
	public boolean delete(String packname) {

		SQLiteDatabase db = helper.getWritableDatabase();
		int result = db.delete("applock", "packname=?",
				new String[] { packname });
		db.close();

		if (result == 0) {
			return false;
		} else {
			return true;
		}
	}
}
