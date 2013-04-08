package com.marakana.android.yamba;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.marakana.android.yamba.svc.YambaServiceHelper;


public class TimelineActivity extends BaseActivity {
	private static final String DETAIL_FRAG = "DETAILS";

	
	private YambaServiceHelper yambaSvc;
	private boolean usingFragments;

	public boolean onOptionsItemSelected(MenuItem item) {
		if (R.id.action_status == item.getItemId()) { return true; }
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
		if (!usingFragments) { startActivity(intent); }
		else {
			replaceFragment(intent.getStringExtra(TimelineDetailFragment.ARG_MESSAGE));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		yambaSvc = YambaServiceHelper.getInstance();

		usingFragments = null != findViewById(R.id.fragment_timeline_detail);

		if (usingFragments) { installDetailsFragment(); }
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

	private void installDetailsFragment() {
		FragmentManager fragMgr = getFragmentManager();

		if ( null != fragMgr.findFragmentByTag(DETAIL_FRAG)) { return; }
		
		FragmentTransaction xact = fragMgr.beginTransaction();
		xact.add(
				R.id.fragment_timeline_detail,
				TimelineDetailFragment.newInstance(getString(R.string.empty)),
				DETAIL_FRAG);
		xact.commit();
	}

	public void replaceFragment(String message) {
		FragmentManager fragMgr = getFragmentManager();

		FragmentTransaction xact = fragMgr.beginTransaction();
		xact.replace(
				R.id.fragment_timeline_detail,
				TimelineDetailFragment.newInstance(message),
				DETAIL_FRAG);
		xact.addToBackStack(null);
		xact.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		xact.commit();
	}
}
