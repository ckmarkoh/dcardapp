package com.ntugiee.markchang.cameratest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ColorPicker extends Activity {

	private Button okBtn;
	private SeekBar redSeekBar;
	private SeekBar greenSeekBar;
	private SeekBar blueSeekBar;
	private TextView redEditText;
	private TextView greenEditText;
	private TextView blueEditText;
	private TableLayout tableLayout;
	
	private int redProgress = 0;
	private int greenProgress = 0;
	private int  blueProgress = 0;
	
//	private int redValue = 0;
//	private int greenValue = 0;
//	private int blueValue = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colorpicker);
        
        //tableLayout = (TableLayout)findViewById(R.id.TableLayout01);
        okBtn = (Button)findViewById(R.id.Button01);
        
        //Seekbars
        redSeekBar = (SeekBar)findViewById(R.id.SeekBarRed);
        greenSeekBar = (SeekBar)findViewById(R.id.SeekBarGreen);
        blueSeekBar = (SeekBar)findViewById(R.id.SeekBarBlue);
        
        //EditTexts
        redEditText = (TextView)findViewById(R.id.EditTextRed);
        greenEditText = (TextView)findViewById(R.id.EditTextGreen);
        blueEditText = (TextView)findViewById(R.id.EditTextBlue);
      
        
        redSeekBar.setOnSeekBarChangeListener(OnSeekBarProgress);
        greenSeekBar.setOnSeekBarChangeListener(OnSeekBarProgress);
        blueSeekBar.setOnSeekBarChangeListener(OnSeekBarProgress);
        
        //okBtn.setBackgroundDrawable(Utility.resizeImage(this,R.drawable.picture, 32, 32));
        
        //set the maximum of seekbars as 100%
        redSeekBar.setMax(254);
        greenSeekBar.setMax(254);
        blueSeekBar.setMax(254);
        
        
    	okBtn.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		}
        	}
    	);
    }
    
    OnSeekBarChangeListener OnSeekBarProgress =
    	new OnSeekBarChangeListener() {

    	public void onProgressChanged(SeekBar s, int progress, boolean touch){
    	
        	if(touch){
    	
        	if(s == redSeekBar){
    		
    		redProgress = progress;
    		redEditText.setText(Integer.toString(redProgress));
        	}

        	if(s == greenSeekBar ){
    		greenProgress = progress;
    		greenEditText.setText(Integer.toString(greenProgress));
        	}

        	if(s == blueSeekBar ){
    		blueProgress = progress;
    		blueEditText.setText(Integer.toString(blueProgress));
        	}

        	int color = Color.rgb(redProgress, greenProgress, blueProgress);
    	
        	//tableLayout.getRootView().setBackgroundColor(color);

        	}
        	
    	}

    	public void onStartTrackingTouch(SeekBar s){

    	}

    	public void onStopTrackingTouch(SeekBar s){

    	}
    	};
    
}
