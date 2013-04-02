package com.marakana.android.yamba;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class StatusActivity extends Activity {
	private static final int STATUS_MAX = 140;
	private static final int STATUS_WARN = 10;
	private static final int STATUS_ERROR = 0;

	private EditText status;
	private TextView count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);

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

	void setCount() {
		int n = STATUS_MAX - status.getText().length();
		count.setText(String.valueOf(n));
		count.setTextColor(
				(STATUS_ERROR > n) ? Color.RED : ((STATUS_WARN > n) ? Color.YELLOW : Color.GREEN));
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
