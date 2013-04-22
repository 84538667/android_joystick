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
import android.os.Bundle;

public class CommonJoyStick extends Activity {
	String mIp;
	private Socket socket = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comonjoystick);
		//Intent intent= this.getIntent();
		
		Rudder rudder = (Rudder)findViewById(R.id.rudder);
		rudder.setRudderListener(new RudderListener() {
			
			@Override
			public void onSteeringWheelChanged(int action, int angle) {
				// TODO Auto-generated method stub
				if(angle >= 360 - 30 || angle < 30){
					sendMsg("right");
				}else if(angle >= 60 && angle < 120){
					sendMsg("upto");
				}else if(angle >= 150 && angle < 210){
					sendMsg("left");
				}else{
					sendMsg("down");
				}
			}
		});
	}
	
	private void sendMsg(final String direct){
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					socket = new Socket("192.168.1.35", 8885);
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
		                socket.close();
		            } catch (IOException e) {  
		                e.printStackTrace();  
		            }  
		        }
			}
		});
		t.start();
	}

}
