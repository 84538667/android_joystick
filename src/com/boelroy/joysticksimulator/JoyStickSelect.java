package com.boelroy.joysticksimulator;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class JoyStickSelect extends Activity {
	private ViewPager joyStickPager = null;
	private View commonJoyStick;
	private View wheelJoyStick;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joystickselect);
		joyStickPager = (ViewPager)findViewById(R.id.joy_select);
		initView();
	}
	
	private void initView(){
		LayoutInflater inflater = LayoutInflater.from(this);
		commonJoyStick = inflater.inflate(R.layout.joystick_item, null);
		wheelJoyStick = inflater.inflate(R.layout.joystick_item, null);
		
		Button commonButton = ((Button)commonJoyStick.findViewById(R.id.joysticktype));
		commonButton.setText("Common Model");
		commonButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(JoyStickSelect.this, CommonJoyStick.class);
				JoyStickSelect.this.startActivity(intent);
			}
		});
		Button wheelButton = (Button)wheelJoyStick.findViewById(R.id.joysticktype);
		wheelButton.setText("Wheel Model");
		
		List<View> joyStickList = new ArrayList<View>();
		joyStickList.add(commonJoyStick);
		joyStickList.add(wheelJoyStick);
		
		joyStickPager.setAdapter(new JoyStickAdapter(joyStickList));
	}
	
	
	
	public class JoyStickAdapter extends PagerAdapter{
		private List<View> mListView;
		public JoyStickAdapter(List<View> mListView){
			this.mListView = mListView;
		}
		//销毁Adapter中某一项时调用
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView(mListView.get(position));
			
		}
		//初始化Adapter中某一项时调用
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(mListView.get(position) );
			
			return mListView.get(position);
		}
		//得到Adapter中View的数量
		@Override
		public int getCount() {
			return mListView.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
	
	/**
	 * 自定义ViewPager的监听事件类
	 * @author Boelroy
	 */
	public class JoyStickChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		//在ViewPager滑动结束后调用
		@Override
		public void onPageSelected(int arg0) {
			
		}
	}
}
