/**
 * ������
 */
package com.fungchoi.sap.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.fungchoi.sap.R;
import com.fungchoi.sap.entity.PassParameter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * @author Administrator
 * 
 */
public final class Helper {
	private final static String host = "10.10.100.239";
	private final static int port = 8000;
	private final static String client = "500";

	// private final static String userName = "test01";
	// private final static String password = "4480340";

	/**
	 * 
	 */
	public Helper() {
	}

	// ����ϵͳ����-���ؼ�
	public static void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	// ��������
	public static void updateDate(EditText et, int mYear, int mMonth, int mDay) {
		et.setText(new StringBuilder().append(mYear).append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay));
	}

	// ����ʱ��
	public static void updateTime(EditText et, int mHour, int mMinute) {
		et.setText(new StringBuilder()
				.append((mHour < 10) ? "0" + mHour : mHour).append(":")
				.append((mMinute < 10) ? "0" + mMinute : mMinute));

	}

	// �����ֻ�����
	public static void hideIM(Activity activity, View edt) {
		try {
			InputMethodManager im = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			IBinder windowToken = edt.getWindowToken();

			if (windowToken != null) {
				im.hideSoftInputFromWindow(windowToken, 0);
			}
		} catch (Exception e) {

		}
	}

	// ����JSON�ַ���������ʵ������б�
	public static <T> LinkedList<T> parseOrderFromJson(String jsonData) {
		if (jsonData == null)
			return null;
		if ("".equals(jsonData))
			return null;
		Type listType = new TypeToken<LinkedList<T>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<T> entitys = gson.fromJson(jsonData, listType);
		return entitys;
	}

	// ��ȡURL
	public static String getUrl(String service) {
		StringBuilder sb = new StringBuilder();
		sb.append("/sap/bc/icf/").append(service).append("?sap-client=")
				.append(Helper.client);
		return sb.toString();
	}

	// ����SAP����
	public static String callSAPService(PassParameter pp, String url,
			List<BasicNameValuePair> pairs) {
		String result = "";
		HttpHost targetHost = new HttpHost(Helper.host, Helper.port, "http");
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getCredentialsProvider().setCredentials(
				new AuthScope(targetHost.getHostName(), targetHost.getPort()),
				new UsernamePasswordCredentials(pp.getUserName(), pp
						.getPassword()));

		// // Create AuthCache instance
		// AuthCache authCache = new BasicAuthCache();
		// // Generate BASIC scheme object and add it to the local auth cache
		// BasicScheme basicAuth = new BasicScheme();
		// authCache.put(targetHost, basicAuth);
		//
		// // Add AuthCache to the execution context
		// BasicHttpContext localcontext = new BasicHttpContext();
		// localcontext.setAttribute(ClientContext.AUTH_SCHEME_PREF, authCache);

		HttpPost request = new HttpPost(url);
		try {
			request.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			result = null;// "UnsupportedEncodingException.";
		}
		ResponseHandler<String> handler = new BasicResponseHandler();
		try {
			result = httpclient.execute(targetHost, request, handler);
		} catch (ClientProtocolException e) {
			result = null;// "ClientException.";
		} catch (IOException e) {
			result = null;// "IOException.";
		} catch (Exception e) {
			result = null;
		}

		httpclient.getConnectionManager().shutdown();
		return result;
	}

	// ���ð�ť
	public static void disenableButton(Button button) {
		if (button == null) {
			return;
		}
		if (button.isEnabled()) {
			button.setEnabled(false);
		}
	}

	// ���ð�ť
	public static void enableButton(Button button) {
		if (button == null) {
			return;
		}
		if (!button.isEnabled()) {
			button.setEnabled(true);
		}
	}

	// ���ô��ڱ���ͼ��
	public static void setIcon(Activity act) {
		Window win = act.getWindow();
		win.requestFeature(Window.FEATURE_LEFT_ICON);
		win.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.fc24);
	}

	// �ж��ֻ�����������10%�����û�һ����ʾ
	private static Activity bat_activity;

	public static void checkBattery(Activity activity) {
		bat_activity = activity;
		bat_activity.registerReceiver(mReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
	}

	private static BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra("level", 0);
			// level��%���ǵ�ǰ������
			if (level <= 10) {
				new AlertDialog.Builder(bat_activity).setTitle("����")
						.setMessage("����ֻ���������ֻ��" + level + "%\n�뼰ʱ���!")
						.setPositiveButton("ȷ��", null).show();
			}
			// ע�����ֻ������ļ���
			bat_activity.unregisterReceiver(mReceiver);
		}
	};

	// �ж�3G�������WIFI��������
	public static boolean checkNet(Activity activity) {
		ConnectivityManager mConnectivity = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		// ����������ӣ������������ã��Ͳ���Ҫ��������������
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			return false;
		}
		// �ж������������ͣ�3G��wifi���Ƿ�����
		int netType = info.getType();
		int netSubtype = info.getSubtype();
		if (netType == ConnectivityManager.TYPE_WIFI) {
			return info.isConnected();
		} else if (netType == ConnectivityManager.TYPE_MOBILE
				&& netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
				&& !mTelephony.isNetworkRoaming()) {
			return info.isConnected();
		} else {
			return false;
		}
	}

	// ����������Ϣ
	public static void message(Activity activity, String msg) {
		new AlertDialog.Builder(activity).setTitle("����").setMessage(msg)
				.setPositiveButton("ȷ��", null).show();
	}

	// ��ȡ�ֻ����룬����һ���ܻ�ȡ�������SIM���Ѿ�д���˱���������ܻ�ȡ
	public static String getPhoneNumber(Activity activity) {
		TelephonyManager tm = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}

	public static boolean checkRadioButton(TableLayout tab) {
		int count = tab.getChildCount();
		boolean flag = false;
		TableRow row;
		RadioButton radio;
		for (int i = 1; i < count; i++) {
			row = (TableRow) tab.getChildAt(i);
			radio = (RadioButton) row.getChildAt(0);
			if (radio.isChecked()) {
				flag = true;
			}
		}
		return flag;
	}

}
