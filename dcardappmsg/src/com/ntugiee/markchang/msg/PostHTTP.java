package com.ntugiee.markchang.msg;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.util.Log;
import com.ntugiee.markchang.msg.lib.AsyncTask;

import com.ntugiee.markchang.msg.Global_Setting;

class PostHTTP extends AsyncTask<List<NameValuePair>, Integer, String> {
	@Override
	
	protected String doInBackground(List<NameValuePair>... params) {
			String url=(params[0].get(0)).getValue();
			Log.d("execute","execute");
			params[0].remove(0);
			if(Global_Setting.http_lock){
				Global_Setting.http_lock=false;
		        try {
					HttpPost request = new HttpPost(url);
		        	request.setEntity(new UrlEncodedFormEntity(params[0], HTTP.UTF_8));
					HttpResponse response = new DefaultHttpClient().execute(request);
		            response = new DefaultHttpClient().execute(request);
					if(response.getStatusLine().getStatusCode() == 200){
						String raw_result = EntityUtils.toString(response.getEntity());
						Log.d("raw_result",raw_result);
						//JSONObject result_json = new JSONObject(raw_result);
						return raw_result;
					}
				} catch (Exception e) {
					String error="{\"error\":" +"\""+e.getMessage().toString()+"\"}";
					//Log.d("error", e.getMessage().toString());
					//e.printStackTrace();
					return error;
				}
			}
			else{
				Log.d("false","false");
			}
			return "{\"error\":" +"\"execute again\"}";
	}
	protected void onPostExecute(String result) {
		Global_Setting.close_progress_dialog();
		Global_Setting.http_lock=true;
	}
}