package kr.co.kspay.m.kspaymobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    WebView          wvMain           = null;
    ProgressDialog   pd = null;
    public static ProgressBar progressBar = null;
    public static String   errorURL = null;
    public static Activity mainActivity; // SplashActivity 에서 MainActivity 를 finish() 하기 위해 사용

    /* 환경파일 일기 */
    SharedPreferences pref;
    /* 자동로그인을 체크하기 위해  True 이면 자동 로그인 처리 False 이면 자동로그인 미처리 */
    boolean prefLoginState = true;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SplashActivity 에서 MainActivity 를 종료하기 위해 설정함
        mainActivity = MainActivity.this;
        startActivity(new Intent(this, SplashActivity.class));

        progressBar = (ProgressBar) this.findViewById(R.id.progress_bar);

        backPressCloseHandler = new BackPressCloseHandler(this);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        wvMain = (WebView)findViewById(R.id.wv_main);
        //tv_ticker  = (TextView) findViewById(R.id.tv_ticker);

        wvMain.clearHistory();
        wvMain.clearCache(true);

        // 내가 만든 웹크롭 설정
        wvMain.setWebChromeClient(new AndroidWebChromeClient(this, progressBar));
        // 자바스크립트 사용하기
        wvMain.getSettings().setJavaScriptEnabled(true);
        wvMain.addJavascriptInterface(new AndroidJavascriptInterface(this), "android"); // Web에서 App 실행하기
        //wv_main.getSettings().setBuiltInZoomControls(true);
        wvMain.setWebViewClient(new MainWebViewClient());

        //wvMain.loadUrl(getString(R.string.login_url));
        //wvMain.loadUrl(getString(R.string.yunasea_url));
        wvMain.loadUrl(getString(R.string.moira_url));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = null;
        switch (id) {
            case R.id.menu_preference:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_reload:
                prefLoginState=true;
                wvMain.reload();
                break;
            case R.id.menu_logout:
                //wvMain.loadUrl(getString(R.string.login_url));
                wvMain.loadUrl(getString(R.string.moira_url));
                break;
            case R.id.menu_notices:
                intent = new Intent(MainActivity.this, NoticesActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_help:
                wvMain.loadUrl(getString(R.string.help_url));
                break;
            case R.id.menu_phonegap:
                intent = new Intent(MainActivity.this, PhoneGapActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_yunasea:
                intent = new Intent(MainActivity.this, YunaSeaActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_yunasea2:
                intent = new Intent(MainActivity.this, YunaSea2Activity.class);
                startActivity(intent);
                break;
            case R.id.menu_camera:
                //wvMain.loadUrl(getString(R.string.camera_url));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class MainWebViewClient extends WebViewClient {                          // 자기 자신 브라우져에 띄우기 위해  추가
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            /*
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }
            */
            if(getString(R.string.login_url).equals(url)) {
                wvMain.clearHistory();
                wvMain.clearCache(true);
                Log.d("foxworld", "auto_login_yn=" + pref.getBoolean("auto_login_yn", false));
                if(prefLoginState == true && pref.getBoolean("auto_login_yn", false) == true) {
                    wvMain.loadUrl(String.format(getString(R.string.login_javascript), pref.getString("shop_id", ""), pref.getString("shop_password", "")));
                }
            } else if(getString(R.string.moira_url).equals(url)) {
                wvMain.clearHistory();
                wvMain.clearCache(true);
                Log.d("foxworld", "auto_login_yn=" + pref.getBoolean("auto_login_yn", false));
                if(prefLoginState == true && pref.getBoolean("auto_login_yn", false) == true) {
                    wvMain.loadUrl(String.format(getString(R.string.login_javascript_moira), pref.getString("shop_id", ""), pref.getString("shop_password", "")));
                }
            } else if(getString(R.string.yunasea_url).equals(url)) {

            }
            prefLoginState = false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            /*
            if (pd == null) {
                pd = ProgressDialog.show(MainActivity.this, "", getString(R.string.page_waiting_message), true);
            }
            */
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, getString(R.string.page_error_message), Toast.LENGTH_LONG).show();
            /*
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            */
    		/* 네트워크 및 기타 페이지를 열수가 없을때 최종 페이지를 담기 위해 처리*/
            wvMain.loadUrl(getString(R.string.network_error_url));
            errorURL = failingUrl;
        }
    };

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if(wvMain.canGoBack()) {
            wvMain.goBack();
        }
        else {
            backPressCloseHandler.onBackPressed();
/*
            new AlertDialog.Builder(this)
                    .setTitle("종료")
                    .setMessage("종료하시겠습니까?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(true)
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
*/
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("foxworld", "MainActivity.onActivityResult="+resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
