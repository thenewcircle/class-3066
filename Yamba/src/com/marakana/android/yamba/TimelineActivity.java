package com.marakana.android.yamba;

import android.os.Bundle;
import android.view.MenuItem;

import com.marakana.android.yamba.svc.YambaServiceHelper;


public class TimelineActivity extends BaseActivity {
	private YambaServiceHelper yambaSvc;

	public boolean onOptionsItemSelected(MenuItem item) {
		if (R.id.action_status == item.getItemId()) { return true; }
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

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
