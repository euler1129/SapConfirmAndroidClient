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
import com.fungchoi.sap.entity.ZuoYe;
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
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Administrator
 * 
 */
public class ZuoYeActivity extends Activity {

	private PassParameter pp;
	private LinkedList<ZuoYe> zuoyes;

	private ProgressDialog pd;
	private Button previous;
	private Button next;

	private EditText vgw01;
	private EditText vgw02;
	private EditText vgw03;
	private EditText vgw04;
	private EditText vgw05;
	private EditText vgw06;
	private EditText vge01;
	private EditText vge02;
	private EditText vge03;
	private EditText vge04;
	private EditText vge05;
	private EditText vge06;

	/**
	 * 
	 */
	public ZuoYeActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.zuoye);

		MyApplication.getInstance().addActivity(this);

		vgw01 = (EditText) this.findViewById(R.id.txtVgw01);
		vgw02 = (EditText) this.findViewById(R.id.txtVgw02);
		vgw03 = (EditText) this.findViewById(R.id.txtVgw03);
		vgw04 = (EditText) this.findViewById(R.id.txtVgw04);
		vgw05 = (EditText) this.findViewById(R.id.txtVgw05);
		vgw06 = (EditText) this.findViewById(R.id.txtVgw06);
		vge01 = (EditText) this.findViewById(R.id.txtVge01);
		vge02 = (EditText) this.findViewById(R.id.txtVge02);
		vge03 = (EditText) this.findViewById(R.id.txtVge03);
		vge04 = (EditText) this.findViewById(R.id.txtVge04);
		vge05 = (EditText) this.findViewById(R.id.txtVge05);
		vge06 = (EditText) this.findViewById(R.id.txtVge06);

		// 按下返回键返回到上一个界面
		previous = (Button) this.findViewById(R.id.btnPrevious03);
		previous.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Helper.simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		// 按下一步跳转到工序选择界面
		next = (Button) this.findViewById(R.id.btnNext03);
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
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
			zuoyes = parseFromJson(result);

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
			if ("ok".equals(msg.getData().getString("flag"))) {
				// 设置默认值
				setDefaultValue();
				pd.dismiss();
				thread.stop();
			}
		}
	};

	// 从SAP服务器获取内容(JSON字符串)
	private String getJSONString(PassParameter pp) {
		String url = Helper.getUrl("service0003");
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("OrderCode", pp.getAUFNR()));
		pairs.add(new BasicNameValuePair("StepCode", pp.getVORNR()));
		pairs.add(new BasicNameValuePair("Quantity", String.valueOf(pp
				.getLMNGA())));
		return Helper.callSAPService(pp, url, pairs);
	}

	// 根据JSON字符串解析成订单实体对象列表
	private LinkedList<ZuoYe> parseFromJson(String jsonData) {
		if (jsonData == null)
			return null;
		if ("".equals(jsonData))
			return null;
		Type listType = new TypeToken<LinkedList<ZuoYe>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<ZuoYe> entitys = gson.fromJson(jsonData, listType);
		return entitys;
	}

	private Boolean setDefaultValue() {
		if (zuoyes == null)
			return false;
		if (zuoyes.isEmpty())
			return false;

		ZuoYe zuoye = zuoyes.getFirst();
		vgw01.setText(String.valueOf(zuoye.getVGW01()));
		vgw02.setText(String.valueOf(zuoye.getVGW02()));
		vgw03.setText(String.valueOf(zuoye.getVGW03()));
		vgw04.setText(String.valueOf(zuoye.getVGW04()));
		vgw05.setText(String.valueOf(zuoye.getVGW05()));
		vgw06.setText(String.valueOf(zuoye.getVGW06()));
		vge01.setText(zuoye.getVGE01());
		vge02.setText(zuoye.getVGE02());
		vge03.setText(zuoye.getVGE03());
		vge04.setText(zuoye.getVGE04());
		vge05.setText(zuoye.getVGE05());
		vge06.setText(zuoye.getVGE06());

		return true;
	}

	// 跳转到下一个Activity
	private void dispatch(PassParameter pp) {
		pp.setISM01(Float.parseFloat(vgw01.getText().toString()));
		pp.setISM02(Float.parseFloat(vgw02.getText().toString()));
		pp.setISM03(Float.parseFloat(vgw03.getText().toString()));
		pp.setISM04(Float.parseFloat(vgw04.getText().toString()));
		pp.setISM05(Float.parseFloat(vgw05.getText().toString()));
		pp.setISM06(Float.parseFloat(vgw06.getText().toString()));
		pp.setILE01(vge01.getText().toString());
		pp.setILE02(vge02.getText().toString());
		pp.setILE03(vge03.getText().toString());
		pp.setILE04(vge04.getText().toString());
		pp.setILE05(vge05.getText().toString());
		pp.setILE06(vge06.getText().toString());

		Intent intent = new Intent(this, DateTimeActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(PassParameter.PP_KEY, pp);
		intent.putExtras(bundle);

		startActivity(intent);
	}

}
