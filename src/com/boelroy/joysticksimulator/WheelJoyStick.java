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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WheelJoyStick extends Activity {

	final static String[] keys = {"KE_1","KE_2","KE_3"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
		this.setContentView(R.layout.activity_wheeljoystick);
		GravitySensor gs = new GravitySensor();
		gs.Listen((SensorManager)getSystemService(SENSOR_SERVICE), "192.168.1.3");
		
		TypedArray ta = this.getResources().obtainTypedArray(R.array.wheel_action_button);
		for(int i = 0; i < ta.length();i++){
			int rid = ta.getResourceId(i, -1);
			if(rid != -1){
				Button btn = (Button)this.findViewById(rid);
				final int index = i;
				btn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						sendMsg(keys[index]);
						getVibator();
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
					socket = new Socket("192.168.1.35", 8886);
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
