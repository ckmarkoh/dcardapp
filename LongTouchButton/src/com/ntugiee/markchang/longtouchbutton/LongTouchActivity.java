package com.ntugiee.markchang.longtouchbutton;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class LongTouchActivity extends Activity {
	private Button ltButton;
	private int timeout_int;
	private CountDownTimer timer; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.long_touch);
		ltButton=(Button)findViewById(R.id.long_touch_button);
		timeout_int=3000;
		timer= new CountDownTimer(timeout_int, 100) {
            public void onTick(long millisUntilFinished) {
            	Log.d("seconds remaining:", String.valueOf(millisUntilFinished / 100));
            }
            public void onFinish() {
                final Intent intent = new Intent();
                intent.setClass(LongTouchActivity.this, ShowActivity.class);
                startActivity(intent);	
            }
         };
		
		ltButton.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					timer.start();
				}
				else if (event.getAction() == MotionEvent.ACTION_UP){
					timer.cancel();
				}
				return false;
		    }
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.long_touch, menu);
		return true;
	}

}
