package com.matejhacin.ispogovor.activities;

import android.content.Intent;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.matejhacin.ispogovor.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QRScannerActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    /*
    Variables
     */

    public static final int QR_RESULT_CODE = 1;
    public static final String QR_RESULT = "qrResult";

    @Bind(R.id.qrCodeReaderView) QRCodeReaderView qrReaderView;

    /*
    Lifecycle
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        // Init
        ButterKnife.bind(this);
        qrReaderView.setOnQRCodeReadListener(this);
    }

    /*
    Callbacks
     */

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Intent result = new Intent();
        result.putExtra(QR_RESULT, text);
        setResult(QR_RESULT_CODE, result);
        finish();
    }

    @Override
    public void QRCodeNotFoundOnCamImage() {
        int i = 0;
    }

    @Override
    public void cameraNotFound() {
        int i = 0;
    }
}
