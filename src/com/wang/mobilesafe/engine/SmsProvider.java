package com.wang.mobilesafe.engine;

import java.io.OutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;
import android.widget.Toast;

/**
 * 提供短信内容的类
 * 
 * @author HeJW
 *
 */
public class SmsProvider {

	private Context context;

	public SmsProvider(Context context) {
		this.context = context;
	}

	public interface BackUpProcessListener {
		void beforeBackUp(int max);

		void onProcessUpdate(int process);
	}

	/**
	 * 短信备份
	 * 
	 * @param os
	 *            输出流
	 * @param pd
	 *            进度条
	 * @throws Exception
	 *             IOException
	 */
	public void backUpSms(OutputStream os, BackUpProcessListener listener)
			throws Exception {

		XmlSerializer serializer = Xml.newSerializer();
		serializer.setOutput(os, "utf-8");
		serializer.startDocument("utf-8", true);
		serializer.startTag(null, "smss");

		Uri uri = Uri.parse("content://sms/");
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { "address", "date",
				"type", "body" }, null, null, null);
		listener.beforeBackUp(cursor.getCount());
		int total = 0;
		while (cursor.moveToNext()) {

			String address = cursor.getString(0);
			String date = cursor.getString(1);
			String type = cursor.getString(2);
			String body = cursor.getString(3);

			serializer.startTag(null, "sms");

			serializer.startTag(null, "address");
			serializer.text(address);
			serializer.endTag(null, "address");

			serializer.startTag(null, "date");
			serializer.text(date);
			serializer.endTag(null, "date");

			serializer.startTag(null, "type");
			serializer.text(type);
			serializer.endTag(null, "type");

			serializer.startTag(null, "body");
			serializer.text(body);
			serializer.endTag(null, "body");

			serializer.endTag(null, "sms");

			os.flush();
			total++;
			listener.onProcessUpdate(total);

			// 方便测试
			Thread.sleep(500);
		}
		cursor.close();
		serializer.endTag(null, "smss");
		serializer.endDocument();
		os.flush();
		os.close();
	}

	/**
	 * 短信还原
	 */
	public void restoreSms() {
		// 读取备份的xml文件，把每一条短信的数据获取出来，插入到系统数据库
		Toast.makeText(context, "功能未实现", 1).show();
	}
}
