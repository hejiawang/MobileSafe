package com.wang.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
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

public class CallSmsSafeActivity extends Activity implements OnClickListener {

	public static final String TAG = "CallSmsSafeActivity";

	private ListView lv_call_sms;
	private View loading;

	private BlackNumberDao dao;
	private List<BlackNumberInfo> infos;

	private CallSmsAdapter adapter;
	private int maxnumber = 20;
	private int offset = 0;
	private int totalNumber; // 总共有多少条黑名单号码.

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms);

		dao = new BlackNumberDao(this);
		totalNumber = dao.getMaxNumber();

		lv_call_sms = (ListView) findViewById(R.id.lv_call_sms);
		loading = findViewById(R.id.loading);

		lv_call_sms.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:

					int position = lv_call_sms.getLastVisiblePosition(); // 19
					int total = infos.size(); // 20
					if (position == (total - 1)) {

						offset += maxnumber;
						if (offset > totalNumber) {
							Toast.makeText(getApplicationContext(), "没有数据了", 1)
									.show();
							return;
						}

						Log.i(TAG, "移动到了最后......");
						fillData();
					}
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

		lv_call_sms.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(CallSmsSafeActivity.this,
						EditBlackNumberActivity.class);

				// 传递要进行修改的黑名单对象.
				MobileSafeApplication app = (MobileSafeApplication) getApplication();
				app.blackNumberInfo = infos.get(position);
				startActivityForResult(intent, 0);
				return false;
			}

		});

		fillData();

		Intent intent = getIntent();
		String number = intent.getStringExtra("blacknumber");
		if (!TextUtils.isEmpty(number)) {

			showAddBlackNumberDialog(number);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {

		super.onNewIntent(intent);
		Log.i(TAG, "onNewIntent...");
		String number = intent.getStringExtra("blacknumber");
		if (!TextUtils.isEmpty(number)) {

			showAddBlackNumberDialog(number);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 200) {

			adapter.notifyDataSetChanged();
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

				if (infos == null) {
					infos = dao.findByPage(maxnumber, offset); // 耗时操作
				} else {
					infos.addAll(dao.findByPage(maxnumber, offset));
				}

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

		showAddBlackNumberDialog("");
	}

	/**
	 * 显示添加黑名单时的对话框
	 * 
	 * @param number
	 *            黑名单号码
	 */
	private void showAddBlackNumberDialog(String number) {
		View dialogView = View.inflate(this, R.layout.dialog_add_blacknumber,
				null);
		ed_blacknumber = (EditText) dialogView
				.findViewById(R.id.ed_blacknumber);
		rg_mode = (RadioGroup) dialogView.findViewById(R.id.rg_mode);
		bt_ok = (Button) dialogView.findViewById(R.id.bt_ok);
		bt_cancle = (Button) dialogView.findViewById(R.id.bt_cancle);

		ed_blacknumber.setText(number);
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
