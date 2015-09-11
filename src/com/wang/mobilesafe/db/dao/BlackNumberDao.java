package com.wang.mobilesafe.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wang.mobilesafe.db.BlackNumberDBOpenHelper;

public class BlackNumberDao {
	
	private BlackNumberDBOpenHelper helper;

	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper( context );
	}
	
	/**
	 * 查找一条黑名单号码
	 * @param number 黑名单号码
	 * @return true or false
	 */
	public boolean find( String number ){
		
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from blacknumber where number=?", new String[]{number});
		if ( cursor.moveToNext() ){
			
			result = true;
		}
		cursor.close();
		db.close();
		
		return result;
	}
	
	/**
	 * 添加黑名单号码
	 * @param number 黑名单号码
	 * @param mode 拦截模式
	 */
	public void add ( String number, String mode ) {
		
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("insert into blacknumber ( number, mode ) values( ?, ? )", new Object[]{number, mode});
		db.close();
	}
	
	/**
	 * 修改黑名单号码的拦截模式
	 * @param number 黑名单号码
	 * @param mode 新的拦截模式
	 */
	public void update( String number, String newmode ) {
		
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("update blacknumber set mode=? where number=?", new Object[]{newmode,number});
		db.close();
	}
	
	/**
	 * 删除黑名单号码
	 * @param number 黑名单号码
	 * @return true or false
	 */
	public boolean delete( String number ){
		
		SQLiteDatabase db = helper.getWritableDatabase();
		int result = db.delete("blacknumber", "number=?", new String[]{number});
		db.close();
		
		if ( result == 0 ) {
			return false;
		} else {
			return true;
		}
	}
}
