package com.ntugiee.markchang.cameratest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Uri outputFileUri;
	private Button uploadButton;
	private Button cameraButton;
	private Button SaveButton;

	private Bitmap bitmap;
	
	private ImageView ivTest;
	private EditText filename; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ivTest = (ImageView)findViewById(R.id.ivTest);
		filename=(EditText)findViewById(R.id.fileEdit);
		uploadButton = (Button)findViewById(R.id.UploadButton);
		cameraButton = (Button)findViewById(R.id.CameraButton);
		SaveButton = (Button)findViewById(R.id.SaveButton);

	    uploadButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
    	
    		ByteArrayOutputStream bao = new ByteArrayOutputStream();
    		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
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
				Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		
        	
               
        	}
        });   
	    cameraButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//利用intent去開啟android本身的照相介面
        		//設定圖片的儲存位置，以及檔名
        		/*File tmpFile = new File(
        		               Environment.getExternalStorageDirectory(),filename.getText().toString()+"0.jpg");
        		 outputFileUri= Uri.fromFile(tmpFile);
        		 Log.d("storage",tmpFile.getPath());*/
        		/*
        		 * 把上述的設定put進去！然後startActivityForResult,
        		 * 記住，因為是有ForResult，所以在本身自己的acitivy裡面等等要複寫onActivityResult
        		 * 稍後再說明onActivityResult
        		 */    
        		//intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri); 
        		startActivityForResult(intent, 0); 
               }
        }); 
	    
	    SaveButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		String path = Environment.getExternalStorageDirectory().toString();
        		OutputStream fOut = null;
        		File file = new File(path, filename.getText().toString()+".jpg");
        		try {
					fOut = new FileOutputStream(file);
	        		bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
	        		Log.d("filename",file.getName());
	        		MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               }
        });  	
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

}
