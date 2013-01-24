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
    private Button SendButton;
    private Button ReceiveButton;
    private Button SignUpButton;

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
        SendButton = (Button) this.findViewById(R.id.ButtonSend);
        ReceiveButton = (Button) this.findViewById(R.id.ButtonReceive);
        SignUpButton = (Button) this.findViewById(R.id.ButtonSignUp);
        
		show_or_hide_button();
/*        if(islogin){
        	username_text.setText("Hello "+username);
			LoginButton.setText("Logout");
        }
        else{*/
        	username_text.setText("Please login");
			LoginButton.setText("Login");
        
        SendButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		if(islogin){
	            	final Intent intent = new Intent();
	            	intent.setClass(MenuActivity.this, SendActivity.class);
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
        ReceiveButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		if(islogin){
	            	final Intent intent = new Intent();
	            	intent.setClass(MenuActivity.this, ReceiveActivity.class);
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
        SignUpButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		if(!islogin){
	            	final Intent intent = new Intent();
	            	intent.setClass(MenuActivity.this, SignUpActivity.class);
	                startActivity(intent);
        		}
        		else{
                    Toast.makeText(MenuActivity.this, "please logout", Toast.LENGTH_LONG).show();
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
        			show_or_hide_button();
        		}
        		else{
	            	final Intent intent = new Intent();
	            	intent.setClass(MenuActivity.this, LoginActivity.class);
	            	startActivityForResult(intent, LOGIN);

        		}
        	}
        });
    }
    public void show_or_hide_button(){
    	if(islogin){
			SignUpButton.setVisibility(View.INVISIBLE);
			SendButton.setVisibility(View.VISIBLE);
			ReceiveButton.setVisibility(View.VISIBLE);
    	}
    	else{
			SignUpButton.setVisibility(View.VISIBLE);
			SendButton.setVisibility(View.INVISIBLE);
			ReceiveButton.setVisibility(View.INVISIBLE);    		
    	}
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
        			show_or_hide_button();

        		}
        		else{
        			username="";
                	username_text.setText("Please login");
        			LoginButton.setText("Login");
        			show_or_hide_button();
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
