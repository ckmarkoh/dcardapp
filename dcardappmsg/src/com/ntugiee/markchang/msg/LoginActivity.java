package com.ntugiee.markchang.msg;
//XDD
//package com.list;

import java.util.ArrayList;
import java.util.List;

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
import org.json.JSONObject;

//import togeather.history.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;

public class LoginActivity extends Activity {
		 
	private Button btnLogin;
	private Button btnBack;

	private EditText etName;
	private EditText etPwd;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        btnBack = (Button) findViewById(R.id.btnBack);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        etName = (EditText) findViewById(R.id.etName);
        etPwd = (EditText) findViewById(R.id.etPassword);
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
 
			public void onClick(View v) {
				HttpPost request = new HttpPost(Global_Setting.site_url+"msg/login");
		//		Toast.makeText(PhptestActivity.this, Global_Setting.site_url+"login", Toast.LENGTH_LONG).show();

				Log.d("login url",Global_Setting.site_url+"login");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("name", etName.getText().toString()));
				params.add(new BasicNameValuePair("pwd", etPwd.getText().toString()));
 
				try {
					request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					HttpResponse response = new DefaultHttpClient().execute(request);
					if(response.getStatusLine().getStatusCode() == 200){
						String raw_result = EntityUtils.toString(response.getEntity());
						Log.d("login",raw_result);
						JSONObject result_json= new JSONObject(raw_result);
						String result=result_json.getString("result");
						if(Boolean.parseBoolean(result)){
							Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_LONG).show();
							login_back(true);
						}
						else{
							Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_LONG).show();
						}
						//String result = EntityUtils.toString(response.getEntity());
						//Toast.makeText(PhptestActivity.this, result, Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		});
        
        btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				login_back(false);
			}
		});   
        
    }
    private void login_back(boolean login){
		Intent i=new Intent();
		Bundle b=new Bundle();
		if(login){
			b.putString("login", "true");
			b.putString("name", etName.getText().toString());
		}
		else{
			b.putString("login", "false");
		}
		i.putExtras(b);
		setResult(RESULT_OK,i);
		finish();
    }
}
