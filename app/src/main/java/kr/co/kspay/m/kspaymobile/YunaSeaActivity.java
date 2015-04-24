package kr.co.kspay.m.kspaymobile;

import android.graphics.Bitmap;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


public class YunaSeaActivity extends ActionBarActivity {
    WebView wvYunaSea = null;
    String sErrorURL = null;
    public static ProgressBar progressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuna_sea);
        progressBar = (ProgressBar) this.findViewById(R.id.progress_bar_yunasea);
         wvYunaSea = (WebView)findViewById(R.id.wv_yunasea);

        wvYunaSea.clearHistory();
        wvYunaSea.clearCache(true);

        wvYunaSea.setWebChromeClient(new AndroidWebChromeClient(this, progressBar));
        wvYunaSea.getSettings().setJavaScriptEnabled(true);
        wvYunaSea.addJavascriptInterface(new AndroidJavascriptInterface(this), "android"); // Web에서 App 실행하기
        wvYunaSea.setWebViewClient(new YunaSeaWebViewClient());

        wvYunaSea.setVerticalScrollBarEnabled(false);
        wvYunaSea.setHorizontalScrollBarEnabled(false);
        //wvYunaSea.requestFocusFromTouch();
        wvYunaSea.getSettings().setUseWideViewPort(true);
        wvYunaSea.getSettings().setLoadWithOverviewMode(true);
        //wvYunaSea.getSettings().setBuiltInZoomControls(true);

        //wvYunaSea.scrollTo(1, 0);
        //wvYunaSea.scrollTo(0, 0);
        //wvYunaSea.zoomIn();
        //wvYunaSea.zoomOut();

        /*
        wvYunaSea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("foxworld", "ACTION_DOWN");
                        if (!v.hasFocus()) {
                            Log.d("foxworld", "ACTION_DOWN_hasFocus");
                            v.requestFocus();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d("foxworld", "ACTION_UP");
                        if (!v.hasFocus()) {
                            Log.d("foxworld", "ACTION_UP_hasFocus");
                            v.requestFocus();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d("foxworld", "ACTION_MOVE");
                        if (!v.hasFocus()) {
                            Log.d("foxworld", "hasFocus");
                            v.requestFocus();
                        }
                        break;
                }
                return false;
                //return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        */
        //wvYunaSea.loadDataWithBaseURL("http://www.catsroof.com/", "webapp/", "text/html", "utf-8", null);
        wvYunaSea.loadUrl(getString(R.string.yunasea_url));

    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_yuna_sea, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d("foxworld", "onOptionsItemSelected");

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    Log.d("foxworld", "KEYCODE_BACK Click!!");
                    break;
                case KeyEvent.KEYCODE_MENU:
                    Log.d("foxworld", "KEYCODE_MENU Click!!");
                    wvYunaSea.loadUrl(getString(R.string.yunasea_menu_click_javascript));
                    return true;
                case KeyEvent.KEYCODE_HOME:
                    // 단말기의 HOME버튼 -> 동작안함
                    Log.d("foxworld", "KEYCODE_HOME Click!!");
                    break;
            }

        }
        return super.dispatchKeyEvent(event);
    }

    class YunaSeaWebViewClient extends WebViewClient {                          // 자기 자신 브라우져에 띄우기 위해  추가
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            wvYunaSea.scrollTo(1, 0);
            wvYunaSea.scrollTo(0, 0);
            wvYunaSea.zoomIn();
            wvYunaSea.zoomOut();
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

            Toast.makeText(YunaSeaActivity.this, getString(R.string.page_error_message), Toast.LENGTH_LONG).show();
    		/* 네트워크 및 기타 페이지를 열수가 없을때 최종 페이지를 담기 위해 처리*/
            wvYunaSea.loadUrl(getString(R.string.network_error_url));
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
        if(wvYunaSea.canGoBack()) {
            wvYunaSea.goBack();
        }
        else {
            finish();
        }
    }
}
