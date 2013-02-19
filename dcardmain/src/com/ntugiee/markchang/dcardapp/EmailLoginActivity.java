package com.ntugiee.markchang.dcardapp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.ntugiee.markchang.dcardapp.util.HttpApplication;
//import togeather.history.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;

//import com.lorenzopolidori.serialexecutor.SerialExecutor;

public class EmailLoginActivity extends Activity {
		 
	private Button btnLogin;
	private Button btnSignUp;

	private EditText etName;
	private EditText etPwd;
 
	private Global_Setting global_setting;
	//private ProgressDialog progressDialog = null;

	
	
	private Handler mHandler;
	protected static final int HTTP_EMAIL_LOGIN = 0x00000001;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        ////requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.email_login);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        global_setting = ((Global_Setting)getApplicationContext());
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        etName = (EditText) findViewById(R.id.etName);
        etPwd = (EditText) findViewById(R.id.etPassword);
        
        
        
	    mHandler = new Handler() {
              @Override
              public void handleMessage(Message msg)
              {
	                  switch (msg.what)
	                  {
	                  // 顯示網路上抓取的資料
	                  case HTTP_EMAIL_LOGIN:
	                        //Toast.makeText(EmailLoginActivity.this, result, Toast.LENGTH_LONG).show();
	                        //Log.d("result",message);
	                 	JSONObject result_json=null;
	    				//MySerialExecutor myserialexecutor=new MySerialExecutor();
	    				try {
	    					result_json=new JSONObject( (String) msg.obj);
	    					//String result=result_json.getString("result");
	    					if(result_json.has("error")){
	    						Toast.makeText(EmailLoginActivity.this, "login failed,error:"+result_json.getString("error"), Toast.LENGTH_LONG).show();
	    					}
	    					else{
	    						//String userid=result_json.getString("userid");
	    						Toast.makeText(EmailLoginActivity.this, "login success", Toast.LENGTH_LONG).show();
	    						global_setting.userid=result_json.getString("userid");
	    						global_setting.session=result_json.getString("session");
	    						global_setting.islogin=true;
	    		                Intent intent = new Intent(EmailLoginActivity.this, CameraMenuActivity.class);
	    		                startActivity(intent);
	    						setResult(RESULT_OK);
	    						finish();
	    					}
	    				} catch (Exception e) {
	    					// TODO Auto-generated catch block
	    			//		e.printStackTrace();
	    					Toast.makeText(EmailLoginActivity.this, "ERROR:"+e.toString(), Toast.LENGTH_LONG).show();
	    				}
                     
                     break;
                  }
              }
           };
          
           
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("name", etName.getText().toString()));
				params.add(new BasicNameValuePair("pwd", Global_Setting.md5(etPwd.getText().toString())));

				global_setting.progressDialog = ProgressDialog.show(EmailLoginActivity.this, "Loading", "please wait...", true);
				new HttpApplication(Global_Setting.site_url+"user/login",params,mHandler,global_setting.progressDialog,HTTP_EMAIL_LOGIN).startHttp();
			    //httpapplication.startHttp();
			}
		});
        
        btnSignUp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
                Intent intent = new Intent(EmailLoginActivity.this, SignUpActivity.class);
                startActivity(intent);

			//	setResult(RESULT_OK);
			//	finish();			
			}
		});   
	}
	/*private static class MySerialExecutor extends SerialExecutor {
	
	    public MySerialExecutor() {
	            super();
	    }
	
	    @Override
	    public void execute(TaskParams params) {
		MyParams myParams = (MyParams) params;
		// do something...
		        }
		
		public static class MyParams extends TaskParams {
		    // ... params definition
		}
		public MyParams(int param) {
		    // ... params init
		    }
		
	}*/

}
