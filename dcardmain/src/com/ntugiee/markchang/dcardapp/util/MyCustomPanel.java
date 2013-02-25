package com.ntugiee.markchang.dcardapp.util;

import java.util.ArrayList;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
public class MyCustomPanel extends View {
    private Paint pathPaint;
   // private Paint textPaint;
    public ArrayList<Long> pathData = new ArrayList<Long>();
    public ArrayList<MyDot > drawData = new ArrayList<MyDot >();

    public Bitmap bitmap;
    public Bitmap wallPaperBitmap;
    public Canvas bitmapCanvas;
    public Path temp_path;
    private int this_height;
    private int this_width;
    private int df_size_text;
    private int df_size_dot;
    
    //private boolean path_complete=false;
    
    public MyCustomPanel (Context context, AttributeSet attrs){
        super(context,attrs);
        pathPaint = new Paint();
        pathPaint.setAntiAlias(true);
        
      //  textPaint = new Paint();
      //  textPaint.setAntiAlias(true);
        //Dotpaint.setColor(Color.BLACK);
        //Dotpaint.setStrokeWidth((float) 3.0);
        //Dotpaint.setStyle(Style.STROKE); 

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       wallPaperBitmap = Bitmap.createBitmap( getWidth (), getHeight(), Bitmap.Config.ARGB_8888);
       this_height=getHeight();
       this_width=getWidth ();
       bitmapCanvas = new Canvas(wallPaperBitmap);    
       df_size_text=(this_width+this_height)/15;
       df_size_dot=(this_width+this_height)/150;
   }
    
    
    @Override
    public void onDraw(Canvas canvas) {
    	Rect bitmap_size= new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
    	Rect wbitmap_size= new Rect(0,0,wallPaperBitmap.getWidth(),wallPaperBitmap.getHeight());
    	Rect canvas_size= new Rect(0,0,this_width,this_height);
    	
    	bitmapCanvas.drawBitmap(bitmap, bitmap_size, canvas_size, null); 

    	//temp_path=new Path();
    	
    	temp_path=null;

    	for(int i=0;i<drawData.size();i++){
    		drawData.get(i).mydraw();
    	}
    	if(temp_path!=null){
    		bitmapCanvas.drawPath(temp_path, pathPaint);
    	}
    	canvas.drawBitmap(wallPaperBitmap,wbitmap_size , canvas_size, null); 

    }
    
    public void addStart(int x, int y,int c){
    	drawData.add(new MyStart(x,y,c));
    }
    public void addDot(int x, int y){
    	drawData.add(new MyDot(x,y));
    }
    public void addEnd(int x, int y){
    	drawData.add(new MyEnd(x,y));
    }
    public void addText(int x, int y,int c,String s){
    	drawData.add(new MyText(x,y,c,s));
    }

    public class MyDot {
    	protected int x;
    	protected int y;
    	MyDot(int x,int y){
    		this.x=x;
    		this.y=y;
    	}
    	public void mydraw(){
    		temp_path.lineTo(x,y);
    		Log.d("draw","x:"+x+" y"+y);
    	}
    }
    public class MyStart extends MyCustomPanel.MyDot {
    	//private a;
    	private int c;
    	private int width;
		MyStart(int x, int y,int c) {
			super(x, y);
			this.c=c;
			this.width=df_size_dot;
		}
    	public void mydraw(){
        	temp_path=new Path();
        	//path_complete=false;
    		Log.d("draw start","x:"+x+" y"+y);
            pathPaint.setStrokeWidth((float) width);
            pathPaint.setStyle(Style.STROKE); 
            pathPaint.setColor(c);
			temp_path.moveTo(x,y);
    		super.mydraw();
    	}
    }
    public class MyEnd extends MyCustomPanel.MyDot {
    	MyEnd(int x, int y) {
			super(x, y);
		}
    	public void mydraw(){
    		super.mydraw();
    		bitmapCanvas.drawPath(temp_path, pathPaint);
        	//path_complete=true;
        	temp_path=null;
    	}
    }
    public class MyText extends MyCustomPanel.MyDot {
    	private String s;
    	private int c;
    	private int size;
    	MyText(int x, int y,int c,String s) {
			super(x, y);
			this.s=s;
			this.c=c;
			this.size=df_size_text;
			toCenter();
		}
    	public void toCenter(){
        	Rect bounds=new Rect();
        	pathPaint.setTextSize(size);
        	pathPaint.getTextBounds(s, 0, s.length(), bounds);
    		x-=bounds.centerX();
    		y-=bounds.centerY();
    	}
    	public void mydraw(){
    		//temp_path=new Path();
        	pathPaint.setTextSize(size);
            pathPaint.setStyle(Style.FILL); 
            pathPaint.setColor(c);
            bitmapCanvas.drawText(s,x,y,pathPaint);
    		Log.d("draw","text x:"+x+" y"+y);
    	}
    }
}