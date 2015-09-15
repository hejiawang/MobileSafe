package com.wang.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wang.mobilesafe.db.BlackNumberDBOpenHelper;
import com.wang.mobilesafe.domain.BlackNumberInfo;

public class BlackNumberDao {

	private BlackNumberDBOpenHelper helper;

	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper(context);
	}

	/**
	 * 查找一条黑名单号码
	 * 
	 * @param number
	 *            黑名单号码
	 * @return true or false
	 */
	public boolean find(String number) {

		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from blacknumber where number=?",
				new String[] { number });
		if (cursor.moveToNext()) {

			result = true;
		}
		cursor.close();
		db.close();

		return result;
	}

	/**
	 * 添加黑名单号码
	 * 
	 * @param number
	 *            黑名单号码
	 * @param mode
	 *            拦截模式
	 */
	public void add(String number, String mode) {

		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("insert into blacknumber ( number, mode ) values( ?, ? )",
				new Object[] { number, mode });
		db.close();
	}

	/**
	 * 修改黑名单号码的拦截模式
	 * 
	 * @param number
	 *            黑名单号码
	 * @param mode
	 *            新的拦截模式
	 */
	public void update(String number, String newmode) {

		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("update blacknumber set mode=? where number=?",
				new Object[] { newmode, number });
		db.close();
	}

	/**
	 * 删除黑名单号码
	 * 
	 * @param number
	 *            黑名单号码
	 * @return true or false
	 */
	public boolean delete(String number) {

		SQLiteDatabase db = helper.getWritableDatabase();
		int result = db.delete("blacknumber", "number=?",
				new String[] { number });
		db.close();

		if (result == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取全部黑名单信息
	 * 
	 * @return 黑名单集合
	 */
	public List<BlackNumberInfo> findAll() {

		List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select number, mode from blacknumber order by _id desc", null);
		while (cursor.moveToNext()) {

			BlackNumberInfo info = new BlackNumberInfo();
			info.setMode(cursor.getString(1));
			info.setNumber(cursor.getString(0));
			infos.add(info);
			info = null;
		}
		cursor.close();
		db.close();

		// 为了测试耗时效果
		/*
		 * try { Thread.sleep(1000); } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 */

		return infos;
	}

	/**
	 * 分页查询数据
	 * 
	 * @param maxnumber
	 *            每页最多查询多少条数据
	 * @param offset
	 *            从第几条开始查询
	 * @return 黑名单集合
	 */
	public List<BlackNumberInfo> findByPage(int maxnumber, int offset) {

		List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select number, mode from blacknumber order by _id desc limit ? offset ?",
						new String[] { maxnumber + "", offset + "" });
		while (cursor.moveToNext()) {

			BlackNumberInfo info = new BlackNumberInfo();
			info.setMode(cursor.getString(1));
			info.setNumber(cursor.getString(0));
			infos.add(info);
			info = null;
		}
		cursor.close();
		db.close();

		return infos;
	}

	/**
	 * 查询一共多少条黑名单号码
	 * 
	 * @return 一共多少条黑名单号码
	 */
	public int getMaxNumber() {

		int maxNumber = 0;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from blacknumber ", null);
		maxNumber = cursor.getCount();
		cursor.close();
		db.close();
		return maxNumber;
	}
}
