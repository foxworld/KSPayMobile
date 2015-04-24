package kr.co.kspay.m.kspaymobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.app.Activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PeterLEE on 2015-02-23.
 */
public class AndroidJavascriptInterface {
    private Activity activity;
    private String errorURL = null;
    private String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    public AndroidJavascriptInterface(Activity context) {
        this.activity = context;
    }

    @JavascriptInterface
    public void call(final int call_flag, final String value) {
        Log.d("foxworld", "call_flag=" + call_flag + ",value=" + value);
        Handler handler  = new Handler();
        handler.post(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                if(call_flag == 1) {
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + value));
                }
                else if(call_flag == 2) {
                    intent.setAction(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + value));
                }
                activity.startActivity(intent);
            }
        });
    }

    @JavascriptInterface
    public void reload() {
        Handler  handler  = new Handler();
        handler.post(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                //activity.wvMain.loadUrl(errorURL);
            }
        });
    }

    @JavascriptInterface
    public void camera(final String value) {
        Log.d("foxworld", "value=" + value);
        Handler handler  = new Handler();
        handler.post(new Runnable() {
            public void run() {
                Log.d("foxworld", "PhotoIntent Finished");
            }
        });
    }
}
