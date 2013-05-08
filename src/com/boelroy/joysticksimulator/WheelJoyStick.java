package com.boelroy.joysticksimulator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class WheelJoyStick extends Activity {

	final static String[] keysUp = {"KE_1U","KE_2U","KE_3U"};
	final static String[] keysDown = {"KE_1D","KE_2D","KE_3D"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
		this.setContentView(R.layout.activity_wheeljoystick);
		GravitySensor gs = new GravitySensor();
		gs.Listen((SensorManager)getSystemService(SENSOR_SERVICE), "192.168.1.4");
		
		TypedArray ta = this.getResources().obtainTypedArray(R.array.wheel_action_button);
		for(int i = 0; i < ta.length();i++){
			int rid = ta.getResourceId(i, -1);
			if(rid != -1){
				Button btn = (Button)this.findViewById(rid);
				final int index = i;
				btn.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						switch(event.getAction()){
							case MotionEvent.ACTION_DOWN:
								sendMsg(keysDown[index]);
								getVibator();
								break;
							case MotionEvent.ACTION_UP:
								sendMsg(keysUp[index]);
								break;
							default :
								break;
						}
						return true;
					}
				});
			}
		}
		ta.recycle();
	}
	
	
	public static void sendMsg(final String direct){
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Socket socket = null;
				try {
					socket = new Socket("192.168.1.4", 8886);
					PrintWriter out = new PrintWriter(new BufferedWriter(  
		                    new OutputStreamWriter(socket.getOutputStream())), true);
					
					out.println(direct);
					Thread.sleep(100);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {  
		            try {
		            	if(socket != null)
		            		socket.close();
		            } catch (IOException e) {  
		                e.printStackTrace();  
		            }  
		        }
			}
		});
		t.start();
	}
	
	private void getVibator(){
		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);  
        long [] pattern = {10,50};   // Í£Ö¹ ¿ªÆô 
        vibrator.vibrate(pattern,-1); 
	}
}
