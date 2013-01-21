package com.ntugiee.markchang.msg;


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





//import togeather.history.R;

public class MenuActivity extends Activity {
    
	private Button LoginButton;
//    private Button LogoutButton;
    private Button ChatroomButton;
    private TextView username_text;
    private String username;
    private boolean islogin=false;
    
    
    private static final int LOGIN=1;

    
	ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        username_text = (TextView) this.findViewById(R.id.MenuUserText);				

        LoginButton = (Button) this.findViewById(R.id.ButtonLogin);
//        LogoutButton = (Button) this.findViewById(R.id.ButtonLogout);
        ChatroomButton = (Button) this.findViewById(R.id.ButtonChatroom);

/*        if(islogin){
        	username_text.setText("Hello "+username);
			LoginButton.setText("Logout");
        }
        else{*/
        	username_text.setText("Please login");
			LoginButton.setText("Login");
        
        
        ChatroomButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		if(islogin){
	            	final Intent intent = new Intent();
	            	intent.setClass(MenuActivity.this, MainActivity.class);
	            	Bundle bundle = new Bundle();
	            	bundle.putString("name", username);
	            	intent.putExtras(bundle);
	                startActivity(intent);
        		}
        		else{
                    Toast.makeText(MenuActivity.this, "please login", Toast.LENGTH_LONG).show();
        		}
        	}
        });
        
        
        LoginButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		if(islogin){
        			username="";
                	username_text.setText("Please login");
        			LoginButton.setText("Login");   
        			islogin=false;
        		}
        		else{
	            	final Intent intent = new Intent();
	            	intent.setClass(MenuActivity.this, PhptestActivity.class);
	            	startActivityForResult(intent, LOGIN);
        		}
        	}
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
        case LOGIN:
        	islogin=Boolean.parseBoolean(data.getExtras().getString("login"));
        		if(islogin){
        			username=data.getExtras().getString("name");
                	username_text.setText("Hello "+username);
        			LoginButton.setText("Logout");
        		}
        		else{
        			username="";
                	username_text.setText("Please login");
        			LoginButton.setText("Login");
        		}    		
        break;
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        
        return true;
    }
}
