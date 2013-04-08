package com.marakana.android.yamba;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.marakana.android.yamba.svc.YambaServiceHelper;


public class TimelineActivity extends Activity {
	private YambaServiceHelper yambaSvc;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);

		yambaSvc = YambaServiceHelper.getInstance();

	}

	@Override
	protected void onPause() {
		super.onPause();
		yambaSvc.stopPolling(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		yambaSvc.startPolling(this);
	}
}
