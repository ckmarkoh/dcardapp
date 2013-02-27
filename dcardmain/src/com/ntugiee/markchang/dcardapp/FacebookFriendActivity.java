package com.ntugiee.markchang.dcardapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ntugiee.markchang.dcardapp.util.HttpApplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class FacebookFriendActivity extends Activity {
    private String[] lv_arr = {};
    private ListView mainListView = null;
    final String SETTING_TODOLIST = "todolist";
    private Button button; 
    //private ArrayList<String> selectedItems = new ArrayList<String>();
    private ArrayList<String> friendList = new ArrayList<String>();
    private JSONArray friendArray=null;
	private Handler mHandler;
	protected static final int SEND_FB_FRIEND = 0x00000001;
	private String selected_friend="";
	private Global_Setting global_setting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb_friend_list);
        global_setting = ((Global_Setting)getApplicationContext());

        
		Intent intent = this.getIntent();
		try {
			friendArray = new JSONArray(intent.getStringExtra("friends") );
			for(int i=0;i<friendArray.length();i++){
				String temp=friendArray.getJSONObject(i).getString("id")+" ("
							+friendArray.getJSONObject(i).getString("name")+")";
				friendList.add(temp);
			}
		} catch (JSONException e) {
			Toast.makeText(FacebookFriendActivity.this, "error:"+e.getMessage().toString(), Toast.LENGTH_LONG).show();
			Log.d("error",e.getMessage().toString());
		}

        
        mainListView=(ListView) findViewById(R.id.FbFriendlistView);
    
        button=(Button) findViewById(R.id.FBFriendAddBut);
        
        mainListView.setCacheColorHint(0);
        lv_arr = (String[]) friendList.toArray(new String[0]);
        mainListView.setAdapter(new ArrayAdapter<String>(FacebookFriendActivity.this,
        			                android.R.layout.simple_list_item_multiple_choice, lv_arr));
        			        		mainListView.setItemsCanFocus(false);
        			        		mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
       for(int i=0;i<friendList.size();i++){
    	   this.mainListView.setItemChecked(i, true);
       }
       button.setOnClickListener(new OnClickListener(){
		public void onClick(View arg0) {
			try {
				selected_friend=sendChooseFriends();
				Log.d("item", selected_friend );
				send_fb_friend_start();
				
			} catch (JSONException e) {
				Toast.makeText(FacebookFriendActivity.this, "error:"+e.getMessage().toString(), Toast.LENGTH_LONG).show();
				Log.d("error",e.getMessage().toString());
				//e.printStackTrace();
			}
		}
    	   
       });
        	
	    mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg)
            {
                  switch (msg.what)
                  {
                  case SEND_FB_FRIEND:
                	global_setting.close_progress_dialog();
                   	JSONObject result_json=null;
					try {
						result_json=new JSONObject( (String) msg.obj);
						if(result_json.has("error")){
    						Toast.makeText(FacebookFriendActivity.this, "error:"+result_json.getString("error"), Toast.LENGTH_LONG).show();
						}
						else{
    						Toast.makeText(FacebookFriendActivity.this, "add success", Toast.LENGTH_LONG).show();
			                Intent intent = new Intent(FacebookFriendActivity.this, CameraMenuActivity.class);
			                startActivity(intent);
							Log.d("addfriend","add success");
    						setResult(RESULT_OK);
    						finish();
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(FacebookFriendActivity.this, "error:"+e.getMessage().toString(), Toast.LENGTH_LONG).show();
					
					}              	  
                  break;
                  }
            }
         };

    }
    
    private String sendChooseFriends() throws JSONException {
	        String savedItems = "";
	        int count = this.mainListView.getAdapter().getCount();
	        for (int i = 0; i < count; i++) {
	            if (this.mainListView.isItemChecked(i)) {
	                if (savedItems.length() > 0) {
	                    savedItems += "," +"\""+friendArray.getJSONObject(i).getString("id")+"\"";
	                } else {
	                    savedItems += "\"" + friendArray.getJSONObject(i).getString("id")+"\"";
	                }
	            }
	        }
	        return "["+savedItems+"]";
	    }
    
    private void send_fb_friend_start(){
		global_setting.show_progress_dialog(FacebookFriendActivity.this, "Loading", "please wait...", true);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id1", global_setting.userid ));
		params.add(new BasicNameValuePair("id2_array", selected_friend ));
		new HttpApplication(global_setting.site_url+"friend/add_friend_array",params,mHandler,SEND_FB_FRIEND).startHttp();
    }
}
