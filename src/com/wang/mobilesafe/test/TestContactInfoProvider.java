package com.wang.mobilesafe.test;

import java.util.List;

import android.test.AndroidTestCase;

import com.wang.mobilesafe.domain.ContactInfo;
import com.wang.mobilesafe.engine.ContactInfoProvider;

/**
 * ContactInfoProvider的测试类
 * @author HeJW
 *
 */
public class TestContactInfoProvider extends AndroidTestCase {
	
	public void testGetContacts() throws Exception{
		
		List<ContactInfo> infos = ContactInfoProvider.getContactInfos(getContext());
		for ( ContactInfo info : infos ) {
			 System.out.println(info.toString());
		}
	}
}
