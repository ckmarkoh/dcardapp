package com.ntugiee.markchang.cameratest;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ntugiee.markchang.cameratest.MyCustomPanel;

public class EditImageActivity extends Activity {
    private int x=0;
    private int y=0;

	private Button b1;
	private Button b2;
	private Button b3;

    //private MyCustomPanel ivEdit;
    //private Bitmap bitmap;
    private OnTouchListener panel_on_touch;
    //private ArrayList<Long> pathData = new ArrayList<Long>();
    //private ArrayList<Path> PathDate = new ArrayList<Path>();
    
    private MyCustomPanel mypanelview;
    //private Bitmap wallPaperBitmap;
    //private Canvas bitmapCanvas;
    //private Path path;
    
    private final static int PANEL_STATE_NONE=0;
    private final static int PANEL_STATE_DRAW=1;
    private final static int PANEL_STATE_TEXT=2;
    //private final static int PANEL_STATE_SEND=3;

    private int panel_state;
    
    private String text_content;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editimage);
        b1 = (Button)findViewById(R.id.EditButton1);
        b2 = (Button)findViewById(R.id.EditButton2);
        b3 = (Button)findViewById(R.id.EditButton3);
        
        
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String encodedString = bundle.getString("img");

		panel_state=PANEL_STATE_NONE;

        mypanelview = (MyCustomPanel)findViewById(R.id.ivEdit);

        byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);

        Display display = getWindowManager().getDefaultDisplay();
        mypanelview.bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        
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
            	case PANEL_STATE_TEXT:
                    switch(event.getAction())
                    {
                    case MotionEvent.ACTION_DOWN:
                    	
                    	MyPair<String,Long> mypair= 
                    		new MyPair<String,Long>(text_content,store_coordinate((int) event.getX(),(int)event.getY()));
                    		mypanelview.textData.add(mypair);
                    		v.invalidate();
                        break;
                    } 		
                    break;
            	default:
            		break;
            	}
                //Choose which motion action has been performed
                return true;
            }
        };
        mypanelview.setOnTouchListener(panel_on_touch);
        
     /*   b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {    
				Log.d("click","click");
			}
		});*/
       
        b1.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		panel_state=PANEL_STATE_DRAW;
        	}
        });   
        
        b2.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
     		   final EditText editText = new EditText(EditImageActivity.this);
  		        new AlertDialog.Builder(EditImageActivity.this)
        		.setTitle("加入文字")
        		.setMessage("請輸入你想要加入的文字：")
        		.setView(editText)
  		        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        		    // do something when the button is clicked
        		    public void onClick(DialogInterface arg0, int arg1) {
        		    	text_content=editText.getText().toString();
                		panel_state=PANEL_STATE_TEXT;
        		     	}
        		    })
        	    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        		          // do something when the button is clicked
        		    public void onClick(DialogInterface arg0, int arg1) {
        		    	//...
        		     	}
        		    })
        		.show();
        		
        	}
        });
        
        b3.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
       		ByteArrayOutputStream bao = new ByteArrayOutputStream();
       		mypanelview.wallPaperBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
       		byte [] ba = bao.toByteArray();
       		String ba1=Base64.encodeToString(ba, Base64.DEFAULT);
			HttpPost request = new HttpPost("http://r444b.ee.ntu.edu.tw/upload_file_test/test_file.php");
	//		Toast.makeText(PhptestActivity.this, Global_Setting.site_url+"login", Toast.LENGTH_LONG).show();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("image",ba1));

			try {
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				HttpResponse response = new DefaultHttpClient().execute(request);
				if(response.getStatusLine().getStatusCode() == 200){
					String raw_result = EntityUtils.toString(response.getEntity());
					Log.d("success",raw_result);

					//String result = EntityUtils.toString(response.getEntity());
					//Toast.makeText(PhptestActivity.this, result, Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Toast.makeText(EditImageActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
              
        	}
        });   
        
    }
    
    public long store_coordinate(int x, int y){
    	
    	return (long) (x*10000+y);
    }

}