package kr.co.kspay.m.kspaymobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class YunaSea2Activity extends ActionBarActivity {
    WebView wvYunaSea2 = null;
    String sErrorURL = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuna_sea2);
        wvYunaSea2 = (WebView)findViewById(R.id.wv_yunasea2);

        //wvYunaSea2.clearHistory();
        //wvYunaSea2.clearCache(true);

        wvYunaSea2.setWebChromeClient(new AndroidWebChromeClient(this));
        wvYunaSea2.getSettings().setJavaScriptEnabled(true);
        wvYunaSea2.addJavascriptInterface(new AndroidJavascriptInterface(this), "android"); // Web에서 App 실행하기
        wvYunaSea2.setWebViewClient(new YunaSea2WebViewClient());

        //wvYunaSea2.setVerticalScrollBarEnabled(true);
        wvYunaSea2.setHorizontalScrollBarEnabled(false);
        //wvYunaSea2.requestFocusFromTouch();
        //wvYunaSea2.getSettings().setUseWideViewPort(true);
        //wvYunaSea2.getSettings().setLoadWithOverviewMode(true);
        wvYunaSea2.getSettings().setBuiltInZoomControls(true);

        //wvYunaSea2.scrollTo(1, 0);
        //wvYunaSea2.scrollTo(0, 0);
        //wvYunaSea2.zoomIn();
        //wvYunaSea2.zoomOut();

        //wvYunaSea2.loadUrl(getString(R.string.yunasea_url));
        //wvYunaSea2.loadUrl("http://m.naver.com");
        wvYunaSea2.loadUrl("file:///android_asset/www/SlidePushMenus/index.html");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_yuna_sea2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class YunaSea2WebViewClient extends WebViewClient {                          // 자기 자신 브라우져에 띄우기 위해  추가
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //wvYunaSea2.scrollTo(1, 0);
            //wvYunaSea2.scrollTo(0, 0);
            wvYunaSea2.zoomIn();
            wvYunaSea2.zoomOut();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            Toast.makeText(YunaSea2Activity.this, getString(R.string.page_error_message), Toast.LENGTH_LONG).show();
    		/* 네트워크 및 기타 페이지를 열수가 없을때 최종 페이지를 담기 위해 처리*/
            wvYunaSea2.loadUrl(getString(R.string.network_error_url));
            sErrorURL = failingUrl;
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend)
        {
            resend.sendToTarget();
        }
    };

    @Override
    public void onBackPressed() {
        if(wvYunaSea2.canGoBack()) {
            wvYunaSea2.goBack();
        }
        else {
            finish();
        }
    }
 }
