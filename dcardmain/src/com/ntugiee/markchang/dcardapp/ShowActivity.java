package com.ntugiee.markchang.dcardapp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
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
    
    protected static final int DOWNLOAD_DATA = 0x00000001;
    protected static final int UPDATE_STATUS = 0x00000002;
	// Progress Dialog
	private ProgressDialog pDialog;
	ImageView my_image;
	// Progress dialog type (0 - for Horizontal progress bar)
	public static final int progress_bar_type = 0; 
	
	// File url to download
	//private static String file_url = "http://r444b.ee.ntu.edu.tw/~markchang/green_campus.jpg";
	private Handler mHandler;

	private ImageView ivTest;

    
    public String timeout_str;
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
                  case UPDATE_STATUS:
                	  pDialog.setProgress((Integer) msg.obj);
                	  break;
                  // 顯示網路上抓取的資料
                  case DOWNLOAD_DATA:
                     String result = null;
                     if (msg.obj instanceof String){
                        result = (String) msg.obj;
                     }
                     if (result != null){
                   	  //message=result;
                        // 印出網路回傳的文字
             			dismissDialog(progress_bar_type);
                        //Toast.makeText(ShowActivity.this, result, Toast.LENGTH_LONG).show();
                        Log.d("result",result);
     		           File root = android.os.Environment.getExternalStorageDirectory();
     		           String filepath=root.getAbsolutePath() + "/mnt/sdcard/"+message+".jpg";
    	                Bitmap bitmap= BitmapFactory.decodeFile(filepath);
    	        	    ivTest.setImageBitmap(bitmap);
    		        	File filed = new File(filepath);
    			        boolean deleted = filed.delete();
                        cdtimer.start();
                     }
                     break;
                  }
              }
           };
           
		   showDialog(progress_bar_type);
           Thread t = new Thread(new sendPostRunnable(mHandler,"http://r444b.ee.ntu.edu.tw/dctest/img/"+message+".jpg"));
           t.start();			

	}
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type:
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Downloading file. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCancelable(true);
			pDialog.show();
			return pDialog;
		default:
			return null;
		}
	}
	class sendPostRunnable implements Runnable {
		      //String strTxt = null;
		      Handler handler= null;
			   private String uriAPI;// = "http://r444b.ee.ntu.edu.tw/~markchang/dctest/index.php/user/login";
			   //private String msg;
		      // 建構子，設定要傳的字串
		      public sendPostRunnable(Handler hd ,String url){//,String m)  {
		           handler = hd;
				   uriAPI=url;
				   //msg = m;
		      }
		      @Override
		      public void run() {
		    	  int count;
			       try {
		           File root = android.os.Environment.getExternalStorageDirectory();               
		           File dir = new File (root.getAbsolutePath() + "/mnt/sdcard");	               
		           if(dir.exists()==false) {
		                    dir.mkdirs();
		           }
		           URL url = new URL(uriAPI); //you can write here any link
		           File file = new File(dir, message+".jpg");
	               //File file = new File(dir, filename+);
		            URLConnection conection = url.openConnection();
		            conection.connect();
		            // getting file length
		            int lenghtOfFile = conection.getContentLength();
		            // input stream to read file - with 8k buffer
		            InputStream input = new BufferedInputStream(url.openStream(), 8192);
    		            // Output stream to write file
		            FileOutputStream output = new FileOutputStream(file);
		            byte data[] = new byte[1024];
		            long total = 0;
		            while ((count = input.read(data)) != -1) {
		                total += count;
		                // publishing the progress....
		                // After this onProgressUpdate will be called
		                //publishProgress(""+(int)((total*100)/lenghtOfFile));
		                //Message myMessage = new Message();
		                //myMessage.obj = "progress";
		                //handler.sendMessage(myMessage);
					    handler.obtainMessage(UPDATE_STATUS, (int)((total*100)/lenghtOfFile)).sendToTarget();
		                // writing data to file
		                output.write(data, 0, count);
		            }
		            // flushing output
		            output.flush();
		            // closing streams
		            output.close();
		            input.close();
		            
			        handler.obtainMessage(DOWNLOAD_DATA, "download complete").sendToTarget();
	                //IV.setImageBitmap(bMap);
	        	    //ivTest.setImageBitmap(bitmap);

			        } catch (Exception e) {
			        	Log.e("Error: ", e.getMessage());
			        
			        }

		      }
	}
	

    
    
    
    @Override
    protected void onDestroy ( ) {
	    super.onDestroy( );
    }
}
