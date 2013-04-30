package com.boelroy.joysticksimulator;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class GravitySensor {
	SensorManager sensorManager;
	TextView textView;
	String ip;
	float lastNum;	//上次num的数字
	float min=0;	//不改变方向的最小值，小于该值想向左转
	float max=0;	//不改变方向的最大值，大于该值向右转
	long lastTime=0;
	long currentTime=0;
	
	///开始监听加速传感器
	public void Listen(SensorManager sensorManager,String _ip)
	{
		this.sensorManager=sensorManager;
		Sensor sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	SensorEventListener sensorEventListener =new SensorEventListener(){
		public void onAccuracyChanged(android.hardware.Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		public void onSensorChanged(SensorEvent e) {
			currentTime=System.currentTimeMillis();
			if(currentTime-lastTime>10)
			{
	            @SuppressWarnings("deprecation")
				float num=e.values[SensorManager.DATA_Y];
	            WheelJoyStick.sendMsg(String.valueOf(num));
	            lastTime=currentTime;
			}
		}};
}
