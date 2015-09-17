package com.gnzlt.AndroidVisionQRReader;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;

import com.gnzlt.AndroidVisionQRReader.camera.CameraSourcePreview;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRActivity extends AppCompatActivity {

    private static final String TAG = "QRActivity";
    public static final String EXTRA_QR_RESULT = "EXTRA_QR_RESULT";

    private BarcodeDetector mBarcodeDetector;
    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        mPreview = (CameraSourcePreview) findViewById(R.id.cameraSourcePreview);

        setupBarcodeDetector();
        setupCameraSource();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    private void setupBarcodeDetector() {
        mBarcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    String data = barcodes.valueAt(0).displayValue;

                    Log.d(TAG, "Barcode detected: " + data);
                    playBeep();

                    returnData(data);
                }
            }
        });

        if (!mBarcodeDetector.isOperational())
            Log.w(TAG, "Detector dependencies are not yet available.");

    }

    private void setupCameraSource() {
        mCameraSource = new CameraSource.Builder(this, mBarcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(15.0f)
                .setRequestedPreviewSize(1600, 1024)
                .build();
    }

    private void startCameraSource() {
        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    private void playBeep() {
        ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME);
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
    }

    private void returnData(String data) {
        if (data != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_QR_RESULT, data);
            setResult(RESULT_OK, resultIntent);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }
}
