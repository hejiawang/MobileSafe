package com.wang.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDBOpenHelper extends SQLiteOpenHelper {

	public BlackNumberDBOpenHelper(Context context) {
		
		super(context, "blacknumber.db", null, 1);
	}

	/**
	 * 拦截模式:   1.全部拦截
	 *           2.电话拦截
	 *           3.短信拦截
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table blacknumber ( _id integer primary key autoincrement, number varchar(20), mode varchar(2))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
