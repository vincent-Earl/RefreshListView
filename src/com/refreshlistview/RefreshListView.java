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
 * 开发人员:   vinent
 * 版本日志:   1.0 
 * 创建时间:   2013年11月19日 下午2:36:03 
 * 
 * 修改历史: 
 * 时间         开发者      版本号    修改内容 
 * ------------------------------------------------------------------ 
 * 2013年11月19日   yuyejiang      1.0         1.0 Version 
 */
public class RefreshListView extends ListView implements OnScrollListener {
	private static final String tag = "RefreshListView";
	private static final int SCROLL_BACK_HEAD = 0;
	private static final int SCROLL_BACK_FOOT = 1;
	private Scroller scroller;
	private HeadView headView;
	private FooterView footerView;
	private int headViewHeight = 0, totalItemCount;
	private RelativeLayout contentLayout;
	private float startY = -1;
	private boolean refreshable = true;
	private boolean refreshing = false;
	private boolean loadable = true;
	private boolean loading = false;
	private int scrollBack;
	private IRefreshListViewListener refreshListener;
	private OnScrollListener scrollListener;
	private boolean footerViewReady = false;

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
		scroller = new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);

		headView = new HeadView(context);
		footerView = new FooterView(context);
		contentLayout = (RelativeLayout) headView.findViewById(R.id.content);
		addHeaderView(headView);
		headView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						// TODO Auto-generated method stub
						headViewHeight = contentLayout.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.totalItemCount = totalItemCount;
		if (scrollListener != null) {
			scrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollListener != null) {
			scrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		if (!footerViewReady) {
			footerViewReady = true;
			addFooterView(footerView);
		}
		super.setAdapter(adapter);
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		// TODO Auto-generated method stub
		scrollListener = l;
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (scroller.computeScrollOffset()) {
			if (scrollBack == SCROLL_BACK_HEAD) {
				headView.setVisiableHeight(scroller.getCurrY());
			} else {
				footerView.setBottomMargin(scroller.getCurrY());
			}
			postInvalidate();
		}
		super.computeScroll();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (startY == -1) {
			startY = ev.getRawY();
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			startY = -1;
			if (getFirstVisiblePosition() == 0) {
				if (refreshable
						&& headView.getVisiableHeight() > headViewHeight) {
					refreshing = true;
					headView.changeView(HeadView.REFRESH);
					if (refreshListener != null) {
						refreshListener.refresh();
					}
				}
				resetHeaderHeight();
			} else if (getLastVisiblePosition() == totalItemCount - 1) {
				if (loadable && footerView.getBottomMargin() > 50) {
					startLoading();
				}
				resetFooterViewHeight();
			}
			break;
		case MotionEvent.ACTION_DOWN:
			startY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			float deltaY = ev.getRawY() - startY;
			startY = ev.getRawY();
			if (getFirstVisiblePosition() == 0
					&& (headView.getVisiableHeight() > 0 || deltaY > 0)) {
				updateHeadViewHeight(deltaY / 1.8f);
			} else if (getLastVisiblePosition() == totalItemCount - 1
					&& (footerView.getBottomMargin() > 0 || deltaY < 0)) {
				updateFooterViewHeight(-deltaY / 1.8f);
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void resetFooterViewHeight() {
		// TODO Auto-generated method stub
		int bottomMargin = footerView.getBottomMargin();
		if (bottomMargin > 0) {
			scrollBack = SCROLL_BACK_FOOT;
			scroller.startScroll(0, bottomMargin, 0, -bottomMargin, 400);
			invalidate();
		}
	}

	private void updateFooterViewHeight(float f) {
		// TODO Auto-generated method stub
		int height = footerView.getBottomMargin() + (int) f;
		if (loadable && !loading) {
			if (height > 50) {
				footerView.changeView(FooterView.READY);
			} else {
				footerView.changeView(FooterView.NORMAL);
			}
		}
		footerView.setBottomMargin(height);

	}

	private void resetHeaderHeight() {
		// TODO Auto-generated method stub
		int height = headView.getVisiableHeight();
		if (height == 0 || (refreshing && height <= headViewHeight))
			return;
		int finalHeight = 0;
		if (refreshing && height > headViewHeight) {
			finalHeight = headViewHeight;
		}
		scrollBack = SCROLL_BACK_HEAD;
		scroller.startScroll(0, height, 0, finalHeight - height, 400);
		invalidate();
	}

	public void stopRefresh() {
		if (refreshing) {
			refreshing = false;
			resetHeaderHeight();
		}
	}

	public void stopLoad() {
		if (loading) {
			loading = false;
			footerView.changeView(FooterView.NORMAL);
		}
	}

	public void setPullRefreshEnable(boolean flag) {
		refreshable = flag;
		if (flag) {
			contentLayout.setVisibility(View.VISIBLE);
		} else {
			contentLayout.setVisibility(View.INVISIBLE);
		}
	}

	public void setRefreshable(boolean flag) {
		refreshable = flag;
		if (!refreshable) {
			contentLayout.setVisibility(View.INVISIBLE);
		} else {
			contentLayout.setVisibility(View.VISIBLE);
		}
	}

	public void setLoadable(boolean flag) {
		loadable = flag;
		if (flag) {
			loading = false;
			footerView.show();
			footerView.changeView(FooterView.NORMAL);
			footerView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startLoading();
				}
			});
		} else {
			footerView.hide();
		}
	}

	protected void startLoading() {
		// TODO Auto-generated method stub
		loading = true;
		footerView.changeView(FooterView.LOADING);
		if (refreshListener != null) {
			refreshListener.load();
		}
	}

	public void setRefreshListener(IRefreshListViewListener listener) {
		refreshListener = listener;
	}

	private void updateHeadViewHeight(float deltaY) {
		// TODO Auto-generated method stub
		headView.setVisiableHeight((int) deltaY + headView.getVisiableHeight());
		if (refreshable && !refreshing) {
			if (headView.getVisiableHeight() > headViewHeight) {
				headView.changeView(HeadView.READY);
			} else {
				headView.changeView(HeadView.NORMAL);
			}
		}
		setSelection(0);
	}

	public interface IRefreshListViewListener {
		public void refresh();

		public void load();

	}
}
