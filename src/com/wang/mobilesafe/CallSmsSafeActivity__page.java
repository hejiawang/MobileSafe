package com.wang.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.mobilesafe.db.dao.BlackNumberDao;
import com.wang.mobilesafe.domain.BlackNumberInfo;
import com.wang.mobilesafe.utils.MyAsyncTask;

public class CallSmsSafeActivity__page extends Activity implements OnClickListener {

	public static final String TAG = "CallSmsSafeActivity";

	private ListView lv_call_sms;
	private View loading;
	private EditText et_page_number;
	private TextView tv_page_status;

	private BlackNumberDao dao;
	private List<BlackNumberInfo> infos;

	private CallSmsAdapter adapter;
	private int maxnumber = 20;
	private int offset = 0;
	private int totalNumber; // 总共有多少条黑名单号码.
	private int currentPage = 1; // 当前页码

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_page);

		dao = new BlackNumberDao(this);
		totalNumber = dao.getMaxNumber();

		lv_call_sms = (ListView) findViewById(R.id.lv_call_sms);
		et_page_number = (EditText) findViewById(R.id.et_page_number);
		tv_page_status = (TextView) findViewById(R.id.tv_page_status);
		tv_page_status.setText("当前/总:" + currentPage + "/"
				+ getTotalPageNumber(totalNumber) + "页");
		loading = findViewById(R.id.loading);
		fillData();
	}

	/**
	 * 跳转页面的单击事件
	 * 
	 * @param view
	 */
	public void jump(View view) {

		String pageNumberStr = et_page_number.getText().toString().trim();
		if (TextUtils.isEmpty(pageNumberStr)) {
			Toast.makeText(this, "请输入页码", 1).show();
			return;
		}
		int pageNumber = Integer.parseInt(pageNumberStr); // 第几页
		
		if ( pageNumber <= 0 ) {
			Toast.makeText(this, "请输入合理页码", 1).show();
			return;
		}
		
		if (currentPage == pageNumber) {
			Toast.makeText(this, "就是当前页", 1).show();
			return;
		}
		
		if (pageNumber > getTotalPageNumber(totalNumber)) {
			Toast.makeText(this, "页码超出范围", 1).show();
			return;
		}

		offset = (pageNumber - 1) * maxnumber;
		currentPage = pageNumber;
		tv_page_status.setText("当前/总:" + currentPage + "/"
				+ getTotalPageNumber(totalNumber) + "页");
		fillData();

	}

	/**
	 * 计算出总页数
	 * 
	 * @param totalNumber
	 *            数据总条数
	 * @return 数据总页数
	 */
	public int getTotalPageNumber(int totalNumber) {

		if (totalNumber % maxnumber == 0) {
			return totalNumber / maxnumber;
		} else {
			return totalNumber / maxnumber + 1;
		}
	}

	/**
	 * 为ListView填充数据
	 */
	private void fillData() {

		MyAsyncTask task = new MyAsyncTask() {

			@Override
			public void onPreExecute() {

				loading.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPostExecute() {

				if (adapter == null) {
					adapter = new CallSmsAdapter();
					lv_call_sms.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
				loading.setVisibility(View.INVISIBLE);
			}

			@Override
			public void doInBackground() {
				infos = dao.findByPage(maxnumber, offset); // 耗时操作
			}
		};
		task.execute();
	}

	private EditText ed_blacknumber;
	private RadioGroup rg_mode;
	private Button bt_ok;
	private Button bt_cancle;
	private AlertDialog dialog;

	/**
	 * 添加黑名单号码的单机事件
	 * 
	 * @param view
	 */
	public void addBlackNumber(View view) {

		View dialogView = View.inflate(this, R.layout.dialog_add_blacknumber,
				null);
		ed_blacknumber = (EditText) dialogView
				.findViewById(R.id.ed_blacknumber);
		rg_mode = (RadioGroup) dialogView.findViewById(R.id.rg_mode);
		bt_ok = (Button) dialogView.findViewById(R.id.bt_ok);
		bt_cancle = (Button) dialogView.findViewById(R.id.bt_cancle);

		bt_ok.setOnClickListener(this);
		bt_cancle.setOnClickListener(this);

		AlertDialog.Builder builder = new Builder(this);
		dialog = builder.create();
		dialog.setView(dialogView, 0, 0, 0, 0);
		dialog.show();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_cancle:

			dialog.dismiss();
			break;
		case R.id.bt_ok:

			String number = ed_blacknumber.getText().toString().trim();
			int id = rg_mode.getCheckedRadioButtonId();
			String mode = "";

			switch (id) {
			case R.id.rb_all:
				mode = "1";
				break;
			case R.id.rb_phone:
				mode = "2";
				break;
			case R.id.rb_sms:
				mode = "3";
				break;
			}

			if (TextUtils.isEmpty(number) || TextUtils.isEmpty(mode)) {

				Toast.makeText(this, "号码或者拦截模式不能为空", 1).show();
				return;
			}

			// 将号码添加进数据库
			dao.add(number, mode);
			totalNumber++;
			tv_page_status.setText("当前/总:" + currentPage + "/"
					+ getTotalPageNumber(totalNumber) + "页");
			// 刷新黑名单号码的显示
			infos.add(0, new BlackNumberInfo(number, mode));
			// lisView没有更新的方,Adapter有
			adapter.notifyDataSetChanged();

			dialog.dismiss();
			break;
		}
	}

	private class CallSmsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final BlackNumberInfo info = infos.get(position);
			String mode = info.getMode();

			ViewHolder holder;

			View view;
			if (convertView != null && convertView instanceof RelativeLayout) {

				view = convertView; // 复用历史缓存的View对象
				// Log.i(TAG, "使用缓存View : " + position);
				holder = (ViewHolder) view.getTag();
			} else {

				view = View.inflate(getApplicationContext(),
						R.layout.list_callsms_item, null);
				// Log.i(TAG, "创建新的View : " + position);
				// 寻找到孩子引用，把引用存起来
				holder = new ViewHolder();
				holder.tv_call_sms_mode = (TextView) view
						.findViewById(R.id.tv_call_sms_mode);
				holder.tv_call_sms_number = (TextView) view
						.findViewById(R.id.tv_call_sms_number);
				holder.iv_callsms_delete = (ImageView) view
						.findViewById(R.id.iv_callsms_delete);
				view.setTag(holder);

			}

			// View view = View.inflate(getApplicationContext(),
			// R.layout.list_callsms_item, null);
			// TextView tv_call_sms_number = (TextView)
			// view.findViewById(R.id.tv_call_sms_number);
			// TextView tv_call_sms_mode = (TextView)
			// view.findViewById(R.id.tv_call_sms_mode);
			holder.tv_call_sms_number.setText(info.getNumber());

			if (mode.equals("1")) {
				holder.tv_call_sms_mode.setText("全部拦截");
			} else if (mode.equals("2")) {
				holder.tv_call_sms_mode.setText("电话拦截");
			} else if (mode.equals("3")) {
				holder.tv_call_sms_mode.setText("短信拦截");
			}

			holder.iv_callsms_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					String number = info.getNumber();
					boolean result = dao.delete(number);
					if (result) {

						// 刷新list界面
						infos.remove(info);
						totalNumber--;
						tv_page_status.setText("当前/总:" + currentPage + "/"
								+ getTotalPageNumber(totalNumber) + "页");
						adapter.notifyDataSetChanged();
					} else {

						Toast.makeText(getApplicationContext(), "删除失败", 1)
								.show();
					}
				}
			});

			return view;
		}
	}

	/**
	 * View对象孩子引用的容器.
	 * 
	 * @author HeJW
	 *
	 */
	static class ViewHolder {

		TextView tv_call_sms_number;
		TextView tv_call_sms_mode;
		ImageView iv_callsms_delete;
	}
}
