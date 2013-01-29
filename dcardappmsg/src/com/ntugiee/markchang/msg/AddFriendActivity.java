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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
   // private String Global_Setting.userid;
    
    private SimpleAdapter cfAdapter;
	private ListView cfListView;
    private JSONArray cf_json_array;
    private JSONObject cf_this_item=null;
	ArrayList<HashMap<String,String>> cfList = new ArrayList<HashMap<String,String>>();
	
    private SimpleAdapter afAdapter;
	private ListView afListView;
    private JSONArray af_json_array;
	ArrayList<HashMap<String,String>> afList = new ArrayList<HashMap<String,String>>();

	
    
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
        Global_Setting.userid = bundle.getString("name");
        
        cfListView = (ListView) this.findViewById(R.id.friendConfirmList);
        afListView = (ListView) this.findViewById(R.id.friendAddedList);

        reload_cf_friends();
        reload_af_friends();

        
        addFriendadd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				HttpPost request = new HttpPost(Global_Setting.site_url+"friend/add_friend");
			//	Toast.makeText(AddFriendActivity.this, Global_Setting.site_url+"friend/add_friend", Toast.LENGTH_LONG).show();
				Log.d("login url",Global_Setting.site_url+"friend/add_friend");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("id1", Global_Setting.userid));
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
							reload_af_friends();
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
    
    public void reload_cf_friends(){
    	HttpPost request = new HttpPost(Global_Setting.site_url+"friend/load_confirm_id2");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id2", Global_Setting.userid));
		Log.d("url",Global_Setting.site_url+"friend/load_confirm_id2");
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200){
				String raw_result = EntityUtils.toString(response.getEntity());
				Log.d("raw_result",raw_result);
				JSONObject result_json= new JSONObject(raw_result);
				String result=result_json.getString("result");
				if(Boolean.parseBoolean(result)){
					Log.d("success",result);
					String result_content=result_json.getString("content");
					cfList.clear();
					cf_json_array = new JSONArray(result_content);
					for(int i=0;i<cf_json_array.length();i++){
						add_cf_Item(cf_json_array.getJSONObject(i).getString("id1"),
								  cf_json_array.getJSONObject(i).getString("confirm"),
								  cf_json_array.getJSONObject(i).getString("time")
								);
			        }
			        cfAdapter = new SimpleAdapter(this, cfList,
			        		R.layout.frienditemlayout,
			        		new String[] { "friend" ,"confirm","time"},
			        		new int[] {R.id.friendiIdView,R.id.friendiStatus,R.id.friendiTimeView}
			        );			        
			        cfListView.setAdapter(cfAdapter);
			        cfListView.setOnItemClickListener(cf_listener);
			        Log.d("size",String.valueOf(cfList.size()));
			        for(int i=0;i<cfList.size();i++){
			        	HashMap<String,String> item_i=cfList.get(i);
			        	if(item_i.get("confirm")=="Confirm"){
			        		//cfListView.getChildAt(0).setBackgroundResource(R.drawable.ic_launcher);//.setBackgroundColor(Color.argb(155, 0, 255, 0));
					        Log.d("Color","BLUE");
			        	}
			        	else{
					        //cfListView.getChildAt(0).setBackgroundColor(R.drawable.ic_launcher);
					        Log.d("Color","RED");
			        	}
			        }

				}
				else{
					String error=result_json.getString("error");
					Toast.makeText(AddFriendActivity.this, "load failed, error: "+error, Toast.LENGTH_LONG).show();
				}
			}
		} catch (Exception e) {
			Toast.makeText(AddFriendActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

    public void add_cf_Item(String s,String c,String t) {
        HashMap<String,String> item = new HashMap<String,String>();
        item.put("friend", s);
        if(Integer.parseInt(c)==0){
        	item.put("confirm", "Unconfirm");
        }
        else{
        	item.put("confirm", "Confirm");
        }
        item.put("time", t);
        cfList.add(item); 
    }
    
    public void read_cf_item(String id,String id1){
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
					reload_cf_friends();
					Toast.makeText(AddFriendActivity.this, "confirm success", Toast.LENGTH_LONG).show();
					}
				else{
					String error=result_json.getString("error");
					Toast.makeText(AddFriendActivity.this, "confirm failed, error: "+error, Toast.LENGTH_LONG).show();
					}
				}
			} catch (Exception e) {
			Toast.makeText(AddFriendActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			//Log.d("error",e.getMessage());
		}	
    }
    
    OnItemClickListener cf_listener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {      	
            try {
            	 cf_this_item = cf_json_array.getJSONObject(position);
            	 String is_confirm=cf_this_item.getString("confirm");
            	 if(Integer.parseInt(is_confirm)==0){
            		 
 			        new AlertDialog.Builder(AddFriendActivity.this)
 			        .setMessage("Are you sure to confirm this friend?")
 			        .setPositiveButton("Yes" ,
 			                new DialogInterface.OnClickListener() {
 			                    public void onClick(DialogInterface dialog, int which) {
 			                    	try {
 			                    		read_cf_item(cf_this_item.getString("id"),cf_this_item.getString("id1"));
										} catch (JSONException e) {
											// TODO Auto-generated catch block
						                    Toast.makeText(AddFriendActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
										}
 			                    }   
 			                })  
 			         .setNegativeButton("No",                    
 			                 new DialogInterface.OnClickListener() {
 			                    public void onClick(DialogInterface dialog, int which) {
 			                }   
 			         }) 
 			         .show();
            		 
            		 
            	 }
            } catch (JSONException e) {
                    Toast.makeText(AddFriendActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }
      };

    public void reload_af_friends(){
      	HttpPost request = new HttpPost(Global_Setting.site_url+"friend/load_confirm_id1");
  		List<NameValuePair> params = new ArrayList<NameValuePair>();
  		params.add(new BasicNameValuePair("id1", Global_Setting.userid));
  		Log.d("url",Global_Setting.site_url+"friend/load_confirm_id1");
  		try {
  			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
  			HttpResponse response = new DefaultHttpClient().execute(request);
  			if(response.getStatusLine().getStatusCode() == 200){
  				String raw_result = EntityUtils.toString(response.getEntity());
  				Log.d("raw_result",raw_result);
  				JSONObject result_json= new JSONObject(raw_result);
  				String result=result_json.getString("result");
  				if(Boolean.parseBoolean(result)){
  					Log.d("success",result);
  					String result_content=result_json.getString("content");
  					afList.clear();
  					af_json_array = new JSONArray(result_content);
  					for(int i=0;i<af_json_array.length();i++){
  						add_af_Item(af_json_array.getJSONObject(i).getString("id2"),
								af_json_array.getJSONObject(i).getString("confirm"),
								af_json_array.getJSONObject(i).getString("time")
								);
  			        }
  			        afAdapter = new SimpleAdapter(this, afList,
			        		R.layout.frienditemlayout,
			        		new String[] { "friend" ,"confirm","time"},
			        		new int[] {R.id.friendiIdView,R.id.friendiStatus,R.id.friendiTimeView}
  			        );
  			        afListView.setAdapter(afAdapter);
  				}
  				else{
  					String error=result_json.getString("error");
  					Toast.makeText(AddFriendActivity.this, "load failed, error: "+error, Toast.LENGTH_LONG).show();
  				}
  			}
  		} catch (Exception e) {
  			Toast.makeText(AddFriendActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
  			e.printStackTrace();
  		}
  	}
    public void add_af_Item(String s,String c,String t) {
        HashMap<String,String> item = new HashMap<String,String>();
        item.put("friend", s);
        if(Integer.parseInt(c)==0){
        	item.put("confirm", "Unconfirm");
        }
        else{
        	item.put("confirm", "confirm");
        }
        item.put("time", t);
        afList.add(item); 
    }

}
