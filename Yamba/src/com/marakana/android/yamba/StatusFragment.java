package com.marakana.android.yamba;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.marakana.android.yamba.svc.YambaContract;

public class StatusFragment extends Fragment {
	private static final int STATUS_MAX = 140;
	private static final int STATUS_WARN = 10;
	private static final int STATUS_ERROR = 0;

	public static class Poster extends AsyncTask<String, Void, Void> {
		private ContentResolver resolver;

		public Poster(ContentResolver resolver) { this.resolver = resolver; }

		@Override
		protected Void doInBackground(String... params) {
			ContentValues vals = new ContentValues();
			vals.put(YambaContract.Posts.Columns.STATUS, params[0]);
			resolver.insert(YambaContract.Posts.URI, vals);
			poster = null;
			return null;
		}
	}

	volatile static private Poster poster;

	
	private EditText status;
	private TextView count;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
		View root = inflater.inflate(R.layout.activity_status, container, false);

		((Button) root.findViewById(R.id.submit)).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) { postStatus(); }
				} );

		status = (EditText) root.findViewById(R.id.status);
		count = (TextView) root.findViewById(R.id.count);
		status.addTextChangedListener(
				new TextWatcher() {
					@Override
					public void afterTextChanged(Editable arg0) { }

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						setCount();
					}
				} );
		
		return root;
	}

	void setCount() {
		int n = STATUS_MAX - status.getText().length();
		count.setText(String.valueOf(n));
		count.setTextColor(
				(STATUS_ERROR > n) ? Color.RED : ((STATUS_WARN > n) ? Color.YELLOW : Color.GREEN));
	} 

	void postStatus() {
		if (null != poster) { return; }

		String msg = status.getText().toString();

		if (TextUtils.isEmpty(msg)) { return; }

		status.setText("");

		poster = new Poster(getActivity().getContentResolver());
		poster.execute(msg);
	}
}
