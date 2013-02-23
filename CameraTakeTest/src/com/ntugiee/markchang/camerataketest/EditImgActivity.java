package com.ntugiee.markchang.camerataketest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class EditImgActivity extends Activity{
	private ImageView imgView;
	private Global_Setting global_setting;

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_img);
		Log.d(EditImgActivity.this.getClass().getName(),"on_create");
	    global_setting = ((Global_Setting)getApplicationContext());
		imgView = (ImageView) this.findViewById(R.id.imgView);
		imgView.setImageBitmap(global_setting.bitmap);
	}
	@Override
	public void onStop(){
		super.onStop();
		Log.d(EditImgActivity.this.getClass().getName(),"on_stop");
		
	}
}
