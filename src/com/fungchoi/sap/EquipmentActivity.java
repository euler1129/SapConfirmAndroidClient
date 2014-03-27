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

		// 当前活动
		activity = this;

		// 将当前活动压入活动堆栈
		MyApplication.getInstance().addActivity(this);

		// 获取界面控件对象
		sv = (ScrollView) this.findViewById(R.id.scrollView3);
		sv.setHorizontalScrollBarEnabled(true);
		hsv = (HorizontalScrollView) this
				.findViewById(R.id.horizontalScrollView3);
		tab = (TableLayout) hsv.getChildAt(0);

		// 按下返回键返回到上一个界面
		previous = (Button) this.findViewById(R.id.btnPrevious05);
		previous.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Helper.simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});

		// 按完成按钮提交报工数据到SAP服务完成报工处理
		finish = (Button) this.findViewById(R.id.btnFinish);
		finish.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 正式报工之前判断是否选择了机台或者手工组
				if (!Helper.checkRadioButton(tab)) {
					Helper.message(activity, "请选择一台设备或一个手工组!");
					return;
				}
				// 提交处理-报工
				process();
			}
		});

		// 从意图里面接收上一个Activity传过来的数据
		Intent intent = this.getIntent();
		pp = (PassParameter) intent.getSerializableExtra(PassParameter.PP_KEY);

		// 设置正在处理窗口
		pd1 = ProgressDialog.show(this, "提示", "正在处理，请稍候...");
		// 启动线程
		if (thread1.getState() == State.NEW) {
			thread1.start();
		} else {
			thread1.run();
		}
	}

	// 工作线程1
	private Thread thread1 = new Thread() {
		@Override
		public void run() {
			// 从SAP系统获取数据
			String result = getJSONString(pp);
			machines = parseFromJson(result);

			Message message = handler1.obtainMessage();
			Bundle b = new Bundle();
			b.putString("flag", "ok");
			message.setData(b);
			handler1.sendMessage(message);
		}
	};

	// 更新UI
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			Helper.enableButton(finish);
			if ("ok".equals(msg.getData().getString("flag"))) {
				// 动态添加数据记录
				addRows();
				pd1.dismiss();
				thread1.stop();
			}
		}
	};

	// 从SAP服务器获取内容(JSON字符串)
	private String getJSONString(PassParameter pp) {
		String url = Helper.getUrl("service0004");
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("OrderCode", pp.getAUFNR()));
		return Helper.callSAPService(pp, url, pairs);
	}

	// 根据JSON字符串解析成订单实体对象列表
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

	// 根据读取的记录动态添加列表行
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

	// 当更改单选按钮时，获它所在行的订单编号及将其他的单选按钮置为未选中状态
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

	// 执行报工
	private void process() {
		// 设置正在处理窗口
		pd2 = ProgressDialog.show(this, "提示", "正在处理，请稍候...");
		// 启动线程
		if (thread2.getState() == State.NEW) {
			thread2.start();
		} else {
			thread2.run();
		}
	}

	// 提交处理,执行报工
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

	// 工作线程1
	private Thread thread2 = new Thread() {
		@Override
		public void run() {
			// 提交数据到SAP系统进行处理-执行报工，返回报工结果
			String result = submit(pp);
			pp.setResult(result);

			Message message = handler2.obtainMessage();
			handler2.sendMessage(message);
		}
	};

	// 更新UI
	private Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			pd2.dismiss();
			thread2.stop();
			// 跳转到结果界面
			dispatch(pp);
		}
	};

	// 跳转到下一个Activity
	private void dispatch(PassParameter pp) {
		Intent intent = new Intent(this, ResultActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(PassParameter.PP_KEY, pp);
		intent.putExtras(bundle);

		startActivity(intent);
	}

}
