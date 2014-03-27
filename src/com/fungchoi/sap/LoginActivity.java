/**
 * 用户登录类
 */
package com.fungchoi.sap;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.fungchoi.sap.entity.PassParameter;
import com.fungchoi.sap.util.Helper;
import com.fungchoi.sap.util.MyApplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Administrator
 * 
 */
public class LoginActivity extends Activity {

	private Activity activity;

	private EditText txtUserName;
	private EditText txtPassword;
	private Button btnLogin;

	private ProgressDialog pd;

	private PassParameter pp;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// 当前活动
		activity = this;
		// 传递参数
		pp = new PassParameter();
		// 设置用户手机号码
		pp.setLTXA1(Helper.getPhoneNumber(activity));

		// 界面控件
		txtUserName = (EditText) this.findViewById(R.id.txtUserName);
		txtPassword = (EditText) this.findViewById(R.id.txtPassword);
		btnLogin = (Button) this.findViewById(R.id.btnLogin);

		// 判断网络连接
		if (!Helper.checkNet(this)) {
			Helper.message(this, "没有可用的3G或Wifi网络!");
			Helper.disenableButton(btnLogin);
			return;
		}

		// 将当前活动压入活动堆栈
		MyApplication.getInstance().addActivity(this);

		// 登录按钮注册事件
		btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 设置用户帐号和密码
				pp.setUserName(txtUserName.getText().toString().trim());
				pp.setPassword(txtPassword.getText().toString().trim());

				// 设置正在处理窗口
				pd = ProgressDialog.show(activity, "提示", "正在登录，请稍候...");

				// 启动线程
				if (thread.getState() == State.NEW) {
					thread.start();
				} else {
					thread.run();
				}
			}
		});
	}

	// 工作线程
	private Thread thread = new Thread() {
		@Override
		public void run() {
			// 从SAP系统获取数据
			String result = getJSONString(pp);

			Message message = handler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("flag", result);
			message.setData(b);
			handler.sendMessage(message);
		}
	};

	// 更新UI
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pd.dismiss();
			if ("success".equals(msg.getData().getString("flag"))) {
				Helper.enableButton(btnLogin);
				// thread.stop();
				// 成功登录，则跳转至开始报工界面
				dispatch(pp);
			} else {
				Helper.message(activity, "帐号或者密码错误!\n登录失败 !");
				thread.stop();
			}
		}
	};

	// 从SAP服务器获取内容(JSON字符串)
	private String getJSONString(PassParameter pp) {
		String url = Helper.getUrl("service0000");
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("UserName", pp.getUserName()));
		pairs.add(new BasicNameValuePair("Password", pp.getPassword()));
		return Helper.callSAPService(pp, url, pairs);
	}

	// 跳转到下一个Activity
	private void dispatch(PassParameter pp) {
		Intent intent = new Intent(this, MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(PassParameter.PP_KEY, pp);
		intent.putExtras(bundle);

		startActivity(intent);
	}
}
