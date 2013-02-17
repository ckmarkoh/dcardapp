package com.ntugiee.markchang.dcardapp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;  

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

public class MsgChooseFriendActivity extends Activity {
    
    private Button BackButton;
    private Button MCFNextButton;
    //private String global_setting.userid;
    private String receiver="";

   // private EditText msgEdit;
    private TextView receiverView;
    
    
    //private String timeout;
	//ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>();
	ArrayList<String> fList=new ArrayList<String>();
	private JSONArray fjson_array;
	
	private Global_Setting global_setting;
	private ListView mListView;
	private RelativeLayout mcfRelativeLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.msg_choose_friend);
        global_setting = ((Global_Setting)getApplicationContext());
        

        mcfRelativeLayout=(RelativeLayout) this.findViewById(R.id.MCFrelativeLayout2);
        mcfRelativeLayout.setVisibility(View.INVISIBLE);

        BackButton = (Button) this.findViewById(R.id.MsgChooseFriendBack);
        MCFNextButton = (Button) this.findViewById(R.id.MCFNextButton);
        mListView = (ListView) this.findViewById(R.id.MsgChooseFriendListView);
        //msgEdit =(EditText) this.findViewById(R.id.MsgEdit);
        //receiverEdit =(EditText) this.findViewById(R.id.ReceiverEdit);
        receiverView=(TextView) this.findViewById(R.id.MCFTextView);
 
		//Log.d("msg",msgEdit.getText().toString());
		HttpPost request = new HttpPost(global_setting.site_url+"friend/get_friend_list");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("session", global_setting.session));
		params.add(new BasicNameValuePair("userid", global_setting.userid));

		params.add(new BasicNameValuePair("id1", global_setting.userid));
		
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200){
				String raw_result = EntityUtils.toString(response.getEntity());
				Log.d("result",raw_result.toString());
				JSONObject result_json= new JSONObject(raw_result);
				String result=result_json.getString("result");
				if(Boolean.parseBoolean(result)){
//							Toast.makeText(SendActivity.this, "send success", Toast.LENGTH_LONG).show();
					//msgEdit.setText("");
					String result_content=result_json.getString("content");
					fList.clear();
				//	fList.clear();
					fjson_array = new JSONArray(result_content);
					for(int i=0;i<fjson_array.length();i++){
						fList.add(fjson_array.getJSONObject(i).getString("id2"));
			        }
					final String[] farray = fList.toArray(new String[fList.size()]);
					/*new AlertDialog.Builder(MsgChooseFriendActivity.this)
			        .setTitle("please choose a friend")
			        .setItems(farray,  new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int item) {
			            	receiver=farray[item];
			            	receiverView.setText("To "+farray[item]);
			            			//Toast.makeText(getApplicationContext(), farray[item], Toast.LENGTH_SHORT).show();
			            }
			        }).show();*/						
			        mListView.setAdapter(new ArrayAdapter<String>(this,
			        		 R.layout.choose_friend_list_item, farray));
			        mListView.setOnItemClickListener(new OnItemClickListener(){
						@Override
			            public void onItemClick(AdapterView<?> parent, View view, int position,
			                    long id) {  	
								Log.d("get friend id", String.valueOf(farray[position]));
				            	receiverView.setText(farray[position]);
				                mcfRelativeLayout.setVisibility(View.VISIBLE);

							// TODO Auto-generated method stub
							}
			        	});
				}
				else{
					String error=result_json.getString("error");
					Toast.makeText(MsgChooseFriendActivity.this, "send failed, error:"+error, Toast.LENGTH_LONG).show();
				}
				//	Toast.makeText(SendActivity.this, "post success", Toast.LENGTH_LONG).show();
				//}
			}
		} catch (Exception e) {
			Toast.makeText(MsgChooseFriendActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			}		
				
			/*	

			
			*/
		MCFNextButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {    
            	if(receiverView.getText().toString()!=""){
	            	global_setting.target_receiver=receiverView.getText().toString();
	            	final Intent intent = new Intent(MsgChooseFriendActivity.this, CameraTakeActivity.class);
	                startActivity(intent);
            	}
            	else{
					Toast.makeText(MsgChooseFriendActivity.this, "請選擇你要傳送給哪位朋友", Toast.LENGTH_LONG).show();
            	}
			}			
		});
        BackButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {    
				setResult(RESULT_OK);
				finish();
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
        //timeout="5";
		//setTimeOutValue.setText("Time Out: "+timeout); 
	}
}
