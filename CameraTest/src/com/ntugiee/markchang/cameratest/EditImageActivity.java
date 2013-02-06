package com.ntugiee.markchang.cameratest;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditImageActivity extends Activity {
    private float x=0;
    private float y=0;
    private Button b1;
    public OnTouchListener panel_on_touch;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editpanel);
        b1 = new Button(this);
        b1.setText("Button1");
        
        MyCustomPanel view = new MyCustomPanel(EditImageActivity.this);

        

                

        addContentView(view, new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        
        addContentView(b1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        
        
        panel_on_touch= new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Choose which motion action has been performed
                switch(event.getAction())
                {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();                 
                    v.invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                }
                return true;
            }
        };
        view.setOnTouchListener(panel_on_touch);
        
        b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {    
				Log.d("click","click");
			}
		});

    }
    private class MyCustomPanel extends View {
        public MyCustomPanel(Context context) {
            super(context);
        }
        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            canvas.drawCircle(x,y,3,paint);
        }
    }

}