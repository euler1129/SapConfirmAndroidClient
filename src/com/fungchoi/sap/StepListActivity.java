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

import com.fungchoi.sap.entity.PassParameter;
import com.fungchoi.sap.entity.Step;
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
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
public class StepListActivity extends Activity {
	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

	private Activity activity;

	private ScrollView sv;
	private HorizontalScrollView hsv;
	private TableLayout tab;

	private PassParameter pp;
	private LinkedList<Step> steps;

	private ProgressDialog pd;
	private Button previous;
	private Button next;

	/**
	 * 
	 */
	public StepListActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.steplist);

		// 当前活动
		activity = this;

		// 将当前活动压入活动堆栈
		MyApplication.getInstance().addActivity(this);

		// 获取界面控件对象
		sv = (ScrollView) this.findViewById(R.id.scrollView2);
		sv.setHorizontalScrollBarEnabled(true);
		hsv = (HorizontalScrollView) this
				.findViewById(R.id.horizontalScrollView2);
		tab = (TableLayout) hsv.getChildAt(0);

		// 按下返回键返回到上一个界面
		previous = (Button) this.findViewById(R.id.btnPrevious02);
		previous.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Helper.simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		// 按下一步跳转到工序选择界面
		next = (Button) this.findViewById(R.id.btnNext02);
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 跳转到下一步之前判断是否选择了工序
				if (!Helper.checkRadioButton(tab)) {
					Helper.message(activity, "请选择一道工序!");
					return;
				}
				dispatch(pp);
			}
		});

		// 从意图里面接收上一个Activity传过来的数据
		Intent intent = this.getIntent();
		pp = (PassParameter) intent.getSerializableExtra(PassParameter.PP_KEY);

		// 设置正在处理窗口
		pd = ProgressDialog.show(this, "提示", "正在处理，请稍候...");

		// 启动线程
		if (thread.getState() == State.NEW) {
			thread.start();
		} else {
			thread.run();
		}
	}

	// 工作线程
	private Thread thread = new Thread() {
		@Override
		public void run() {
			// 从SAP系统获取数据
			String result = getJSONString(pp);
			steps = parseFromJson(result);

			Message message = handler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("flag", "ok");
			message.setData(b);
			handler.sendMessage(message);
		}
	};

	// 更新UI
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Helper.enableButton(next);
			if ("ok".equals(msg.getData().getString("flag"))) {
				// 动态添加数据记录
				addRows();
				pd.dismiss();
				thread.stop();
			}
		}
	};

	// 从SAP服务器获取内容(JSON字符串)
	private String getJSONString(PassParameter pp) {
		String url = Helper.getUrl("service0002");
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("OrderCode", pp.getAUFNR()));
		return Helper.callSAPService(pp, url, pairs);
	}

	// 根据JSON字符串解析成订单实体对象列表
	private LinkedList<Step> parseFromJson(String jsonData) {
		if (jsonData == null)
			return null;
		if ("".equals(jsonData))
			return null;
		Type listType = new TypeToken<LinkedList<Step>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<Step> entitys = gson.fromJson(jsonData, listType);
		return entitys;
	}

	// 根据读取的记录动态添加列表行
	private Boolean addRows() {
		if (steps == null) {
			Helper.disenableButton(next);
			return false;
		}
		if (steps.isEmpty()) {
			Helper.disenableButton(next);
			return false;
		}

		TableRow row;
		TextView view;
		EditText edit;
		RadioButton radio;

		for (Step step : steps) {
			row = new TableRow(this);

			radio = new RadioButton(this);
			radio.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					changedRadio((RadioButton) v);
				}
			});
			row.addView(radio);

			view = new TextView(this);
			view.setText(step.getVORNR());
			row.addView(view);

			view = new TextView(this);
			view.setText(step.getLTXA1());
			row.addView(view);

			edit = new EditText(this);
			edit.setInputType(InputType.TYPE_CLASS_NUMBER);
			edit.setText(String.valueOf(step.getMGVRG()));
			row.addView(edit);

			view = new TextView(this);
			view.setText(step.getMSEHT());
			row.addView(view);

			view = new TextView(this);
			view.setVisibility(View.INVISIBLE);
			view.setText(step.getMEINH());
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
				pp.setVORNR(((TextView) row.getChildAt(1)).getText().toString());
				pp.setLMNGA(Float.parseFloat(((TextView) row.getChildAt(3))
						.getText().toString().trim()));
				pp.setMEINH(((TextView) row.getChildAt(5)).getText().toString());
			} else {
				if (radio.isChecked()) {
					radio.setChecked(false);
				}
			}
		}
	}

	// 重新设置传递参数中的（选中工序的)工序数量
	private void resetLMNGA() {
		int count = tab.getChildCount();
		TableRow row;
		RadioButton radio;
		for (int i = 1; i < count; i++) {
			row = (TableRow) tab.getChildAt(i);
			radio = (RadioButton) row.getChildAt(0);
			if (radio.isChecked()) {
				pp.setLMNGA(Float.parseFloat(((TextView) row.getChildAt(3))
						.getText().toString().trim()));
				break;
			}
		}
	}

	// 跳转到下一个Activity
	private void dispatch(PassParameter pp) {
		resetLMNGA();

		Intent intent = new Intent(this, ZuoYeActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(PassParameter.PP_KEY, pp);
		intent.putExtras(bundle);

		startActivity(intent);
	}

}
