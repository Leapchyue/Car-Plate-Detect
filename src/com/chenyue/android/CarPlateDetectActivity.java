package com.chenyue.android;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class CarPlateDetectActivity extends Activity {
    private static final String TAG         = "CarPlateDetectActivity";
    private static String mode;

    private MenuItem            menuItem50;
    private MenuItem            menuItem40;
    private MenuItem            menuItem30;
    private MenuItem            menuItem20;
    private MenuItem            menuItem10;
    private CarPlateDetectView carPlateDectView;

    private FrameLayout frameLayout;
    public static RelativeLayout detectLayout;
    private SeekBar seekBar;
    private Button back;

    public static LinearLayout resultLayout;
    private Button resultBack;
    public static ImageView result;
    private Button yes;
    private Button no;
    private Button save;
    public static boolean detect;
    private static Context context;
    public static Mat detectResult;
    private static Bitmap bmp;
    public static float         minCarPlateSize = 0.2f; 
    public static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                case 1:
                	if (mode.equals("record")) break;
                    bmp = Bitmap.createBitmap(detectResult.cols(), detectResult.rows(), Bitmap.Config.ARGB_8888);
                    if (Utils.matToBitmap(detectResult, bmp)) {
                    	result.setImageBitmap(bmp);
                    	detectLayout.setVisibility(View.GONE);
                    	resultLayout.setVisibility(View.VISIBLE);
                    	CarPlateDetectActivity.detect = false;
                    	Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                	if (mode.equals("record")) break;
                	CarPlateDetectActivity.detect = true;
                	resultLayout.setVisibility(View.GONE);
                	detectLayout.setVisibility(View.VISIBLE);
                	break;
                }
            }
            super.handleMessage(msg);
        }
    };

    public CarPlateDetectActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mode = bundle.getString("type");
        context = this;
        initViews();
        detect = true;
        setContentView(frameLayout);
    }
    
    
    
    @Override
	protected void onDestroy() {
    	detectResult = null;
    	bmp = null;
    	super.onDestroy();
	}

	private void initViews() {
    	carPlateDectView = new CarPlateDetectView(this);
    	frameLayout = new FrameLayout(this);
    	frameLayout.addView(carPlateDectView);
    	initDetectLayout();
    	frameLayout.addView(detectLayout);
    	initResultLayout();
    	frameLayout.addView(resultLayout);
    	resultLayout.setVisibility(View.GONE);
	}

    private void initResultLayout() {
		resultLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.result, null);
		resultBack = (Button) resultLayout.findViewById(R.id.back);
	    result = (ImageView) resultLayout.findViewById(R.id.result);
	    yes = (Button) resultLayout.findViewById(R.id.yes);
	    no = (Button) resultLayout.findViewById(R.id.no);
	    save = (Button) resultLayout.findViewById(R.id.save);
	    
	    resultBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(2);
			}
		});
	    yes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(2);
			}
		});
	    no.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(2);
			}
		});
	    save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				View view  =  getLayoutInflater().inflate(R.layout.inputtext, null);
				final EditText fileName = (EditText) view.findViewById(R.id.editText);
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				fileName.setText(sdf.format(date));
                AlertDialog.Builder builder = new AlertDialog.Builder(
                		CarPlateDetectActivity.this)
                        .setView(view)
                        .setTitle("请输入文件名")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                            int whichButton) {
                                    	saveImage(fileName.getText().toString());
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                            int whichButton) {
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.setCanceledOnTouchOutside(true);
                alert.show();
			}
		});
	}

	private void initDetectLayout() {
    	detectLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.detect, null);
    	back = (Button) detectLayout.findViewById(R.id.back);
    	back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
    	seekBar = (SeekBar) detectLayout.findViewById(R.id.seekBar);
    	minCarPlateSize = 0.2f;
    	seekBar.setProgress(25);
    	seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				minCarPlateSize = (progress/25 + 1 ) * 0.1f;
				back.setText(String.valueOf(minCarPlateSize));
			}
		});
    }
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu");
        menuItem50 = menu.add("size 50%");
        menuItem40 = menu.add("size 40%");
        menuItem30 = menu.add("size 30%");
        menuItem20 = menu.add("size 20%");
        menuItem10 = menu.add("size 20%");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "Menu Item selected " + item);
        if (item == menuItem50) {
            minCarPlateSize = 0.5f;
        } else if (item == menuItem40) {
            minCarPlateSize = 0.4f;
        } else if (item == menuItem30) {
            minCarPlateSize = 0.3f;
        } else if (item == menuItem20) {
            minCarPlateSize = 0.2f;
        } else if (item == menuItem10) {
        	minCarPlateSize = 0.1f;
        }
        return true;
    }

	private void saveImage(String fileName) {

		System.out.println(fileName);
		writeFile(fileName, bmp);
	}

    public void writeFile(String fileName,Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        try {
        	final String BASE = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "carplatedetect" + File.separator;
            File file = new File(BASE);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(file + File.separator, fileName+".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            Toast.makeText(context, "文件保存成功", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessage(2);
        } catch (Exception e) {
            Log.w("Image Manageer", "write file " + fileName + " exception", e);
        } finally {
        }
        
    }

}