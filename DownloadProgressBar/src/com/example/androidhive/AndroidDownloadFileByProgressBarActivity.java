package com.example.androidhive;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AndroidDownloadFileByProgressBarActivity extends Activity {

	// button to show progress dialog
	private Button btnShowProgress;
	private Button btnThreadShowProgress;
    protected static final int DOWNLOAD_DATA = 0x00000001;
    protected static final int UPDATE_STATUS = 0x00000002;

	// Progress Dialog
	private ProgressDialog pDialog;
	ImageView my_image;
	// Progress dialog type (0 - for Horizontal progress bar)
	public static final int progress_bar_type = 0; 
	
	// File url to download
	private static String file_url = "http://r444b.ee.ntu.edu.tw/~markchang/green_campus.jpg";

	
	   private Handler mHandler;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// show progress bar button
		btnShowProgress = (Button) findViewById(R.id.btnProgressBar);
		btnThreadShowProgress = (Button) findViewById(R.id.btnThreadProgressBar);

		// Image view to show image after downloading
		my_image = (ImageView) findViewById(R.id.my_image);
		/**
		 * Show Progress bar click event
		 * */
		
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
                        Toast.makeText(AndroidDownloadFileByProgressBarActivity.this, result, Toast.LENGTH_LONG).show();
                        //Log.d("result",message);
                     }
                     break;
                  }
              }
           };
           
		
		btnShowProgress.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// starting new Async Task
				new DownloadFileFromURL().execute(file_url);
			}
		});
		btnThreadShowProgress.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(progress_bar_type);
				// starting new Async Task
	            Thread t = new Thread(new sendPostRunnable(mHandler,file_url));
	            t.start();			
	         }
		});
	}

	/**
	 * Showing Dialog
	 * */
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
			   private String message;
		      // 建構子，設定要傳的字串
		      public sendPostRunnable(Handler hd ,String url)  {
		           handler = hd;
				   uriAPI=url;
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
	               String filename=UUID.randomUUID().toString();
	               File file = new File(dir, filename+".jpg");

		            URL url = new URL(uriAPI);
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
			        } catch (Exception e) {
			        	Log.e("Error: ", e.getMessage());
			        
			        }
			       handler.obtainMessage(DOWNLOAD_DATA, "download complete").sendToTarget();
		   }
	}
	

	/**
	 * Background Async Task to download file
	 * */
	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread
		 * Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type);
		}

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {
			int count;
	        try {
	        	
               File root = android.os.Environment.getExternalStorageDirectory();               

               File dir = new File (root.getAbsolutePath() + "/mnt/sdcard");	               
               if(dir.exists()==false) {
                    dir.mkdirs();
               }
               String filename=UUID.randomUUID().toString();
               File file = new File(dir, filename+".jpg");

	            URL url = new URL(f_url[0]);
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
	                publishProgress(""+(int)((total*100)/lenghtOfFile));
	                
	                // writing data to file
	                output.write(data, 0, count);
	            }

	            // flushing output
	            output.flush();
	            
	            // closing streams
	            output.close();
	            input.close();
	            
	        } catch (Exception e) {
	        	Log.e("Error: ", e.getMessage());
	        }
            //Toast.makeText(AndroidDownloadFileByProgressBarActivity.this, "download_complete", Toast.LENGTH_LONG).show();

	        return null;
		}
		
		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
       }

		/**
		 * After completing background task
		 * Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after the file was downloaded
			dismissDialog(progress_bar_type);
			
			// Displaying downloaded image into image view
			// Reading image path from sdcard
			//String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
			// setting downloaded into image view
			//my_image.setImageDrawable(Drawable.createFromPath(imagePath));
		}

	}
}