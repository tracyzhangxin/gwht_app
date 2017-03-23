package com.example.administrator.gwht;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import Model.NewsModel;
import org.xutils.x;
@ContentView(R.layout.activity_news_detail)
public class NewsDetail extends BaseActivity {
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.webview)
    private WebView webview;
    @ViewInject(R.id.collectTab)
    private ImageView collectTab;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
         toolbar.setNavigationIcon(R.drawable.left_arrow);
         toolbar.setNavigationOnClickListener(new Toolbar.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onBackPressed();
             }
         });
        //setContentView(R.layout.activity_news_detail);
        Intent intent = getIntent();
        String url = intent.getExtras().getString("url");
       /* webview = (WebView) findViewById(R.id.webview);*/
        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webview.loadUrl(url);
        //设置Web视图
        webview.setWebViewClient(new webViewClient ());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        finish();//结束退出程序
        return false;
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Event(value = R.id.collectTab)
    private void onSinginClick(View view) {
        Toast.makeText(x.app(), "收藏成功", Toast.LENGTH_LONG).show();
    }

}
