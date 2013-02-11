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

public class SignUpActivity extends Activity {
		 
	private Button signupSubmit;
	private Button signupBack;

	private EditText signUpId;
	private EditText signUpEmail;

	private EditText signUpPwd;
	
	
	private Global_Setting global_setting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.signup);
        
        global_setting = ((Global_Setting)getApplicationContext());

        signupSubmit = (Button) findViewById(R.id.SignUpSubmit);
        signupBack = (Button) findViewById(R.id.SignUpBack);
        signUpId = (EditText) findViewById(R.id.SignUpID);
        signUpEmail = (EditText) findViewById(R.id.SignUpEmail);
        signUpPwd = (EditText) findViewById(R.id.SignUpPassword);

        signupSubmit.setOnClickListener(new View.OnClickListener() {
 
			public void onClick(View v) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("url", Global_Setting.site_url+"user/signup"));
	
				params.add(new BasicNameValuePair("id", signUpId.getText().toString()));
				params.add(new BasicNameValuePair("email", signUpEmail.getText().toString()));
				params.add(new BasicNameValuePair("password", Global_Setting.md5(signUpPwd.getText().toString())) );
				
				JSONObject result_json=null;
				global_setting.progressDialog = ProgressDialog.show(SignUpActivity.this, "Loading",
	            		"please wait...", true);
				try {
					result_json=new JSONObject(new PostHTTP().execute(params).get());
					//String result=result_json.getString("result");
					if(result_json.getString("error")!=""){
						Toast.makeText(SignUpActivity.this, "signup failed,error:"+result_json.getString("error"), Toast.LENGTH_LONG).show();
					}
					else{
						Toast.makeText(SignUpActivity.this, "signup success", Toast.LENGTH_LONG).show();
						setResult(RESULT_OK);
						finish();
					}
				} catch (Exception e) {
					Toast.makeText(SignUpActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();

					// TODO Auto-generated catch block
			//		e.printStackTrace();
				}
				/*JSONObject result_json=Global_Setting.http_request(SignUpActivity.this, "user/signup", params);
				try {
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
				} catch (Exception e) {
					Toast.makeText(SignUpActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}*/
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
