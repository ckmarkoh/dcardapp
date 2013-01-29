package com.ntugiee.markchang.msg;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

//繼承android.app.Service
public class GetMsgService extends Service {
	
    private String uid;

	
    private Handler handler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
    	
        Bundle bundle = intent.getExtras();
       // uid = bundle.getString("name");    	
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
//        Bundle bundle = intent.getExtras();
        //uid = "mark";//bundle.getString("name");    	
        super.onStart(intent, startId);        
    	handler.postDelayed(showTime, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(showTime);
    }
    
    private Runnable showTime = new Runnable() {
        public void run() {
            //log目前時間
            Log.d("receiver","service");
        	/*HttpPost request = new HttpPost(Global_Setting.site_url+"msg/receiver_get");
    		List<NameValuePair> params = new ArrayList<NameValuePair>();
    		params.add(new BasicNameValuePair("receiver", ReceiveActivity.username));
    		//Log.d("url",Global_Setting.site_url+"receiver_get");
    		Log.d("userid","userid: "+ReceiveActivity.username);

    		try {
    			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
    			HttpResponse response = new DefaultHttpClient().execute(request);
    			if(response.getStatusLine().getStatusCode() == 200){
    				String result = EntityUtils.toString(response.getEntity());
    				//Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
    				Log.d("success",result);
    				if (ReceiveActivity.mThis != null) {
    				    Button rbb= (Button)ReceiveActivity.mThis.findViewById(R.id.ReloadBut);
    				    rbb.setText("received c2dm");
        				Log.d("receive","service");
    				}
//    				Log.d("success",result);
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    			//Log.d("error",e.getMessage());
    		}       
        	        	//Log.i("time:", new Date().toString());
    		
    		*/
    		
    		
    		
        	HttpPost request = new HttpPost(Global_Setting.site_url+"msg/receiver_get");
    		List<NameValuePair> params = new ArrayList<NameValuePair>();
    		params.add(new BasicNameValuePair("receiver", ReceiveActivity.username));
    		Log.d("url",Global_Setting.site_url+"receiver_get");
    		try {
    			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
    			HttpResponse response = new DefaultHttpClient().execute(request);
    			if(response.getStatusLine().getStatusCode() == 200){
    				String result = EntityUtils.toString(response.getEntity());
    				//Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
    				Log.d("success",result);
    				ReceiveActivity.mList.clear();
    				ReceiveActivity.msg_json_array = new JSONArray(result);
    				
    				for(int i=0;i<ReceiveActivity.msg_json_array.length();i++){
    	//				Log.d("msg",json_array.getJSONObject(i).getString("name")+json_array.getJSONObject(i).getString("msg"));
    					ReceiveActivity.addNewItem(ReceiveActivity.msg_json_array.getJSONObject(i).getString("sender"),
    							ReceiveActivity.msg_json_array.getJSONObject(i).getString("status"),
    							ReceiveActivity.msg_json_array.getJSONObject(i).getString("time")
    							);
    		        }
    				ReceiveActivity.sAdapter = new SimpleAdapter(ReceiveActivity.mThis, ReceiveActivity.mList,
    		        		R.layout.msgitemlayout,
    		        		new String[] { "sender","status" ,"time"},
    		        		new int[] {R.id.msgiSender, R.id.msgiStatus,R.id.msgiTime}
    		        );
    				ReceiveActivity.mListView.setAdapter(ReceiveActivity.sAdapter);
    				ReceiveActivity.mListView.setOnItemClickListener(ReceiveActivity.listener);
//    				Log.d("success",result);
    			}
    		} catch (Exception e) {
    			Toast.makeText(ReceiveActivity.mThis, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
    			e.printStackTrace();
    			//Log.d("error",e.getMessage());
    		}
            handler.postDelayed(this, 10000);
        }
    };
}
