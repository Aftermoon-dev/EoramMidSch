package com.seven.emsmeals;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class HPView extends ActionBarActivity {
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hpview);

        webview = (WebView) findViewById(R.id.webView);
        WebSettings webviewset = webview.getSettings();
        webviewset.setJavaScriptEnabled(true);
        webviewset.setBuiltInZoomControls(true);
        webview.loadUrl("http://www.eoram.ms.kr/");
        webview.setWebViewClient(new WebClient());
    }

    private class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webview.loadUrl(url);
            return true;
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
           webview.goBack();
           return true;
        }
        else
        {
           finish();
           Intent intent = new Intent(HPView.this, MainActivity.class);
           startActivity(intent);
           return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hpview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.returnmain)
        {
            finish();
            Intent intent = new Intent(HPView.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
