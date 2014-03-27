/**
 * 
 */
package com.fungchoi.sap;

import java.lang.Thread.State;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.fungchoi.sap.entity.Machine;
import com.fungchoi.sap.entity.PassParameter;
import com.fungchoi.sap.util.Helper;
import com.fungchoi.sap.util.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author Administrator
 * 
 */
public class EquipmentActivity extends Activity {

	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

	private Activity activity;

	private ScrollView sv;
	private HorizontalScrollView hsv;
	private TableLayout tab;

	private PassParameter pp;
	private LinkedList<Machine> machines;

	private ProgressDialog pd1;
	private ProgressDialog pd2;
	private Button previous;
	private Button finish;

	/**
	 * 
	 */
	public EquipmentActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.equipment);

		// ��ǰ�
		activity = this;

		// ����ǰ�ѹ����ջ
		MyApplication.getInstance().addActivity(this);

		// ��ȡ����ؼ�����
		sv = (ScrollView) this.findViewById(R.id.scrollView3);
		sv.setHorizontalScrollBarEnabled(true);
		hsv = (HorizontalScrollView) this
				.findViewById(R.id.horizontalScrollView3);
		tab = (TableLayout) hsv.getChildAt(0);

		// ���·��ؼ����ص���һ������
		previous = (Button) this.findViewById(R.id.btnPrevious05);
		previous.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Helper.simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});

		// ����ɰ�ť�ύ�������ݵ�SAP������ɱ�������
		finish = (Button) this.findViewById(R.id.btnFinish);
		finish.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// ��ʽ����֮ǰ�ж��Ƿ�ѡ���˻�̨�����ֹ���
				if (!Helper.checkRadioButton(tab)) {
					Helper.message(activity, "��ѡ��һ̨�豸��һ���ֹ���!");
					return;
				}
				// �ύ����-����
				process();
			}
		});

		// ����ͼ���������һ��Activity������������
		Intent intent = this.getIntent();
		pp = (PassParameter) intent.getSerializableExtra(PassParameter.PP_KEY);

		// �������ڴ�����
		pd1 = ProgressDialog.show(this, "��ʾ", "���ڴ������Ժ�...");
		// �����߳�
		if (thread1.getState() == State.NEW) {
			thread1.start();
		} else {
			thread1.run();
		}
	}

	// �����߳�1
	private Thread thread1 = new Thread() {
		@Override
		public void run() {
			// ��SAPϵͳ��ȡ����
			String result = getJSONString(pp);
			machines = parseFromJson(result);

			Message message = handler1.obtainMessage();
			Bundle b = new Bundle();
			b.putString("flag", "ok");
			message.setData(b);
			handler1.sendMessage(message);
		}
	};

	// ����UI
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			Helper.enableButton(finish);
			if ("ok".equals(msg.getData().getString("flag"))) {
				// ��̬������ݼ�¼
				addRows();
				pd1.dismiss();
				thread1.stop();
			}
		}
	};

	// ��SAP��������ȡ����(JSON�ַ���)
	private String getJSONString(PassParameter pp) {
		String url = Helper.getUrl("service0004");
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("OrderCode", pp.getAUFNR()));
		return Helper.callSAPService(pp, url, pairs);
	}

	// ����JSON�ַ��������ɶ���ʵ������б�
	private LinkedList<Machine> parseFromJson(String jsonData) {
		if (jsonData == null)
			return null;
		if ("".equals(jsonData))
			return null;
		Type listType = new TypeToken<LinkedList<Machine>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<Machine> entitys = gson.fromJson(jsonData, listType);
		return entitys;
	}

	// ���ݶ�ȡ�ļ�¼��̬����б���
	private Boolean addRows() {
		if (machines == null) {
			Helper.disenableButton(finish);
			return false;
		}
		if (machines.isEmpty()) {
			Helper.disenableButton(finish);
			return false;
		}

		TableRow row;
		TextView view;
		RadioButton radio;

		for (Machine machine : machines) {
			row = new TableRow(this);

			radio = new RadioButton(this);
			radio.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					changedRadio((RadioButton) v);
				}
			});
			row.addView(radio);

			view = new TextView(this);
			view.setText(machine.getZZSB());
			row.addView(view);

			view = new TextView(this);
			view.setText(machine.getZZSBMS());
			row.addView(view);

			tab.addView(row, new TableLayout.LayoutParams(FP, WC));
		}
		return true;
	}

	// �����ĵ�ѡ��ťʱ�����������еĶ�����ż��������ĵ�ѡ��ť��Ϊδѡ��״̬
	private void changedRadio(RadioButton rb) {
		int count = tab.getChildCount();
		TableRow row;
		RadioButton radio;
		for (int i = 1; i < count; i++) {
			row = (TableRow) tab.getChildAt(i);
			radio = (RadioButton) row.getChildAt(0);
			if (rb.equals(radio)) {
				pp.setZZSB(((TextView) row.getChildAt(1)).getText().toString());
			} else {
				if (radio.isChecked()) {
					radio.setChecked(false);
				}
			}
		}
	}

	// ִ�б���
	private void process() {
		// �������ڴ�����
		pd2 = ProgressDialog.show(this, "��ʾ", "���ڴ������Ժ�...");
		// �����߳�
		if (thread2.getState() == State.NEW) {
			thread2.start();
		} else {
			thread2.run();
		}
	}

	// �ύ����,ִ�б���
	private String submit(PassParameter pp) {
		String url = Helper.getUrl("service0005");
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("AUFNR", pp.getAUFNR()));
		pairs.add(new BasicNameValuePair("VORNR", pp.getVORNR()));
		pairs.add(new BasicNameValuePair("LMNGA", String.valueOf(pp.getLMNGA())));
		pairs.add(new BasicNameValuePair("MEINH", pp.getMEINH()));
		pairs.add(new BasicNameValuePair("ISM01", String.valueOf(pp.getISM01())));
		pairs.add(new BasicNameValuePair("ILE01", pp.getILE01()));
		pairs.add(new BasicNameValuePair("ISM02", String.valueOf(pp.getISM02())));
		pairs.add(new BasicNameValuePair("ILE02", pp.getILE02()));
		pairs.add(new BasicNameValuePair("ISM03", String.valueOf(pp.getISM03())));
		pairs.add(new BasicNameValuePair("ILE03", pp.getILE03()));
		pairs.add(new BasicNameValuePair("ISM04", String.valueOf(pp.getISM04())));
		pairs.add(new BasicNameValuePair("ILE04", pp.getILE04()));
		pairs.add(new BasicNameValuePair("ISM05", String.valueOf(pp.getISM05())));
		pairs.add(new BasicNameValuePair("ILE05", pp.getILE05()));
		pairs.add(new BasicNameValuePair("ISM06", String.valueOf(pp.getISM06())));
		pairs.add(new BasicNameValuePair("ILE06", pp.getILE06()));
		pairs.add(new BasicNameValuePair("ISDD", pp.getISDD()));
		pairs.add(new BasicNameValuePair("ISDZ", pp.getISDZ()));
		pairs.add(new BasicNameValuePair("IEDD", pp.getIEDD()));
		pairs.add(new BasicNameValuePair("IEDZ", pp.getIEDZ()));
		pairs.add(new BasicNameValuePair("BUDAT", pp.getBUDAT()));
		pairs.add(new BasicNameValuePair("LTXA1", pp.getLTXA1()));
		pairs.add(new BasicNameValuePair("ZZSB", pp.getZZSB()));
		return Helper.callSAPService(pp, url, pairs);
	}

	// �����߳�1
	private Thread thread2 = new Thread() {
		@Override
		public void run() {
			// �ύ���ݵ�SAPϵͳ���д���-ִ�б��������ر������
			String result = submit(pp);
			pp.setResult(result);

			Message message = handler2.obtainMessage();
			handler2.sendMessage(message);
		}
	};

	// ����UI
	private Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			pd2.dismiss();
			thread2.stop();
			// ��ת���������
			dispatch(pp);
		}
	};

	// ��ת����һ��Activity
	private void dispatch(PassParameter pp) {
		Intent intent = new Intent(this, ResultActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(PassParameter.PP_KEY, pp);
		intent.putExtras(bundle);

		startActivity(intent);
	}

}
