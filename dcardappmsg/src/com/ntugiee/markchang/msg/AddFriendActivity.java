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

public class AddFriendActivity extends Activity {
		 
	private Button addFriendadd;
	private Button addFriendBack;

	private EditText addFriendEdit;
	//private EditText etPwd;
    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriend);
        
        addFriendadd = (Button) findViewById(R.id.AddFriendAdd);
        addFriendBack = (Button) findViewById(R.id.AddFriendBack);
        addFriendEdit = (EditText) findViewById(R.id.AddFriendEdit);
        //etPwd = (EditText) findViewById(R.id.etPassword);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        username = bundle.getString("name");

        
        
        
        addFriendadd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				HttpPost request = new HttpPost(Global_Setting.site_url+"friend/add_friend");
				Toast.makeText(AddFriendActivity.this, Global_Setting.site_url+"friend/add_friend", Toast.LENGTH_LONG).show();

				Log.d("login url",Global_Setting.site_url+"friend/add_friend");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				
				params.add(new BasicNameValuePair("id1", username));
				params.add(new BasicNameValuePair("id2", addFriendEdit.getText().toString()));
 
				try {
					request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					HttpResponse response = new DefaultHttpClient().execute(request);
					if(response.getStatusLine().getStatusCode() == 200){
						String raw_result = EntityUtils.toString(response.getEntity());
						Log.d("login",raw_result);
						JSONObject result_json= new JSONObject(raw_result);
						String result=result_json.getString("result");
						if(Boolean.parseBoolean(result)){
							String userid=result_json.getString("userid");
							Toast.makeText(AddFriendActivity.this, "add success", Toast.LENGTH_LONG).show();
							//login_back(true,userid);
							addFriendEdit.setText("");

						}
						else{
							String error=result_json.getString("result");

							Toast.makeText(AddFriendActivity.this, "add failed, error:"+error, Toast.LENGTH_LONG).show();
						}
						//String result = EntityUtils.toString(response.getEntity());
						//Toast.makeText(PhptestActivity.this, result, Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					Toast.makeText(AddFriendActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		});
        
        addFriendBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();			
			}
		});   
    }
   /* private void login_back(boolean login,String userid){
		Intent i=new Intent();
		Bundle b=new Bundle();
		if(login){
			b.putString("login", "true");
			b.putString("name", userid);
		}
		else{
			b.putString("login", "false");
		}
		i.putExtras(b);
		setResult(RESULT_OK,i);
		finish();
    }*/
}
