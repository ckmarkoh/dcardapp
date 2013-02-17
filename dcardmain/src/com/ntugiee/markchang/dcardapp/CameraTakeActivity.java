package com.ntugiee.markchang.dcardapp;

import java.io.ByteArrayOutputStream;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;

public class CameraTakeActivity extends Activity {


	private Bitmap bitmap;

	
	//private EditText filename; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_take);
	    
		final Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//利用intent去開啟android本身的照相介面
		startActivityForResult(intent, 0); 

	}
	
	public String encode_bitmap(){
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
		byte [] ba = bao.toByteArray();
		String ba1=Base64.encodeToString(ba, Base64.DEFAULT);
		return ba1;
	}
	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	  super.onActivityResult(requestCode, resultCode, data);
	  
	  if (resultCode == RESULT_OK) {
		    bitmap=(Bitmap) data.getExtras().getParcelable("data");
    		String ba1=encode_bitmap();
        	final Intent intent = new Intent();
        	intent.setClass(CameraTakeActivity.this, CameraImgActivity.class);
        	Bundle bundle = new Bundle();
        	bundle.putString("img", ba1);
        	intent.putExtras(bundle);
            startActivity(intent);      
			setResult( RESULT_OK );
			finish();
	  }
	 } 
	
}
