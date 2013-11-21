package com.refreshlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/** 
 * 文件名称:   FooterView.java 
 * 功能描述:  
 * 版本信息:   Copyright (c)2013 
 * 开发人员:   vincent
 * 版本日志:   1.0 
 * 创建时间:   2013年11月19日 上午11:59:01 
 * 
 * 修改历史: 
 * 时间         开发者      版本号    修改内容 
 * ------------------------------------------------------------------ 
 * 2013年11月19日   yuyejiang      1.0         1.0 Version 
 */
public class FooterView extends LinearLayout {
	public static final int NORMAL = 0;
	public static final int READY = 1;
	public static final int LOADING = 2;
	private TextView textView;
	private ProgressBar progress;
	private RelativeLayout container;

	public FooterView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		// TODO Auto-generated method stub
		LinearLayout view = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.layout_footer, null);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(params);
		addView(view);

		container = (RelativeLayout) view.findViewById(R.id.contentView);
		textView = (TextView) view.findViewById(R.id.load_more);
		progress = (ProgressBar) view.findViewById(R.id.footer_progress);
	}

	public void changeView(int state) {
		if (state == NORMAL) {
			textView.setVisibility(View.VISIBLE);
			textView.setText("查看更多");
			progress.setVisibility(View.INVISIBLE);
		} else if (state == READY) {
			textView.setVisibility(View.VISIBLE);
			textView.setText("松开载入更多");
			progress.setVisibility(View.INVISIBLE);
		} else {
			textView.setVisibility(View.INVISIBLE);
			progress.setVisibility(View.VISIBLE);
		}
		invalidate();
	}

	public void hide() {
		LayoutParams lp = (LayoutParams) container.getLayoutParams();
		lp.height = 0;
		container.setLayoutParams(lp);
	}

	public void show() {
		LayoutParams lp = (LayoutParams) container.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		container.setLayoutParams(lp);
	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LayoutParams params = (LayoutParams) container.getLayoutParams();
		params.bottomMargin = height;
		container.setLayoutParams(params);
	}

	public int getBottomMargin() {
		LayoutParams params = (LayoutParams) container.getLayoutParams();
		return params.bottomMargin;
	}
}
