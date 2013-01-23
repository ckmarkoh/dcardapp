package com.ntugiee.markchang.msg;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
//import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
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

public class SendActivity extends Activity {
    
    private Button SendMessageButton;
    private Button BackButton;

    private String username;
    
    private EditText msgEdit;
    private EditText receiverEdit;
    
    private String timeout;
    private TextView setTimeOutValue;
	ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send);

     //   username_text = (TextView) this.findViewById(R.id.UserNameText);				
     //   selected_text= (TextView) this.findViewById(R.id.SelectedMsgText);

        SendMessageButton = (Button) this.findViewById(R.id.SendBut);
        BackButton = (Button) this.findViewById(R.id.BackBut);

      //  mListView = (ListView) this.findViewById(R.id.msglist);
        msgEdit =(EditText) this.findViewById(R.id.MsgEdit);
        receiverEdit =(EditText) this.findViewById(R.id.ReceiverEdit);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        username = bundle.getString("name");
        
        
        
        SeekBar setTimeOutBar = (SeekBar)findViewById(R.id.SetTimeOutBar);  
        setTimeOutValue = (TextView)findViewById(R.id.SetTimeOutView);  
          
        setTimeOutBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){  
        	   public void onProgressChanged(SeekBar seekBar, int progress,  
        	     boolean fromUser) {  
        	    // TODO Auto-generated method stub  
        		   timeout=String.valueOf(progress+1);
        		   setTimeOutValue.setText("Time Out: "+timeout); 
        	   }  
        	  
        	   public void onStartTrackingTouch(SeekBar seekBar) {  
        	    // TODO Auto-generated method stub  
        	   }  
        	  
        	   public void onStopTrackingTouch(SeekBar seekBar) {  
        	    // TODO Auto-generated method stub  
        	   }  
        });  
        
        BackButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {    
				setResult(RESULT_OK);
				finish();
			}
		});
             
        SendMessageButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {    	
					Log.d("msg",msgEdit.getText().toString());
					HttpPost request = new HttpPost(Global_Setting.site_url+"msg/insert");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("sender", username));
					params.add(new BasicNameValuePair("message", msgEdit.getText().toString()));
					params.add(new BasicNameValuePair("receiver", receiverEdit.getText().toString()));
					params.add(new BasicNameValuePair("timeout", timeout));
					try {
						request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
						HttpResponse response = new DefaultHttpClient().execute(request);
						if(response.getStatusLine().getStatusCode() == 200){
							String raw_result = EntityUtils.toString(response.getEntity());
							Log.d("result",raw_result.toString());
						//	String result = EntityUtils.toString(response.getEntity());
					//		Toast.makeText(PhptestActivity.this, result, Toast.LENGTH_LONG).show();
							//if(result=="1"){
							JSONObject result_json= new JSONObject(raw_result);
							String result=result_json.getString("result");
							if(Boolean.parseBoolean(result)){
								Toast.makeText(SendActivity.this, "send success", Toast.LENGTH_LONG).show();
								msgEdit.setText("");
							}
							else{
								String error=result_json.getString("error");

								Toast.makeText(SendActivity.this, "send failed, error:"+error, Toast.LENGTH_LONG).show();
							}
							//	Toast.makeText(SendActivity.this, "post success", Toast.LENGTH_LONG).show();
							//}
						}
					} catch (Exception e) {
						Toast.makeText(SendActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
						e.printStackTrace();
						}		
				}
			
		});
    }

    /*@Override
    protected void onListItemClick(ListView l,  View v, int position, long arg3) {
    		String selected_msg="";
            HashMap<String,String> item =  mList.get(position);
            selected_msg=item.get("name")+" ";
            selected_msg+=item.get("msg");
            selected_text.setText(selected_msg);
			Toast.makeText(MainActivity.this, selected_msg, Toast.LENGTH_LONG).show();
    }*/
	@Override
	public void onResume(){
		 super.onResume();
        Log.d("on_resume","on_resume");
        timeout="5";
		setTimeOutValue.setText("Time Out: "+timeout); 
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
