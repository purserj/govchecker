package com.govchecker;

import com.govchecker.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.ShareActionProvider;

public class OAWebView extends Activity{
	
	private WebView wv;
	private ShareActionProvider mShareActionProvider;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		wv = (WebView) findViewById(R.id.WebView);
		
		Bundle extras = getIntent().getExtras();
		Log.d("fin_url", extras.getString("finurl"));
		
		wv.loadUrl(extras.getString("finurl"));
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_webview, menu);
		
		mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.share).getActionProvider();
		
		mShareActionProvider.setShareIntent(getDefaultShareIntent());
		
		return super.onCreateOptionsMenu(menu);	
	}
	
	private Intent getDefaultShareIntent(){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
		intent.putExtra(Intent.EXTRA_TEXT, wv.getUrl().toString());
		return intent;
	}

}
