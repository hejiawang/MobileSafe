package com.wang.mobilesafe.ui;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.mobilesafe.R;

/**
 * 自定义toast
 * 
 * @author HeJW
 *
 */
public class MyToast {

	/**
	 * 显示自定义toast
	 * 
	 * @param icon
	 *            显示的图片资源ID
	 * @param msg
	 *            显示的信息
	 * @param context
	 *            context
	 */
	public static void show(int icon, String msg, Context context) {
		
		View view = View.inflate(context, R.layout.my_toast, null);
		TextView tv_toast_msg = (TextView)view.findViewById(R.id.tv_toast_msg);
		tv_toast_msg.setText(msg);
		ImageView tv_toast_icon = (ImageView)view.findViewById(R.id.tv_toast_icon);
		tv_toast_icon.setImageResource(icon);
	
		Toast toast = new Toast(context);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();
		
	}
}
