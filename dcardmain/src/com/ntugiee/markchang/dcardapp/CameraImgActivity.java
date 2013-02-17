package com.ntugiee.markchang.dcardapp;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.ntugiee.markchang.dcardapp.util.MyCustomPanel;





import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraImgActivity extends Activity {
	private Uri outputFileUri;
	private Button nextButton;

	private Button drawButton;
	private Button cancelButton;

	private Bitmap bitmap;
	private ImageView ivTest;
	
	private Global_Setting global_setting;

    private final static int PANEL_STATE_NONE=0;
    private final static int PANEL_STATE_DRAW=1;
    private final static int PANEL_STATE_TEXT=2;
    private static int panel_state=PANEL_STATE_NONE;

    private MyCustomPanel mypanelview;
    private OnTouchListener panel_on_touch;
    private int x=0;
    private int y=0;

	
	//private EditText filename; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_img);
		
        global_setting = ((Global_Setting)getApplicationContext());

		//ivTest = (ImageView)findViewById(R.id.ivTest);
		//filename=(EditText)findViewById(R.id.fileEdit);
		nextButton = (Button)findViewById(R.id.CINextButton);
		//cameraButton = (Button)findViewById(R.id.CameraButton);
		drawButton = (Button)findViewById(R.id.CIDrawButton);
		cancelButton = (Button)findViewById(R.id.CICancelButton);
		
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String encodedString = bundle.getString("img");

		panel_state=PANEL_STATE_NONE;

        mypanelview = (MyCustomPanel)findViewById(R.id.ivTest);

        byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);

        Display display = getWindowManager().getDefaultDisplay();
        mypanelview.bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
		
		
		
		
		cancelButton.setOnClickListener( new View.OnClickListener() {
			public void onClick( View v ) {    
				setResult( RESULT_OK );
				finish();
			}
		});
		
		
		drawButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		panel_state=PANEL_STATE_DRAW;
       
                }
        });   
		

		
	/*	nextButton.setOnClickListener( new View.OnClickListener() {
			public void onClick( View v ) {    
            	final Intent intent = new Intent(CameraImgActivity.this, MsgChooseFriendActivity.class);
                startActivity(intent);
			}
		});*/
		
		
	    nextButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
       		String ba1=encode_bitmap();
			HttpPost request = new HttpPost("http://r444b.ee.ntu.edu.tw/dctest/index.php?/msg/insert_img");
	//		Toast.makeText(PhptestActivity.this, Global_Setting.site_url+"login", Toast.LENGTH_LONG).show();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("image",ba1));
			params.add(new BasicNameValuePair("session", global_setting.session));
			params.add(new BasicNameValuePair("userid", global_setting.userid));			
			params.add(new BasicNameValuePair("sender", global_setting.userid));
			//params.add(new BasicNameValuePair("message", msgEdit.getText().toString()));
			params.add(new BasicNameValuePair("receiver", global_setting.target_receiver));
			params.add(new BasicNameValuePair("timeout", "5"));
			
			try {
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				HttpResponse response = new DefaultHttpClient().execute(request);
				if(response.getStatusLine().getStatusCode() == 200){
					String raw_result = EntityUtils.toString(response.getEntity());
					Toast.makeText(CameraImgActivity.this, "送出成功", Toast.LENGTH_LONG).show();

					Log.d("success",raw_result);
					setResult( RESULT_OK );
					finish();
					//String result = EntityUtils.toString(response.getEntity());
					//Toast.makeText(PhptestActivity.this, result, Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Toast.makeText(CameraImgActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
              
        	}
        });
	    
	    /*cameraButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
               }
        });*/ 
        panel_on_touch= new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	//long positionXY;
            	switch (panel_state){
            	case PANEL_STATE_DRAW:
                    switch(event.getAction())
                    {
                    case MotionEvent.ACTION_DOWN:
                        //positionXY=(long) (9999);
                        mypanelview.pathData.add((long) (9999));
                        //x =(int) event.getX();
                        //y = (int)event.getY();
                        //positionXY=store_coordinate((int) event.getX(),(int)event.getY());
                        mypanelview.pathData.add(store_coordinate((int) event.getX(),(int)event.getY()));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mypanelview.pathData.add(store_coordinate((int) event.getX(),(int)event.getY()));
        				v.invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                    	v.invalidate();
                        break;
                    }
                    break;
            	/*case PANEL_STATE_TEXT:
                    switch(event.getAction())
                    {
                    case MotionEvent.ACTION_DOWN:
                    	
                    	MyPair<String,Long> mypair= 
                    		new MyPair<String,Long>(text_content,store_coordinate((int) event.getX(),(int)event.getY()));
                    		mypanelview.textData.add(mypair);
                    		v.invalidate();
                        break;
                    } 		
                    break;*/
            	default:
            		break;
            	}
                //Choose which motion action has been performed
                return true;
            }
        };
        mypanelview.setOnTouchListener(panel_on_touch);

	}
	
	public String encode_bitmap(){
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
   		mypanelview.wallPaperBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
   		byte [] ba = bao.toByteArray();
   		String ba1=Base64.encodeToString(ba, Base64.DEFAULT);
		return ba1;
	}
	
    private long store_coordinate(int x, int y){
    	
    	return (long) (x*10000+y);
    }
}
