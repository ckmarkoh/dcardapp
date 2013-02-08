package com.ntugiee.markchang.cameratest;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import com.ntugiee.markchang.cameratest.MyPair;
public class MyCustomPanel extends View {
    private Paint paint;// = new Paint();
    public ArrayList<Long> pathData = new ArrayList<Long>();
    public ArrayList<MyPair<String,Long> > textData = new ArrayList<MyPair<String,Long> >();

    public Bitmap bitmap;
    public Bitmap wallPaperBitmap;
    public Canvas bitmapCanvas;
    

    
    public MyCustomPanel (Context context, AttributeSet attrs){//,int wallpaperWidth,int wallpaperHeight) {
        super(context,attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth((float) 3.0);
        paint.setStyle(Style.STROKE); 

        
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       wallPaperBitmap = Bitmap.createBitmap( getWidth (), getHeight(), Bitmap.Config.ARGB_8888);
       
       //Log.d("size",String.valueOf(getWidth ())+" "+String.valueOf(getHeight ()) );
       bitmapCanvas = new Canvas(wallPaperBitmap);    
   }
    
    @Override
    public void onDraw(Canvas canvas) {
    	
    	canvas.drawBitmap(bitmap, 0, 0, null); 
    	bitmapCanvas.drawBitmap(bitmap, 0, 0, null); 

    	
    	Path temp_path=new Path();
    	int i=0;
        paint.setStyle(Style.STROKE); 
        
        while(i<pathData.size()){
        	//Path temp_path=PathDate.get(i);
        	long positionXY = pathData.get(i);
			if(positionXY==9999){
				i++;
            	positionXY = pathData.get(i);
            	//int positionX1=(int) (positionXY1/10000);
            	//int positionY1=(int) (positionXY1%10000);
            	temp_path.moveTo((int)(positionXY/10000), (int) (positionXY%10000));
            	temp_path.lineTo((int)(positionXY/10000), (int) (positionXY%10000));
            	i++;
			}
			else{
				positionXY = pathData.get(i);
        	//int positionX=(int) (positionXY/10000);
        	//int positionY=(int) (positionXY%10000);
				temp_path.lineTo((int)(positionXY/10000), (int) (positionXY%10000));
			//Log.d("positionX",String.valueOf(positionX));
			//Log.d("positionY",String.valueOf(positionY));
			//Log.d("path_start",String.valueOf(i));
				i++;
			}
        }
    	//canvas.drawBitmap(bitmap, 0, 0, null); 
    	canvas.drawPath(temp_path, paint);
    	
    	//bitmapCanvas.drawBitmap(bitmap, 0, 0, null); 
    	bitmapCanvas.drawPath(temp_path, paint);
    	
    	
    	i=0;
    	paint.setTextSize(25);
        paint.setStyle(Style.FILL); 
        while(i<textData.size()){
        	//Path temp_path=PathDate.get(i);
        	MyPair<String,Long> mypair = textData.get(i);
        	long positionXY =  mypair.getRight();
        	        	/* 建立文字訊息 */
        	/* drawText (String text, float x, float y, Paint paint)*/
        	canvas.drawText(mypair.getLeft(),(positionXY/10000),(positionXY%10000),paint);
        	bitmapCanvas.drawText(mypair.getLeft(),(positionXY/10000),(positionXY%10000),paint);
        	
			//Log.d("positionX",String.valueOf(positionX));
			//Log.d("positionY",String.valueOf(positionY));
			//Log.d("path_start",String.valueOf(i));
        	
			i++;
        }

    }


}