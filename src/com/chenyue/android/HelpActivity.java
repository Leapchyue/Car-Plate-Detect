package com.chenyue.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class HelpActivity extends Activity {
	private static final String TAG         = "HelpActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("Help");
    }
}
