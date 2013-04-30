package com.boelroy.joysticksimulator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class JoyStickSelect extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_joystickselect);
		ImageView common = (ImageView)findViewById(R.id.common_stick);
		common.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(JoyStickSelect.this, CommonJoyStick.class);
				JoyStickSelect.this.startActivity(intent);
			}
		});
		ImageView wheel = (ImageView)findViewById(R.id.wheel_stick);
		wheel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(JoyStickSelect.this, WheelJoyStick.class);
				JoyStickSelect.this.startActivity(intent);
			}
		});
	}
	
	
}
