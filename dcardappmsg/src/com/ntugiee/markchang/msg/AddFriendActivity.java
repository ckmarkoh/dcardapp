package com.ntugiee.markchang.msg;
//XDD
//package com.list;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import togeather.history.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddFriendActivity extends Activity {
		 
	private Button addFriendadd;
	private Button addFriendBack;

	private EditText addFriendEdit;
	//private EditText etPwd;
    private String username;

    
    
    private SimpleAdapter sAdapter;
	private ListView mListView;
    private JSONArray msg_json_array;
    private JSONObject this_item=null;
	ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>();
	
	
    
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

        
        mListView = (ListView) this.findViewById(R.id.friendConfirmList);
         
        reload_new_friends();
        
        addFriendadd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				HttpPost request = new HttpPost(Global_Setting.site_url+"friend/add_friend");
			//	Toast.makeText(AddFriendActivity.this, Global_Setting.site_url+"friend/add_friend", Toast.LENGTH_LONG).show();

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
							//String userid=result_json.getString("userid");
							Toast.makeText(AddFriendActivity.this, "add success", Toast.LENGTH_LONG).show();
							//login_back(true,userid);
							addFriendEdit.setText("");

						}
						else{
							String error=result_json.getString("error");
							Toast.makeText(AddFriendActivity.this, "add failed, error: "+error, Toast.LENGTH_LONG).show();
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
    
    
    
    
    public void reload_new_friends(){
    	HttpPost request = new HttpPost(Global_Setting.site_url+"friend/load_confirm_id2");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id2", username));
		Log.d("url",Global_Setting.site_url+"friend/load_confirm_id2");
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200){
				String raw_result = EntityUtils.toString(response.getEntity());
				Log.d("login",raw_result);
				JSONObject result_json= new JSONObject(raw_result);
				String result=result_json.getString("result");
				if(Boolean.parseBoolean(result)){
				//Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
					Log.d("success",result);
					String result_content=result_json.getString("content");

					mList.clear();
					msg_json_array = new JSONArray(result_content);
					for(int i=0;i<msg_json_array.length();i++){
		//				Log.d("msg",json_array.getJSONObject(i).getString("name")+json_array.getJSONObject(i).getString("msg"));
						addNewItem(msg_json_array.getJSONObject(i).getString("id1"),
								  msg_json_array.getJSONObject(i).getString("time")
								);
			        }
			        sAdapter = new SimpleAdapter(this, mList,
			        		R.layout.frienditemlayout,
			        		new String[] { "friend" ,"time"},
			        		new int[] {R.id.friendiIdView, R.id.friendiTimeView}
			        );
			        mListView.setAdapter(sAdapter);
			        mListView.setOnItemClickListener(listener);
				}
				else{
					String error=result_json.getString("error");
					Toast.makeText(AddFriendActivity.this, "add failed, error: "+error, Toast.LENGTH_LONG).show();
				}
//				Log.d("success",result);
			}
		} catch (Exception e) {
			Toast.makeText(AddFriendActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			//Log.d("error",e.getMessage());
		}
	}

    
    public void addNewItem(String s,String t) {
        HashMap<String,String> item = new HashMap<String,String>();
        item.put("friend", s);
        item.put("time", t);
        mList.add(item); 
    }
    
    public void readitem(String id,String id1){
		Log.d("id",id+" friend"+id1);//+" timeout"+timeout);
    	HttpPost request = new HttpPost(Global_Setting.site_url+"friend/confirm_friend");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200){
				String raw_result = EntityUtils.toString(response.getEntity());
				Log.d("success",raw_result);
				JSONObject result_json= new JSONObject(raw_result);
				String result=result_json.getString("result");
				if(Boolean.parseBoolean(result)){
					reload_new_friends();
					Toast.makeText(AddFriendActivity.this, "add success", Toast.LENGTH_LONG).show();
					}
				else{
					String error=result_json.getString("error");
					Toast.makeText(AddFriendActivity.this, "add failed, error: "+error, Toast.LENGTH_LONG).show();
					}
				}
			} catch (Exception e) {
			Toast.makeText(AddFriendActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			//Log.d("error",e.getMessage());
		}
    	
    }
    
    OnItemClickListener listener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {      	
            try {
            	
            	
            	 this_item = msg_json_array.getJSONObject(position);
                    //int this_status = Integer.parseInt(this_item.getString("status"));
            		//Log.d("this_status",this_status);
                    readitem(this_item.getString("id"),this_item.getString("id1"));
            } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(AddFriendActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }
      };
}
