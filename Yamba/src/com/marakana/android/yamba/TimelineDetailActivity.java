package com.marakana.android.yamba;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;


public class TimelineDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.timeline_detail);

        ((TextView) findViewById(R.id.timeline_detail))
            .setText(getIntent().getStringExtra(TimelineDetailFragment.ARG_MESSAGE));
    }
}
