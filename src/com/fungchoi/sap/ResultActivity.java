/**
 * ������
 */
package com.fungchoi.sap;

import com.fungchoi.sap.entity.PassParameter;
import com.fungchoi.sap.util.Helper;
import com.fungchoi.sap.util.MyApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Administrator
 * 
 */
public class ResultActivity extends Activity {

	/**
	 * 
	 */
	public ResultActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.result);

		MyApplication.getInstance().addActivity(this);

		// ����ͼ���������һ��Activity������������
		Intent intent = this.getIntent();
		PassParameter pp = (PassParameter) intent
				.getSerializableExtra(PassParameter.PP_KEY);

		TextView view = (TextView) this.findViewById(R.id.txtContent);
		view.setText(pp.getResult());

		// ���·��ؼ����ص���һ������
		Button previous = (Button) this.findViewById(R.id.btnPrevious06);
		previous.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Helper.simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});

		// �˳�ϵͳ
		Button exit = (Button) this.findViewById(R.id.btnExit);
		exit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MyApplication.getInstance().exit();
			}
		});
	}

}
