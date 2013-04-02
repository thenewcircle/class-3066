package com.marakana.android.yamba;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

public class StatusActivity extends Activity {
	private static final String TAG = "STATUS";
	
	private static final int STATUS_MAX = 140;
	private static final int STATUS_WARN = 10;
	private static final int STATUS_ERROR = 0;

	public static boolean send(String status) {
		YambaClient client = new YambaClient(
				"student",
				"password",
				"http://yamba.marakana.com/api");


		boolean succeeded = true;
		try { client.postStatus(status); }
		catch (YambaClientException e) {
			Log.e(TAG, "Post failed: " + e, e);
			succeeded = false;
		}

		return succeeded;
		
	}

	public class Poster extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			return Integer.valueOf(
					(send(params[0]))
					? R.string.post_succeeded
					: R.string.post_failed);
		}

		protected void onPostExecute(Integer status) {
			Toast.makeText(StatusActivity.this, status.intValue(), Toast.LENGTH_LONG).show();
			poster = null;
		}
	}


	private EditText status;
	private TextView count;
	private Poster poster;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);

		((Button) findViewById(R.id.submit)).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) { postStatus(); }
				} );

		status = (EditText) findViewById(R.id.status);
		count = (TextView) findViewById(R.id.count);
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
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	void setCount() {
		int n = STATUS_MAX - status.getText().length();
		count.setText(String.valueOf(n));
		count.setTextColor(
				(STATUS_ERROR > n) ? Color.RED : ((STATUS_WARN > n) ? Color.YELLOW : Color.GREEN));
	} 

	void postStatus() {
		if (null != poster) { return; }
		poster = new Poster();
		String msg = status.getText().toString();
		status.setText("");
		poster.execute(msg);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
