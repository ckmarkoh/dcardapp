package com.ntugiee.markchang.longtouchbutton;



import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LongTouchActivity extends Activity {
	private Button ltButton;
	private int timeout_int;
	private CountDownTimer timer; 
	private ImageView imview;
	private TextView count_down;
	private OnTouchListener longTouchListenerDown;
	private OnTouchListener longTouchListenerUp;

	private OnClickListener upclickListener;
	private boolean isstart=false;
	private boolean hasread=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.long_touch);
		//ltButton=(Button)findViewById(R.id.long_touch_button);
		count_down=(TextView)this.findViewById(R.id.countdown_textview2);
		imview=(ImageView)this.findViewById(R.id.imageView1);


		timeout_int=4000;
		
		imview.setVisibility(View.INVISIBLE);

		
		timer= new CountDownTimer(timeout_int, 100) {
            public void onTick(long millisUntilFinished) {
            	Log.d("seconds remaining:", String.valueOf(millisUntilFinished));
            	count_down.setText(String.valueOf(millisUntilFinished)+"ms");
            }
            public void onFinish() {
            	//isstart=false;
 				Log.d("timer","timer stop");
            	count_down.setText("結束");
            	hasread=true;
                //final Intent intent = new Intent();
                //intent.setClass(LongTouchActivity.this, ShowActivity.class);
                //startActivity(intent);	
            }
         };
		
         


         
         longTouchListenerDown = new OnTouchListener() {
     		@Override
     		public boolean onTouch(View v, MotionEvent event) {
     			switch(event.getAction()){
     			case MotionEvent.ACTION_DOWN:
     				Log.d("action","down listener action_down");
     				if(!isstart){
     					isstart=true;
     	 				Log.d("timer","timer start");
     					timer.start();
     				}
     				if(!hasread){
     					imview.setVisibility(View.VISIBLE);
     				}
     			break;
     			case MotionEvent.ACTION_UP:
     				Log.d("action","down listener action_up");
     				//timer.cancel();
					imview.setVisibility(View.INVISIBLE);
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
     				/*if(!isstart){
     					isstart=true;
     	 				Log.d("timer","timer start");
     					timer.start();
     				}
					imview.setVisibility(View.VISIBLE);*/
     			break;
     			case MotionEvent.ACTION_UP:
     				Log.d("action","up listener action_up");
     				//timer.cancel();
					imview.setVisibility(View.INVISIBLE);
     			break;
     			}
     			return false;
     	    }
         };
         
         upclickListener=new OnClickListener() {
 	    	public void onClick(View view) {
 				//Toast.makeText(LongTouchActivity.this, "onclick", Toast.LENGTH_LONG).show();
 				imview.setVisibility(View.INVISIBLE);
 	    		}
 		    };	
 		    
		//ltButton.setOnTouchListener(longTouchListenerDown);
        //imview.setOnTouchListener(longTouchListenerDown);

        //ltButton.setOnClickListener(upclickListener);
		BindData();
		
	}
	
	
	private void BindData() {
	    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	    ListView listView = (ListView) findViewById(R.id.listView1);
	 
	    HashMap<String, String> item = new HashMap<String, String>();
	    item.put("id", "15");
	    item.put("name", "請按我");
	    list.add(item);
	 
	    listView.setAdapter(new MyBaseAdapter(this, list));
	 
	    // 點擊項目
	    listView.setOnItemClickListener(new OnItemClickListener() {
	 
	        public void onItemClick(AdapterView<?> arg0, View view,
	                int position, long id) {
	            Log.d("allenj", "ListViewItem = " + id);
	 
	        }
	    });
	}

	private class MyBaseAdapter extends BaseAdapter{
		private LongTouchActivity mainActivity;
		private LayoutInflater myInflater;
		private ArrayList<HashMap<String, String>> list = null;
		private ViewTag viewTag;
		 
		public MyBaseAdapter(LongTouchActivity context, ArrayList<HashMap<String, String>> list) {
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
		 // 取得listItem容器 view
		convertView = myInflater.inflate(R.layout.mylistview, null);
		
		   // 建構listItem內容view
		viewTag = new ViewTag( 
				(RelativeLayout)convertView.findViewById(R.id.RelativeLayout1),
				(TextView) convertView.findViewById(R.id.textView1)
				//(Button)convertView.findViewById(R.id.button1)
		);
				 
		   // 設置容器內容
		   convertView.setTag(viewTag);
		 
		    } else {
		        viewTag = (ViewTag) convertView.getTag();
		    }
		
		
			convertView.setOnTouchListener(longTouchListenerDown);
			//convertView.setOnClickListener(upclickListener);

		    viewTag.text1.setText(list.get(position).get("name"));
		    //viewTag.rlayout.setOnTouchListener(longTouchListener);
		    //viewTag.btn1.setOnTouchListener(longTouchListenerDown);
		    parent.setOnTouchListener(longTouchListenerUp);
		    //(new ItemButton_Click(this.mainActivity, position));
		     
		    return convertView;
		}
		 
		public class ViewTag {
		    TextView text1;
		    RelativeLayout rlayout;
		    //Button btn1;
		     
		    public ViewTag(RelativeLayout relativelayout,TextView textview1){//,Button button1 ) {
		        this.rlayout =relativelayout;
		        this.text1 = textview1;
		        //this.btn1 = button1;
		    }
		}
		
	}
}
