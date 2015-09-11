package com.wang.mobilesafe.test;

import java.util.Random;

import com.wang.mobilesafe.db.BlackNumberDBOpenHelper;
import com.wang.mobilesafe.db.dao.BlackNumberDao;

import android.test.AndroidTestCase;

/**
 * 黑名单数据库测试类
 * @author HeJW
 *
 */
public class TestBlackNumber extends AndroidTestCase {

	public void testCreateDB() throws Exception {

		BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(
				getContext());
		helper.getWritableDatabase();
	}

	public void testAdd() throws Exception {

		long l = 13800000000L;
		Random r = new Random();
		
		BlackNumberDao dao = new BlackNumberDao(getContext());
		for ( int i=0; i<200; i++ ) {
			long number = l+i;
			dao.add(""+number, String.valueOf(r.nextInt(3)+1));
		}
	}

	public void testFind() throws Exception {

		BlackNumberDao dao = new BlackNumberDao(getContext());
		boolean result = dao.find("13888888888");
		assertEquals(true, result);
	}

	public void testUpdate() throws Exception {

		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.update("13888888888", "2");
	}

	public void testDelete() throws Exception {

		BlackNumberDao dao = new BlackNumberDao(getContext());
		boolean result = dao.delete("13888888888");
		assertEquals(true, result);
	}
}
