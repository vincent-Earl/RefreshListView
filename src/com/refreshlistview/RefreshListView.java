package com.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
	private static final String tag = "RefreshListView";
	private static final int SCROLL_BACK_HEAD = 0;
	private static final int SCROLL_BACK_FOOT = 1;
	private Scroller scroller ;
	private HeadView headView;
	private FooterView footerView;
	private int headViewHeight = 0,totalItemCount;
	private RelativeLayout contentLayout;
	private float startY = -1;
	private boolean refreshable = true;
	private boolean refreshing = false;
	private int scrollBack;
	private IRefreshListViewListener refreshListener;
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
//		footerView = new FooterView(context);
		contentLayout = (RelativeLayout) headView.findViewById(R.id.content);
		addHeaderView(headView);
		headView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				headViewHeight = contentLayout.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
		Log.d(tag, "getFirstVisiblePosition  initview"+getFirstVisiblePosition());
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.totalItemCount = totalItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		super.setAdapter(adapter);
	}
 
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if(scroller.computeScrollOffset()){
			if(scrollBack == SCROLL_BACK_HEAD){
				headView.setVisiableHeight(scroller.getCurrY());
			}
			postInvalidate();
		}
		super.computeScroll();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(startY == -1){
			startY = ev.getRawY();
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			startY = -1;
			Log.d(tag, "startY =="+startY +"; ACTION_UP");
			if(getFirstVisiblePosition() ==0){
				if(refreshable&&headView.getVisiableHeight()>headViewHeight){
					refreshing = true;
					headView.changeView(HeadView.REFRESH);
					if(refreshListener != null){
						refreshListener.refresh();
					}
				}
				resetHeaderHeight();
			}
			break;
		case MotionEvent.ACTION_DOWN:
			startY = ev.getRawY();
			Log.d(tag, "startY =="+startY +"; ACTION_DOWN");
			break;
		case MotionEvent.ACTION_MOVE:
			float deltaY = ev.getRawY()-startY;
			startY = ev.getRawY();
			Log.d(tag, "deltaY =="+deltaY +"; ACTION_MOVE");
			Log.d(tag, "startY =="+startY +"; ACTION_MOVE");
			if(getFirstVisiblePosition() == 0&&(headView.getVisiableHeight()>0||deltaY >0)){
				Log.d(tag, "getFirstVisiblePosition   === 1");
				updateHeadViewHeight(deltaY/1.8f);
			}else if(getFirstVisiblePosition() == 0){
				Log.d(tag, "getFirstVisiblePosition   === 0");
			}
			break;

		default:
			break;
		} 
		return super.onTouchEvent(ev);
	}
	
	private  void resetHeaderHeight() {
		// TODO Auto-generated method stub
		int height = headView.getVisiableHeight();
		if(height == 0 ||(refreshing&&height <= headViewHeight))
			return;
		int finalHeight = 0;
		if(refreshing && height > headViewHeight){
			finalHeight = headViewHeight;
		}
		scrollBack = SCROLL_BACK_HEAD;
		scroller.startScroll(0, height, 0, finalHeight - height, 400);
		invalidate();
	}
	
	public void stopRefresh(){
		if(refreshing){
			refreshing = false;
			resetHeaderHeight();
		}
	}
	
	public void setRefreshable(boolean flag){
		refreshable = flag;
		if(!refreshable){
			contentLayout.setVisibility(View.INVISIBLE);
		}else{
			contentLayout.setVisibility(View.VISIBLE);
		}
	}
	public void setRefreshListener(IRefreshListViewListener listener){
		refreshListener = listener;
	}
	private void updateHeadViewHeight(float deltaY) {
		// TODO Auto-generated method stub
		headView.setVisiableHeight((int)deltaY+headView.getVisiableHeight());
		if(refreshable&&!refreshing){
			if(headView.getVisiableHeight() > headViewHeight){
				headView.changeView(HeadView.READY);
			}else{
				headView.changeView(HeadView.NORMAL);
			}
		}
		setSelection(0);
	}
	public interface IRefreshListViewListener{
		public void refresh();
		
	}
}
