package com.boelroy.joysticksimulator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ConnectActivity extends Activity {

	public static final String IP = "IP_FROM_URSE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        Button btn = (Button)findViewById(R.id.start);
        final EditText edt = (EditText)findViewById(R.id.editview);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				String ip = edt.getEditableText().toString();
				//Verify the IP is Valid or not
//				if(! isValidIp(ip)){
//					Toast.makeText(ConnectActivity.this, "Invalid ip address", Toast.LENGTH_SHORT).show();
//					return;
//				}
				
				intent.putExtra(IP, ip);
				intent.setClass(ConnectActivity.this, JoyStickSelect.class);
				ConnectActivity.this.startActivity(intent);
			}
		});
    }
    
//    private boolean isValidIp(String ip){
//    	String[] ipNum = ip.split(".");
//    	if(ipNum.length != 4)
//    		return false;
//    	
//    	for(int i = 0; i < ipNum.length; i++){
//    		try{
//    			int num = Integer.parseInt(ipNum[i]);
//    			if(!(num < 256 && num >= 0))
//        			return false;
//    		}
//    		catch(Exception e){
//    			return false;
//    		}
//    	}
//    	
//    	return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.connect, menu);
        return true;
    }
    
}
