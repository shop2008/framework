package com.wxxr.mobile.stock.client.widget;

import com.wxxr.mobile.stock.client.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class ArticleBodyView extends ViewGroup {

	private MyWebViewClient webClient;
	private WebSettings webSettings;
	private LinearLayout loadingBody;
	private WebView mWebView;
	private View view;
	private boolean isLoading = false;
	private int mArticleViewWidth;
	private int mArticleViewHeight;

	public ArticleBodyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ArticleBodyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ArticleBodyView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {

		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.article_body_layout, this, false);
		loadingBody = (LinearLayout) view.findViewById(R.id.ll_article_body);
		mWebView = (WebView) view.findViewById(R.id.wv_article_body);
		webSettings = mWebView.getSettings();
		webClient = new MyWebViewClient();
		mWebView.setWebViewClient(webClient);
		//mWebView.setWebChromeClient(new MyWebViewCromeClient());
		setWebViewAttribute();
		isLoading = false;
		addView(view);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		measureChild(view, widthMeasureSpec, heightMeasureSpec);
		mArticleViewWidth = view.getMeasuredWidth();
		mArticleViewHeight = view.getMeasuredHeight();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setWebViewAttribute() {
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		// webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebView.setInitialScale(0);
	}

	class MyWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			isLoading = true;
			loadingBody.setVisibility(View.VISIBLE);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			isLoading = false;
			loadingBody.setVisibility(View.GONE);
		}

	}

	public void loadURL(final String url) {
		if (isLoading)
			return;
		mWebView.loadUrl(url);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		view.layout(0, 0, mArticleViewWidth, mArticleViewHeight);
	}
}
