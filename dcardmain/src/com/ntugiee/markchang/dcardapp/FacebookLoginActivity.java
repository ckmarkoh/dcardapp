/**
 * Copyright 2012 Facebook
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ntugiee.markchang.dcardapp;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

import com.ntugiee.markchang.dcardapp.util.HttpApplication;


public class FacebookLoginActivity extends Activity {
    private static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?fields=id,name&access_token=";

    private TextView textInstructionsOrLink;
    private Button buttonLoginLogout;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private Button getFriendButton;
	private Button getFriendButton2;
	private Button syncFbFriend;
	private EditText signupEdit;
	private String friend_result="";
	private String access_token="";
	private String fbid="";
    private ProfilePictureView profilePictureView;
    private TextView userNameView;
	private Global_Setting global_setting;
	private Handler mHandler;
	
	protected static final int GET_FB_FRIEND = 0x00000001;
	protected static final int SYNC_FB_FRIEND = 0x00000002;
	protected static final int FB_LOGIN=0x00000003;
	protected static final int FB_SIGNUP=0x00000004;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.fb_login);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        buttonLoginLogout = (Button)findViewById(R.id.buttonLoginLogout);
        textInstructionsOrLink = (TextView)findViewById(R.id.instructionsOrLink);

        getFriendButton = (Button) findViewById(R.id.getFbFriendButton);
        getFriendButton2 = (Button) findViewById(R.id.getFbFriendButton2);
        userNameView = (TextView) findViewById(R.id.selection_user_name);
        profilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
        profilePictureView.setCropped(true);
        global_setting = ((Global_Setting)getApplicationContext());
        syncFbFriend = (Button) findViewById(R.id.syncFbFriend);
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        signupEdit  = (EditText) findViewById(R.id.SignUpID);
        
        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }

        
	    mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg)
            {
                  switch (msg.what)
                  {
                  case FB_LOGIN:
                	global_setting.close_progress_dialog();
                   	JSONObject result_json=null;
					try {
						result_json=new JSONObject( (String) msg.obj);
						if(result_json.has("error")){
    						Toast.makeText(FacebookLoginActivity.this, "error:"+result_json.getString("error"), Toast.LENGTH_LONG).show();
						}
						else if(Boolean.parseBoolean(result_json.getString("signup"))){
							Log.d("login","has signup");
						}
						else{
							Log.d("login","haven't signup");
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(FacebookLoginActivity.this, "error:"+e.getMessage().toString(), Toast.LENGTH_LONG).show();
					
					}              	  
                  break;
                  case FB_SIGNUP:
                	global_setting.close_progress_dialog();
					JSONObject result_json2 = null;
  					try {
  						 result_json2 = new JSONObject( (String) msg.obj);
						if(result_json2.has("error")){
    						Toast.makeText(FacebookLoginActivity.this, "error:"+result_json2.getString("error"), Toast.LENGTH_LONG).show();
						}
						else {
    						Toast.makeText(FacebookLoginActivity.this, "signup success", Toast.LENGTH_LONG).show();

						}
					} catch (JSONException e2) {
						e2.printStackTrace();
						Toast.makeText(FacebookLoginActivity.this, "error:"+e2.getMessage().toString(), Toast.LENGTH_LONG).show();
					
					}              	  

                  break;
                  case GET_FB_FRIEND:
					friend_result=(String) msg.obj;
					Log.d("raw_result_1",friend_result);
                 	JSONObject result_json1=null;
    				try {
    					result_json1=new JSONObject( friend_result);
    					if(result_json1.has("error")){
    						global_setting.close_progress_dialog();
    						Toast.makeText(FacebookLoginActivity.this, "error:"+result_json1.getString("error"), Toast.LENGTH_LONG).show();
    					}
    					else{
    						sync_fb_friend_start();
    					}
    				} catch (Exception e1) {
    					global_setting.close_progress_dialog();
    					e1.printStackTrace();
						Toast.makeText(FacebookLoginActivity.this, "error:"+e1.getMessage().toString(), Toast.LENGTH_LONG).show();
    				}
    				
    			  break;
                  case SYNC_FB_FRIEND:
    					global_setting.close_progress_dialog();
						Log.d("raw_result_2",(String) msg.obj);
	    		  break;
                  }
            }
         };
         
         
         getFriendButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					fb_user_login();
				}
         });     
         
         getFriendButton2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					fb_user_signup();
				}
         });      
         
         syncFbFriend.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					get_fb_friend_start();
				}
         });     
    }
    
    private void fb_user_login(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("fbid", fbid ));
		params.add(new BasicNameValuePair("fb_access", access_token ));
		new HttpApplication(global_setting.site_url+"user/fb_login",params,mHandler,FB_LOGIN).startHttp();
    }
    private void fb_user_signup(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("fbid", fbid ));
		params.add(new BasicNameValuePair("fb_access", access_token ));
		params.add(new BasicNameValuePair("id", signupEdit.getText().toString() ));
		new HttpApplication(global_setting.site_url+"user/fb_signup",params,mHandler,FB_SIGNUP).startHttp();
    }
    
    private void get_fb_friend_start(){
		global_setting.show_progress_dialog(FacebookLoginActivity.this, null, "同步facebook好友中", true);
		new HttpApplication(URL_PREFIX_FRIENDS+access_token,mHandler,GET_FB_FRIEND).startHttp();
    }
    
    private void sync_fb_friend_start(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id1", "mark" ));
		params.add(new BasicNameValuePair("fb_friend_json", friend_result ));
		new HttpApplication(global_setting.site_url+"friend/get_fb_friend",params,mHandler,SYNC_FB_FRIEND).startHttp();
    }
    
    
    @Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(statusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    private void updateView() {
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
            
        	
        	//textInstructionsOrLink.setText(URL_PREFIX_FRIENDS + session.getAccessToken());
        	access_token=session.getAccessToken();
            buttonLoginLogout.setText(R.string.logout);
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
            	
            	public void onClick(View view) { onClickLogout(); }
            });
        } else {
            //textInstructionsOrLink.setText(R.string.instructions);
            buttonLoginLogout.setText(R.string.login);
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { onClickLogin(); }
            });
        }
    }

    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, statusCallback);
        }
    }

    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
            onSessionStateChange(session, state, exception);

        }

        private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
            if (session != null && session.isOpened()) {
                if (state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
                 //   tokenUpdated();
                } else {
                    makeMeRequest(session);
                }
            }
        }
        private void makeMeRequest(final Session session) {
            Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (session == Session.getActiveSession()) {
                        if (user != null) {
                            profilePictureView.setProfileId(user.getId());
                            userNameView.setText(user.getName());
                            fbid=user.getId();
                        }
                    }
                    if (response.getError() != null) {
                        //handleError(response.getError());
                    }
                }
            });
            request.executeAsync();

        }
        
    }
    
}
