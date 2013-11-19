package com.refreshlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/** 
 * 文件名称:   FooterView.java 
 * 功能描述:  
 * 版本信息:   Copyright (c)2013 
 * 公司信息:   瑜旭网络
 * 开发人员:   yuyejiang
 * 版本日志:   1.0 
 * 创建时间:   2013年11月19日 上午11:59:01 
 * 
 * 修改历史: 
 * 时间         开发者      版本号    修改内容 
 * ------------------------------------------------------------------ 
 * 2013年11月19日   yuyejiang      1.0         1.0 Version 
 */
public class FooterView extends LinearLayout {
	private static final int NORMAL = 0;
	private static final int READY = 1;
	private static final int REFRESH = 2;
	private TextView textView;
	private ProgressBar progress;
	public FooterView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	private void initView(Context context) {
		// TODO Auto-generated method stub
		View view = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.layout_footer, null);
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(params);
		textView = (TextView)view.findViewById(R.id.load_more);
		progress = (ProgressBar) view.findViewById(R.id.footer_progress);
		addView(view);
	}
	
	public void changeView(int state){
		if(state == NORMAL){
			normalView();
		}else if(state == READY ){
			readyView();
		}else{
			refreshView();
		}
		invalidate();
	}

	private void refreshView() {
		// TODO Auto-generated method stub
		textView.setVisibility(View.GONE);
		progress.setVisibility(View.VISIBLE);
	}

	private void readyView() {
		// TODO Auto-generated method stub
		textView.setVisibility(View.VISIBLE);
		textView.setText("松开载入更多");
		progress.setVisibility(View.GONE);
	}

	private void normalView() {
		// TODO Auto-generated method stub
		textView.setVisibility(View.VISIBLE);
		textView.setText("查看更多");
		progress.setVisibility(View.GONE);
	}

}
