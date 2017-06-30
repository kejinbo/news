package com.itheima.news01;

import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.yls.test.R;
import com.itheima.news01.bean.NewsEntity;

/**
 * Created by yls on 2017/6/29.
 */
public class NewsDetailActivity extends BaseActivity {
    private WebView webView;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initView() {
        initWebView();
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.web_view);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        NewsEntity.ResultBean newsBean =
                (NewsEntity.ResultBean) getIntent().getSerializableExtra("news");
        String newUrl = newsBean.getUrl();
        webView.loadUrl(newUrl);
        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            
        }
        return super.onKeyDown(keyCode, event);
    }
}
