package com.ntugiee.markchang.dcardapp.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class HttpApplication {
	   private Handler mHandler=null;
	   protected int STATE;
	   private String uriAPI;// = "http://r444b.ee.ntu.edu.tw/~markchang/dctest/index.php/user/login";
	   private String message;
	   private List<NameValuePair> params=null;//new ArrayList<NameValuePair>();
	   //private ProgressDialog progressDialog; 
	   private HttpPost httpPostRequest;
	   private HttpGet httpGetRequest;
	   private HttpResponse httpResponse;
	   private boolean request_is_post;
	   
	   public HttpApplication(String url,List<NameValuePair> prms,Handler handler,int S,boolean rtype){
		   params=prms;
		   uriAPI=url;
		   mHandler=handler;
		   STATE=S;
		   request_is_post=rtype;
	   }
	   public HttpApplication(String url,List<NameValuePair> prms,Handler handler,int S){
		   //message=msg;
		   this(url,prms,handler,S,true);
		   //progressDialog=pgs;
	   }
	   public HttpApplication(String url,Handler handler,int S){
		   this(url,null,handler,S,false);
		   //message=msg;
		   //progressDialog=pgs;
	   }

       public void startHttp(){          
       // 啟動一個Thread(執行緒)，將要傳送的資料放進Runnable中，讓Thread執行
            Thread t = new Thread(new sendRequestRunnable(mHandler));
            t.start();
            //return message;
       }

	   class sendRequestRunnable implements Runnable {
	      //String strTxt = null;
	      Handler handler= null;
	      // 建構子，設定要傳的字串
	      public sendRequestRunnable(Handler hd )  {
	         this.handler = hd;
	      }
	      @Override
	      public void run() {
	          String result = sendPostDataToInternet(params);
             /* if (progressDialog != null) {
                  progressDialog.dismiss();
                  progressDialog = null;
              }*/
	          handler.obtainMessage(STATE, result).sendToTarget();
	      }
	   }
	   private String sendPostDataToInternet(List<NameValuePair> params){
		   if(request_is_post){
			   httpPostRequest = new HttpPost(uriAPI);
		   }
		   else{
			   httpGetRequest =new HttpGet(uriAPI);
		   }
	      /*
	       * Post運作傳送變數必須用NameValuePair[]陣列儲存
	       */
	      //List<NameValuePair> params = new ArrayList<NameValuePair>();
	       try {
			   if(request_is_post){
				   httpPostRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				   httpResponse = new DefaultHttpClient().execute(httpPostRequest);
			   }
			   else{
				   httpResponse = new DefaultHttpClient().execute(httpGetRequest);
			   }

	          if (httpResponse.getStatusLine().getStatusCode() == 200) {
	             /* 取出回應字串 */
	             String strResult = EntityUtils.toString(httpResponse .getEntity());
	             // 回傳回應字串
			     Log.d("raw_result",strResult);
	             return strResult;
	          }
	          else{
					String error="{\"error\": http status " +"\""+httpResponse.getStatusLine().getStatusCode()+"\"}";
					return error;
	          }
	      } catch (Exception e){
				String error="{\"error\":" +"\""+e.getMessage().toString()+"\"}";
				return error;
	      }
	   }

}
