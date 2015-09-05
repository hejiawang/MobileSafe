package com.wang.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 * @author HeJW
 *
 */
public class MD5Util {
	
	public static void main(String[] args) {
		String s = "123456";
		String ss = encode(s);
		System.out.println(ss);
	}
	
	/**
	 * MD5加密
	 * @param text
	 * @return
	 */
	public static String encode( String text ){
		
		try {
			
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] result = digest.digest(text.getBytes());
			StringBuffer sb = new StringBuffer();
			
			for ( byte b : result ) {
				int number = b&0xff;   //可以加盐
				String hex = Integer.toHexString(number);
				if ( hex.length() == 1 ) {
					sb.append("0");
				}
				sb.append(hex);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
}
