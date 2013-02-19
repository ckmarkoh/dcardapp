package com.ntugiee.markchang.dcardapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public class Global_Setting extends Application{
	public static final String site_url="http://r444b.ee.ntu.edu.tw/~markchang/dctest/index.php/";
	public static String userid="";//="http://r444b.ee.ntu.edu.tw/~markchang/dctest/index.php/";
	public static String session="";
	public static boolean islogin=false;
	//private static ProgressDialog pDialog = null;
	public static JSONObject http_result=null;
	public static ProgressDialog progressDialog;
	public static boolean http_lock=true;
	//public static PostHTTP mMyAsyncTask = null;
	public static String target_receiver="";
	//public static Bitmap bitmap;
	
	public static String md5(String string) {
	    byte[] hash;
	    try {
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Huh, MD5 should be supported?", e);
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
	    }

	    StringBuilder hex = new StringBuilder(hash.length * 2);
	    for (byte b : hash) {
	        if ((b & 0xFF) < 0x10) hex.append("0");
	        hex.append(Integer.toHexString(b & 0xFF));
	    }
	    return hex.toString();
	}
	public static void close_progress_dialog(){
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
	}
	//public static JSONObject http_request(final Activity activity,final String url,final List<NameValuePair> params ){
	
	//public static void http_request(final Activity activity,final String url,final List<NameValuePair> params ){
		

	
	/*	public static String http_request(Activity activity, List<NameValuePair> params) {
            progressDialog = ProgressDialog.show(activity, "Loading",
            		"please wait...", true);
            
            //postHTTP task = new postHTTP();
    		String results = null;
			try {
				//results = task.execute(params).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		//textView = (TextView) findViewById(R.id.textView1);
    		//textView.setText("User: "+ user +" Pass: "+ pass +" URL: "+ endpoint);
			return results;
    	}
     
    	class postHTTP extends AsyncTask<List<NameValuePair>, Integer, String> {
    		@Override
    		protected String doInBackground(List<NameValuePair>... params) {
    				String url=(params[0].get(0)).getValue();
    		        try {
        				HttpPost request = new HttpPost(Global_Setting.site_url+url);
    		        	request.setEntity(new UrlEncodedFormEntity(params[0], HTTP.UTF_8));
    					HttpResponse response = new DefaultHttpClient().execute(request);

    		            response = new DefaultHttpClient().execute(request)
    					if(response.getStatusLine().getStatusCode() == 200){
    						String raw_result = EntityUtils.toString(response.getEntity());
    						Log.d("raw_result",raw_result);
    						//JSONObject result_json = new JSONObject(raw_result);
    						return raw_result;
    					}
					} catch (Exception e) {
						String error="{\"error\":" +"\""+e.getMessage().toString()+"\"}"
						return error;
						//Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_LONG).show();
						//e.printStackTrace();
					}    				
    				return null;
    		}
    		protected void onPostExecute(String result) {
    	        if (progressDialog != null) {
    	            progressDialog.dismiss();
    	            progressDialog = null;
    	        }
    		}
    	}
    
		try{
		HttpPost request = new HttpPost(Global_Setting.site_url+url);
			Log.d("url",Global_Setting.site_url+url);
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200){
				String raw_result = EntityUtils.toString(response.getEntity());
				Log.d("raw_result",raw_result);
				JSONObject result_json = new JSONObject(raw_result);
				return result_json;
			}
			} catch (Exception e) {
				Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
        finally{
        }
		//return null;
	//}*/
}
