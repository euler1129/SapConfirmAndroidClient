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

		// �ж��ֻ�����
		Helper.checkBattery(this);

		// ����ǰ�ѹ����ջ
		MyApplication.getInstance().addActivity(this);

		// ��ǰ�
		activity = this;

		// ����ͼ���������һ��Activity������������
		Intent intent = this.getIntent();
		pp = (PassParameter) intent.getSerializableExtra(PassParameter.PP_KEY);

		// ��ȡ��ǰ����ʵ��
		final Calendar c = Calendar.getInstance(Locale.CHINA);
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// ����ؼ�
		txtBeginDate = (EditText) this.findViewById(R.id.txtBeginDate);
		txtEndDate = (EditText) this.findViewById(R.id.txtEndDate);
		// ����Ĭ������
		Helper.updateDate(txtEndDate, mYear, mMonth, mDay);
		setBeginDate(c, -2);
		// ע�����ڿؼ������¼�
		registerEvents();

		// ����ؼ�
		txtOrderCode = (EditText) this.findViewById(R.id.txtOrderCode);
		txtMaterialDesc = (EditText) this.findViewById(R.id.txtMatnr);

		// ���·��ؼ����ص���һ������
		Button previous = (Button) this.findViewById(R.id.btnPrevious00);
		previous.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Helper.simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		// ��ʼ������ť����ע������¼�
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

	// ���ڿؼ�
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			Helper.updateDate(tempDate, mYear, mMonth, mDay);
		}
	};

	// ��ʾ���ڿؼ�
	private void showDialog() {
		DatePickerDialog dialog = new DatePickerDialog(this, mDateSetListener,
				mYear, mMonth, mDay);

		dialog.show();
	}

	// ע���¼�
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

	// ��ת����һ��Activity
	private void dispatch(PassParameter pp) {
		Intent intent = new Intent(this, OrderListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(PassParameter.PP_KEY, pp);
		intent.putExtras(bundle);

		startActivity(intent);
	}

	// ���ÿ�ʼ����
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