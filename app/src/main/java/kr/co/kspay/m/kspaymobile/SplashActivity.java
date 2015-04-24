package kr.co.kspay.m.kspaymobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class SplashActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (isNetworkConnected(this) == false) {
            showNetworkError();
        }
        else {
            Handler hd = new Handler();
            hd.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.mainActivity = null;
                    finish();
                }
            }, 2000);
            super.onResume();
            showGuide();
        }
    }

    public void showGuide() {
        Toast toast = Toast.makeText(this, getString(R.string.start_title), Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showNetworkError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.show_network_error_message))
                .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton(getString(R.string.alert_title), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity mainActivity = (MainActivity)MainActivity.mainActivity;
                        mainActivity.finish();
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean  isNetworkConnected(Context context){
        boolean isConnected = false;
        ConnectivityManager connManager = null;
        NetworkInfo mobile = null;
        NetworkInfo wifi = null;
        try {
            connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mobile != null && mobile.isConnected()) {
                Log.d("foxworld", "mobile connected...");
                isConnected = true;
            }
            if (wifi != null && wifi.isConnected()) {
                Log.d("foxworld", "wifi connected ...");
                isConnected = true;
            }

            if (isConnected == false) {
                Log.d("foxworld", "Network is not connected ...");
            }
        } catch(Exception e) {
            Log.d("foxworld", e.getMessage());
        } finally {

        }

        return isConnected;
    }
}

