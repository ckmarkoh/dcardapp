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
    private static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?fields=id&access_token=";

    private TextView textInstructionsOrLink;
    private Button buttonLoginLogout;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
	//private Button getFriendButton;
	//private Button getFriendButton2;
	private Button syncFbFriend;

	private String friend_result="";
	private String access_token="";
    private ProfilePictureView profilePictureView;
    private TextView userNameView;
	private Global_Setting global_setting;
	private Handler mHandler;
	
	protected static final int GET_FB_FRIEND = 0x00000001;
	protected static final int SYNC_FB_FRIEND = 0x00000002;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.fb_login);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        buttonLoginLogout = (Button)findViewById(R.id.buttonLoginLogout);
        textInstructionsOrLink = (TextView)findViewById(R.id.instructionsOrLink);

        //getFriendButton = (Button) findViewById(R.id.getFbFriendButton);
        //getFriendButton2 = (Button) findViewById(R.id.getFbFriendButton2);
        userNameView = (TextView) findViewById(R.id.selection_user_name);
        profilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
        profilePictureView.setCropped(true);
        global_setting = ((Global_Setting)getApplicationContext());
        syncFbFriend = (Button) findViewById(R.id.syncFbFriend);
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

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
	                  // 顯示網路上抓取的資料
	                  case GET_FB_FRIEND:
						global_setting.close_progress_dialog();
						friend_result=(String) msg.obj;
						Log.d("raw_result_1",friend_result);
	                 	JSONObject result_json=null;
	    				try {
	    					result_json=new JSONObject( friend_result);
	    					if(result_json.has("error")){
		    					global_setting.close_progress_dialog();
	    						Toast.makeText(FacebookLoginActivity.this, "error:"+result_json.getString("error"), Toast.LENGTH_LONG).show();
	    					}
	    					else{
	    						sync_fb_friend_start();
	    					}
	    				} catch (Exception e) {
	    					global_setting.close_progress_dialog();
	    					e.printStackTrace();
    						Toast.makeText(FacebookLoginActivity.this, "error:"+e.getMessage().toString(), Toast.LENGTH_LONG).show();
	    				}
	    			  break;
	                  case SYNC_FB_FRIEND:
	    					global_setting.close_progress_dialog();
							Log.d("raw_result_2",(String) msg.obj);
		    		  break;
                }
            }
         };
         
         syncFbFriend.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					get_fb_friend_start();
				}
         });     
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
            
        	
        	textInstructionsOrLink.setText(URL_PREFIX_FRIENDS + session.getAccessToken());
        	access_token=session.getAccessToken();
            
            buttonLoginLogout.setText(R.string.logout);
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
            	
            	public void onClick(View view) { onClickLogout(); }
            });
        } else {
            textInstructionsOrLink.setText(R.string.instructions);
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
