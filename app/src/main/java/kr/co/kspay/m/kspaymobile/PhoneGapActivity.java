package kr.co.kspay.m.kspaymobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaChromeClient;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhoneGapActivity extends ActionBarActivity implements CordovaInterface {
    CordovaWebView cwv;

    private CordovaPlugin activityResultCallback;
    private boolean activityResultKeepRunning;
    private boolean keepRunning;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_gap);


        cwv = (CordovaWebView) findViewById(R.id.cordvaWebView);

        WebSettings webSettings = cwv.getSettings();
        // webView set Option
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //webSettings.setDatabasePath(databasePath);

        Config.init(this);
        cwv.init(this,
            new CordovaWebViewClient(this, cwv),
            new CordovaChromeClient(this, cwv),
            Config.getPluginEntries(),
            Config.getWhitelist(),
            Config.getExternalWhitelist(),
            Config.getPreferences()
        );

        cwv.loadUrl(Config.getStartUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_phone_gap, menu);
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

    @Override
    public Object onMessage(String id, Object data) {
        Log.d("foxworld", "onMessage");
        return null;
    }

    @Override
    public ActionBarActivity getActivity() {
        return this;
    }

    @Override
    public void setActivityResultCallback(CordovaPlugin plugin) {
        Log.d("foxworld", "setActivityResultCallback");
        this.activityResultCallback = plugin;
    }
    /**
     * Launch an activity for which you would like a result when it finished. When this activity exits,
     * your onActivityResult() method is called.
     *
     * @param command           The command object
     * @param intent            The intent to start
     * @param requestCode       The request code that is passed to callback to identify the activity
     */
    public void startActivityForResult(CordovaPlugin command, Intent intent, int requestCode) {
        Log.d("foxworld", "startActivityForResult");
        this.activityResultCallback = command;
        this.activityResultKeepRunning = this.keepRunning;


// If multitasking turned on, then disable it for activities that return results
        if (command != null) {
            this.keepRunning = false;
        }
// Start activity
        super.startActivityForResult(intent, requestCode);
    }

    @Override
/**
 * Called when an activity you launched exits, giving you the requestCode you started it with,
 * the resultCode it returned, and any additional data from it.
 *
 * @param requestCode       The request code originally supplied to startActivityForResult(),
 *                          allowing you to identify who this result came from.
 * @param resultCode        The integer result code returned by the child activity through its setResult().
 * @param data              An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
 */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("foxworld", "onActivityResult");
        CordovaPlugin callback = this.activityResultCallback;
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Override
    public ExecutorService getThreadPool() {
        Log.d("foxworld", "ExecutorService");
        return threadPool;
    }


}

