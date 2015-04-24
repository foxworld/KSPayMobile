package kr.co.kspay.m.kspaymobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by PeterLEE on 2015-02-23.
 */
public class AndroidWebChromeClient  extends WebChromeClient {
    private Activity activity;
    private ProgressBar progressBar;

    public AndroidWebChromeClient(Activity activity, ProgressBar progressBar) { // 다른 객체에서 사용을 할수 있도록 추가
        this.activity = activity;
        this.progressBar = progressBar;
    }
    public AndroidWebChromeClient(Activity activity) { // 다른 객체에서 사용을 할수 있도록 추가
        this.activity = activity;
        this.progressBar = null;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if( this.progressBar != null) {
            //Log.d("foxworld", "newProgress="+newProgress);
            this.progressBar.setProgress(newProgress);
        }
    }

    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        // TODO Auto-generated method stub

        new AlertDialog.Builder(this.activity)
                .setTitle("알림")
                .setMessage(message)  // 기존 alert 내용을 넣어주기 위해 받은거 그래로 넣기
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() { // 두개이상  같은 메소드가 있으면 하나는 풀메소드 이름을 부여 하여함

                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                result.confirm();
                            }
                        })
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {  // 백버튼 처리하기위해 넣어줌
                    public void onCancel(DialogInterface dialog) {
                        // TODO Auto-generated method stub
                        result.cancel();
                    }
                })
                .show();
        return true;
    }
}
