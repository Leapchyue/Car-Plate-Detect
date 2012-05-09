package com.chenyue.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;

public class DocActivity extends Activity {
	private static final String TAG         = "DocActivity";
	private Gallery gallery;
	private TextView text;
	private Button previous;
	private Button delete;
	private Button next;
	private int curPosition = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc);
        setTitle("查看结果");
        initViews();
    }
	private void initViews() {
        gallery = (Gallery) findViewById(R.id.gallery);
        
        final ImageAdapter adapter = new ImageAdapter(this);
        gallery.setAdapter(adapter);
        gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				text.setText(adapter.getFileName(position));
				curPosition = position;
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});

        text = (TextView) findViewById(R.id.text);
        text.setText("add");
        previous = (Button) findViewById(R.id.previous);
        delete = (Button) findViewById(R.id.delete);
        delete.setVisibility(View.INVISIBLE);
        next = (Button) findViewById(R.id.next);
        previous.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = (curPosition - 1 > 0) ? (curPosition - 1) : 0;
				gallery.setSelection(position);
			}
		});
        next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = curPosition + 1 < adapter.getCount() ? curPosition + 1 : adapter.getCount() - 1;
				gallery.setSelection(position);
			}
		});
	}
}
