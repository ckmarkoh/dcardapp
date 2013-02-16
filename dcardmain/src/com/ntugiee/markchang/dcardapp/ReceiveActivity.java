package com.ntugiee.markchang.dcardapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.Intent;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;

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


public class ReceiveActivity extends Activity {
    
	public  Button ReloadButton;
    private Button BackButton;
    public  Button ReceiveClearBut;
	//private TextView selected_text;
   // private TextView global_setting.userid_text;
    public String username;
    //private String selected_msg;
    public static SimpleAdapter sAdapter;
    public static ListView mListView;

    public static JSONArray msg_json_array;
    public static JSONObject this_item=null;
    public static ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>();

    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
   // private MyTimerTask t;
  //  private int times;
    //private Timer t ;
    public static OnItemLongClickListener listener;
	public static ReceiveActivity mThis = null;

	private Global_Setting global_setting;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.receive);
        
        global_setting = ((Global_Setting)getApplicationContext());

        
        ReloadButton = (Button) this.findViewById(R.id.ReceiveReloadBut);
        
        ReceiveClearBut = (Button) this.findViewById(R.id.ReceiveClearBut);
        BackButton = (Button) this.findViewById(R.id.ReceiveActBack);

        mListView = (ListView) this.findViewById(R.id.msglist);

        
        BackButton.setOnClickListener( new View.OnClickListener() {
			public void onClick( View v ) {    
				setResult( RESULT_OK );
				finish();
			}
		});
        
        ReloadButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {   
				mList.clear();
		       /* sAdapter = new SimpleAdapter(ReceiveActivity.this, mList,
		        		R.layout.msgitemlayout,
		        		new String[] { "sender","status" ,"time"},
		        		new int[] {R.id.msgiSender, R.id.msgiStatus,R.id.msgiTime}
		        );
		        mListView.setAdapter(sAdapter);
		        mListView.setOnItemClickListener(listener);*/
				get_msg();
			}
		});    
        ReceiveClearBut.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {  
		    	HttpPost request = new HttpPost(global_setting.site_url+"msg/receiver_delete");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				
				params.add(new BasicNameValuePair("session", global_setting.session));
				params.add(new BasicNameValuePair("userid", global_setting.userid));
				
				params.add(new BasicNameValuePair("receiver", global_setting.userid));
				Log.d("url",global_setting.site_url+"msg/receiver_delete");
				try {
					request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					HttpResponse response = new DefaultHttpClient().execute(request);
					if(response.getStatusLine().getStatusCode() == 200){
						String result = EntityUtils.toString(response.getEntity());
						//Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
						Log.d("success",result);
						get_msg();
					}
				} catch (Exception e) {
					Toast.makeText(ReceiveActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
					//Log.d("error",e.getMessage());
				}
			}
		});    
        listener = new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                long id) {      	
                try {
                	 this_item = msg_json_array.getJSONObject(position);
                        int this_status = Integer.parseInt(this_item.getString("status"));
                		Log.d("this_status","long_click");
                        if(this_status==0){
                        	try {
								readitem(this_item.getString("id"),this_item.getString("message"),this_item.getString("timeout"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
			                    Toast.makeText(ReceiveActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
							}

        			        /*new AlertDialog.Builder(ReceiveActivity.this)
        			        .setMessage("Are you sure to open this message?")
        			        .setPositiveButton("Yes" ,
        			                new DialogInterface.OnClickListener() {
        			                    public void onClick(DialogInterface dialog, int which) {
        			                    	        			                    }   
        			                })  
        			         .setNegativeButton("No",                    
        			                 new DialogInterface.OnClickListener() {
        			                    public void onClick(DialogInterface dialog, int which) {
        			                }   
        			         }) 
        			         .show();*/
                    		//Log.d("this_status","long_click_true");

        			        return true;
                        }                    
                } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(ReceiveActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
        		//Log.d("this_status","long_click_false");

				return false;
            }
          };
        
    }
    public void get_msg(){
    	HttpPost request = new HttpPost(global_setting.site_url+"msg/receiver_get");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("session", global_setting.session));
		params.add(new BasicNameValuePair("userid", global_setting.userid));
		
		params.add(new BasicNameValuePair("receiver", global_setting.userid));
		Log.d("url",global_setting.site_url+"receiver_get");
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200){
				String result = EntityUtils.toString(response.getEntity());
				//Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
				Log.d("success",result);
				mList.clear();
				msg_json_array = new JSONArray(result);
				for(int i=0;i<msg_json_array.length();i++){
	//				Log.d("msg",json_array.getJSONObject(i).getString("name")+json_array.getJSONObject(i).getString("msg"));
					addNewItem(msg_json_array.getJSONObject(i).getString("sender"),
							msg_json_array.getJSONObject(i).getString("status"),
							msg_json_array.getJSONObject(i).getString("time")
							);
		        }
		        sAdapter = new SimpleAdapter(this, mList,
		        		R.layout.msgitemlayout,
		        		new String[] { "sender","status" ,"time"},
		        		new int[] {R.id.msgiSender, R.id.msgiStatus,R.id.msgiTime}
		        );
		        mListView.setAdapter(sAdapter);
		        mListView.setOnItemLongClickListener(listener);
//				Log.d("success",result);
			}
		} catch (Exception e) {
			Toast.makeText(ReceiveActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			//Log.d("error",e.getMessage());
		}
	}
    

	
    public static void addNewItem(String s,String u,String t) {
        HashMap<String,String> item = new HashMap<String,String>();
        item.put("sender", s);
        if(Integer.parseInt(u)==0){
            item.put("status","unread");
        }
        else{
            item.put("status","read");
        }
        item.put("time",t);
        
        mList.add(item); 
    }
  
    public void readitem(String id,final String message,final String timeout){
		Log.d("readitem",id+" message"+message+" timeout"+timeout);

    	HttpPost request = new HttpPost(global_setting.site_url+"msg/open_item");
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
                   final Intent intent = new Intent();
                    intent.setClass(ReceiveActivity.this, ShowActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("message", message);
                    bundle.putString("timeout", timeout);
                    intent.putExtras(bundle);
                    startActivity(intent);
					}
				else{
					String error=result_json.getString("error");
					Toast.makeText(ReceiveActivity.this, "error: "+error, Toast.LENGTH_LONG).show();
					}
				}
			} catch (Exception e) {
			Toast.makeText(ReceiveActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			//Log.d("error",e.getMessage());
		}
    	
    }
	@Override
	public void onResume(){
		 super.onResume();
		 mThis = this;
		 get_msg();
        Log.d("on_resume","on_resume");

    	//Bundle bundle2= new Bundle();
    	//bundle2.putString("name", global_setting.userid);
    	//intent2.putExtras(bundle2);
    	
    //    long scTime = 5000;//2mins
      /*   pendingIntent = PendingIntent.getBroadcast(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
         alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
         alarmManager.set(AlarmManager.RTC, 0, pendingIntent);

         alarmManager.setRepeating(AlarmManager.RTC,
         		3000,
         		3000, 
         	    pendingIntent);
        */
    	
       // Intent intent2 = new Intent(ReceiveActivity.this, GetMsgService.class);
        //intent2.putExtras(bundle2);
        //startService(intent2);
        
	}
	@Override
	public void onPause(){
		 super.onPause();
		 //t.cancel();
		 //alarmManager.cancel(pendingIntent);
		 mThis = null;
        //Intent intent = new Intent(ReceiveActivity.this, GetMsgService.class);
        //stopService(intent);
	}
	 


	/*class MyTimerTask extends TimerTask {
	    //times member represent calling times.
	    public void run() {
	    		//times++;
	    		get_msg();
	            Log.d("timer","I'm alive...");
	    }
	    @Override
	    public boolean cancel(){
	    	super.cancel();
            	Log.d("timer","timer cancel...");
			return false;
	    }
	}*/
   
}
