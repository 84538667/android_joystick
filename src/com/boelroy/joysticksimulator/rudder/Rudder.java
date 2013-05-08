package com.boelroy.joysticksimulator.rudder;

import com.boelroy.joysticksimulator.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class Rudder extends SurfaceView implements Runnable,Callback{
	
	private SurfaceHolder mHolder;
    private boolean isStop = false;
    private Thread mThread;
    private Paint  mPaint;
    private Point  mRockerPosition; //摇杆位置
    private Point  mCtrlPoint = new Point(190,190);//摇杆起始位置
    private int    mWheelRadius = 120;//摇杆活动范围半径
    private RudderListener listener = null; //事件回调接口
    private float lastadian;
    public static final int ACTION_RUDDER = 1 , ACTION_ATTACK = 2; // 1：摇杆事件 2：按钮事件（未实现）
    
	public Rudder(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public Rudder(Context context, AttributeSet attribu){
		super(context, attribu);
        this.setKeepScreenOn(true);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mThread = new Thread(this);
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);//抗锯齿
        mRockerPosition = new Point(mCtrlPoint);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setZOrderOnTop(true);
        mHolder.setFormat(PixelFormat.TRANSPARENT);//设置背景透明
	}
	  
	  
	public Rudder(Context context, AttributeSet attribu,int defStyle){
		super(context,attribu);
	}
	
	public void setRudderListener(RudderListener rockerListener) {
        listener = rockerListener;
    }

    @Override
    public void run() {
        Canvas canvas = null;
        while(!isStop) {
            try {
                canvas = mHolder.lockCanvas();
                canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);//清除屏幕
                mPaint.setColor(Color.TRANSPARENT);
                canvas.drawCircle(mCtrlPoint.x, mCtrlPoint.y, mWheelRadius, mPaint);//绘制范围
                mPaint.setColor(Color.RED);
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.direct_button);
                int height = bmp.getHeight();
                int width = bmp.getWidth();
                canvas.drawBitmap(bmp, mRockerPosition.x - width / 2, mRockerPosition.y - height/ 2, mPaint);//绘制摇杆
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(canvas != null) {
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isStop = true;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int len = MathUtils.getLength(mCtrlPoint.x, mCtrlPoint.y, event.getX(), event.getY());
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //如果屏幕接触点不在摇杆挥动范围内,则不处理
            if(len > mWheelRadius) {
                return true;
            }
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(len <= mWheelRadius) {
                //如果手指在摇杆活动范围内，则摇杆处于手指触摸位置
                mRockerPosition.set((int)event.getX(), (int)event.getY());
                
            }else{
                //设置摇杆位置，使其处于手指触摸方向的 摇杆活动范围边缘
                mRockerPosition = MathUtils.getBorderPoint(mCtrlPoint, new Point((int)event.getX(), (int)event.getY()), mWheelRadius);
            }
            if(listener != null) {
                float radian = MathUtils.getRadian(mCtrlPoint, new Point((int)event.getX(), (int)event.getY()));
                lastadian = radian;
                if(len >= 0)
                	listener.onSteeringWheelChanged(ACTION_RUDDER,Rudder.this.getAngleCouvert(radian),event);
                else
                	listener.onSteeringWheelChanged(ACTION_RUDDER,-1,event);
            }
        }
        //如果手指离开屏幕，则摇杆返回初始位置
        if(event.getAction() == MotionEvent.ACTION_UP) {
        	listener.onSteeringWheelChanged(ACTION_RUDDER,Rudder.this.getAngleCouvert(lastadian),event);
            mRockerPosition = new Point(mCtrlPoint);
        }
        return true;
    }
    
    //获取摇杆偏移角度 0-360°
    private int getAngleCouvert(float radian) {
        int tmp = (int)Math.round(radian/Math.PI*180);
        if(tmp < 0) {
            return -tmp;
        }else{
            return 180 + (180 - tmp);
        }
    }
    
    //回调接口
    public interface RudderListener {
        void onSteeringWheelChanged(int action,int angle,MotionEvent e);
    }
}

