package com.chenyue.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity{
    private static final String TAG         = "MainActivity";
    private Button detect;
    private Button record;
    private Button doc;
    private Button help;
    private Button exit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mainpage);
        initViews();
    }
	private void initViews() {
		detect = (Button) findViewById(R.id.detect);
		record = (Button) findViewById(R.id.record);
		doc = (Button) findViewById(R.id.doc);
		help = (Button) findViewById(R.id.help);
		exit = (Button) findViewById(R.id.exit);

		detect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
                intent.setClass(MainActivity.this, CarPlateDetectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", "detect");
                intent.putExtras(bundle);
                startActivity(intent);
			}
		});
		record.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
                intent.setClass(MainActivity.this, CarPlateDetectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", "record");
                intent.putExtras(bundle);
                startActivity(intent);
			}
		});
		doc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		help.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
    
}
