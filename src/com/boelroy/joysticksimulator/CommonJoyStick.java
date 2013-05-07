package com.boelroy.joysticksimulator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.boelroy.joysticksimulator.rudder.Rudder;
import com.boelroy.joysticksimulator.rudder.Rudder.RudderListener;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class CommonJoyStick extends Activity {
	String mIp;
	final static String[] keysUp = {"KE_Y","KE_X","KE_AU","KE_BU","KE_SU","KEY_EU"};
	final static String[] keysDonw = {"KE_Y","KE_X","KE_AD","KE_BD","KE_SD","KEY_ED"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
		setContentView(R.layout.activity_comonjoystick);
		//Intent intent= this.getIntent();
		
		
		Rudder rudder = (Rudder)findViewById(R.id.rudder);
		rudder.setRudderListener(new RudderListener() {
			
			@Override
			public void onSteeringWheelChanged(int action, int angle) {
				// TODO Auto-generated method stub
				if(angle >= 360 - 30 || angle < 30){
					sendMsg("righ");
				}else if(angle >= 60 && angle < 120){
					sendMsg("UPTO");
				}else if(angle >= 150 && angle < 210){
					sendMsg("LEFT");
				}else{
					sendMsg("DOWN");
				}
			}
		});
		
		TypedArray ta = this.getResources().obtainTypedArray(R.array.action_button);
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
								sendMsg(keysDonw[index]);
								getVibator();
								break;
							case MotionEvent.ACTION_UP:
								sendMsg(keysUp[index]);
								getVibator();
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
					socket = new Socket("192.168.1.3", 8885);
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
        long [] pattern = {10,50};   // Í£Ö¹ ¿ªÆô Í£Ö¹ ¿ªÆô   
        vibrator.vibrate(pattern,-1); 
	}

}
