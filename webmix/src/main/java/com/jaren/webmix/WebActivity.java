package com.jaren.webmix;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.jaren.webmix.utils.DensityUtil;

public class WebActivity extends Activity {
    private static final int TV_HEIGHT = 100;
    private WebView mWebView;
    private WebView mWebViewBottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_activity);
        mWebView= findViewById(R.id.webview);
//        mWebViewBottom= findViewById(R.id.webview_bottom);
        WebSettings settings=mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

//        setLocalWeb();
        setNetWeb();

    }



    private void setLocalWeb() {
        mWebView.loadUrl("file:///android_asset/html_mix.html");
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                L.e("mWebView:",mWebView.getChildCount()+"");
                final TextView textView = getTextView();

                mWebView .evaluateJavascript("javaScript:getAdPosition()",
                    new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
//                            Log.i("getAdPosition:",value);
                            textView.setTranslationY(DensityUtil.dp2px(WebActivity.this,Float.parseFloat(value)));
                            mWebView.addView(textView);
                            mWebView.loadUrl("javaScript:setAdHeight("+TV_HEIGHT+")");
                        }
                    });
            }
        });
    }


    private void setNetWeb() {
        mWebView.loadUrl("https://www.baidu.com/");
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                L.e("mWebView:",mWebView.getChildCount()+"");
                final TextView textView = getTextView();
                mWebView.evaluateJavascript("javaScript: function getAdPosition() {\n"
                        + "        var advertisement = document.getElementById(\"index-form\");\n"
                        + "        return advertisement.offsetTop;\n"
                        + "    };getAdPosition()",
                    new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.e("getAdPosition:", value);
                            textView.setTranslationY(DensityUtil.dp2px(WebActivity.this, Float.parseFloat(value)));
                            mWebView.addView(textView);
                            mWebView.loadUrl("javaScript: function setAdHeight(height) {\n"
                                + "        var advertisement = document.getElementById(\"index-form\");\n"
                                + "        advertisement.style.marginTop=height+\"px\";\n"
                                + "    }; setAdHeight("+(TV_HEIGHT+16)+");");
                        }
                    });
            }
        });



    }

    @NonNull
    private TextView getTextView() {
        final TextView textView=new TextView(getApplication());
        textView.setTextColor(Color.GRAY);
        textView.setTextSize(20f);
        textView.setBackgroundColor(Color.YELLOW);
        textView.setText("WebActivity TextView ");
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,DensityUtil
            .dp2px(getApplication(),TV_HEIGHT)));
        return textView;
    }

}


//获取文档中 class="example" 的第一个元素:
//
//    document.querySelector(".example");