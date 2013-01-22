package com.ntugiee.markchang.msg;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class ShowActivity  extends Activity {
    private TextView count_down;
    private TextView show_message;
    private String message;
   // private Timer timer =null;
    private Handler handler = new Handler();  

    public String timeout_str;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show);
        count_down=(TextView) this.findViewById(R.id.CountDownView);
        show_message=(TextView) this.findViewById(R.id.ShowMsgView);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        message = bundle.getString("message");
        show_message.setText(message);
        timeout_str=bundle.getString("timeout");
        int timeout_int=Integer.parseInt(timeout_str)*1000;
        //  username_text.setText(username);
       // Timer CountdownTimer = new Timer(true);
        new CountDownTimer(timeout_int, 1000) {
            public void onTick(long millisUntilFinished) {
            	count_down.setText("seconds remaining: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
				setResult( RESULT_OK );
				finish();
            }
         }.start();
        
	}
    
    @Override
    protected void onDestroy ( ) {
	    super.onDestroy( );
    }
}
