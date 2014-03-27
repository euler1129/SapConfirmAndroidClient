/**
 * ���������б�ѡ����
 */
package com.fungchoi.sap;

import java.lang.Thread.State;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.fungchoi.sap.entity.Order;
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
public class OrderListActivity extends Activity {
	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

	private Activity activity;

	private ScrollView sv;
	private HorizontalScrollView hsv;
	private TableLayout tab;

	private PassParameter pp;
	private LinkedList<Order> orders;

	private ProgressDialog pd;
	private Button previous;
	private Button next;

	/**
	 * 
	 */
	public OrderListActivity() {
		// TODO Auto-generated constructor stub
	}

	// @SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.orderlist);

		// ��ǰ�
		activity = this;

		// ����ǰ�ѹ����ջ
		MyApplication.getInstance().addActivity(this);

		// ��ȡ����ؼ�����
		sv = (ScrollView) this.findViewById(R.id.scrollView1);
		sv.setHorizontalScrollBarEnabled(true);
		hsv = (HorizontalScrollView) this
				.findViewById(R.id.horizontalScrollView1);
		tab = (TableLayout) hsv.getChildAt(0);

		// ���·��ؼ����ص���һ������
		previous = (Button) this.findViewById(R.id.btnPrevious01);
		previous.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Helper.simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		// ����һ����ת������ѡ�����
		next = (Button) this.findViewById(R.id.btnNext01);
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// ��ת����һ��֮ǰ�ж��Ƿ�ѡ������������
				if (!Helper.checkRadioButton(tab)) {
					Helper.message(activity, "��ѡ��һ����������!");
					return;
				}
				dispatch(pp);
			}
		});

		// ����ͼ���������һ��Activity������������
		Intent intent = this.getIntent();
		pp = (PassParameter) intent.getSerializableExtra(PassParameter.PP_KEY);

		// �������ڴ�����
		pd = ProgressDialog.show(this, "��ʾ", "���ڴ������Ժ�...");

		// �����߳�
		if (thread.getState() == State.NEW) {
			thread.start();
		} else {
			thread.run();
		}
	}

	// �����߳�
	private Thread thread = new Thread() {
		@Override
		public void run() {
			// ��SAPϵͳ��ȡ����
			String result = getJSONString(pp);
			orders = parseFromJson(result);

			Message message = handler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("flag", "ok");
			message.setData(b);
			handler.sendMessage(message);
		}
	};

	// ����UI
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Helper.enableButton(next);
			if ("ok".equals(msg.getData().getString("flag"))) {
				// ��̬������ݼ�¼
				addRows(orders);
				pd.dismiss();
				thread.stop();
			}
		}
	};

	// ��SAP��������ȡ����(JSON�ַ���)
	private String getJSONString(PassParameter pp) {
		String url = Helper.getUrl("service0001");
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("BeginDate", pp.getBeginDate()));
		pairs.add(new BasicNameValuePair("EndDate", pp.getEndDate()));
		pairs.add(new BasicNameValuePair("OrderCode", pp.getOrderCode()));
		pairs.add(new BasicNameValuePair("MaterialDesc", pp.getMaterialDesc()));
		return Helper.callSAPService(pp, url, pairs);
	}

	// ����JSON�ַ��������ɶ���ʵ������б�
	private LinkedList<Order> parseFromJson(String jsonData) {
		if (jsonData == null)
			return null;
		if ("".equals(jsonData))
			return null;
		Type listType = new TypeToken<LinkedList<Order>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<Order> entitys = gson.fromJson(jsonData, listType);
		return entitys;
	}

	// ���ݶ�ȡ�ļ�¼��̬����б���
	private Boolean addRows(LinkedList<Order> orders) {
		if (orders == null) {
			Helper.disenableButton(next);
			return false;
		}
		if (orders.isEmpty()) {
			Helper.disenableButton(next);
			return false;
		}

		TableRow row;
		TextView view;
		RadioButton radio;

		for (Order order : orders) {
			row = new TableRow(this);

			radio = new RadioButton(this);
			radio.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					changedRadio((RadioButton) v);
				}
			});
			row.addView(radio);

			view = new TextView(this);
			view.setText(order.getAUFNR());
			row.addView(view);

			view = new TextView(this);
			view.setText(order.getMAKTX());
			row.addView(view);

			view = new TextView(this);
			view.setText(String.valueOf(order.getGAMNG()));
			row.addView(view);

			view = new TextView(this);
			view.setText(order.getMSEHT());
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
				pp.setAUFNR(((TextView) row.getChildAt(1)).getText().toString());
			} else {
				if (radio.isChecked()) {
					radio.setChecked(false);
				}
			}
		}
	}

	// ��ת����һ��Activity
	private void dispatch(PassParameter pp) {
		Intent intent = new Intent(this, StepListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(PassParameter.PP_KEY, pp);
		intent.putExtras(bundle);

		startActivity(intent);
	}

}
