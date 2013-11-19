package com.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/** 
 * 文件名称:   RefreshListView.java 
 * 功能描述:  
 * 版本信息:   Copyright (c)2013 
 * 公司信息:   瑜旭网络
 * 开发人员:   yuyejiang
 * 版本日志:   1.0 
 * 创建时间:   2013年11月19日 下午2:36:03 
 * 
 * 修改历史: 
 * 时间         开发者      版本号    修改内容 
 * ------------------------------------------------------------------ 
 * 2013年11月19日   yuyejiang      1.0         1.0 Version 
 */
public class RefreshListView extends ListView implements OnScrollListener{
	
	private Scroller scroller ;
	private HeadView headView;
	private FooterView footerView;
	private int headViewHeight = 0;
	private RelativeLayout contentLayout;
	private float startY = -1;
	private boolean refreshable = true;
	private boolean refreshing = false;
	public RefreshListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	private void initView(Context context) {
		// TODO Auto-generated method stub
		scroller =  new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);
		
		headView = new HeadView(context);
		footerView = new FooterView(context);
		contentLayout = (RelativeLayout) headView.findViewById(R.id.content);
		
		headView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				headViewHeight = contentLayout.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
		addView(headView);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(startY == -1){
			startY = ev.getRawY();
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			
			break;
		case MotionEvent.ACTION_DOWN:
			startY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			float deltaY = ev.getRawY()-startY;
			startY = ev.getRawY();
			if(getFirstVisiblePosition() ==1&&(headView.getVisiableHeight()>0||deltaY >0)){
				updateHeadViewHeight(deltaY/1.8f);
			}else if(getFirstVisiblePosition() == 0){
				
			}
			break;

		default:
			break;
		} 
		return super.onTouchEvent(ev);
	}
	
	public void setRefreshable(boolean flag){
		refreshable = flag;
		if(!refreshable){
			contentLayout.setVisibility(View.INVISIBLE);
		}else{
			contentLayout.setVisibility(View.VISIBLE);
		}
	}
	
	private void updateHeadViewHeight(float deltaY) {
		// TODO Auto-generated method stub
		headView.setVisiableHeight((int)deltaY+headView.getVisiableHeight());
		if(refreshable&&!refreshing){
			if(deltaY > headViewHeight){
				headView.changeView(HeadView.READY);
			}else{
				headView.changeView(HeadView.NORMAL);
			}
		}
		setSelection(0);
	}
}
