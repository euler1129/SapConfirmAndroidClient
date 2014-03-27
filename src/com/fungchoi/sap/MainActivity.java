package com.fungchoi.sap;

import java.util.Calendar;
import java.util.Locale;

import com.fungchoi.sap.entity.PassParameter;
import com.fungchoi.sap.util.Helper;
import com.fungchoi.sap.util.MyApplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class MainActivity extends Activity {
	private Activity activity;

	private EditText tempDate;
	private EditText txtBeginDate;
	private EditText txtEndDate;
	private EditText txtOrderCode;
	private EditText txtMaterialDesc;

	private int mYear;
	private int mMonth;
	private int mDay;

	private PassParameter pp;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// 判断手机电量
		Helper.checkBattery(this);

		// 将当前活动压入活动堆栈
		MyApplication.getInstance().addActivity(this);

		// 当前活动
		activity = this;

		// 从意图里面接收上一个Activity传过来的数据
		Intent intent = this.getIntent();
		pp = (PassParameter) intent.getSerializableExtra(PassParameter.PP_KEY);

		// 获取当前日期实例
		final Calendar c = Calendar.getInstance(Locale.CHINA);
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// 界面控件
		txtBeginDate = (EditText) this.findViewById(R.id.txtBeginDate);
		txtEndDate = (EditText) this.findViewById(R.id.txtEndDate);
		// 设置默认日期
		Helper.updateDate(txtEndDate, mYear, mMonth, mDay);
		setBeginDate(c, -2);
		// 注册日期控件监听事件
		registerEvents();

		// 界面控件
		txtOrderCode = (EditText) this.findViewById(R.id.txtOrderCode);
		txtMaterialDesc = (EditText) this.findViewById(R.id.txtMatnr);

		// 按下返回键返回到上一个界面
		Button previous = (Button) this.findViewById(R.id.btnPrevious00);
		previous.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Helper.simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		// 开始报工按钮，并注册监听事件
		Button btnStart = (Button) this.findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pp.setBeginDate(txtBeginDate.getText().toString()
						.replaceAll("-", ""));
				pp.setEndDate(txtEndDate.getText().toString()
						.replaceAll("-", ""));
				pp.setOrderCode(txtOrderCode.getText().toString().trim());
				pp.setMaterialDesc(txtMaterialDesc.getText().toString().trim());
				dispatch(pp);
			}
		});
	}

	// 日期控件
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			Helper.updateDate(tempDate, mYear, mMonth, mDay);
		}
	};

	// 显示日期控件
	private void showDialog() {
		DatePickerDialog dialog = new DatePickerDialog(this, mDateSetListener,
				mYear, mMonth, mDay);

		dialog.show();
	}

	// 注册事件
	private void registerEvents() {
		txtBeginDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Helper.hideIM(activity, v);
				tempDate = txtBeginDate;
				showDialog();
			}
		});
		txtEndDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Helper.hideIM(activity, v);
				tempDate = txtEndDate;
				showDialog();
			}
		});
		txtBeginDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					Helper.hideIM(activity, v);
					tempDate = txtBeginDate;
					showDialog();
				}
			}
		});
		txtEndDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					Helper.hideIM(activity, v);
					tempDate = txtEndDate;
					showDialog();
				}
			}
		});
	}

	// 跳转到下一个Activity
	private void dispatch(PassParameter pp) {
		Intent intent = new Intent(this, OrderListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(PassParameter.PP_KEY, pp);
		intent.putExtras(bundle);

		startActivity(intent);
	}

	// 设置开始日期
	private void setBeginDate(Calendar c, int val) {
		c.add(Calendar.DATE, val);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		txtBeginDate.setText(new StringBuilder().append(year).append("-")
				.append((month + 1) < 10 ? "0" + (month + 1) : (month + 1))
				.append("-").append((day < 10) ? "0" + day : day));
	}

}