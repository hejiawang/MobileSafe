package com.wang.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * 模仿APIDemo，通过配置资源文件，改变显示效果
 * </br>
 * int displayHeight = display.getHeight()-100;//不好.....
 * @author HeJW
 *
 */
public class DragViewActivity extends Activity {
	
	private static final String TAG = "DragViewActivity";
	private LinearLayout iv_drag_view;
	private TextView tv_drag_view;

	private Display display;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_view);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		
		iv_drag_view = (LinearLayout)findViewById(R.id.iv_drag_view);
		tv_drag_view = (TextView)findViewById(R.id.tv_drag_view);
		
		int lastX = sp.getInt("lastX", 0);
		int lastY = sp.getInt("lastY", 0);
		
		//初始化图片显示位置
		RelativeLayout.LayoutParams iv_params = (LayoutParams) iv_drag_view.getLayoutParams();
		iv_params.leftMargin = lastX;
		iv_params.topMargin = lastY;
		iv_drag_view.setLayoutParams(iv_params);
		
		//初始化文本显示位置
		RelativeLayout.LayoutParams tv_params = (LayoutParams) tv_drag_view.getLayoutParams();
		tv_params.leftMargin = tv_drag_view.getLeft();
		if ( lastY > (display.getHeight()-100)/2 ) {
			//文本在上面
			tv_params.topMargin = 0;
		} else {
			//文本在下面
			tv_params.topMargin = display.getHeight()-275 ;
		}
		tv_drag_view.setLayoutParams(tv_params);
		
		iv_drag_view.setOnTouchListener(new OnTouchListener() {
			
			//定义初始坐标
			int startX;
			int startY;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:	//按下
					
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_MOVE:	//移动
					
					//移动后坐标
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					
					int dx = newX - startX;
					int dy = newY - startY;
					
					int l = iv_drag_view.getLeft();
					int t = iv_drag_view.getTop();
					int r = iv_drag_view.getRight();
					int b = iv_drag_view.getBottom();
					
					int newL = l + dx;
					int newR = r + dx;
					int newT = t + dy;
					int newB = b + dy;
					
					int tv_height = tv_drag_view.getBottom() - tv_drag_view.getTop();	//文本高度
					int displayHeight = display.getHeight()-100;	/////////////////////不好.............
					
					if ( newL<0 || newR>display.getWidth() || newT<0 || newB>displayHeight ) {
						break;
					}
					
					if ( newT > displayHeight/2 ) {
						
						//图片在下面，文本在上面
						tv_drag_view.layout(tv_drag_view.getLeft(), 0, tv_drag_view.getRight(), tv_height);
					} else {
						
						//图片在上面，文本在下面
						tv_drag_view.layout(tv_drag_view.getLeft(), displayHeight-tv_height, tv_drag_view.getRight(), displayHeight);
					}
					
					iv_drag_view.layout(newL, newT, newR, newB);
					
					//要记得重新初始化开始坐标,否则会感觉“很灵敏”,因为dx,dy不稳定
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP:	//抬起
					
					Editor editor = sp.edit();
					editor.putInt("lastX", iv_drag_view.getLeft());
					editor.putInt("lastY", iv_drag_view.getTop());
					editor.commit();
					
					break;
				}
				return true;
			}
		});
	}
}
