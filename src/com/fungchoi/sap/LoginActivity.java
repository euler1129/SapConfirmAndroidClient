/**
 * �û���¼��
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

		// ��ǰ�
		activity = this;
		// ���ݲ���
		pp = new PassParameter();
		// �����û��ֻ�����
		pp.setLTXA1(Helper.getPhoneNumber(activity));

		// ����ؼ�
		txtUserName = (EditText) this.findViewById(R.id.txtUserName);
		txtPassword = (EditText) this.findViewById(R.id.txtPassword);
		btnLogin = (Button) this.findViewById(R.id.btnLogin);

		// �ж���������
		if (!Helper.checkNet(this)) {
			Helper.message(this, "û�п��õ�3G��Wifi����!");
			Helper.disenableButton(btnLogin);
			return;
		}

		// ����ǰ�ѹ����ջ
		MyApplication.getInstance().addActivity(this);

		// ��¼��ťע���¼�
		btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// �����û��ʺź�����
				pp.setUserName(txtUserName.getText().toString().trim());
				pp.setPassword(txtPassword.getText().toString().trim());

				// �������ڴ�����
				pd = ProgressDialog.show(activity, "��ʾ", "���ڵ�¼�����Ժ�...");

				// �����߳�
				if (thread.getState() == State.NEW) {
					thread.start();
				} else {
					thread.run();
				}
			}
		});
	}

	// �����߳�
	private Thread thread = new Thread() {
		@Override
		public void run() {
			// ��SAPϵͳ��ȡ����
			String result = getJSONString(pp);

			Message message = handler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("flag", result);
			message.setData(b);
			handler.sendMessage(message);
		}
	};

	// ����UI
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pd.dismiss();
			if ("success".equals(msg.getData().getString("flag"))) {
				Helper.enableButton(btnLogin);
				// thread.stop();
				// �ɹ���¼������ת����ʼ��������
				dispatch(pp);
			} else {
				Helper.message(activity, "�ʺŻ����������!\n��¼ʧ�� !");
				thread.stop();
			}
		}
	};

	// ��SAP��������ȡ����(JSON�ַ���)
	private String getJSONString(PassParameter pp) {
		String url = Helper.getUrl("service0000");
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("UserName", pp.getUserName()));
		pairs.add(new BasicNameValuePair("Password", pp.getPassword()));
		return Helper.callSAPService(pp, url, pairs);
	}

	// ��ת����һ��Activity
	private void dispatch(PassParameter pp) {
		Intent intent = new Intent(this, MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(PassParameter.PP_KEY, pp);
		intent.putExtras(bundle);

		startActivity(intent);
	}
}
