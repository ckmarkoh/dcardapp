package com.ntugiee.markchang.dcardapp;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.content.Intent;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import com.ntugiee.markchang.dcardapp.ShowActivity.DownloadImgRunnable;
import com.ntugiee.markchang.dcardapp.ShowActivity.DownloadImgRunnable.FlushedInputStream;
import com.ntugiee.markchang.dcardapp.util.HttpApplication;





//import togeather.history.R;


public class ReceiveActivity extends Activity {
    
	public  Button ReloadButton;
    private Button BackButton;
    public  Button ReceiveClearBut;
	//private TextView selected_text;
   // private TextView global_setting.userid_text;
    public String username;
    //private String selected_msg;
    public static SimpleAdapter sAdapter;
    public static ListView mListView;

    public static JSONArray msg_json_array;
    public static JSONObject this_item=null;
    //public static ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>();

    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
   // private MyTimerTask t;
  //  private int times;
    //private Timer t ;
 //   public static OnItemLongClickListener listener;
	//public static ReceiveActivity mThis = null;

	private Global_Setting global_setting;

	private Handler mHandler;
	private OnTouchListener longTouchListenerDown;
	private OnTouchListener longTouchListenerUp;
	private ImageView imview;
	
	
	private boolean isstart=false;
	//private boolean hasread=false;
	private CountDownTimer timer; 
	private TextView count_down;
	private TextView count_down_item;
	
	private Bitmap bitmap=null;

	
    protected static final int DOWNLOAD_SUCCESS = 0x00000001;
    protected static final int DOWNLOAD_ERROR = 0x00000002;
    protected static final int UPDATE_STATUS = 0x00000003;
    protected static final int READITEM_CHECK = 0x00000004;

	private int current_timeout;
	private int current_id;
	private String current_msg;
	private int current_position;
	
	private RelativeLayout receivewrapper;
	
	private ProgressBar receiveprogress;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.receive);
        
        global_setting = ((Global_Setting)getApplicationContext());

        
        ReloadButton = (Button) this.findViewById(R.id.ReceiveReloadBut);
        
        ReceiveClearBut = (Button) this.findViewById(R.id.ReceiveClearBut);
        BackButton = (Button) this.findViewById(R.id.ReceiveActBack);

        mListView = (ListView) this.findViewById(R.id.msglist);
        
		count_down=(TextView)this.findViewById(R.id.CountDownView);
		
		imview=(ImageView)this.findViewById(R.id.showImg);

		
		receivewrapper=(RelativeLayout) this.findViewById(R.id.ReceiveImgWrapper);
			
		
		receivewrapper.setVisibility(View.INVISIBLE);

		receiveprogress=(ProgressBar) this.findViewById(R.id.ReceiveImgProgress);
		
        BackButton.setOnClickListener( new View.OnClickListener() {
			public void onClick( View v ) {    
				setResult( RESULT_OK );
				finish();
			}
		});
        
        ReloadButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {   
				//mList.clear();
				get_msg();
			}
		});
        
        
		

        
        
        ReceiveClearBut.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {  
		    	HttpPost request = new HttpPost(global_setting.site_url+"msg/receiver_delete");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("session", global_setting.session));
				params.add(new BasicNameValuePair("userid", global_setting.userid));
				params.add(new BasicNameValuePair("receiver", global_setting.userid));
				Log.d("url",global_setting.site_url+"msg/receiver_delete");
				try {
					request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					HttpResponse response = new DefaultHttpClient().execute(request);
					if(response.getStatusLine().getStatusCode() == 200){
						String result = EntityUtils.toString(response.getEntity());
						//Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
						Log.d("success",result);
						get_msg();
					}
				} catch (Exception e) {
					Toast.makeText(ReceiveActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
					//Log.d("error",e.getMessage());
				}
			}
		});    
        /*listener = new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                long id) {      	
                try {
                	 this_item = msg_json_array.getJSONObject(position);
                        int this_status = Integer.parseInt(this_item.getString("status"));
                		Log.d("this_status","long_click");
                        if(this_status==0){
                        	try {
								readitem(this_item.getString("id"),this_item.getString("message"),this_item.getString("timeout"));
							} catch (JSONException e) {
			                    Toast.makeText(ReceiveActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
							}

        			        return true;
                        }                    
                } catch (JSONException e) {
                        Toast.makeText(ReceiveActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
        		//Log.d("this_status","long_click_false");

				return false;
            }
          };*/
          
          

          
          longTouchListenerDown = new OnTouchListener() {
       		@Override
       		public boolean onTouch(View v, MotionEvent event) {
       			switch(event.getAction()){
       			case MotionEvent.ACTION_DOWN:
       				Log.d("action","down listener action_down");
    		        MyBaseAdapter.ViewTag viewTag = (MyBaseAdapter.ViewTag) v.getTag();
    		        HashMap<String, String> item= (HashMap<String, String>) mListView.getAdapter().getItem(viewTag.position);//TODO
       				if(viewTag.canread){
	    		        if(!isstart){
	       					isstart=true;
	        		        current_position=viewTag.position;
	        		        current_id=Integer.valueOf(item.get("id"));
	        		        current_msg=item.get("message");
	        		        current_timeout=Integer.valueOf(item.get("timeout"));
	        		        count_down_item=(TextView) viewTag.text_countdown;
	        		        //timeout_int= 
	        		        //timer.start();
	        		        checkitem_start(String.valueOf(current_id));
	       					receivewrapper.setVisibility(View.VISIBLE);
	       					receiveprogress.setVisibility(View.VISIBLE);
	       				}
	       				else{
	       					if(Integer.valueOf(item.get("id"))==current_id){
	           					receivewrapper.setVisibility(View.VISIBLE);
	       					}
	       				}
       				}
       					//if(!hasread){
       				//}
       			break;
       			case MotionEvent.ACTION_UP:
       				Log.d("action","down listener action_up");
       				//timer.cancel();
  					receivewrapper.setVisibility(View.INVISIBLE);
       			break;
       			}
       			return false;
       	    }
           };
           
           longTouchListenerUp = new OnTouchListener() {
       		@Override
       		public boolean onTouch(View v, MotionEvent event) {
   				Log.d("action","getAction()"+event.getAction());
       			switch(event.getAction()){
       			case MotionEvent.ACTION_DOWN:
       				Log.d("action","up listener action_down");
       			break;
       			case MotionEvent.ACTION_UP:
       				Log.d("action","up listener action_up");
       				//timer.cancel();
  					receivewrapper.setVisibility(View.INVISIBLE);
       			break;
       			}
       			return false;
       	    }
           };
           
           mHandler = new Handler() {
               @Override
               public void handleMessage(Message msg)
               {
                   switch (msg.what)
                   {
          /*         case UPDATE_STATUS:
                 	  pDialog.setProgress((Integer) msg.obj);
                 	  break;
                   // 顯示網路上抓取的資料*/
                   case READITEM_CHECK:
                 	  checkitem_end((String) msg.obj);
                 	  break;
                   case DOWNLOAD_ERROR:
                 	  Toast.makeText(ReceiveActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                 	  break;
                   case DOWNLOAD_SUCCESS:
                  	  loaditem_end((String) msg.obj);

                      
                      break;
                   }
               }
            };
          
    }
    
    public void get_msg(){
    	HttpPost request = new HttpPost(global_setting.site_url+"msg/receiver_get");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("session", global_setting.session));
		params.add(new BasicNameValuePair("userid", global_setting.userid));
		
		params.add(new BasicNameValuePair("receiver", global_setting.userid));
		Log.d("url",global_setting.site_url+"receiver_get");
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200){
				String result = EntityUtils.toString(response.getEntity());
				//Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
				Log.d("success",result);
				JSONObject result_json = new JSONObject(  result);
				if(result_json.has("error")){
					Toast.makeText(ReceiveActivity.this, "error:"+result_json.getString("error"), Toast.LENGTH_LONG).show();
				}
				else{
				     ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>();
					msg_json_array = new JSONArray(result_json.getString("content"));
					for(int i=0;i<msg_json_array.length();i++){
						JSONObject jso=msg_json_array.getJSONObject(i);
		//				Log.d("msg",json_array.getJSONObject(i).getString("name")+json_array.getJSONObject(i).getString("msg"));
				        HashMap<String,String> item = new HashMap<String,String>();
				        item.put("sender", jso.getString("sender"));
				        item.put("receiver", jso.getString("receiver"));
			            item.put("status",jso.getString("status"));
				        item.put("time",jso.getString("time"));
				        item.put("id",jso.getString("id"));
				        item.put("message",jso.getString("message"));
				        item.put("timeout",jso.getString("timeout"));
				        item.put("type",jso.getString("type"));

				        mList.add(item); 
			        }
					mListView.setAdapter( new MyBaseAdapter(ReceiveActivity.this, mList));
				}
			}
		} catch (Exception e) {
			Toast.makeText(ReceiveActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			//Log.d("error",e.getMessage());
		}
	}
    

	/*
    public static void addNewItem(JSONObject jso) throws JSONException{//String sender,String status,String time,String type,String id) {

    }*/  
    public void checkitem_start(String id){
		Log.d("status","checkitem start");
		List<NameValuePair> params = new ArrayList<NameValuePair>();		
		params.add(new BasicNameValuePair("session", global_setting.session));
		params.add(new BasicNameValuePair("userid", global_setting.userid));
		params.add(new BasicNameValuePair("id", id));
		
		new HttpApplication(global_setting.site_url+"msg/open_item",params,mHandler,READITEM_CHECK).startHttp();

    }

    public void checkitem_end(String raw_result){
		Log.d("status","checkitem end");

		try {
				JSONObject result_json= new JSONObject(raw_result);
				String result=result_json.getString("result");
				Log.d("status","result:"+result);
				if(Boolean.parseBoolean(result)){
					loaditem_start(current_msg);
					}
				else{
	            	get_msg();
					String error=result_json.getString("error");
					Toast.makeText(ReceiveActivity.this, "error: "+error, Toast.LENGTH_LONG).show();
					}
			} catch (Exception e) {
			Toast.makeText(ReceiveActivity.this, "error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
    }
    public void loaditem_start(String message){
		Log.d("status","loaditem start");

        Thread t = new Thread(new DownloadImgRunnable(mHandler,global_setting.site_base_url+"img/"+message+".jpg"));
        t.start();			
    	
    }
    public void loaditem_end(String raw_result){
		Log.d("status","loaditem end");
		Log.d("timer","timer start");
        imview.setImageBitmap(bitmap);
        receiveprogress.setVisibility(View.INVISIBLE);
  		timer= new CountDownTimer(current_timeout*1000, 100) {
            public void onTick(long millisUntilFinished) {
            	Log.d("seconds remaining:", String.valueOf(millisUntilFinished));
            	
            	count_down_item.setText(String.valueOf((float)millisUntilFinished/1000).substring(0, 3)+"秒");
            	count_down.setText(String.valueOf((float)millisUntilFinished/1000).substring(0, 3)+"秒");
            }
            public void onFinish() {
            	imview.setImageResource(R.drawable.white_img);
            	isstart=false;
 				Log.d("timer","timer stop");
            	count_down.setText("結束");
            	count_down_item.setText("");
            	count_down_item=null;
            	get_msg();
            	receivewrapper.setVisibility(View.INVISIBLE);
            }
         };
        timer.start();
		Log.d("timer","timeout:"+current_timeout);
    }

	@Override
	public void onResume(){
		 super.onResume();
		 //mThis = this;
		 get_msg();
        Log.d("on_resume","on_resume");

        
	}
	@Override
	public void onPause(){
		 super.onPause();
		 //mThis = null;
	}
	 


	private class MyBaseAdapter extends BaseAdapter{
		private ReceiveActivity mainActivity;
		private LayoutInflater myInflater;
		private ArrayList<HashMap<String, String>> list = null;
		private ViewTag viewTag;
		 
		public MyBaseAdapter(ReceiveActivity context, ArrayList<HashMap<String, String>> list) {
		    this.mainActivity = context;
		    this.myInflater = LayoutInflater.from(context);
		    this.list = list;
		}
		 
		public int getCount() {
		    return list.size();
		}
		 
		public Object getItem(int position) {
		    return list.get(position);
		}
		 
		public long getItemId(int position) {
		    return Long.valueOf(list.get(position).get("id"));
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = myInflater.inflate(R.layout.receive_item, null);
				viewTag = new ViewTag( 
						(RelativeLayout)convertView.findViewById(R.id.ReceiveRelative),
						(TextView) convertView.findViewById(R.id.msgiSender),
						(TextView) convertView.findViewById(R.id.msgiStatus),
						(TextView) convertView.findViewById(R.id.msgiTime),
						(TextView) convertView.findViewById(R.id.countdown_textview2),
						(ImageView) convertView.findViewById(R.id.imgStatus)

				);
				convertView.setTag(viewTag);
		    } else {
		        viewTag = (ViewTag) convertView.getTag();
		    }
		
			
		    viewTag.text_time.setText(list.get(position).get("time"));
		    viewTag.position=position;

			
	        if(Integer.parseInt(list.get(position).get("status"))==0){
	        	viewTag.im_status.setImageResource(R.drawable.star);
	        	viewTag.text_status.setText("unread");
	        	viewTag.canread=true;
	        }
	        else{
	        	viewTag.text_status.setText("read");
	        	viewTag.canread=false;
	        	viewTag.im_status.setImageResource(R.drawable.star_shooting);
	        }
        	Log.d("list type",list.get(position).get("type"));

	        if(list.get(position).get("type").endsWith("sender")){
	        	Log.d("list type sender",list.get(position).get("type"));
	        	viewTag.im_status.setImageResource(R.drawable.star_arrow);
			    viewTag.text_sender.setText(list.get(position).get("receiver"));
	        	viewTag.canread=false;
	        }
	        else{
			    viewTag.text_sender.setText(list.get(position).get("sender"));
	        }
			convertView.setOnTouchListener(longTouchListenerDown);


		    parent.setOnTouchListener(longTouchListenerUp);
		     
		    return convertView;
		}
		 
		public class ViewTag {
		     TextView text_sender;
		     TextView text_status;
		     TextView text_time;
		     TextView text_countdown;
		     ImageView im_status;
		     boolean canread;
		     //String message;
		     //int timeout;
		     //int id;
		     int position;

		     RelativeLayout rlayout;
		     
		    public ViewTag(RelativeLayout relativelayout,TextView sender,TextView status,TextView time,TextView countdown,ImageView imstatus){//,Button button1 ) {
		        this.rlayout =relativelayout;
		        this.text_sender = sender;
		        this.text_status = status;
		        this.text_time = time;
		        this.text_countdown=countdown;
		        this.im_status=imstatus;
		    }

		}
	}
	class DownloadImgRunnable implements Runnable {
	      //String strTxt = null;
	      Handler handler= null;
		   private String uriAPI;// = "http://r444b.ee.ntu.edu.tw/~markchang/dctest/index.php/user/login";
		   //private String msg;
	      // 建構子，設定要傳的字串
	      public DownloadImgRunnable(Handler hd ,String url){//,String m)  {
	           handler = hd;
			   uriAPI=url;
			   //msg = m;
	      }
	      @Override
	      public void run() {	
  //public static Bitmap getBitmap(String url) {
			       // Bitmap bm = null;
			        try {
			            URL aURL = new URL(uriAPI);
			            URLConnection conn = aURL.openConnection();
			            //conn.connect();
			            InputStream is = conn.getInputStream();
			            BufferedInputStream bis = new BufferedInputStream(is);
			            bitmap = BitmapFactory.decodeStream(new FlushedInputStream(is));
			            bis.close();
			            is.close();
				        handler.obtainMessage(DOWNLOAD_SUCCESS, "download complete").sendToTarget();
			        } catch (Exception e) {
				        handler.obtainMessage(DOWNLOAD_ERROR, e.getMessage().toString() ).sendToTarget();

//			            e.printStackTrace();
			        } 
			       // return bm;
			    }
	
		    class FlushedInputStream extends FilterInputStream {
		        public FlushedInputStream(InputStream inputStream) {
		            super(inputStream);
		        }
		        @Override
		        public long skip(long n) throws IOException {
		            long totalBytesSkipped = 0L;
		            while (totalBytesSkipped < n) {
		                long bytesSkipped = in.skip(n - totalBytesSkipped);
		                if (bytesSkipped == 0L) {
		                    int b = read();
		                    if (b < 0) {
		                        break; // we reached EOF
		                    } else {
		                        bytesSkipped = 1; // we read one byte
		                    }
		                }
		                totalBytesSkipped += bytesSkipped;
		            }
		            return totalBytesSkipped;
		        }
		    }
	}
}
