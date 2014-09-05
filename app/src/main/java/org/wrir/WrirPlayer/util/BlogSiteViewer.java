package org.wrir.WrirPlayer.util;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


import org.wrir.WrirPlayer.R;

/**
 * Created by xha89407 on 9/5/14.
 */
public class BlogSiteViewer extends Activity {
    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String blogUrl = extras.getString("blogUrl");
        setContentView(R.layout.blog_site_viewer);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(blogUrl);

    }
}
