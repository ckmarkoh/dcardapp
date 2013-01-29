package com.ntugiee.markchang.msg;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

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

//繼承android.app.Service
public class GetMsgService extends Service {
	
    private String uid;

	
    private Handler handler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
    	
        Bundle bundle = intent.getExtras();
        uid = bundle.getString("name");    	
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        
        Bundle bundle = intent.getExtras();
        //uid = "mark";//bundle.getString("name");    	
    	handler.postDelayed(showTime, 2000);
        
        super.onStart(intent, startId);
        
        
        
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(showTime);
        super.onDestroy();
    }
    
    private Runnable showTime = new Runnable() {
        public void run() {
            //log目前時間
            Log.d("receiver","service");
        	HttpPost request = new HttpPost(Global_Setting.site_url+"msg/receiver_get");
    		List<NameValuePair> params = new ArrayList<NameValuePair>();
    		params.add(new BasicNameValuePair("receiver", uid));
    		//Log.d("url",Global_Setting.site_url+"receiver_get");
    		Log.d("userid","userid: "+uid);

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
            handler.postDelayed(this, 2000);
            
            
        }
    };
}
