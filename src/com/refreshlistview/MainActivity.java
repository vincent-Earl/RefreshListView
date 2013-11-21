package com.refreshlistview;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ArrayAdapter;

import com.refreshlistview.RefreshListView.IRefreshListViewListener;

public class MainActivity extends Activity implements IRefreshListViewListener{
	private RefreshListView refreshListView;
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> dataList = new ArrayList<String>();
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		for (int i = 0; i < 20; i++) {
			dataList.add("每一行" + i);
		}
		refreshListView = (RefreshListView)findViewById(R.id.listView);
		mAdapter = new ArrayAdapter<String>(this, R.layout.listitem_test, dataList);
		refreshListView.setAdapter(mAdapter);
		refreshListView.setRefreshable(true);
		refreshListView.setLoadable(true);
		refreshListView.setRefreshListener(this);
		handler = new Handler();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void refresh()  {
		// TODO Auto-generated method stub
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				refreshListView.stopRefresh();
			}
		}, 2000);
		
	}
	@Override
	public void load() {
		// TODO Auto-generated method stub
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				refreshListView.stopLoad();
			}
		}, 2000);
	}

}
