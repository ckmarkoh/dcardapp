package com.ntugiee.markchang.camerataketest;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CameraTakeTestActivity extends Activity{
	private SurfaceHolder surfaceHolder;
	private SurfaceView surfaceView1;
	private Button button1;
	//ImageView imageView1;
		private Global_Setting global_setting;
	//private Bitmap bitmap;
	private MySurfaceHolder msholder;
	@Override
	public void onCreate(Bundle savedInstanceState) {		
	super.onCreate(savedInstanceState);
	Log.d(CameraTakeTestActivity.this.getClass().getName(),"on_create");
	setContentView(R.layout.camera_take_test);
    global_setting = ((Global_Setting)getApplicationContext());

	button1=(Button)findViewById(R.id.button1);
	//在AndroidManifest.xml中設定或是用下面的setRequestedOrientation(0)設定也可以
	//0代表橫向、1代表縱向
	setRequestedOrientation(1);
	//設為横向顯示。因為攝影頭會自動翻轉90度，所以如果不横向顯示，看到的畫面就是翻轉的。
	
	surfaceView1=(SurfaceView)findViewById(R.id.surfaceView1);
	surfaceHolder=surfaceView1.getHolder();
	surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	msholder=new MySurfaceHolder();
	surfaceHolder.addCallback(msholder);
	button1.setOnClickListener(new OnClickListener(){
		public void onClick(View v) {
			//自動對焦
			msholder.takePicture();
			}
		});
	}
	@Override
	public void onResume(){
		super.onResume();
		Log.d(CameraTakeTestActivity.this.getClass().getName(),"on_resume");
		global_setting.bitmap=null;
		
	}
	
	private class MySurfaceHolder implements SurfaceHolder.Callback{
		public Camera camera;

		public void takePicture(){
			camera.autoFocus(afcb);
		}
		
		public PictureCallback piccallback =new PictureCallback(){
			public void onPictureTaken(byte[] data, Camera camera) {
			
				global_setting.bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
        	    float width = (float) global_setting.bitmap.getWidth();
        	    float height = (float) global_setting.bitmap.getHeight();
        	    int newWidth = 200;
        	    int newHeight = (int) ( 200*(height / width)) ;
        	    // calculate the scale - in this case = 0.4f
        	    float scaleWidth = ((float) newWidth) / width;
        	    float scaleHeight = ((float) newHeight) / height;
        	    // createa matrix for the manipulation
        	    Matrix matrix = new Matrix();
        	    matrix.postScale(scaleWidth, scaleHeight);
        	    matrix.postRotate(90);
        	    Bitmap resizedBitmap = Bitmap.createBitmap(global_setting.bitmap, 0, 0,
        	    						(int) width, (int) height, matrix, true);
        	    // make a Drawable from Bitmap to allow to set the BitMap
        	    // to the ImageView, ImageButton or what ever
        	    //BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
        	    global_setting.bitmap=resizedBitmap;
	
				Intent intent= new Intent(CameraTakeTestActivity.this,EditImgActivity.class);
				startActivity(intent);
				//byte數组轉換成Bitmap
				//imageView1.setImageBitmap(bmp);
				//拍下圖片顯示在下面的ImageView裡
				/* FileOutputStream fop;
				try {
					fop=new FileOutputStream("/sdcard/dd.jpg");
					//實例化FileOutputStream，參數是生成路徑
					bmp.compress(Bitmap.CompressFormat.JPEG, 100, fop);
					//壓缩bitmap寫進outputStream 參數：輸出格式輸出質量目標OutputStream
					//格式可以為jpg,png,jpg不能存儲透明
					fop.close();
					System.out.println("拍照成功");
					//關閉流
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
					System.out.println("FileNotFoundException");
					
				} catch (IOException e) {
					
					e.printStackTrace();
					System.out.println("IOException");
				}*/
				camera.startPreview();
			//需要手動重新startPreview，否則停在拍下的瞬間
			}
			
		};
	
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			Log.d("surface","surface change");
			camera=Camera.open();
			try {
				Camera.Parameters parameters=camera.getParameters();
			    List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
			    Camera.Size pS=previewSizes.get(previewSizes.size()-1);
			    
				parameters.setPictureFormat(PixelFormat.JPEG);
				parameters.setPreviewSize(pS.width, pS.height);
				camera.setParameters(parameters);
				//設置參數
				camera.setPreviewDisplay(surfaceHolder);
				//鏡頭的方向和手機相差90度，所以要轉向
				camera.setDisplayOrientation(90);
				//攝影頭畫面顯示在Surface上
				camera.startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			}
	
		
		}
		public void surfaceCreated(SurfaceHolder holder) {
			Log.d("surface","surface create");
	
		}
		
		public void surfaceDestroyed(SurfaceHolder holder) {
			
			System.out.println("surfaceDestroyed");
			camera.stopPreview();
			//關閉預覽
			camera.release();
			camera=null;
			//
		}
	
		//自動對焦監聽式
		AutoFocusCallback afcb= new AutoFocusCallback(){
			public void onAutoFocus(boolean success, Camera camera) {
				if(success){
				//對焦成功才拍照
				camera.takePicture(null, null, piccallback);
				}
			}
		};
	/*public String encode_bitmap(){
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
		byte [] ba = bao.toByteArray();
		String ba1=Base64.encodeToString(ba, Base64.DEFAULT);
		return ba1;
	}*/
	}
}