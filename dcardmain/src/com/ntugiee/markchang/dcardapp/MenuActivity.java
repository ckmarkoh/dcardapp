package com.ntugiee.markchang.dcardapp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
//import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;


//import togeather.history.R;

public class MenuActivity extends Activity {
    
	private Button LogoutButton;
//    private Button LogoutButton;
    private Button SendButton;
    private Button ReceiveButton;
   // private Button SignUpButton;
    private Button AddFriendButton;
    private Button ConfirmFriendButton;

    
    private TextView username_text;

    private AlertDialog DialogAddFriend;
    private Button DialogAddFriendButton;
    private Button DialogCloseButton;
    private EditText DialogAddFriendEdit;
   // private String global_setting.userid;
 //   private boolean islogin=false;
    
    private View addFriendView;
    
    private static final int LOGIN=1;

	private Global_Setting global_setting;

	ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        global_setting = ((Global_Setting)getApplicationContext());


        username_text = (TextView) this.findViewById(R.id.MenuUserText);				
        LogoutButton = (Button) this.findViewById(R.id.ButtonLogout);
        SendButton = (Button) this.findViewById(R.id.ButtonSend);
        ReceiveButton = (Button) this.findViewById(R.id.ButtonReceive);
        //SignUpButton = (Button) this.findViewById(R.id.ButtonSignUp);
        AddFriendButton = (Button) this.findViewById(R.id.ButtonAddFriend);
        ConfirmFriendButton = (Button) this.findViewById(R.id.ButtonConfirmFriend);
		username_text.setText(global_setting.userid);


		LayoutInflater inflater = LayoutInflater.from(MenuActivity.this);
		
		addFriendView = inflater.inflate(R.layout.addfrienddialog,null);
		AlertDialog.Builder builder= new AlertDialog.Builder(MenuActivity.this);
		builder.setView(addFriendView);
		DialogAddFriend = builder.create();
		
		DialogAddFriendButton= (Button) addFriendView.findViewById(R.id.DialogAddFriendAdd);
		DialogCloseButton= (Button) addFriendView.findViewById(R.id.DialogAddFriendBack);
		DialogAddFriendEdit = (EditText) addFriendView.findViewById(R.id.DialogAddFriendEdit); //here

        
		DialogAddFriendButton.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View view) {
					HttpPost request = new HttpPost(global_setting.site_url+"friend/add_friend");
					//	Toast.makeText(AddFriendActivity.this, global_setting.site_url+"friend/add_friend", Toast.LENGTH_LONG).show();
						Log.d("login url",global_setting.site_url+"friend/add_friend");
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						
						params.add(new BasicNameValuePair("session", global_setting.session));
						params.add(new BasicNameValuePair("userid", global_setting.userid));
						
						params.add(new BasicNameValuePair("id1", global_setting.userid));
						params.add(new BasicNameValuePair("id2", DialogAddFriendEdit.getText().toString()));
						try {
							request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
							HttpResponse response = new DefaultHttpClient().execute(request);
							if(response.getStatusLine().getStatusCode() == 200){
								String raw_result = EntityUtils.toString(response.getEntity());
								Log.d("login",raw_result);
								JSONObject result_json= new JSONObject(raw_result);
								String result=result_json.getString("result");
								if(Boolean.parseBoolean(result)){
									//String userid=result_json.getString("userid");
									Toast.makeText(MenuActivity.this, "add success", Toast.LENGTH_LONG).show();
									//login_back(true,userid);
									DialogAddFriendEdit.setText("");
									DialogAddFriend.dismiss();
									//reload_af_friends();
								}
								else{
									String error=result_json.getString("error");
									Toast.makeText(MenuActivity.this, "add failed, error: "+error, Toast.LENGTH_LONG).show();
								}
								//String result = EntityUtils.toString(response.getEntity());
								//Toast.makeText(PhptestActivity.this, result, Toast.LENGTH_LONG).show();
							}
						} catch (Exception e) {
							Toast.makeText(MenuActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}
        		
	        	}
	        });        
		
		DialogCloseButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
				DialogAddFriend.dismiss();
        	}
        });        
		
		AddFriendButton.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View view) {
					DialogAddFriendEdit.setText("");
	        		DialogAddFriend.show();
	        	}
	        });        
        SendButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
	            	final Intent intent = new Intent(MenuActivity.this, MsgChooseFriendActivity.class);
	                startActivity(intent);
        		
        	}
        });
        ReceiveButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
	            	final Intent intent = new Intent(MenuActivity.this, ReceiveActivity.class);
	                startActivity(intent);
        	}
        });    
        ConfirmFriendButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
	            	final Intent intent = new Intent(MenuActivity.this, ConfirmFriendActivity.class);
	                startActivity(intent);
        	}
        });    
        /*SignUpButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        			if(!global_setting.islogin){
	            	final Intent intent = new Intent();
	            	intent.setClass(MenuActivity.this, SignUpActivity.class);
	                startActivity(intent);
        		}
        		else{
                    Toast.makeText(MenuActivity.this, "please logout", Toast.LENGTH_LONG).show();
        		}
        	}
        });  */  
        LogoutButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        			global_setting.userid="";
					global_setting.islogin=false;     
					global_setting.session="";
					setResult( RESULT_OK );
					finish();
        	}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
        case LOGIN:
        	//islogin=Boolean.parseBoolean(data.getExtras().getString("login"));
        		if(global_setting.islogin){
        			//global_setting.userid=data.getExtras().getString("name");
                	username_text.setText("Hello "+global_setting.userid);
        			//LoginButton.setText("Logout");

        		}
        		else{
        			global_setting.userid="";
        			username_text.setText("Please login");
        			//LoginButton.setText("Login");
        		}    		
        break;
        }
    }
    
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.activity_main, menu);
        
        return true;
    }*/
}
