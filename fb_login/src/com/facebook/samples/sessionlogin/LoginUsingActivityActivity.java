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

package com.facebook.samples.sessionlogin;

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
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class LoginUsingActivityActivity extends Activity {
    private static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?access_token=";

    private TextView textInstructionsOrLink;
    private Button buttonLoginLogout;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private Button getFriendButton;
	private Button getFriendButton2;
	String friend_result="";

   private ProfilePictureView profilePictureView;
    private TextView userNameView;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        buttonLoginLogout = (Button)findViewById(R.id.buttonLoginLogout);
        textInstructionsOrLink = (TextView)findViewById(R.id.instructionsOrLink);

        getFriendButton = (Button) findViewById(R.id.getFbFriendButton);
        getFriendButton2 = (Button) findViewById(R.id.getFbFriendButton2);
        userNameView = (TextView) findViewById(R.id.selection_user_name);
        profilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
        profilePictureView.setCropped(true);
        
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

        getFriendButton.setOnClickListener(new View.OnClickListener() {
        				public void onClick(View v) {
        					HttpPost request = new HttpPost("http://r444b.ee.ntu.edu.tw/~markchang/fb_friend.html");//.site_url+"login");
        					try {
        						HttpResponse response = new DefaultHttpClient().execute(request);
        						if(response.getStatusLine().getStatusCode() == 200){
        							friend_result = EntityUtils.toString(response.getEntity());
	
        							Log.d("raw_result_1",friend_result);
        							
        						}
        					} catch (Exception e) {
        						Toast.makeText(LoginUsingActivityActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
        						e.printStackTrace();
        						//Log.d("error",e.getMessage());
        					}
        				}
			
        			});
        
        
        getFriendButton2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			HttpPost request1 = new HttpPost("http://r444b.ee.ntu.edu.tw/~markchang/dctest/index.php/friend/get_fb_friend");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id1", "mark" ));
			params.add(new BasicNameValuePair("fb_friend_json", friend_result ));

			//params.add(new BasicNameValuePair("id1", "mark2" ));
			Log.d("param2","param2"+friend_result);

			try {
				request1.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				HttpResponse response1 = new DefaultHttpClient().execute(request1);
				if(response1.getStatusLine().getStatusCode() == 200){
					String raw_result = EntityUtils.toString(response1.getEntity());
					Log.d("result_2","result_2"+raw_result);
				}
			} catch (Exception e) {
				Toast.makeText(LoginUsingActivityActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
				}	
			}
			
        });	
        
        updateView();
			
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
