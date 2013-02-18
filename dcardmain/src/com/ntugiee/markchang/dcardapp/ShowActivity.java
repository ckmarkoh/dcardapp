package com.ntugiee.markchang.dcardapp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowActivity  extends Activity {
    private TextView count_down;
    //private TextView show_message;
    private String message;
   // private Timer timer =null;
    //private Handler handler = new Handler();  
    
    private CountDownTimer cdtimer;
    
    protected static final int DOWNLOAD_SUCCESS = 0x00000001;
    protected static final int DOWNLOAD_ERROR = 0x00000002;
    protected static final int UPDATE_STATUS = 0x00000003;

	// Progress Dialog
	private ProgressDialog pDialog;
	private Bitmap bitmap=null;
	ImageView my_image;
	// Progress dialog type (0 - for Horizontal progress bar)
	public static final int progress_bar_type = 0; 
	
	// File url to download
	//private static String file_url = "http://r444b.ee.ntu.edu.tw/~markchang/green_campus.jpg";
	private Handler mHandler;

	private ImageView ivTest;
	public String timeout_str;
    
    
    public Bitmap wallPaperBitmap;
    public Canvas bitmapCanvas;
    
    private int this_height;
    private int this_width;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.show);
		ivTest = (ImageView)findViewById(R.id.ShowMsgView);

        count_down=(TextView) this.findViewById(R.id.CountDownView);
        //show_message=(TextView) this.findViewById(R.id.ShowMsgView);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        message = bundle.getString("message");
        //show_message.setText(message);
        timeout_str=bundle.getString("timeout");
        count_down.setText(timeout_str+"s");
        int timeout_int=Integer.parseInt(timeout_str)*1000;
        //  Global_Setting.userid_text.setText(Global_Setting.userid);
       // Timer CountdownTimer = new Timer(true);
        cdtimer=new CountDownTimer(timeout_int, 1000) {
            public void onTick(long millisUntilFinished) {
            	count_down.setText(millisUntilFinished / 1000+"s");
            }
            public void onFinish() {
				setResult( RESULT_OK );
				finish();
            }
         };//.start();
		  mHandler = new Handler() {
              @Override
              public void handleMessage(Message msg)
              {
                  switch (msg.what)
                  {
         /*         case UPDATE_STATUS:
                	  pDialog.setProgress((Integer) msg.obj);
                	  break;
                  // 顯示網路上抓取的資料*/
                  case DOWNLOAD_ERROR:
                	  Toast.makeText(ShowActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                	  break;
                  case DOWNLOAD_SUCCESS:
                     String result = (String) msg.obj;
                     if (result != null){
                   	  //message=result;
                        // 印出網路回傳的文字
             			dismissDialog(progress_bar_type);
                        //Toast.makeText(ShowActivity.this, result, Toast.LENGTH_LONG).show();
                        Log.d("result",result);
     		            //File root = android.os.Environment.getExternalStorageDirectory();
     		            //String filepath=root.getAbsolutePath() + "/mnt/sdcard/"+message+".jpg";
    	                //Bitmap bitmap= BitmapFactory.decodeFile(filepath);                        
                       /* wallPaperBitmap = Bitmap.createBitmap( this_width, this_height, Bitmap.Config.ARGB_8888);
                        bitmapCanvas = new Canvas(wallPaperBitmap);    
                    	Rect bitmap_size= new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
                    	Rect canvas_size= new Rect(0,0,this_width,this_height);
                    	bitmapCanvas.drawBitmap(bitmap, bitmap_size, canvas_size, null);*/ 
    	        	    ivTest.setImageBitmap(bitmap);
    		        	//File filed = new File(filepath);
    			        //boolean deleted = filed.delete();
                        cdtimer.start();
                     }
                     break;
                  }
              }
           };

         
           
		   showDialog(progress_bar_type);
           Thread t = new Thread(new DownloadImgRunnable(mHandler,"http://r444b.ee.ntu.edu.tw/dctest/img/"+message+".jpg"));
           t.start();			

	}
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type:
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Downloading file. Please wait...");
			//pDialog.setIndeterminate(false);
			//pDialog.setMax(100);
			//pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			//pDialog.setCancelable(true);
			pDialog.show();
			return pDialog;
		default:
			return null;
		}
	}
	
	
	
	class DownloadImgRunnable implements Runnable {
	      //String strTxt = null;
	      Handler handler= null;
		   private String uriAPI;// = "http://r444b.ee.ntu.edu.tw/~markchang/dctest/index.php/user/login";
		   //private String msg;
	      // 建構子，設定要傳的字串
	      public DownloadImgRunnable(Handler hd ,String url){//,String m)  {
	           handler = hd;
			   uriAPI=url;
			   //msg = m;
	      }
	      @Override
	      public void run() {	
    //public static Bitmap getBitmap(String url) {
			       // Bitmap bm = null;
			        try {
			            URL aURL = new URL(uriAPI);
			            URLConnection conn = aURL.openConnection();
			            //conn.connect();
			            InputStream is = conn.getInputStream();
			            BufferedInputStream bis = new BufferedInputStream(is);
			            bitmap = BitmapFactory.decodeStream(new FlushedInputStream(is));
			            bis.close();
			            is.close();
				        handler.obtainMessage(DOWNLOAD_SUCCESS, "download complete").sendToTarget();
			        } catch (Exception e) {
				        handler.obtainMessage(DOWNLOAD_ERROR, e.getMessage().toString() ).sendToTarget();

//			            e.printStackTrace();
			        } 
			       // return bm;
			    }
	
		    class FlushedInputStream extends FilterInputStream {
		        public FlushedInputStream(InputStream inputStream) {
		            super(inputStream);
		        }
		        @Override
		        public long skip(long n) throws IOException {
		            long totalBytesSkipped = 0L;
		            while (totalBytesSkipped < n) {
		                long bytesSkipped = in.skip(n - totalBytesSkipped);
		                if (bytesSkipped == 0L) {
		                    int b = read();
		                    if (b < 0) {
		                        break; // we reached EOF
		                    } else {
		                        bytesSkipped = 1; // we read one byte
		                    }
		                }
		                totalBytesSkipped += bytesSkipped;
		            }
		            return totalBytesSkipped;
		        }
		    }
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
	       this_height=ivTest.getHeight();
	       this_width=ivTest.getWidth();
	       Log.d("dimenstion","height:"+this_height+" width:"+this_width);	       
	}

   
}
