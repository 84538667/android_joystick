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
	final static String[] keysUp = {"KE_YU","KE_XU","KE_AU","KE_BU","KE_SU","KE_EU"};
	final static String[] keysDonw = {"KE_YD","KE_XD","KE_AD","KE_BD","KE_SD","KE_ED"};
	String lastDirect = "";
	
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
			public void onSteeringWheelChanged(int action, int angle, MotionEvent e) {
				// TODO Auto-generated method stub
				switch (e.getAction()){
				case MotionEvent.ACTION_MOVE:
					if(angle >= 360 - 30 || angle < 30){
						if(!lastDirect.equals("RIGHD")){
							if(!lastDirect.equals(""))
								sendMsg(lastDirect.substring(0, 4) +"U");
							
							sendMsg("RIGHD");
							getVibator();
							lastDirect = "RIGHD";
						}
					}else if(angle >= 60 && angle < 120){
						if(!lastDirect.equals("UPTOD")){
							if(!lastDirect.equals(""))
								sendMsg(lastDirect.substring(0, 4) +"U");
							sendMsg("UPTOD");
							getVibator();
							lastDirect = "UPTOD";
						}
					}else if(angle >= 150 && angle < 210){
						if(!lastDirect.equals("LEFTD")){
							if(!lastDirect.equals(""))
								sendMsg(lastDirect.substring(0, 4) +"U");
							sendMsg("LEFTD");
							getVibator();
							lastDirect = "LEFTD";
						}
					}else{
						if(!lastDirect.equals("DOWND")){
							if(!lastDirect.equals(""))
								sendMsg(lastDirect.substring(0, 4) +"U");
							sendMsg("DOWND");
							getVibator();
							lastDirect = "DOWND";
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					sendMsg(lastDirect.substring(0, 4) +"U");
					lastDirect = "";
					break;
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
					socket = new Socket("192.168.1.4", 8885);
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
					// TODO Auto-generated catch blockwz
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
