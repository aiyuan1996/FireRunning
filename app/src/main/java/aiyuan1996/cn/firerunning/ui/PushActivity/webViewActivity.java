package aiyuan1996.cn.firerunning.ui.PushActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import aiyuan1996.cn.firerunning.R;
import aiyuan1996.cn.firerunning.Receiver.MyReceiver;

/**
 * Created by aiyuan on 2017/2/20
 */
public class webViewActivity extends AppCompatActivity {
    private static final String TAG = "webViewActivity";
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(MyReceiver.path);
    }
}
