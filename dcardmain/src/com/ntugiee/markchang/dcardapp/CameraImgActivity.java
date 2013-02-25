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
import org.json.JSONException;
import org.json.JSONObject;

import com.ntugiee.markchang.dcardapp.util.MyCustomPanel;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class CameraImgActivity extends Activity {
	private Uri outputFileUri;
	private Button nextButton;

	private Button drawButton;
	private Button clearButton;
	private Button hourGlasslButton;
	private Button textBut;
	private Button colorBut;

	private ImageView ivTest;
	
	private Global_Setting global_setting;

    //private final static int PANEL_STATE_NONE=0;
    private final static int PANEL_STATE_DRAW=1;
    private final static int PANEL_STATE_TEXT=2;
    private static int panel_state=PANEL_STATE_DRAW;

    private MyCustomPanel mypanelview;
    private OnTouchListener panel_on_touch;
    private String timeout=null;
    private int x=0;
    private int y=0;
    
    
    private SeekBar setTimeOutBar;
    private TextView setTimeOutValue;
    private TextView timeOutValue;
	
    private AlertDialog timeoutDialog;
    private View timeoutDialogView;

    
    private int color_r=0;
    private int color_g=0;
    private int color_b=0;
	private SeekBar redSeekBar;
	private SeekBar greenSeekBar;
	private SeekBar blueSeekBar;
	private TextView redEditText;
	private TextView greenEditText;
	private TextView blueEditText;
	private View colorPreView;
	
	private int redProgress = 0;
	private int greenProgress = 0;
	private int  blueProgress = 0;
  
    
    private AlertDialog colorChooseDialog;
    private View colorPickerView;
    private OnSeekBarChangeListener OnSeekBarProgress;
    
//    private String text_content;
    private AlertDialog textEditDialog;
	private EditText drawEditText;
    private int pos_x;
    private int pos_y;

    
    private AlertDialog clearDrawDialog;

    
    private DialogInterface.OnDismissListener dismissInvalidate;
	//private EditText filename; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_img);
		
        global_setting = ((Global_Setting)getApplicationContext());
		nextButton = (Button)findViewById(R.id.CINextButton);
		drawButton = (Button)findViewById(R.id.CIDrawButton);
		hourGlasslButton = (Button)findViewById(R.id.CIHourGlasslButton);
		colorBut = (Button)findViewById(R.id.CIColorButton);

		
		
		clearButton = (Button)findViewById(R.id.CICancelButton);
		panel_state=PANEL_STATE_DRAW;
        mypanelview = (MyCustomPanel)findViewById(R.id.ivTest);
        mypanelview.bitmap=global_setting.bitmap;

        
        LayoutInflater inflater = LayoutInflater.from(CameraImgActivity.this);
        timeoutDialogView = inflater.inflate(R.layout.image_timeout,null);
        
        setTimeOutBar = (SeekBar)timeoutDialogView.findViewById(R.id.SetTimeOutBar);  
        setTimeOutValue = (TextView)timeoutDialogView.findViewById(R.id.SetTimeOutView);  
        timeOutValue =  (TextView)findViewById(R.id.CITextView);  

        
        
        AlertDialog.Builder timeoutBuilder=new AlertDialog.Builder(CameraImgActivity.this)
	      .setMessage("請選擇圖片消逝的時間")
	      .setView(timeoutDialogView)
	      .setPositiveButton("確定" ,
              new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
  	      	    		 timeOutValue.setText(timeout+"秒"); 
                  }   
              })  
       .setNegativeButton("取消",                    
               new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
              }   
       }) ;
        timeoutDialog=timeoutBuilder.create();
        
        setTimeOutBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){  
        	   public void onProgressChanged(SeekBar seekBar, int progress,  
        	     boolean fromUser) {  
        		   timeout=String.valueOf(progress+1);
        		   setTimeOutValue.setText(timeout+"秒"); 
        	   }  
        	   public void onStartTrackingTouch(SeekBar seekBar) {  
        		   
        	   }  
        	   public void onStopTrackingTouch(SeekBar seekBar) {  

        	   }  
        });  	
        
        
        
        
        colorPickerView = inflater.inflate(R.layout.colorpicker,null);
        
        AlertDialog.Builder colorChooseBuilder=new AlertDialog.Builder(CameraImgActivity.this)
	      .setMessage("請選擇顏色")
	      .setView(colorPickerView)
	      .setPositiveButton("確定" ,
              new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                    	color_r=redProgress;
	                    	color_g=greenProgress;
	                    	color_b=blueProgress;
	                    }
              })  
       .setNegativeButton("取消",                    
               new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
              }   
       }) ;
        colorChooseDialog=colorChooseBuilder.create();

        colorPreView = (View) colorPickerView.findViewById(R.id.ColorPreView);
        //Seekbars
        redSeekBar = (SeekBar) colorPickerView.findViewById(R.id.SeekBarRed);
        greenSeekBar = (SeekBar) colorPickerView.findViewById(R.id.SeekBarGreen);
        blueSeekBar = (SeekBar) colorPickerView.findViewById(R.id.SeekBarBlue);
        //EditTexts
        redEditText = (TextView) colorPickerView.findViewById(R.id.EditTextRed);
        greenEditText = (TextView) colorPickerView.findViewById(R.id.EditTextGreen);
        blueEditText = (TextView) colorPickerView.findViewById(R.id.EditTextBlue);
        OnSeekBarProgress =new OnSeekBarChangeListener() {
            	public void onProgressChanged(SeekBar s, int progress, boolean touch){
                	if(touch){
                	if(s == redSeekBar){
	            		redProgress = progress;
	            		redEditText.setText(Integer.toString(redProgress));
                	}
                	if(s == greenSeekBar ){
	            		greenProgress = progress;
	            		greenEditText.setText(Integer.toString(greenProgress));
                	}
                	if(s == blueSeekBar ){
	            		blueProgress = progress;
	            		blueEditText.setText(Integer.toString(blueProgress));
                	}
	                	int color = Color.rgb(redProgress, greenProgress, blueProgress);
	                	colorPreView.setBackgroundColor(color);
                	}
            	}
            	public void onStartTrackingTouch(SeekBar s){
            	}

            	public void onStopTrackingTouch(SeekBar s){
            	}
            };
        redSeekBar.setOnSeekBarChangeListener(OnSeekBarProgress);
        greenSeekBar.setOnSeekBarChangeListener(OnSeekBarProgress);
        blueSeekBar.setOnSeekBarChangeListener(OnSeekBarProgress);
        colorBut.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        			colorChooseDialog.show();
		        }
        }); 
        drawEditText = new EditText(CameraImgActivity.this);

        
        dismissInvalidate=new DialogInterface.OnDismissListener(){
        	public void onDismiss(DialogInterface dialog){
        		Log.d("dialog","dismiss");
        		mypanelview.invalidate();
        	}
        };
        AlertDialog.Builder drawTextBuilder=new AlertDialog.Builder(CameraImgActivity.this)
   		.setTitle("加入文字")
   		.setMessage("請輸入你想要加入的文字：")
   		.setView(drawEditText)
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    // do something when the button is clicked
		    public void onClick(DialogInterface arg0, int arg1) {
//		    		text_content=drawEditText.getText().toString();  
		    		mypanelview.addText( pos_x ,pos_y , Color.rgb(color_r, color_g, color_b),drawEditText.getText().toString());
		    		drawEditText.setText("");
		    		//text_content=editText.getText().toString();  
		    		}
	        	}
		    )
   	    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		          // do something when the button is clicked
		    public void onClick(DialogInterface arg0, int arg1) {
		    	//...
		     	}
		    });
        textEditDialog=drawTextBuilder.create();
        textEditDialog.setOnDismissListener(dismissInvalidate);

        AlertDialog.Builder clearDrawBuilder=new AlertDialog.Builder(CameraImgActivity.this)
        .setMessage("你確定要清掉所有筆跡？")
        .setPositiveButton("是" ,
                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                    	mypanelview.drawData.clear();
	                    }   
                })  
         .setNegativeButton("否",                    
                 new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                }   
         }) ;
        clearDrawDialog=clearDrawBuilder.create();
        clearDrawDialog.setOnDismissListener(dismissInvalidate);

		clearButton.setOnClickListener( new View.OnClickListener() {
			public void onClick( View v ) {  
				if(mypanelview.drawData.isEmpty()){
    				setResult( RESULT_OK );
    				finish();
				}
				else{
					clearDrawDialog.show();		
				}
			}
		});
		
		
		drawButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		if(panel_state==PANEL_STATE_DRAW){
        			panel_state=PANEL_STATE_TEXT;
        			drawButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.keyboard_button_x));
                }
        		else if(panel_state==PANEL_STATE_TEXT){
        			drawButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.pencil_button_x));
            		panel_state=PANEL_STATE_DRAW;
            		
        		}
        		else{
        			drawButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.pencil_button_x));
            		panel_state=PANEL_STATE_DRAW;

        		}
        	}
        });   
		hourGlasslButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        			timeoutDialog.show();
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
        	final Intent intent = new Intent(CameraImgActivity.this, CameraFriendActivity.class);
        	String ba1=encode_bitmap();
			intent.putExtra("img", ba1);
			intent.putExtra("timeout", timeout);
			
			startActivity(intent);
			setResult( RESULT_OK );
			finish();
    		}
	    });	
	    
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
                        mypanelview.addStart(  (int) event.getX(), (int)event.getY(),Color.rgb(color_r, color_g, color_b));
                        //x =(int) event.getX();
                        //y = (int)event.getY();
                        //positionXY=store_coordinate((int) event.getX(),(int)event.getY());
                        mypanelview.addDot(  (int) event.getX(), (int)event.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mypanelview.addDot(  (int) event.getX(), (int)event.getY());
                        mypanelview.invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        mypanelview.addEnd(  (int) event.getX(), (int)event.getY());
                        mypanelview.invalidate();
                        break;
                    }
                    break;
            	case PANEL_STATE_TEXT:
                    switch(event.getAction())
                    {
                    case MotionEvent.ACTION_DOWN:
                    	pos_x=(int) event.getX();
                    	pos_y=(int)event.getY();
                    	textEditDialog.show();
	               		//.show();
                        break;
                    case MotionEvent.ACTION_UP:
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

	}
	
	public String encode_bitmap(){
		
	    float width = (float) mypanelview.wallPaperBitmap.getWidth();
	    float height = (float) mypanelview.wallPaperBitmap.getHeight();
	    int newWidth = 200;
	    int newHeight = (int) ( 200*(height / width)) ;
	    // calculate the scale - in this case = 0.4f
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // createa matrix for the manipulation
	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);
	    Bitmap resizedBitmap = Bitmap.createBitmap(mypanelview.wallPaperBitmap, 0, 0,
	    		(int) width, (int) height, matrix, true);
	    mypanelview.wallPaperBitmap=resizedBitmap;
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
   		mypanelview.wallPaperBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
   		byte [] ba = bao.toByteArray();
   		String ba1=Base64.encodeToString(ba, Base64.DEFAULT);
		return ba1;
	}
	
    private long store_coordinate(int x, int y){
    	
    	return (long) (x*10000+y);
    }
    @Override
    public void onPause(){
		super.onPause();

    }
    @Override
    public void onResume(){
		super.onResume();
		   timeout="5";
		   setTimeOutValue.setText(timeout+"秒"); 
		   timeOutValue.setText(timeout+"秒"); 

    }
     

}
