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
	float lastNum;	//�ϴ�num������
	float min=0;	//���ı䷽�����Сֵ��С�ڸ�ֵ������ת
	float max=0;	//���ı䷽������ֵ�����ڸ�ֵ����ת
	long lastTime=0;
	long currentTime=0;
	
	///��ʼ�������ٴ�����
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
