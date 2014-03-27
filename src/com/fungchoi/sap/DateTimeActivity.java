/**
 * 
 */
package com.fungchoi.sap;

import java.util.Calendar;
import java.util.Locale;

import com.fungchoi.sap.entity.PassParameter;
import com.fungchoi.sap.util.Helper;
import com.fungchoi.sap.util.MyApplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * @author Administrator
 * 
 */
public class DateTimeActivity extends Activity {
	private Activity activity;

	private PassParameter pp;

	private Button previous;
	private Button next;

	private EditText txtISDD;
	private EditText txtISDZ;
	private EditText txtIEDD;
	private EditText txtIEDZ;
	private EditText txtBUDAT;
	private EditText tempDate;
	private EditText tempTime;

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	/**
	 * 
	 */
	public DateTimeActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.datetime);

		MyApplication.getInstance().addActivity(this);

		activity = this;

		txtISDD = (EditText) this.findViewById(R.id.txtISDD);
		txtISDZ = (EditText) this.findViewById(R.id.txtISDZ);
		txtIEDD = (EditText) this.findViewById(R.id.txtIEDD);
		txtIEDZ = (EditText) this.findViewById(R.id.txtIEDZ);
		txtBUDAT = (EditText) this.findViewById(R.id.txtBUDAT);

		final Calendar c = Calendar.getInstance(Locale.CHINA);
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		Helper.updateDate(txtISDD, mYear, mMonth, mDay);
		Helper.updateDate(txtIEDD, mYear, mMonth, mDay);
		txtISDZ.setText("08:30");
		txtIEDZ.setText("17:30");
		Helper.updateDate(txtBUDAT, mYear, mMonth, mDay);

		// 日期控件注册
		registerEvents1();
		// 时间控件注册
		registerEvents2();

		// 按下返回键返回到上一个界面
		previous = (Button) this.findViewById(R.id.btnPrevious04);
		previous.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Helper.simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		// 按下一步跳转到工序选择界面
		next = (Button) this.findViewById(R.id.btnNext04);
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dispatch(pp);
			}
		});

		// 从意图里面接收上一个Activity传过来的数据
		Intent intent = this.getIntent();
		pp = (PassParameter) intent.getSerializableExtra(PassParameter.PP_KEY);

	}

	// 跳转到下一个Activity
	private void dispatch(PassParameter pp) {
		pp.setISDD(txtISDD.getText().toString().replaceAll("-", ""));
		pp.setIEDD(txtIEDD.getText().toString().replaceAll("-", ""));
		pp.setISDZ(txtISDZ.getText().toString().replaceAll(":", "") + "00");
		pp.setIEDZ(txtIEDZ.getText().toString().replaceAll(":", "") + "00");
		pp.setBUDAT(txtBUDAT.getText().toString().replaceAll("-", ""));
		Intent intent = new Intent(this, EquipmentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(PassParameter.PP_KEY, pp);
		intent.putExtras(bundle);

		startActivity(intent);
	}

	// 日期控件注册事件
	private void registerEvents1() {
		txtISDD.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Helper.hideIM(activity, v);
				tempDate = txtISDD;
				showDialog1();
			}
		});
		txtIEDD.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Helper.hideIM(activity, v);
				tempDate = txtIEDD;
				showDialog1();
			}
		});
		txtISDD.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					Helper.hideIM(activity, v);
					tempDate = txtISDD;
					showDialog1();
				}
			}
		});
		txtIEDD.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					Helper.hideIM(activity, v);
					tempDate = txtIEDD;
					showDialog1();
				}
			}
		});
		txtBUDAT.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Helper.hideIM(activity, v);
				tempDate = txtBUDAT;
				showDialog1();
			}
		});
	}

	// 时间控件注册事件
	private void registerEvents2() {
		txtISDZ.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				tempTime = txtISDZ;
				showDialog2();
			}
		});
		txtIEDZ.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				tempTime = txtIEDZ;
				showDialog2();
			}
		});
		txtISDZ.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					Helper.hideIM(activity, v);
					tempTime = txtISDZ;
					showDialog2();
				}
			}
		});
		txtIEDZ.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					Helper.hideIM(activity, v);
					tempTime = txtIEDZ;
					showDialog2();
				}
			}
		});
	}

	// 显示日期控件
	private void showDialog1() {
		DatePickerDialog dialog = new DatePickerDialog(this, mDateSetListener,
				mYear, mMonth, mDay);

		dialog.show();
	}

	// 显示时间控件
	private void showDialog2() {
		TimePickerDialog dialog = new TimePickerDialog(this, mTimeSetListener,
				mHour, mMinute, true);

		dialog.show();
	}

	// 日期控件事件
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			Helper.updateDate(tempDate, mYear, mMonth, mDay);
		}
	};

	// 时间控件事件
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;

			Helper.updateTime(tempTime, mHour, mMinute);
		}
	};

}
