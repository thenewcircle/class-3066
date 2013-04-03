package com.marakana.android.yamba;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;

import com.marakana.android.yamba.svc.YambaServiceHelper;

public class TimelineActivity extends ListActivity implements LoaderCallbacks<Cursor>{
	private YambaServiceHelper yambaSvc;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Implement
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Implement
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Implement
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
