package com.marakana.android.yamba;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.marakana.android.yamba.svc.YambaContract;


public class TimelineFragment extends ListFragment implements LoaderCallbacks<Cursor>{
	private static final int LOADER_ID = 42;

	private static final String[] PROJ = {
		YambaContract.Timeline.Columns.ID,
		YambaContract.Timeline.Columns.TIMESTAMP,
		YambaContract.Timeline.Columns.USER,
		YambaContract.Timeline.Columns.STATUS
	};

	private static final String[] FROM;
	static {
		FROM = new String[PROJ.length - 1];
		System.arraycopy(PROJ, 1, FROM, 0, FROM.length);
	};

	private static final int[] TO = {
		R.id.timeline_date,
		R.id.timeline_user,
		R.id.timeline_status
	};

	static class TimelineBinder implements SimpleCursorAdapter.ViewBinder {
		@Override
		public boolean setViewValue(View view, Cursor cursor, int colIndex) {
			if (view.getId() != R.id.timeline_date) { return false; }

			String tStr = "ages ago";
			long t = cursor.getLong(colIndex);
			if (0 < t) {
				tStr = DateUtils.getRelativeTimeSpanString(t, System.currentTimeMillis(), 0)
						.toString();
			}
			((TextView) view).setText(tStr);
			return true;
		}
	}


	private SimpleCursorAdapter listAdapter;

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(
				getActivity(),
				YambaContract.Timeline.URI,
				PROJ,
				null,
				null,
				YambaContract.Timeline.Columns.TIMESTAMP + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		listAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		listAdapter.swapCursor(null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
		View v = super.onCreateView(inflater, container, b);

		listAdapter = new SimpleCursorAdapter(
				getActivity(),
				R.layout.timeline_row,
				null,
				FROM,
				TO,
				0);

		listAdapter.setViewBinder(new TimelineBinder());
		setListAdapter(listAdapter);

		getLoaderManager().initLoader(LOADER_ID, null, this);
		
		return v;
	}
}
