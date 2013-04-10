package com.marakana.android.yamba;

import android.os.Bundle;
import android.view.MenuItem;

public class StatusActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_status == item.getItemId()) { return true; }
        return super.onOptionsItemSelected(item);
    }
}
