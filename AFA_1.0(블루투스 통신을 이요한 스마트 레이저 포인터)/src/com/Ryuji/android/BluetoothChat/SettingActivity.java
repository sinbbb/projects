package com.Ryuji.android.BluetoothChat;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class SettingActivity extends Activity {

	String m = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		
		
		ImageButton SmallBlue = (ImageButton) findViewById(R.id.sbp1);
		ImageButton BigBlue = (ImageButton) findViewById(R.id.sbp2);
		ImageButton SmallYellow = (ImageButton) findViewById(R.id.yp1);
		ImageButton BigYellow = (ImageButton) findViewById(R.id.yp2);
		ImageButton SmallRed = (ImageButton) findViewById(R.id.rp1);
		ImageButton BigRed = (ImageButton) findViewById(R.id.rp2);
		
		
		SmallBlue.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				m = "b";				
				((BluetoothChat) BluetoothChat.mContext).sendMessage(m);
				
			}
		});
		BigBlue.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				m = "B";				
				((BluetoothChat) BluetoothChat.mContext).sendMessage(m);
				
			}
		});
		SmallYellow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				m = "y";				
				((BluetoothChat) BluetoothChat.mContext).sendMessage(m);
				
			}
		});
		BigYellow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				m = "Y";				
				((BluetoothChat) BluetoothChat.mContext).sendMessage(m);
				
			}
		});
		SmallRed.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				m = "r";				
				((BluetoothChat) BluetoothChat.mContext).sendMessage(m);
				
			}
		});
		BigRed.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				m = "E";				
				((BluetoothChat) BluetoothChat.mContext).sendMessage(m);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option_menu, menu);
		
		
		return true;
	}

}
