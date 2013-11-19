package com.refreshlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/** 
 * 文件名称:   HeadView.java 
 * 功能描述:  
 * 版本信息:   Copyright (c)2013 
 * 公司信息:   瑜旭网络
 * 开发人员:   yuyejiang
 * 版本日志:   1.0 
 * 创建时间:   2013年11月19日 上午11:59:48 
 * 
 * 修改历史: 
 * 时间         开发者      版本号    修改内容 
 * ------------------------------------------------------------------ 
 * 2013年11月19日   yuyejiang      1.0         1.0 Version 
 */
public class HeadView extends LinearLayout {
	private RotateAnimation upAnimation,downAnimation;
	private TextView statusText,refreshTime;
	private ImageView image;
	private ProgressBar progress;
	public static final int NORMAL = 0;
	public static final int READY = 1;
	public static final int REFRESH =2;
	private LinearLayout container;
	private int status = NORMAL;
	public HeadView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		// TODO Auto-generated method stub
		container = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.layout_head, null);
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,0);
		
		upAnimation = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(1000);
		upAnimation.setFillAfter(false);
		
		downAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		downAnimation.setDuration(1000);
		downAnimation.setFillAfter(false);
		
		statusText = (TextView) container.findViewById(R.id.pull_to_refresh);
		refreshTime = (TextView) container.findViewById(R.id.refresh_time);
		image = (ImageView)container.findViewById(R.id.iamge);
		progress = (ProgressBar)container.findViewById(R.id.head_progress);
		
		addView(container,params);
	}
	
	public void changeView(int state){
		if(state == status)
			return ;
		if(state == NORMAL){
			if(status == READY){
				image.startAnimation(downAnimation);
			}else{
				image.clearAnimation();
			}
			normalView();
		}else if(state == READY ){
			image.clearAnimation();
			image.startAnimation(upAnimation);
			readyView();
		}else{
			refreshView();
		}
	}
	
	public int getVisiableHeight(){
		return container.getHeight();
	}
	
	public void setVisiableHeight(int height){
		if(height<0){
			height = 0;
		}
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, height);
		container.setLayoutParams(params);
	}

	private void refreshView() {
		// TODO Auto-generated method stub
		statusText.setText("正在加载");
		refreshTime.setText("刚刚");
		image.setVisibility(View.GONE);
		progress.setVisibility(View.VISIBLE);
	}

	private void readyView() {
		// TODO Auto-generated method stub
		statusText.setText("松开刷新数据");
		refreshTime.setText("刚刚");
		image.setAnimation(upAnimation);
	}

	private void normalView() {
		// TODO Auto-generated method stub
		statusText.setText("下拉刷新");
		refreshTime.setText("刚刚");
	}
	
}
