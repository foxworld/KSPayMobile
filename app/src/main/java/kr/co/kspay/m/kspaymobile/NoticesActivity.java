package kr.co.kspay.m.kspaymobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class NoticesActivity extends ActionBarActivity {
    WebView wvNotices = null;
    String sErrorURL = null;
    public static ProgressBar progressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);

        progressBar = (ProgressBar) this.findViewById(R.id.progress_bar_notices);
        wvNotices = (WebView)findViewById(R.id.wv_notices);
        wvNotices.clearHistory();
        wvNotices.clearCache(true);
        wvNotices.setWebChromeClient(new AndroidWebChromeClient(this, progressBar));
        wvNotices.getSettings().setJavaScriptEnabled(true);
        wvNotices.addJavascriptInterface(new AndroidJavascriptInterface(this), "android"); // Web에서 App 실행하기
        wvNotices.getSettings().setBuiltInZoomControls(true);
        wvNotices.setWebViewClient(new NoticesWebViewClient());
        wvNotices.loadUrl(getString(R.string.notices_url));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notices, menu);
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

    class NoticesWebViewClient extends WebViewClient {                          // 자기 자신 브라우져에 띄우기 위해  추가
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            Toast.makeText(NoticesActivity.this, getString(R.string.page_error_message), Toast.LENGTH_LONG).show();
    		/* 네트워크 및 기타 페이지를 열수가 없을때 최종 페이지를 담기 위해 처리*/
            wvNotices.loadUrl(getString(R.string.network_error_url));
            sErrorURL = failingUrl;
        }
    };

    @Override
    public void onBackPressed() {
        if(wvNotices.canGoBack()) {
            wvNotices.goBack();
        }
        else {
            finish();
        }
    }
}

