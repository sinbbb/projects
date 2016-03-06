package com.Ryuji.android.BluetoothChat;




import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.inputmethod.*;
import android.widget.*;

public class TimerActivity extends Activity implements OnClickListener {

	public CountDownTimer countDownTimer;
	public boolean timerHasStarted = false;
	public ImageButton startB;
	public ImageButton startA;
	public TextView text;
	public EditText et;
	public String str;
	public long startTime = 0;
	public long st = 0;
	public final long interval = 1 * 1000;
	Vibrator vib ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);
		startB = (ImageButton) this.findViewById(R.id.startbtn);
		startA = (ImageButton) this.findViewById(R.id.inputbtn);
		text = (TextView) this.findViewById(R.id.timer);
		et = (EditText) this.findViewById(R.id.editText1);
		vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		startB.setOnClickListener(this);
		

		startA.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				InputMethodManager in = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				in.hideSoftInputFromWindow(et.getWindowToken(), 0);
				startTime = Long.parseLong(et.getText().toString())*1000*60;
				text.setText(String.valueOf(startTime / 1000));
			}
		});
		//countDownTimer = new MyCountDownTimer(startTime, interval);
		//text.setText(text.getText() + String.valueOf(startTime / 1000));
	}

	@Override
	public void onClick(View v) {
		countDownTimer = new MyCountDownTimer(startTime, interval);
		if (!timerHasStarted) {
			countDownTimer.start();
			text.setText(String.valueOf(startTime / 1000));

			timerHasStarted = true;
			//startB.setText("STOP");
		} /*else {
			countDownTimer.cancel();
			timerHasStarted = false;
			startB.setText("RESTART");
		}*/
	}

	public class MyCountDownTimer extends CountDownTimer {
		public MyCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		@Override
		public void onFinish() {			
			text.setText("Time's up!");
			vib.vibrate(3000);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			//Toast.makeText(getApplicationContext(), String.valueOf(millisUntilFinished), Toast.LENGTH_LONG).show();
			text.setText("" + millisUntilFinished / 1000);
		}
	}	
}
