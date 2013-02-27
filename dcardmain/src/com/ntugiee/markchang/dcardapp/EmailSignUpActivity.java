package com.ntugiee.markchang.dcardapp;
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

public class EmailSignUpActivity extends Activity {
		 
	private Button signupSubmit;
	private Button signupBack;
	private EditText signUpId;
	private EditText signUpEmail;
	private EditText signUpPwd;
	private Global_Setting global_setting;
	private Handler mHandler;
	protected static final int SIGN_UP_RESULT=0x00000001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.email_signup);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        global_setting = ((Global_Setting)getApplicationContext());

        signupSubmit = (Button) findViewById(R.id.SignUpSubmit);
        signupBack = (Button) findViewById(R.id.SignUpBack);
        signUpId = (EditText) findViewById(R.id.SignUpID);
        signUpEmail = (EditText) findViewById(R.id.SignUpMail);
        signUpPwd = (EditText) findViewById(R.id.SignUpPassword);

        
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg)
            {
                  switch (msg.what)
                  {
                  case SIGN_UP_RESULT:
                	global_setting.close_progress_dialog();
                   	JSONObject result_json=null;
					try {
						result_json=new JSONObject( (String) msg.obj);
						if(result_json.has("error")){
    						Toast.makeText(EmailSignUpActivity.this, "error:"+result_json.getString("error"), Toast.LENGTH_LONG).show();
						}
						else {
							Log.d("login","has signup");
    						Toast.makeText(EmailSignUpActivity.this, "signup success", Toast.LENGTH_LONG).show();
    						global_setting.userid=result_json.getString("userid");
    						global_setting.session=result_json.getString("session");
    						global_setting.islogin=true;
    						global_setting.isFBlogin=false;
			                Intent intent = new Intent(EmailSignUpActivity.this, CameraMenuActivity.class);
			                startActivity(intent);
							Log.d("addfriend","add success");
    						setResult(RESULT_OK);
    						finish();
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(EmailSignUpActivity.this, "error:"+e.getMessage().toString(), Toast.LENGTH_LONG).show();
					}              	  
                  break;
                  }
            }
        };
            
        
        signupSubmit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				global_setting.show_progress_dialog(EmailSignUpActivity.this, "Loading", "please wait...", true);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("id", signUpId.getText().toString()));
				params.add(new BasicNameValuePair("email", signUpEmail.getText().toString()));
				params.add(new BasicNameValuePair("password", Global_Setting.md5(signUpPwd.getText().toString())) );
				new HttpApplication(global_setting.site_url+"user/signup",params,mHandler,SIGN_UP_RESULT).startHttp();
				
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
