package com.ntugiee.markchang.longtouchbutton;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.Button;

public class LongTouchButton extends Button {
    public LongTouchButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
    public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
		
		
		}
		
		return false;
    }
	
}