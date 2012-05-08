package com.chenyue.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.SurfaceHolder;

class CarPlateDetectView extends SampleCvViewBase {
    private static final String TAG = "Sample::FdView";
    private Mat                 rgba;
    private Mat                 gray;

    private CascadeClassifier   cascade;

    public CarPlateDetectView(Context context) {
        super(context);

        try {
            InputStream is = context.getResources().openRawResource(R.raw.car_plate_detection_2);
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
            File cascadeFile = new File(cascadeDir, "car_plate_detection_2.xml");
            FileOutputStream os = new FileOutputStream(cascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            cascade = new CascadeClassifier(cascadeFile.getAbsolutePath());
            if (cascade.empty()) {
                Log.e(TAG, "Failed to load cascade classifier");
                cascade = null;
            } else
                Log.i(TAG, "Loaded cascade classifier from " + cascadeFile.getAbsolutePath());

            cascadeFile.delete();
            cascadeDir.delete();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder _holder, int format, int width, int height) {
        super.surfaceChanged(_holder, format, width, height);

        synchronized (this) {
            gray = new Mat();
            rgba = new Mat();
        }
    }

    @Override
    protected Bitmap processFrame(VideoCapture capture) {
        capture.retrieve(rgba, Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGBA);
        capture.retrieve(gray, Highgui.CV_CAP_ANDROID_GREY_FRAME);

        if (cascade != null && CarPlateDetectActivity.detect) {
            int height = gray.rows();
            int carSize = (int) (height * CarPlateDetectActivity.minCarPlateSize);
            Size carPlateSize = new Size(carSize * 3, carSize);
            List<Rect> carPlates = new LinkedList<Rect>();
            cascade.detectMultiScale(gray, carPlates, 1.1, 1, 0, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
            		carPlateSize);
            
            for (Rect r : carPlates) {
                Core.rectangle(rgba, r.tl(), r.br(), new Scalar(0, 255, 0, 255), 3);
            }
            
            if (carPlates.size()!=0) {
            	Rect r = carPlates.get(0);
            	CarPlateDetectActivity.detectResult = new Mat(r.height, r.width, rgba.type());
            	rgba.submat(r).copyTo(CarPlateDetectActivity.detectResult);
            	CarPlateDetectActivity.handler.sendEmptyMessage(1);
            }
        }

        Bitmap bmp = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888);

        if (Utils.matToBitmap(rgba, bmp))
            return bmp;

        bmp.recycle();
        return null;
    }

    @Override
    public void run() {
        super.run();

        synchronized (this) {
            // Explicitly deallocate Mats
            if (rgba != null)
                rgba.release();
            if (gray != null)
                gray.release();

            rgba = null;
            gray = null;
        }
    }
}
