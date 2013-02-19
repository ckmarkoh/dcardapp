package com.ntugiee.markchang.dcardapp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
//import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;


//import togeather.history.R;

public class CameraMenuActivity extends Activity implements SurfaceHolder.Callback{
    
	private Button MenuLogoutBut;
//    private Button MenuLogoutBut;
    private Button SendMsgButton;
    private Button ReceiveButton;
   // private Button SignUpButton;
    private Button MenuAddFriendBut;
    private Button MenuConfirmFriendBut;
    private Button OptionButton;
    
    private TextView menu_username;

    //private AlertDialog DialogAddFriend;
    private Button DialogAddFriendButton;
    private Button DialogCloseButton;
    private EditText DialogAddFriendEdit;
   // private String global_setting.userid;
 //   private boolean islogin=false;
    
    private View addFriendView;
    private View menuOptionView;

    private LinearLayout llayout;

    private static final int LOGIN=1;

	private Global_Setting global_setting;

	private SurfaceHolder surfaceHolder;
	private SurfaceView surfaceView1;
	//private Button button1;
	//ImageView imageView1;
	private Camera camera;

	
	
	ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_camera);

        global_setting = ((Global_Setting)getApplicationContext());
        
        
    	setRequestedOrientation(1);
    	//設為横向顯示。因為攝影頭會自動翻轉90度，所以如果不横向顯示，看到的畫面就是翻轉的。
    	
    	surfaceView1=(SurfaceView)findViewById(R.id.surfaceView1);
    	//imageView1=(ImageView)findViewById(R.id.imageView1);
    	surfaceHolder=surfaceView1.getHolder();
    	surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    	surfaceHolder.addCallback(this);

    	

        
        SendMsgButton = (Button) this.findViewById(R.id.ButtonSend);
        ReceiveButton = (Button) this.findViewById(R.id.ButtonReceive);
        //SignUpButton = (Button) this.findViewById(R.id.ButtonSignUp);
        OptionButton = (Button) this.findViewById(R.id.ButtonOption);
		llayout=(LinearLayout)this.findViewById(R.id.MenuLinear1);
		llayout.setVisibility(View.INVISIBLE);
		
		LayoutInflater inflater = LayoutInflater.from(CameraMenuActivity.this);
		addFriendView = inflater.inflate(R.layout.menu_addfriend,null);
		DialogAddFriendButton= (Button) addFriendView.findViewById(R.id.DialogAddFriendAdd);
		DialogCloseButton= (Button) addFriendView.findViewById(R.id.DialogAddFriendBack);
		DialogAddFriendEdit = (EditText) addFriendView.findViewById(R.id.DialogAddFriendEdit); //here

		menuOptionView = inflater.inflate(R.layout.menu_option,null);
        MenuLogoutBut = (Button) menuOptionView.findViewById(R.id.MenuLogoutBut);
        MenuAddFriendBut = (Button) menuOptionView.findViewById(R.id.MenuAddFriendBut);
        MenuConfirmFriendBut = (Button) menuOptionView.findViewById(R.id.MenuConfirmFriendBut);
        menu_username = (TextView) menuOptionView.findViewById(R.id.MenuUserText);				
		menu_username.setText(global_setting.userid);

		//LayoutInflater inflater2 = LayoutInflater.from(MenuActivity.this);
		
		
		
		llayout_add_view(menuOptionView);
		
    	SendMsgButton.setOnClickListener(new OnClickListener(){
        	
    		public void onClick(View v) {
    			//自動對焦
    			camera.autoFocus(afcb);
    			camera.takePicture( null, piccallback, null);
    			}
    		});
		//addFriendView = inflater.inflate(R.layout.addfrienddialog,null);
		//AlertDialog.Builder builder= new AlertDialog.Builder(MenuActivity.this);
		//builder.setView(addFriendView);
		//DialogAddFriend = builder.create();
		
		DialogAddFriendButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
				HttpPost request = new HttpPost(global_setting.site_url+"friend/add_friend");
				//	Toast.makeText(AddFriendActivity.this, global_setting.site_url+"friend/add_friend", Toast.LENGTH_LONG).show();
					Log.d("login url",global_setting.site_url+"friend/add_friend");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("session", global_setting.session));
					params.add(new BasicNameValuePair("userid", global_setting.userid));
					params.add(new BasicNameValuePair("id1", global_setting.userid));
					params.add(new BasicNameValuePair("id2", DialogAddFriendEdit.getText().toString()));
					try {
						request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
						HttpResponse response = new DefaultHttpClient().execute(request);
						if(response.getStatusLine().getStatusCode() == 200){
							String raw_result = EntityUtils.toString(response.getEntity());
							Log.d("login",raw_result);
							JSONObject result_json= new JSONObject(raw_result);
							String result=result_json.getString("result");
							if(Boolean.parseBoolean(result)){
								//String userid=result_json.getString("userid");
								Toast.makeText(CameraMenuActivity.this, "add success", Toast.LENGTH_LONG).show();
								//login_back(true,userid);
								//DialogAddFriendEdit.setText("");
				        		llayout_add_view(menuOptionView);

								//DialogAddFriend.dismiss();
								//reload_af_friends();
							}
							else{
								String error=result_json.getString("error");
								Toast.makeText(CameraMenuActivity.this, "add failed, error: "+error, Toast.LENGTH_LONG).show();
							}
							//String result = EntityUtils.toString(response.getEntity());
							//Toast.makeText(PhptestActivity.this, result, Toast.LENGTH_LONG).show();
						}
					} catch (Exception e) {
						Toast.makeText(CameraMenuActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
        		
	        	}
	        });        
		
		DialogCloseButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
				//llayout.removeAllViews();
		        //llayout.addView(menuOptionView);
        		llayout_add_view(menuOptionView);
				//DialogAddFriend.dismiss();
        	}
        });        
		
		MenuAddFriendBut.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View view) {
					//DialogAddFriendEdit.setText("");
					//llayout.removeAllViews();
			        //llayout.addView(addFriendView);
	        		llayout_add_view(addFriendView);

	        		//DialogAddFriend.show();
	        	}
	        });
		
		
		
		
        /*SendMsgButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
	            	//final Intent intent = new Intent(MenuActivity.this, CameraImgActivity.class);
	            	final Intent intent = new Intent(CameraMenuActivity.this, MsgChooseFriendActivity.class);
	            	startActivity(intent);
        		
        	}
        });*/
        ReceiveButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
	            	final Intent intent = new Intent(CameraMenuActivity.this, ReceiveActivity.class);
	                startActivity(intent);
        	}
        });    
        MenuConfirmFriendBut.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
	            	final Intent intent = new Intent(CameraMenuActivity.this, ConfirmFriendActivity.class);
	                startActivity(intent);
        	}
        });    
        OptionButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		if(llayout.getVisibility()==View.VISIBLE){
        			//Animation fadeOutAnim = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.fadeout);
        			//llayout.startAnimation(fadeOutAnim);
        			//llayout.setVisibility(View.GONE);

        			llayout.setVisibility(View.INVISIBLE);
        		}
        		else if(llayout.getVisibility()==View.INVISIBLE){
        			llayout.setVisibility(View.VISIBLE);
        		}
        	}
        });    
        MenuLogoutBut.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
					setResult( RESULT_OK );
					finish();
        	}
        });
    }

    private void llayout_add_view(View view){
		llayout.removeAllViews();
        llayout.addView(view);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
        case LOGIN:
        	//islogin=Boolean.parseBoolean(data.getExtras().getString("login"));
        		if(global_setting.islogin){
        			//global_setting.userid=data.getExtras().getString("name");
                	menu_username.setText("Hello "+global_setting.userid);
        			//LoginButton.setText("Logout");

        		}
        		else{
        			global_setting.userid="";
        			menu_username.setText("Please login");
        			//LoginButton.setText("Login");
        		}    		
        break;
        }
    }
	@Override
	public void onResume(){
		super.onResume();
		Log.d(CameraMenuActivity.this.getClass().getName(),"on_resume");
		//global_setting.bitmap=null;
		
	}
    @Override
    public void onDestroy(){
    	super.onDestroy();
		global_setting.userid="";
		global_setting.islogin=false;     
		global_setting.session="";
		Log.d("activity","on destroy");
    }
    
	PictureCallback piccallback =new PictureCallback(){
		
		public void onPictureTaken(byte[] data, Camera camera) {
		
			//global_setting.bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
			
	   		String ba1=Base64.encodeToString(data, Base64.DEFAULT);
			Intent intent= new Intent(CameraMenuActivity.this,CameraImgActivity.class);
			intent.putExtra("img", ba1);
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
			parameters.setPictureFormat(PixelFormat.JPEG);
			parameters.setPreviewSize(320, 200);
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

}
