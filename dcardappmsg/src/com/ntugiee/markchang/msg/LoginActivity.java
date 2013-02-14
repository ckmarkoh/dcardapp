package com.ntugiee.markchang.msg;

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

//import togeather.history.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import com.ntugiee.markchang.msg.lib.AsyncTask;

//import com.lorenzopolidori.serialexecutor.SerialExecutor;

public class LoginActivity extends Activity {
		 
	private Button btnLogin;
	private Button btnBack;

	private EditText etName;
	private EditText etPwd;
 
	private Global_Setting global_setting;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ////requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.login);
        
        global_setting = ((Global_Setting)getApplicationContext());

        btnBack = (Button) findViewById(R.id.btnBack);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        etName = (EditText) findViewById(R.id.etName);
        etPwd = (EditText) findViewById(R.id.etPassword);
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
		//		setProgressBarIndeterminateVisibility(true);
		//		HttpPost request = new HttpPost(Global_Setting.site_url+"user/login");
		//		Toast.makeText(PhptestActivity.this, Global_Setting.site_url+"login", Toast.LENGTH_LONG).show();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("url", Global_Setting.site_url+"user/login"));
	
				params.add(new BasicNameValuePair("name", etName.getText().toString()));
				params.add(new BasicNameValuePair("pwd", Global_Setting.md5(etPwd.getText().toString())));
				//params.add(new BasicNameValuePair("pwd", Global_Setting.md5(etPwd.getText().toString())));
				//PostHTTP http_request=new PostHTTP();
				JSONObject result_json=null;
				//global_setting.progressDialog = ProgressDialog.show(LoginActivity.this, "Loading", "please wait...", true);
				//MySerialExecutor myserialexecutor=new MySerialExecutor();
				try {
					result_json=new JSONObject(new PostHTTP().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,params).get());
					//String result=result_json.getString("result");
					if(result_json.has("error")){
						Toast.makeText(LoginActivity.this, "login failed,error:"+result_json.getString("result"), Toast.LENGTH_LONG).show();
					}
					else{
						//String userid=result_json.getString("userid");
						Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_LONG).show();
						global_setting.userid=result_json.getString("userid");
						global_setting.session=result_json.getString("session");
						global_setting.islogin=true;
						//login_back(true,,);
					//	setProgressBarIndeterminateVisibility(false);
						setResult(RESULT_OK);
						finish();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
			//		e.printStackTrace();
					Toast.makeText(LoginActivity.this, "ERROR:"+e.toString(), Toast.LENGTH_LONG).show();
				}
				//Global_Setting.http_request(LoginActivity.this, "user/login", params);
				/*JSONObject result_json=Global_Setting.http_request(LoginActivity.this, "user/login", params);
				try {
					//result_json = new JSONObject(raw_result);
					//JSONObject result_json= new JSONObject(raw_result);
					String result=result_json.getString("result");
					if(Boolean.parseBoolean(result)){
						String userid=result_json.getString("userid");
						Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_LONG).show();
						global_setting.userid=result_json.getString("userid");
						global_setting.session=result_json.getString("session");
						global_setting.islogin=true;
						//login_back(true,,);
					//	setProgressBarIndeterminateVisibility(false);
						setResult(RESULT_OK);
						finish();	
					}
					else{
						Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(LoginActivity.this, "ERROR:"+e.toString(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}*/
			}
		});
        
        btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();			
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
