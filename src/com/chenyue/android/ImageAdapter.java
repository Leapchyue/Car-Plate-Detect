package com.chenyue.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	int mGalleryItemBackground;
	private Context montext;
	String image[];
	File imageDir;

	public ImageAdapter(Context c) {
		montext = c;
		TypedArray attr = montext.obtainStyledAttributes(R.styleable.HelloGallery);
		mGalleryItemBackground = attr.getResourceId(R.styleable.HelloGallery_android_galleryItemBackground, 0);
		attr.recycle();
		getFile();
		System.out.println("image adapter down " + image.length);
	}

	@Override
	public int getCount() {
		return image.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(montext);
		Bitmap bitmap = getImage(image[position]);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.main);
		}
		imageView.setScaleType(ImageView.ScaleType.CENTER);
		imageView.setBackgroundResource(mGalleryItemBackground);

		return imageView;
	}

	public String getFileName(int position) {
		return image[position];
	}
	private Bitmap getImage(String fileName) {
		File f = new File(imageDir + File.separator, fileName);
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			Log.w("Image Manageer", "file " + fileName + " not found");
			return null;
		}
		Bitmap bitmap = BitmapFactory.decodeStream(fis);
		if (fis != null) {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	private void getFile() {
		final String BASE = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ "carplatedetect"
				+ File.separator;
		imageDir = new File(BASE);
		if (!imageDir.exists()) {
			imageDir.mkdir();
		}
		image = imageDir.list(new ImageFilter());
	}

	public class ImageFilter implements FilenameFilter {
		public boolean isGif(String file) {
			if (file.toLowerCase().endsWith(".gif")) {
				return true;
			} else {
				return false;
			}
		}

		public boolean isJpg(String file) {
			if (file.toLowerCase().endsWith(".jpg")) {
				return true;
			} else {
				return false;
			}
		}

		public boolean isPng(String file) {
			if (file.toLowerCase().endsWith(".png")) {
				return true;
			} else {
				return false;
			}
		}

		public boolean accept(File dir, String fname) {
			return (isGif(fname) || isJpg(fname) || isPng(fname));

		}

	}
}
