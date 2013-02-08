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

public class SignUpActivity extends Activity {
		 
	private Button signupSubmit;
	private Button signupBack;

	private EditText signUpId;
	private EditText signUpEmail;

	private EditText signUpPwd;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        
        signupSubmit = (Button) findViewById(R.id.SignUpSubmit);
        signupBack = (Button) findViewById(R.id.SignUpBack);
        signUpId = (EditText) findViewById(R.id.SignUpID);
        signUpEmail = (EditText) findViewById(R.id.SignUpEmail);
        signUpPwd = (EditText) findViewById(R.id.SignUpPassword);

        signupSubmit.setOnClickListener(new View.OnClickListener() {
 
			public void onClick(View v) {
				HttpPost request = new HttpPost(Global_Setting.site_url+"user/signup");
		//		Toast.makeText(PhptestActivity.this, Global_Setting.site_url+"login", Toast.LENGTH_LONG).show();
				Log.d("signup url",Global_Setting.site_url+"user/signup");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("id", signUpId.getText().toString()));
				params.add(new BasicNameValuePair("email", signUpEmail.getText().toString()));
				params.add(new BasicNameValuePair("password", Global_Setting.md5(signUpPwd.getText().toString())) );
				Log.d("password",Global_Setting.md5(signUpPwd.getText().toString())  );

				try {
					request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					HttpResponse response = new DefaultHttpClient().execute(request);
					if(response.getStatusLine().getStatusCode() == 200){
						String raw_result = EntityUtils.toString(response.getEntity());
						Log.d("sign up result",raw_result);
						JSONObject result_json= new JSONObject(raw_result);
						String result=result_json.getString("result");
						if(Boolean.parseBoolean(result)){
							Toast.makeText(SignUpActivity.this, "sign up success", Toast.LENGTH_LONG).show();
							setResult(RESULT_OK);
							finish();
						}
						else{
							String error=result_json.getString("error");
							Toast.makeText(SignUpActivity.this, "sign up failed. error:"+error, Toast.LENGTH_LONG).show();
						}
						//String result = EntityUtils.toString(response.getEntity());
						//Toast.makeText(PhptestActivity.this, result, Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					Toast.makeText(SignUpActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		});
 
        signupBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {    
				setResult(RESULT_OK);
				finish();
			}
		});   
    }
}
