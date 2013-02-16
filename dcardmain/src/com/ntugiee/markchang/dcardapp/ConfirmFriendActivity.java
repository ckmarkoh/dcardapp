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
    
    private SimpleAdapter cfAdapter;
	private ListView cfListView;
    private JSONArray cf_json_array;
    private JSONObject cf_this_item=null;
	ArrayList<HashMap<String,String>> cfList = new ArrayList<HashMap<String,String>>();
	 
    private SimpleAdapter afAdapter;
	private ListView afListView;
    private JSONArray af_json_array;
	ArrayList<HashMap<String,String>> afList = new ArrayList<HashMap<String,String>>();
	
	private Global_Setting global_setting;
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.confirm_friend);
        
        
        
        //addFriendadd = (Button) findViewById(R.id.AddFriendAdd);
        //confirmFriendBack = (Button) findViewById(R.id.ConfirmFriendBack);
        //addFriendEdit = (EditText) findViewById(R.id.AddFriendEdit);

        
        global_setting = ((Global_Setting)getApplicationContext());

        //global_setting.userid = bundle.getString("name");
        
        cfListView = (ListView) this.findViewById(R.id.friendConfirmList);
        afListView = (ListView) this.findViewById(R.id.friendAddedList);

        reload_cf_friends();
       // reload_af_friends();
        //BindData();

        /*confirmFriendBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();			
			}
		});  */ 
    }
    
    public void reload_cf_friends(){
    	HttpPost request = new HttpPost(global_setting.site_url+"friend/load_confirm");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("session", global_setting.session));
		params.add(new BasicNameValuePair("userid", global_setting.userid));
  		params.add(new BasicNameValuePair("type", "id2"));
  		params.add(new BasicNameValuePair("confirm", "0"));
		
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
					cfList.clear();
					cf_json_array = new JSONArray(result_content);
					for(int i=0;i<cf_json_array.length();i++){
						add_cf_Item(
								cf_json_array.getJSONObject(i).getString("id"),
								cf_json_array.getJSONObject(i).getString("id1")
								  //cf_json_array.getJSONObject(i).getString("confirm"),
								  //cf_json_array.getJSONObject(i).getString("time")
								);
			        }
			       /* cfAdapter = new SimpleAdapter(this, cfList,
			        		R.layout.confirm_friend_list_item,
			        		new String[] { "friend"},// ,"confirm","time"},
			        		new int[] {R.id.friendiIdView}//,R.id.friendiStatus,R.id.friendiTimeView}
			        );	*/		        
			        cfListView.setAdapter(new MyBaseAdapter(this, cfList));
			        //cfListView.setOnItemClickListener(cf_listener);
			        Log.d("size",String.valueOf(cfList.size()));
			        /*for(int i=0;i<cfList.size();i++){
			        	HashMap<String,String> item_i=cfList.get(i);
			        	if(item_i.get("confirm")=="Confirm"){
			        		//cfListView.getChildAt(0).setBackgroundResource(R.drawable.ic_launcher);//.setBackgroundColor(Color.argb(155, 0, 255, 0));
					        Log.d("Color","BLUE");
			        	}
			        	else{
					        //cfListView.getChildAt(0).setBackgroundColor(R.drawable.ic_launcher);
					        Log.d("Color","RED");
			        	}
			        }*/

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

    

    public void add_cf_Item(String i,String s){//,String c,String t) {
        HashMap<String,String> item = new HashMap<String,String>();
        item.put("id", i);
        item.put("friend", s);
       /* if(Integer.parseInt(c)==0){
        	item.put("confirm", "Unconfirm");
        }
        else{
        	item.put("confirm", "Confirm");
        }
        item.put("time", t);*/
        cfList.add(item); 
    }

    
    OnItemClickListener cf_listener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {      	
            try {
            	Log.d("onclick","position:"+position+" id"+id);
            	 cf_this_item = cf_json_array.getJSONObject(position);
            	 String is_confirm=cf_this_item.getString("confirm");
            	 if(Integer.parseInt(is_confirm)==0){
 			        new AlertDialog.Builder(ConfirmFriendActivity.this)
 			        .setMessage("Are you sure to confirm this friend?")
 			        .setPositiveButton("Yes" ,
 			                new DialogInterface.OnClickListener() {
 			                    public void onClick(DialogInterface dialog, int which) {
 			                    	try {
 			                    		read_cf_item(cf_this_item.getString("id"),cf_this_item.getString("id1"));
										} catch (JSONException e) {
											// TODO Auto-generated catch block
						                    Toast.makeText(ConfirmFriendActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(ConfirmFriendActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }
      };
      
      
      public void read_cf_item(String id,String id1){
  		Log.d("id",id+" friend"+id1);//+" timeout"+timeout);
      	HttpPost request = new HttpPost(global_setting.site_url+"friend/confirm_friend");
  		List<NameValuePair> params = new ArrayList<NameValuePair>();
  		
  		params.add(new BasicNameValuePair("session", global_setting.session));
  		params.add(new BasicNameValuePair("userid", global_setting.userid));
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
  					Toast.makeText(ConfirmFriendActivity.this, "confirm success", Toast.LENGTH_LONG).show();
  					}
  				else{
  					String error=result_json.getString("error");
  					Toast.makeText(ConfirmFriendActivity.this, "confirm failed, error: "+error, Toast.LENGTH_LONG).show();
  					}
  				}
  			} catch (Exception e) {
  			Toast.makeText(ConfirmFriendActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
  			e.printStackTrace();
  			//Log.d("error",e.getMessage());
  		}	
      }
      /*
    public void reload_af_friends(){
      	HttpPost request = new HttpPost(global_setting.site_url+"friend/load_confirm_id1");
  		List<NameValuePair> params = new ArrayList<NameValuePair>();
  		
		params.add(new BasicNameValuePair("session", global_setting.session));
		params.add(new BasicNameValuePair("userid", global_setting.userid));
  		
  		params.add(new BasicNameValuePair("id1", global_setting.userid));
  		Log.d("url",global_setting.site_url+"friend/load_confirm_id1");
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
			        		R.layout.confirm_friend_list_item,
			        		new String[] { "friend" ,"confirm","time"},
			        		new int[] {R.id.friendiIdView,R.id.friendiStatus,R.id.friendiTimeView}
  			        );
  			        afListView.setAdapter(afAdapter);
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
    }*/
     /* private void BindData() {
      	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
      	ListView listView = (ListView) findViewById(R.id.friendConfirmList);
      	HashMap<String, String> item = new HashMap<String, String>();
      	item.put("id", "15");
      	item.put("name", "用戶A");
      	list.add(item);
      	
      	listView.setAdapter(new MyBaseAdapter(this, list));
      	
      	listView.setOnItemClickListener(new OnItemClickListener() {
              public void onItemClick(AdapterView<?> parent, View view, int position,
                      long id) {   
              		Log.d("allenj", "ListViewItem ="+id);
                  }
                });
      }*/
      
      
      public void myDialog(final int id) {
          Log.d("allenj", "myDialog = " + id);
       
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setMessage("確定刪除")
                  .setPositiveButton("是", new DialogInterface.OnClickListener() {
       
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          Log.d("allenj", "刪除成功 " + id);
                      }
                  })
                  .setNegativeButton("否", new DialogInterface.OnClickListener() {
       
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          Log.d("allenj", "不要刪除 " + id);
                      }
                  });
       
          AlertDialog ad = builder.create();
          ad.show();
      }
      
      
    private class MyBaseAdapter extends BaseAdapter{
      private ConfirmFriendActivity mainActivity;
      private LayoutInflater myInflater;
      private ArrayList<HashMap<String, String>> list = null;
      private ViewTag viewTag;
       
      public MyBaseAdapter(ConfirmFriendActivity context, ArrayList<HashMap<String, String>> list) {
          //取得 MainActivity 實體
          this.mainActivity = context;
          this.myInflater = LayoutInflater.from(context);
          this.list = list;
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
          viewTag.btn1.setOnClickListener(new PlusButton_Click(this.mainActivity, position));
          viewTag.btn2.setOnClickListener(new PlusButton_Click(this.mainActivity, position));

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
           
          PlusButton_Click(ConfirmFriendActivity context, int pos) {
              this.mainActivity = context;
              position = pos;
          }
       
          public void onClick(View v) {
		        new AlertDialog.Builder(ConfirmFriendActivity.this)
		        .setMessage("你確定要將'"+list.get(position).get("friend")+"'加為朋友?")
		        .setPositiveButton("是" ,
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int which) {
		                       	 	read_cf_item(list.get(position).get("id"),list.get(position).get("friend"));
		                    }   
		                })  
		         .setNegativeButton("否",                    
		                 new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int which) {
		                }   
		         }) 
		         .show();
        	 // read_cf_item(list.get(position).get("id"),list.get(position).get("friend"));
        	  
              //Log.d("allenj", "ItemButton = " + list.get(position).get("id"));
              //this.mainActivity.myDialog(Integer.valueOf(list.get(position).get("id")));
          }
          
      }
    }
}
