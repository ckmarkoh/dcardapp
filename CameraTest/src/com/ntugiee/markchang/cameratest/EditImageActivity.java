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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.ntugiee.markchang.cameratest.MyCustomPanel;

public class EditImageActivity extends Activity {
    private int x=0;
    private int y=0;

	private Button b1;
	private Button b2;
	private Button b3;
	private Button b4;

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
    private AlertDialog textEditDialog;
	private EditText drawEditText;
    private int pos_x;
    private int pos_y;

    private View colorPickerView;

    
    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editimage);
        b1 = (Button)findViewById(R.id.EditButton1);
        b2 = (Button)findViewById(R.id.EditButton2);
        b3 = (Button)findViewById(R.id.EditButton3);
        b4 = (Button)findViewById(R.id.EditButton4);

        
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String encodedString = bundle.getString("img");

		panel_state=PANEL_STATE_NONE;

        mypanelview = (MyCustomPanel)findViewById(R.id.ivEdit);

        byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);

        Display display = getWindowManager().getDefaultDisplay();
        mypanelview.bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        
        
        
        LayoutInflater inflater = LayoutInflater.from(EditImageActivity.this);
        colorPickerView = inflater.inflate(R.layout.colorpicker,null);
        
        AlertDialog.Builder builder=new AlertDialog.Builder(EditImageActivity.this)
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
		   
        drawEditText = new EditText(EditImageActivity.this);

        AlertDialog.Builder builder2=new AlertDialog.Builder(EditImageActivity.this)
   		.setTitle("加入文字")
   		.setMessage("請輸入你想要加入的文字：")
   		.setView(drawEditText)
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    // do something when the button is clicked
		    public void onClick(DialogInterface arg0, int arg1) {
		    		text_content=drawEditText.getText().toString();  
		    		mypanelview.addText( pos_x ,pos_y , Color.rgb(color_r, color_g, color_b),text_content);
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
        textEditDialog=builder2.create();
        textEditDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
        	public void onDismiss(DialogInterface dialog){
        		mypanelview.invalidate();
        	}
        });
        colorPreView = (View) colorPickerView.findViewById(R.id.ColorPreView);
        //Seekbars
        redSeekBar = (SeekBar) colorPickerView.findViewById(R.id.SeekBarRed);
        greenSeekBar = (SeekBar) colorPickerView.findViewById(R.id.SeekBarGreen);
        blueSeekBar = (SeekBar) colorPickerView.findViewById(R.id.SeekBarBlue);
        //EditTexts
        redEditText = (TextView) colorPickerView.findViewById(R.id.EditTextRed);
        greenEditText = (TextView) colorPickerView.findViewById(R.id.EditTextGreen);
        blueEditText = (TextView) colorPickerView.findViewById(R.id.EditTextBlue);
        redSeekBar.setOnSeekBarChangeListener(OnSeekBarProgress);
        greenSeekBar.setOnSeekBarChangeListener(OnSeekBarProgress);
        blueSeekBar.setOnSeekBarChangeListener(OnSeekBarProgress);
        colorChooseDialog=builder.create();
        
        
		b4.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        			colorChooseDialog.show();
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
        		panel_state=PANEL_STATE_TEXT;


        		
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
    OnSeekBarChangeListener OnSeekBarProgress =
        	new OnSeekBarChangeListener() {

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

}