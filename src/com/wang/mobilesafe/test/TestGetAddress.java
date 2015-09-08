package com.wang.mobilesafe.test;

import com.wang.mobilesafe.db.dao.AddressDao;

import android.test.AndroidTestCase;

/**
 * 电话话码归属地测试类
 * @author HeJW
 *
 */
public class TestGetAddress extends AndroidTestCase {

	public void testGetAddress() throws Exception{
		
		String result = AddressDao.getAddress("13888888888");
		System.out.println(result);
	}
}
