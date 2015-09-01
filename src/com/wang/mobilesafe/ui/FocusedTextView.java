package com.wang.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 使文本框能够获得焦点，支持信息滚动的效果
 * @author HeJW
 *
 */
public class FocusedTextView extends TextView {

	public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FocusedTextView(Context context) {
		super(context);
	}

	/**
	 * 欺骗系统，让系统认为FocusedTextView得到焦点，以支持信息滚动的效果
	 */
	@Override
	public boolean isFocused() {
		return true;
	}
	
}
