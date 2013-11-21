package com.wxxr.mobile.stock.client.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
//import android.webkit.WebSettings;
import android.webkit.WebView;

public class ArticleBodyView extends WebView {

	
	private MyWebViewClient webClient;
	private WebSettings webSettings;
	private HideProgressListener listener;
	
	private boolean isLoaded = false;
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
		webClient = new MyWebViewClient();
		
		webSettings = getSettings();
		webSettings.setJavaScriptEnabled(true);
		setWebChromeClient(webClient);
	}

	
	public void setHideProgressListener(HideProgressListener listener) {
		this.listener = listener;
	}
	
	public interface HideProgressListener {
		void hide();
	}
	
	class MyWebViewClient extends WebChromeClient {
		
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			
			if (newProgress == 100) {
				if (listener != null) {
					listener.hide();
				}
			}
			super.onProgressChanged(view, newProgress);
		}
	}
	
	
	public void loadUrl(final String url) {
		if (isLoaded) {
			return;
		}
		
		System.out.println("-----loadUrl-----");
		isLoaded = true;
		new Thread(){
			
			public void run() {
				loadUrl(url);
			};
			
		}.start();
	}
	
}
