package com.Ryuji.android.BluetoothChat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashactivity);
		
		Handler handler = new Handler(){
			public void handleMessage(Message msg){
				finish();
			}
		};
		handler.sendEmptyMessageDelayed(0, 2000);
	}

}
