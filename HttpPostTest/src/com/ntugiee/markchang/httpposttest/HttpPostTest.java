package com.ntugiee.markchang.httpposttest;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

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

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast; 

public class HttpPostTest extends Activity implements OnClickListener
{
   private ProgressDialog progressDialog = null;
   private EditText txtMessage;
   private Button sendBtn;
   /** 「要更新版面」的訊息代碼 */
   private Handler mHandler;
   protected static final int REFRESH_DATA = 0x00000001;
   /** 建立UI Thread使用的Handler，來接收其他Thread來的訊息 */
   private String myuri= "http://r444b.ee.ntu.edu.tw/~markchang/dctest/index.php/user/login";
   private String message;
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.http_post_test);

      txtMessage = (EditText) findViewById(R.id.editText1);
      sendBtn = (Button) findViewById(R.id.button1);

      if (sendBtn != null)
          sendBtn.setOnClickListener(this);
          //  final String msg = txtMessage.getEditableText().toString();
     }
   @Override
   public void onClick(View v){
      if (v == sendBtn){
          if (txtMessage != null) {
        	  List<NameValuePair> params = new ArrayList<NameValuePair>();
		      params.add(new BasicNameValuePair("name", "mark"));
		      params.add(new BasicNameValuePair("pwd", "900150983cd24fb0d6963f7d28e17f72"));

		      progressDialog = ProgressDialog.show(HttpPostTest.this, "Loading", "please wait...", true);

		      mHandler = new Handler() {
	               @Override
	               public void handleMessage(Message msg)
	               {
	                   switch (msg.what)
	                   {
	                   // 顯示網路上抓取的資料
	                   case REFRESH_DATA:
	                      String result = null;
	                      if (msg.obj instanceof String){
	                         result = (String) msg.obj;
	                      }
	                      if (result != null){
	                    	  message=result;
	                         // 印出網路回傳的文字
	                         Toast.makeText(HttpPostTest.this, result, Toast.LENGTH_LONG).show();
	                         Log.d("result",message);
	                      
	                      }
	                      break;
	                   }
	               }
	            };
	            
			  HttpApplication httpapplication= new HttpApplication(myuri,params,mHandler,progressDialog);
		      httpapplication.startHttp();

             // 擷取文字框上的文字
             // progressDialog = ProgressDialog.show(HttpPostTest.this, "Loading","please wait...", true);
            // String msg = txtMessage.getEditableText().toString();
             

          }
      }
   }

 



}