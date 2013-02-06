package com.ntugiee.markchang.cameratest;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditImageActivity extends Activity {
	
	private Button b1;
	private Button b2;
	private Button b3;

	private TextView tv;
	private ImageView ivEdit;
	private Bitmap bitmap;
	private Canvas canvas;
	private Paint paint ;
	private int fieldImgXY[] = new int[2];

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editimage);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String encodedString = bundle.getString("img");

        ivEdit = (ImageView)findViewById(R.id.ivEdit);
	    byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
	    bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
	    
	    ivEdit.getLocationInWindow(fieldImgXY);

	    ivEdit.setImageBitmap(bitmap);
	    //Region region = new Region(ivEdit.getLeft(), ivEdit.getTop(), ivEdit.getRight(), ivEdit.getBottom());
	    
	    Log.d("position",String.valueOf(ivEdit.getLeft())+" "+String.valueOf(ivEdit.getTop())+ " "
	    		+String.valueOf(ivEdit.getHeight())+" "+String.valueOf(ivEdit.getWidth())
	    		);
	    Log.d( "fieldImage lockation on screen: ", 
	    		String.valueOf(fieldImgXY[0])+" "+String.valueOf(fieldImgXY[1]));    
	    
        b1 = (Button)findViewById(R.id.EditButton1);
        b2 = (Button)findViewById(R.id.EditButton2);
        b3 = (Button)findViewById(R.id.EditButton3);

        canvas = new Canvas();
        canvas.drawColor(Color.TRANSPARENT);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        
        
        b1.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
                    
        	}
        } );     
        
        ivEdit.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Choose which motion action has been performed
                switch(event.getAction())
                {
                case MotionEvent.ACTION_DOWN:
                    //Get X, Y coordinates from the ImageView
                    int X = (int) event.getX();
                    int Y = (int) event.getY();
            	    Log.d("position",String.valueOf(X)+" "+String.valueOf(Y));
            	    
            	    //canvas.drawBitmap(bitmap, X, Y, paint);
                    //ivEdit.setImageBitmap(bitmap);
                    /*float[] pts={50,50,  
                            400,50,  
                            400,600,  
                            60,600, };                      //数据  
                    canvas.drawColor(Color.BLACK);                  //白色背景  
                    paint.setStrokeWidth((float) 20.0);         //线宽  
                    canvas.drawPoints(pts,paint);                   //绘制点  */
            	    
                    //Do something
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                }
                return true;
            }
        }); 
	}
}