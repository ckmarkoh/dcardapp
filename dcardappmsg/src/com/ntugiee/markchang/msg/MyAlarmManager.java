package com.ntugiee.markchang.msg;

import java.util.ArrayList;
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MyAlarmManager extends BroadcastReceiver {

    Context _context;
    private String uid;
        @Override
        public void onReceive(Context context, Intent intent) {
        	
            _context= context;            
            Bundle bundle = intent.getExtras();
            uid = bundle.getString("name");

            Log.d("alarm","alarm manager");
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
        				Log.d("receive","received c2dm");

    				}

//    				Log.d("success",result);
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    			//Log.d("error",e.getMessage());
    		}        
    }
}