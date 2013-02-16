package com.ntugiee.markchang.dcardapp;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ConfirmFriendActivity extends Activity {
		 
	private Button confirmFriendBack;
	//private EditText etPwd;
   // private String global_setting.userid;
    
	private ListView cfListView;
	ArrayList<HashMap<String,String>> cfList = new ArrayList<HashMap<String,String>>();
	 
    //private SimpleAdapter afAdapter;
	private ListView afListView;
    //private JSONArray af_json_array;
	ArrayList<HashMap<String,String>> afList = new ArrayList<HashMap<String,String>>();
	
	private Global_Setting global_setting;
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.confirm_friend);
        
        global_setting = ((Global_Setting)getApplicationContext());

        
        cfListView = (ListView) this.findViewById(R.id.friendConfirmList);
        afListView = (ListView) this.findViewById(R.id.friendAddedList);
        confirmFriendBack = (Button) this.findViewById(R.id.ConfirmFriendBack);
        		
        reload_cf_friends(0);
        reload_cf_friends(2);

       // reload_af_friends();
        //BindData();

        confirmFriendBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();			
			}
		}); 
    }
    
    public void reload_cf_friends(int type){
    	HttpPost request = new HttpPost(global_setting.site_url+"friend/load_confirm");
		List<NameValuePair> params = new ArrayList<NameValuePair>();		
		params.add(new BasicNameValuePair("session", global_setting.session));
		params.add(new BasicNameValuePair("userid", global_setting.userid));
  		params.add(new BasicNameValuePair("type", "id2"));
  		params.add(new BasicNameValuePair("confirm", String.valueOf(type)));
		params.add(new BasicNameValuePair("id2", global_setting.userid));
		Log.d("url",global_setting.site_url+"friend/load_confirm");
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
					ArrayList<HashMap<String,String>> cfList = new ArrayList<HashMap<String,String>>();
					JSONArray cf_json_array = new JSONArray(result_content);
					for(int i=0;i<cf_json_array.length();i++){
				        HashMap<String,String> item = new HashMap<String,String>();
				        item.put("id", cf_json_array.getJSONObject(i).getString("id"));
				        item.put("friend", cf_json_array.getJSONObject(i).getString("id1"));
				        cfList.add(item); 
					}  
					if(type==0){
						cfListView.setAdapter(new MyBaseAdapter(this, cfList,type));
					}
					else if(type==2){
						afListView.setAdapter(new MyBaseAdapter(this, cfList,type));
					}
			        Log.d("size",String.valueOf(cfList.size()));
				}
				else{
					String error=result_json.getString("error");
					Toast.makeText(ConfirmFriendActivity.this, "load failed, error: "+error, Toast.LENGTH_LONG).show();
				}
			}
		} catch (Exception e) {
			Toast.makeText(ConfirmFriendActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
           
      public void read_cf_item(String id,String id1,int type){
  		Log.d("id",id+" friend "+id1);//+" timeout"+timeout);
  		String target_url=null;
  		String toast_msg=null;
  		if(type==0){
  			target_url="confirm_friend";
  			toast_msg="接受邀請";
  		}
  		else if(type==2){
  			target_url="unconfirm_friend";
  			toast_msg="拒絕邀請";
  		}
  		
      	HttpPost request = new HttpPost(global_setting.site_url+"friend/"+target_url);
      	List<NameValuePair> params = new ArrayList<NameValuePair>();  		
  		params.add(new BasicNameValuePair("session", global_setting.session));
  		params.add(new BasicNameValuePair("userid", global_setting.userid));
  		params.add(new BasicNameValuePair("id", id));
  		Log.d("url",global_setting.site_url+target_url);

  		try {
  			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
  			HttpResponse response = new DefaultHttpClient().execute(request);
  			if(response.getStatusLine().getStatusCode() == 200){
  				String raw_result = EntityUtils.toString(response.getEntity());
  				Log.d("success",raw_result);
  				JSONObject result_json= new JSONObject(raw_result);
  				String result=result_json.getString("result");
  				if(Boolean.parseBoolean(result)){
  					reload_cf_friends(0);
  					reload_cf_friends(2);
  					Toast.makeText(ConfirmFriendActivity.this, toast_msg+"成功", Toast.LENGTH_LONG).show();
  					}
  				else{
  					String error=result_json.getString("error");
  					Toast.makeText(ConfirmFriendActivity.this, toast_msg+"失敗, error: "+error, Toast.LENGTH_LONG).show();
  					}
  				}
  			else{
  				
  	  			Log.d("error","httpstatus:"+String.valueOf(response.getStatusLine().getStatusCode()));
  			}
  			} catch (Exception e) {
  			Toast.makeText(ConfirmFriendActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
  			e.printStackTrace();
  			Log.d("error",e.getMessage().toString());
  		}	
      }


      
    private class MyBaseAdapter extends BaseAdapter{
      private ConfirmFriendActivity mainActivity;
      private LayoutInflater myInflater;
      private ArrayList<HashMap<String, String>> list = null;
      private ViewTag viewTag;
      private int type;
      
      public MyBaseAdapter(ConfirmFriendActivity context, ArrayList<HashMap<String, String>> list,int type) {
          //取得 MainActivity 實體
          this.mainActivity = context;
          this.myInflater = LayoutInflater.from(context);
          this.list = list;
          this.type=type;
      }
       
      public int getCount() {
          return list.size();
      }
       
      public Object getItem(int position) {
          return list.get(position);
      }
       
      public long getItemId(int position) {
          return Long.valueOf(list.get(position).get("id"));
      }
       
      public View getView(int position, View convertView, ViewGroup parent) {
       
          if (convertView == null) {
              // 取得listItem容器 view
              convertView = myInflater.inflate(R.layout.confirm_friend_list_item, null);
       
              // 建構listItem內容view
              viewTag = new ViewTag(
                      (TextView) convertView.findViewById(R.id.friendiIdView),
                      (Button) convertView.findViewById(R.id.CFListItemPlus),
                      (Button) convertView.findViewById(R.id.CFListItemMinus)
            		  );
       
              // 設置容器內容
              convertView.setTag(viewTag);
       
          } else {
              viewTag = (ViewTag) convertView.getTag();
          }
           
          viewTag.text1.setText(list.get(position).get("friend"));
          //設定按鈕監聽事件及傳入 MainActivity 實體
          viewTag.btn1.setOnClickListener(new PlusButton_Click(this.mainActivity, position,0));
          if(type==0){
        	  viewTag.btn2.setOnClickListener(new PlusButton_Click(this.mainActivity, position,2));
          }
          if(type==2){
        	  viewTag.btn2.setVisibility(View.INVISIBLE);
          }
          return convertView;
      }
       
      private class ViewTag {
          TextView text1;
          TextView btn1;
          TextView btn2;
          public ViewTag(TextView textview1, Button button1,Button button2) {
              this.text1 = textview1;
              this.btn1 = button1;
              this.btn2 = button2;
          }
      }
       
      //自訂按鈕監聽事件
      class PlusButton_Click implements OnClickListener {
          private int position;
          private ConfirmFriendActivity mainActivity;
          private int type;
          private String msg_type_str=null;
          PlusButton_Click(ConfirmFriendActivity context, int pos,int type) {
              this.mainActivity = context;
              position = pos;
              this.type=type;
              if(type==0){
            	  msg_type_str="接受";
              }
              else if(type==2){
            	  msg_type_str="拒絕";
              }
          }
       
          public void onClick(View v) {
		        new AlertDialog.Builder(ConfirmFriendActivity.this)
		        .setMessage("你確定要"+msg_type_str+"'"+list.get(position).get("friend")+"'的朋友邀請?")
		        .setPositiveButton("是" ,
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int which) {
		                       	 	read_cf_item(list.get(position).get("id"),list.get(position).get("friend"),type);
		                    }   
		                })  
		         .setNegativeButton("否",                    
		                 new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int which) {
		                }   
		         }) 
		         .show();
         }
          
      }
    }
}
