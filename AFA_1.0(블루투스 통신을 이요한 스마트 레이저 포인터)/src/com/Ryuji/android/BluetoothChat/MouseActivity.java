package com.Ryuji.android.BluetoothChat;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MouseActivity extends Activity {

	// BluetoothChat bc = new BluetoothChat();

	float x, y =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mouse);

		ImageButton lbt = (ImageButton) findViewById(R.id.leftmb);
		ImageButton rbt = (ImageButton) findViewById(R.id.rightmb);
		ImageButton dbt = (ImageButton) findViewById(R.id.doublemb);
		ImageButton bbt = (ImageButton) findViewById(R.id.backbtn);
		FrameLayout fl = (FrameLayout) findViewById(R.id.Touch);

		lbt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String msg = "L";
				((BluetoothChat) BluetoothChat.mContext).sendMessage(msg);

			}
		});

		dbt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String msg = null;
				msg = "D";
				// TODO Auto-generated method stub
				((BluetoothChat) BluetoothChat.mContext).sendMessage(msg);
			}
		});
		
		rbt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String msg = null;
				msg = "R";
				// TODO Auto-generated method stub
				((BluetoothChat) BluetoothChat.mContext).sendMessage(msg);
			}
		});
		bbt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		fl.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				String m = null;
				switch (action) {
				case (MotionEvent.ACTION_MOVE):
					if (event.getX() > x && event.getY() < y) {
						m = "9";
						x = event.getX();
						y = event.getY();
						((BluetoothChat) BluetoothChat.mContext).sendMessage(m);
					} else if (event.getX() < x && event.getY() < y) {
						m = "7";
						x = event.getX();
						y = event.getY();
						((BluetoothChat) BluetoothChat.mContext).sendMessage(m);
					} else if (event.getX() < x && event.getY() > y) {
						m = "1";
						x = event.getX();
						y = event.getY();
						((BluetoothChat) BluetoothChat.mContext).sendMessage(m);
					} else if (event.getX() > x && event.getY() > y) {
						m = "3";
						x = event.getX();
						y = event.getY();
						((BluetoothChat) BluetoothChat.mContext).sendMessage(m);
					} else if (event.getX() == x && event.getY() < y) {
						m = "8";
						x = event.getX();
						y = event.getY();
						((BluetoothChat) BluetoothChat.mContext).sendMessage(m);
					} else if (event.getX() < x && event.getY() == y) {
						m = String.valueOf(4);
						x = event.getX();
						y = event.getY();
						((BluetoothChat) BluetoothChat.mContext).sendMessage(m);
					} else if (event.getX() == x && event.getY() > y) {
						m = String.valueOf(2);
						x = event.getX();
						y = event.getY();
						((BluetoothChat) BluetoothChat.mContext).sendMessage(m);
					} else if (event.getX() > x && event.getY() == y) {
						m = String.valueOf(6);
						x = event.getX();
						y = event.getY();
						((BluetoothChat) BluetoothChat.mContext).sendMessage(m);
					}

					break;
				}
				return true;
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
