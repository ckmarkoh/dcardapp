package com.ntugiee.markchang.cameratest;

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
import org.json.JSONObject;



import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraTestActivity extends Activity {
	private Uri outputFileUri;
	private Button uploadButton;
	private Button downloadButton;

	private Button uploadFileButton;
	private Button downloadFileButton;

	
	private Button cameraButton;
	private Button editButton;
	private Bitmap bitmap;
	private ImageView ivTest;
	//private EditText filename; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ivTest = (ImageView)findViewById(R.id.ivTest);
		//filename=(EditText)findViewById(R.id.fileEdit);
		uploadButton = (Button)findViewById(R.id.UploadButton);
		downloadButton = (Button)findViewById(R.id.DownloadButton);

		uploadFileButton = (Button)findViewById(R.id.UploadFileButton);
		downloadFileButton = (Button)findViewById(R.id.DownloadFileButton);

		
		cameraButton = (Button)findViewById(R.id.CameraButton);
		editButton = (Button)findViewById(R.id.EditButton);

	    uploadButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
    	
       		String ba1=encode_bitmap();
    		
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
				Toast.makeText(CameraTestActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
              
        	}
        });   
	    cameraButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		final Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//利用intent去開啟android本身的照相介面
        		startActivityForResult(intent, 0); 
               }
        }); 
	    
	    downloadButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		bitmap = getBitmap("http://upload.wikimedia.org/wikipedia/commons/8/89/Illu_muscle_structure.jpg");
        		ivTest.setImageBitmap(bitmap);
               }
        });
	    editButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		String ba1=encode_bitmap();
            	final Intent intent = new Intent();
            	intent.setClass(CameraTestActivity.this, EditImageActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putString("img", ba1);
            	intent.putExtras(bundle);
                startActivity(intent);               
                }
        });
	    
	    
	    downloadFileButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		String url="http://1.bp.blogspot.com/-bAswhDJvv1I/ULs1cemzETI/AAAAAAAAA4k/KnleZVVhlDo/s1600/wallpaper+desktop-nacozinhacomamalves.blogspot.com-moth_-_desktop_wallpaper.jpg";
        		String filename="greencampus";
        		DownloadFromUrl(url,filename);
               }
        });
	    
	    uploadFileButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        	    // load the origial BitMap (500 x 500 px)

        	    int width = bitmap.getWidth();
        	    int height = bitmap.getHeight();
        	    int newWidth = 240;
        	    int newHeight = 400;

        	    // calculate the scale - in this case = 0.4f
        	    float scaleWidth = ((float) newWidth) / width;
        	    float scaleHeight = ((float) newHeight) / height;

        	    // createa matrix for the manipulation
        	    Matrix matrix = new Matrix();
        	    matrix.postScale(scaleWidth, scaleHeight);
        	    matrix.postRotate(90);
        	    Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
        	                      width, height, matrix, true);

        	    bitmap=resizedBitmap;
        		//DownloadFromUrl("http://r444b.ee.ntu.edu.tw/~markchang/green_campus.jpg","greencampus");
        	    ivTest.setImageBitmap(bitmap);

        	
        	}
        });

	}
	
	public String encode_bitmap(){
		
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
		byte [] ba = bao.toByteArray();
		String ba1=Base64.encodeToString(ba, Base64.DEFAULT);
		return ba1;
		
	}
	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	  super.onActivityResult(requestCode, resultCode, data);
	  
	  if (resultCode == RESULT_OK) {
		  bitmap = (Bitmap) data.getExtras().getParcelable("data");
	//   Bitmap bmp = BitmapFactory.decodeFile(outputFileUri.getPath()); //利用BitmapFactory去取得剛剛拍照的圖像
	   ivTest.setImageBitmap(bitmap);
	  }
	 }



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		
		return true;
	}

	
    public static Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            //conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(new FlushedInputStream(is));
            bis.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return bm;
    }
	
    static class FlushedInputStream extends FilterInputStream {
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
    
	public void DownloadFromUrl(String DownloadUrl, String fileName) {

	       try {
	               File root = android.os.Environment.getExternalStorageDirectory();               

	               File dir = new File (root.getAbsolutePath() + "/mnt/sdcard");	               if(dir.exists()==false) {
	                    dir.mkdirs();
	               }

	               URL url = new URL(DownloadUrl); //you can write here any link
	               File file = new File(dir, fileName);

	               long startTime = System.currentTimeMillis();
	               Log.d("DownloadManager", "download begining");
	               Log.d("DownloadManager", "download url:" + url);
	               Log.d("DownloadManager", "downloaded file name:" + fileName);
	               /* Open a connection to that URL. */
	               URLConnection ucon = url.openConnection();
	               /*
	                * Define InputStreams to read from the URLConnection.
	                */
	               InputStream is = ucon.getInputStream();
	               BufferedInputStream bis = new BufferedInputStream(is);
	               /*
	                * Read bytes to the Buffer until there is nothing more to read(-1).
	                */
	               ByteArrayBuffer baf = new ByteArrayBuffer(5000);
	               int current = 0;
	               while ((current = bis.read()) != -1) {
	                  baf.append((byte) current);
	               }
	               /* Convert the Bytes read to a String. */
	               FileOutputStream fos = new FileOutputStream(file);
	               fos.write(baf.toByteArray());
	               fos.flush();
	               fos.close();
	               Log.d("DownloadManager", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");

	               bitmap= BitmapFactory.decodeFile(root.getAbsolutePath() + "/mnt/sdcard/"+fileName);
	               //IV.setImageBitmap(bMap);
	        	   ivTest.setImageBitmap(bitmap);
	        	   File filed = new File(root.getAbsolutePath() + "/mnt/sdcard/"+fileName);
	        	 //  boolean deleted = filed.delete();
	       } catch (IOException e) {
	           Log.d("DownloadManager", "Error: " + e);
	       }
		//return bitmap;

	    }


}
