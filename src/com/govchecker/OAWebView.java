package com.govchecker;

import com.govchecker.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class OAWebView extends Activity{
	
	private WebView wv;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		wv = (WebView) findViewById(R.id.WebView);
		
		Bundle extras = getIntent().getExtras();
		Log.d("fin_url", extras.getString("finurl"));
		
		wv.loadUrl(extras.getString("finurl"));
	}

}
