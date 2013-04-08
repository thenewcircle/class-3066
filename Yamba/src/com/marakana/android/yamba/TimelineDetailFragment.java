package com.marakana.android.yamba;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TimelineDetailFragment extends Fragment {
	public static final String ARG_MESSAGE = "TimelineDetailFragment.MESSAGE";

	public static TimelineDetailFragment newInstance(String message) {
		TimelineDetailFragment instance = new TimelineDetailFragment();
		
		Bundle b = new Bundle();
		b.putString(ARG_MESSAGE, message);
		
		instance.setArguments(b);
		
		return instance;
	}

	
	TextView detailsText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
		detailsText =  (TextView) inflater.inflate(R.layout.timeline_detail, container, false);
		
		if (null == b) { b = getArguments(); }
		
		detailsText.setText((null == b) ? "nothing" : b.getString(ARG_MESSAGE));

		return detailsText;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(ARG_MESSAGE, detailsText.getText().toString());
	}
	
	
}
