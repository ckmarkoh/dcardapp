package com.ntugiee.markchang.msg
;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
//import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
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

public class MainActivity extends Activity {
    
    private Button SendMessageButton;
    private Button ReloadButton;
    private Button BackButton;

	private ListView mListView;
	private TextView selected_text;
    private TextView username_text;
    private String username;
    
    private EditText msgEdit;
    
	ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username_text = (TextView) this.findViewById(R.id.UserNameText);				
        selected_text= (TextView) this.findViewById(R.id.SelectedMsgText);

        SendMessageButton = (Button) this.findViewById(R.id.MsgBut);
        ReloadButton = (Button) this.findViewById(R.id.ReloadBut);
        BackButton = (Button) this.findViewById(R.id.BackBut);

        mListView = (ListView) this.findViewById(R.id.msglist);
        msgEdit =(EditText) this.findViewById(R.id.MsgEdit);
        
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        username = bundle.getString("name");
        username_text.setText(username);
        
        SimpleAdapter sAdapter;
        sAdapter = new SimpleAdapter(this, mList, R.layout.itemlayout, new String[] {"name","msg","time"}, new int[] {R.id.textView1, R.id.textView2,R.id.textView3}  );
        mListView.setAdapter(sAdapter);
        mListView.setTextFilterEnabled(true);

        
        BackButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {    
				setResult(RESULT_OK);
				finish();
			}
		});
        
        ReloadButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {    
				get_msg();
			}
		});
        
        SendMessageButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {    

					Log.d("msg",msgEdit.getText().toString());
					HttpPost request = new HttpPost(Global_Setting.site_url+"insert");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("name", username_text.getText().toString()));
					params.add(new BasicNameValuePair("msg", msgEdit.getText().toString()));
	 
					try {
						request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
						HttpResponse response = new DefaultHttpClient().execute(request);
						if(response.getStatusLine().getStatusCode() == 200){
						//	String result = EntityUtils.toString(response.getEntity());
					//		Toast.makeText(PhptestActivity.this, result, Toast.LENGTH_LONG).show();
							//if(result=="1"){
								get_msg();
								msgEdit.setText("");
								Toast.makeText(MainActivity.this, "post success", Toast.LENGTH_LONG).show();
							//}
						}
					} catch (Exception e) {
						Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
						e.printStackTrace();
						}			
				}
			
		});
        get_msg();
       
    }
    public void get_msg(){
    	HttpPost request = new HttpPost(Global_Setting.site_url);
		try {
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200){
				String result = EntityUtils.toString(response.getEntity());
				//Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
				//Log.d("success",result);
				mList.clear();
				JSONArray json_array = new JSONArray(result);
				for(int i=0;i<json_array.length();i++){
	//				Log.d("msg",json_array.getJSONObject(i).getString("name")+json_array.getJSONObject(i).getString("msg"));
					addNewItem(json_array.getJSONObject(i).getString("name"),
									json_array.getJSONObject(i).getString("msg"), 
									json_array.getJSONObject(i).getString("time") 

							);
		        }
//				Log.d("success",result);

			}
		} catch (Exception e) {
			Toast.makeText(MainActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			//Log.d("error",e.getMessage());
		}
	}
    
    OnItemClickListener listener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
          //setTitle(parent.getItemAtPosition(position).toString());
  		String selected_msg="";		
        HashMap<String,String> item =  mList.get(position);
        selected_msg=item.get("name")+" ";
        selected_msg+=item.get("msg")+" ";
        selected_msg+=item.get("time");

        selected_text.setText(selected_msg);
		Toast.makeText(MainActivity.this, selected_msg, Toast.LENGTH_LONG).show();
        }
      };
      
    public void addNewItem(String s,String t,String u) {
        HashMap<String,String> item = new HashMap<String,String>();
        item.put("name", s+":");
        item.put("msg", t);
        item.put("time", "@"+u);
        mList.add(item); 
        SimpleAdapter sAdapter;
        sAdapter = new SimpleAdapter(this, mList,
        		R.layout.itemlayout,
        		new String[] { "name","msg" ,"time"},
        		new int[] {R.id.textView1, R.id.textView2,R.id.textView3}
        );
        mListView.setAdapter(sAdapter);
        mListView.setTextFilterEnabled(true);
        mListView.setOnItemClickListener(listener);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        
        return true;
    }
}
