package com.yuri.push;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.push.example.PushDemoActivity;
import com.baidu.push.example.Utils;
import com.yuri.baidu.push.demo.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ProgressDialog progressDialog;
	private Handler mHandler = new Handler();

	Runnable runnable = new Runnable() {
		@Override
		public void run() {

			getData();
			// testIccMore();
			Log.i("A302", "Copy run ");
			mHandler.postDelayed(this, 300);

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);

		Utils.logStringCache = Utils.getLogText(getApplicationContext());

		Resources resource = this.getResources();
		String pkgName = this.getPackageName();

		// Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
		// 这里把apikey存放于manifest文件中，只是一种存放方式，
		// 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
		// "api_key")
		// 通过share preference实现的绑定标志开关，如果已经成功绑定，就取消这次绑定
		if (!Utils.hasBind(getApplicationContext())) {
			// ！！ 请将AndroidManifest.xml 104行处 api_key 字段值修改为自己的 api_key 方可使用 ！！
			// ！！ ATTENTION：You need to modify the value of api_key to your own
			// at row 104 in AndroidManifest.xml to use this Demo !!
			PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, Utils.getMetaValue(MainActivity.this, "api_key"));
			// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
			// PushManager.enableLbs(getApplicationContext());
		}

		// Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
		// 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
		// 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
		/*
		 * CustomPushNotificationBuilder cBuilder = new
		 * CustomPushNotificationBuilder(getApplicationContext(),
		 * resource.getIdentifier( "notification_custom_builder", "layout",
		 * pkgName), resource.getIdentifier("notification_icon", "id", pkgName),
		 * resource.getIdentifier( "notification_title", "id", pkgName),
		 * resource.getIdentifier("notification_text", "id", pkgName));
		 * cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
		 * cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND |
		 * Notification.DEFAULT_VIBRATE);
		 * cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
		 * cBuilder.setLayoutDrawable
		 * (resource.getIdentifier("simple_notification_icon", "drawable",
		 * pkgName)); PushManager.setNotificationBuilder(this, 1, cBuilder);
		 */

		List<String> tags = Utils.getTagsList("aa,bb");
		PushManager.setTags(getApplicationContext(), tags);

//		Button b1 = (Button) findViewById(R.id.btn);
		Button b1 = null;
		b1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				/*
				 * Intent intent = new Intent();
				 * intent.setClass(Home.this,ProductCategory.class);
				 * startActivity(intent);
				 */

				new Thread(new Runnable() {
					@Override
					public void run() {
						ActionDo2();
					}
				}).start();

			}
		});

		new Thread(new Runnable() {
			@Override
			public void run() {
				// getData();
			}
		}).start();

	}

	private void getData() {
		String url = "https://channel.api.duapp.com/rest/2.0/channel/channel";

		// 这里需要分析服务器回传的json格式数据，
		try {
			String reStr = HttpHelper.getJsonData(url);
			JSONObject reObject = new JSONObject(reStr);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
		}

	}
	protected void ActionDo2() {
		System.out.println(new BaiduPushUtil().push_msg("818670655850242201", "4222129398189191299", "推送成功", "推送成功"));
	}
	protected void ActionDo1() {
		
		
		String url = "https://channel.api.duapp.com/rest/2.0/channel/channel";
		HttpPost request = new HttpPost(url);
		// 先封装一个 JSON 对象
		JSONObject param = new JSONObject();
		
		try {
			/*String sTime = Long.toString(new Date().getTime());
			String eTime = Long.toString(new Date().getTime()+1000000);
			param.put("method", "push_msg");

			param.put("apikey", "w6C2KXKNlHxH7KedQZphLx22");
			param.put("push_type", 3);
			//param.put("tag", "aa");
			param.put("messages", "{\"title\":\"title\",\"description\":\"test description\"}");
			param.put("msg_keys", "testkey");
			param.put("timestamp", sTime);
			
			param.put("expires", eTime);
			param.put("v", 1);
			
			String signStr = "";
			// MD5(urlencode($http_method$url$k1=$v1$k2=$v2$k3=$v3$secret_key));
			String canshu = "apikey=w6C2KXKNlHxH7KedQZphLx22,expires="+ eTime +",messages={\"title\":\"title\",\"description\":\"test description\"}";
			canshu += ",method=push_msg,msg_keys=testkey,push_type=1,timestamp=" + sTime+",v=1";
			
			try {
				signStr = MD5(urlencode("push_msg" + url + canshu + "6jTEpirP1rbD4Rk35zyaairTfCyD69DF"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			param.put("sign", signStr);*/
			String qs = "method=push_msg&apikey=Ljc710pzAa99GULCo8y48NvB&sign=8777F555E8C16715EBA5C85341684C58&timestamp=12344543232&expires=1238747373&v=1&push_type=1&device_type=3&channel_id=98765432123456789&user_id=12345678987654321&message_type=1&messages={\"title\":\"\",\"description\":\"test\"}&msg_keys=testkey";
			String[] q = qs.split("&");
			for(int i = 0 ; i < q.length;i++){
				param.put(q[i].split("=")[0], q[i].split("=")[1]);
			}
			
			// 绑定到请求 Entry
			StringEntity se = new StringEntity(param.toString());
			request.setEntity(se);
			// 发送请求
			HttpResponse httpResponse = new DefaultHttpClient().execute(request);
			// 得到应答的字符串，这也是一个 JSON 格式保存的数据
			String retSrc = EntityUtils.toString(httpResponse.getEntity());
			// 生成 JSON 对象
			JSONObject result = new JSONObject(retSrc);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String token = result.get("token");
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void ActionDo() {

		String uriAPI = "https://channel.api.duapp.com/rest/2.0/channel/channel";
		String sTime = Long.toString(new Date().getTime());
		// String uriAPI =
		// "http://channel.api.duapp.com/rest/2.0/channel/channel" ;
		HttpPost httpRequest = new HttpPost(uriAPI);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "push_msg"));
		params.add(new BasicNameValuePair("apikey", "w6C2KXKNlHxH7KedQZphLx22"));
		// params.add(new BasicNameValuePair("user_id", ""));
		params.add(new BasicNameValuePair("push_type", "1"));
		params.add(new BasicNameValuePair("tag", "aa"));
		params.add(new BasicNameValuePair("messages", "{\"title\":\"title\",\"description\":\"test description\"}"));
		params.add(new BasicNameValuePair("msg_keys", "testkey"));
		params.add(new BasicNameValuePair("timestamp", sTime));
		String signStr = "";
		// MD5(urlencode($http_method$url$k1=$v1$k2=$v2$k3=$v3$secret_key));
		String canshu = "apikey=w6C2KXKNlHxH7KedQZphLx22,messages={\"title\":\"title\",\"description\":\"test description\"}";
		canshu += ",method=push_msg,msg_keys=testkey,push_type=1,tag=aa,timestamp=" + sTime;
		try {
			signStr = MD5(urlencode("push_msg" + uriAPI + canshu + "6jTEpirP1rbD4Rk35zyaairTfCyD69DF"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		params.add(new BasicNameValuePair("sign", signStr));
		// params.add(new BasicNameValuePair("contact", "�ֻ���"));
		try {
			// httpRequest.setEntity(new UrlEncodedFormEntity(params,
			// HTTP.UTF_8));
			// httpRequest.setHeader("Referer", "")
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse.getEntity());

				Log.i("dz", "strResult:" + strResult);

				JSONObject reObject = new JSONObject(strResult);
				if (reObject.getInt("status") == 0) {
					Toast.makeText(MainActivity.this, reObject.getString("error"), Toast.LENGTH_SHORT).show();
					return;
				} else {

				}

			} else {

				// Toast("Error Response: "+
				// httpResponse.getStatusLine().toString() + "   0");
			}
		} catch (ClientProtocolException e) {
			// Toast(e.getMessage().toString() + "   1");
			// e.printStackTrace();
			// Toast(getString(R.string.loading_error));
		} catch (IOException e) {
			// Toast(e.getMessage().toString()+ "   2");
			// e.printStackTrace();
			// Toast(getString(R.string.loading_error));
		} catch (Exception e) {
			// Toast(e.getMessage().toString()+ "   3");
			e.printStackTrace();
			// Toast(getString(R.string.loading_error));
		}

	}

	private static String urlencode(String str) throws UnsupportedEncodingException {
		String rc = URLEncoder.encode(str, "utf-8");
		return rc.replace("*", "%2A");
	}

	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {   
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString().toUpperCase();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
