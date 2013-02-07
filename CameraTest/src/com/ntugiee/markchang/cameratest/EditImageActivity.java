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
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditImageActivity extends Activity {
    private int x=0;
    private int y=0;
    private Button b1;
    private ImageView ivEdit;
    private Bitmap bitmap;
    private OnTouchListener panel_on_touch;
    private ArrayList<Long> lngDate = new ArrayList<Long>();
    private ArrayList<Path> PathDate = new ArrayList<Path>();
    
    
    private Bitmap wallPaperBitmap;
    private Canvas bitmapCanvas;
    //private Path path;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editpanel);
        b1 = new Button(this);
        b1.setText("Button1");
        
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String encodedString = bundle.getString("img");

        ivEdit = (ImageView)findViewById(R.id.ivPanelEdit);
        byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
        bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        //ivEdit.setImageBitmap(bitmap);

        
        MyCustomPanel view = new MyCustomPanel(EditImageActivity.this);
        
        addContentView(view, new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        
        addContentView(b1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        
        
        Display display = getWindowManager().getDefaultDisplay();

        
        int wallpaperHeight = display.getHeight();
        //wallpaperWidth = wallpaperManager.getDesiredMinimumWidth();
        int wallpaperWidth = display.getWidth();
        //path_of_shape_for_WallPaperBitmap = new Path();
        
        wallPaperBitmap = Bitmap.createBitmap(wallpaperWidth, wallpaperHeight, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(wallPaperBitmap);
        
        
        
        
        
        
        

        
        panel_on_touch= new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	long positionXY;
                //Choose which motion action has been performed
                switch(event.getAction())
                {
                case MotionEvent.ACTION_DOWN:
                    positionXY=(long) (9999);
                	lngDate.add(positionXY);
                    x =(int) event.getX();
                    y = (int)event.getY();
                    positionXY=(long) (x*10000+y);
                    //if(!lngDate.contains(positionXY)){
                	lngDate.add(positionXY);
    				Log.d("x",String.valueOf(x));
    				Log.d("y",String.valueOf(y));
    				Log.d("positionXY",String.valueOf(positionXY));

                   //}
        				//path=new Path() ;
//        				path.moveTo(event.getX(), event.getY());

        				//path.lineTo(event.getX(), event.getY());
        				//path.moveTo(event.getX(), event.getY());

                    break;
                case MotionEvent.ACTION_MOVE:
                    x = (int)event.getX();
                    y = (int)event.getY();
                    positionXY=(long) (x*10000+y);
                    //if(!lngDate.contains(positionXY)){
                	lngDate.add(positionXY);
    				Log.d("x",String.valueOf(x));
    				Log.d("y",String.valueOf(y));
    				Log.d("positionXY",String.valueOf(positionXY));
                    //}
        				//path.moveTo(event.getX(), event.getY());
        				//path.lineTo(event.getX(), event.getY());
    				v.invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    //positionXY=(long) (9999);
                	//lngDate.add(positionXY);
                	//path.lineTo(event.getX(), event.getY());
                	//path.moveTo(event.getX(), event.getY());
                	//Path temp_path=path;
                	//PathDate.add(temp_path);
                	v.invalidate();
                    break;
                }
                return true;
            }
        };
        view.setOnTouchListener(panel_on_touch);
        
     /*   b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {    
				Log.d("click","click");
			}
		});*/

        b1.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
       		ByteArrayOutputStream bao = new ByteArrayOutputStream();
       		wallPaperBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
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
    private class MyCustomPanel extends View {
        private Paint paint;// = new Paint();

        public MyCustomPanel(Context context) {
            super(context);
             paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth((float) 3.0);
            paint.setStyle(Style.STROKE);  
        }
        @Override
        public void draw(Canvas canvas) {
           /* Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth((float) 3.0);
            paint.setStyle(Style.STROKE);  */
        	//long positionXY;
        	Path temp_path=new Path();

        	int i=0;
            while(i<lngDate.size()){
            	//Path temp_path=PathDate.get(i);
            	long positionXY = lngDate.get(i);

				if(positionXY==9999){
					i++;
	            	long positionXY1 = lngDate.get(i);
	            	int positionX1=(int) (positionXY1/10000);
	            	int positionY1=(int) (positionXY1%10000);
	            	temp_path.moveTo(positionX1, positionY1);
					
				}
            	positionXY = lngDate.get(i);
            	int positionX=(int) (positionXY/10000);
            	int positionY=(int) (positionXY%10000);
            	temp_path.lineTo(positionX, positionY);
				Log.d("positionX",String.valueOf(positionX));
				Log.d("positionY",String.valueOf(positionY));
				Log.d("path_start",String.valueOf(i));
				i++;
            }
        	canvas.drawBitmap(bitmap, 0, 0, null); 
        	canvas.drawPath(temp_path, paint);
        	bitmapCanvas.drawBitmap(bitmap, 0, 0, null);
        	bitmapCanvas.drawPath(temp_path, paint);
        }
    }
    

}