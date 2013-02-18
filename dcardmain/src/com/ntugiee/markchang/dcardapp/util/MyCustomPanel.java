package com.ntugiee.markchang.dcardapp.util;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
public class MyCustomPanel extends View {
    private Paint paint;// = new Paint();
    public ArrayList<Long> pathData = new ArrayList<Long>();
    //public ArrayList<MyPair<String,Long> > textData = new ArrayList<MyPair<String,Long> >();

    public Bitmap bitmap;
    public Bitmap wallPaperBitmap;
    public Canvas bitmapCanvas;
    
    private int this_height;
    private int this_width;
    //Rect bitmap_size;
    //Rect canvas_size;
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
       bitmapCanvas = new Canvas(wallPaperBitmap);    
       this_height=getHeight();
       this_width=getWidth ();
      // canvas_size= new Rect(0,0,getWidth (),getHeight());
   }
  /*  public void setBitMap(Bitmap bmp){
    	bitmap=bmp;
    }*/
    @Override
    public void onDraw(Canvas canvas) {
    	//bitmap_size= new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
    	Rect bitmap_size= new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
    	Rect canvas_size= new Rect(0,0,this_width,this_height);
    	canvas.drawBitmap(bitmap,bitmap_size , canvas_size, null); 
    	bitmapCanvas.drawBitmap(bitmap, bitmap_size, canvas_size, null); 
    	
    	Path temp_path=new Path();
    	int i=0;
        paint.setStyle(Style.STROKE); 
        
        while(i<pathData.size()){
        	//Path temp_path=PathDate.get(i);
        	long positionXY = pathData.get(i);
			if(positionXY==9999){
				i++;
            	positionXY = pathData.get(i);

            	temp_path.moveTo((int)(positionXY/10000), (int) (positionXY%10000));
            	temp_path.lineTo((int)(positionXY/10000), (int) (positionXY%10000));
            	i++;
			}
			else{
				positionXY = pathData.get(i);

				temp_path.lineTo((int)(positionXY/10000), (int) (positionXY%10000));

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
        /*while(i<textData.size()){
        	//Path temp_path=PathDate.get(i);
        	MyPair<String,Long> mypair = textData.get(i);
        	long positionXY =  mypair.getRight();
        	// drawText (String text, float x, float y, Paint paint)
        	canvas.drawText(mypair.getLeft(),(positionXY/10000),(positionXY%10000),paint);
        	bitmapCanvas.drawText(mypair.getLeft(),(positionXY/10000),(positionXY%10000),paint);
        	

        	
			i++;
        }*/

    }


}