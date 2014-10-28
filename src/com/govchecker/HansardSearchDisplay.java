package com.govchecker;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.openaussearchdroid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.LayoutInflater;

public class HansardSearchDisplay extends Activity{
	
	private LinearLayout hansLayout;
	View v;
	private String url; 
	private static final String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.hansardsearchdisplay);
		url = "http://www.openaustralia.org/api/getDebates?key="+oakey;
		hansLayout = (LinearLayout) findViewById(R.id.HansardResults);
		Bundle extras = getIntent().getExtras();
		v = new View(this);
		
		url += "&type=";
		url += extras.getString("searchHouse");
		/*switch(extras.getInt("searchHouse")){
		case(1): //House of Reps
			url += "representatives";
			break;
		case(2): //Senate
			url += "senate";
		break;
		}*/
		
		switch(extras.getInt("searchType")){
		case(1): //Date Search
			break;
		case(2): //Word Search
			url += "&search=";
			url += "\"" + extras.getString("searchTerm") + "\"";
			break;
		case(3): //Person Search
			
			url += "&person=" + extras.getInt("personId");
			break;
		}
		
		HansardSearch hsearch = new HansardSearch(url, v, hansLayout);
		PerformHansardSearch phsearch = new PerformHansardSearch();
		phsearch.execute(hsearch);
	}

	private class PerformHansardSearch extends AsyncTask <HansardSearch, Integer, JSONArray>
	{
		private View v;
		private LinearLayout hansInnerLayout;
		LayoutInflater inflater = getLayoutInflater();

		@Override
		protected JSONArray doInBackground(HansardSearch... hansSearchArray)
		{
			HansardSearch hansSearch = hansSearchArray[0];
			this.v = hansSearch.getView();
			this.hansInnerLayout = hansSearch.getHansInnerLayout();
			this.hansInnerLayout.removeAllViews();
			try
			{
				hansSearch.fetchSearchResultAndSetJsonResult();
			}
			catch (IOException e)
			{
				Utilities.recordStackTrace(e);
			}
			catch (JSONException e)
			{
				Utilities.recordStackTrace(e);
			}
			if (hansSearch.getResultJson() == null)
			{
				Log.e("json is null", "in doInBackground in PerformHansardSearch");
				return null;
			}

			JSONArray jsonArray;
			try
			{
				jsonArray = hansSearch.getResultJson().getJSONArray("rows");
			}
			catch (JSONException e)
			{
				Utilities.recordStackTrace(e);
				return null;
			}
			return jsonArray;
		}

		@Override
		protected void onPostExecute(JSONArray json)
		{
			if (json == null)
			{
				TextView noResultsMessage = new TextView(v.getContext());
				noResultsMessage.setText("An error occured\nNo results were found.");
				hansInnerLayout.addView(noResultsMessage);
				Log.e("json is null", "on post exec in PerformHansardSearch");

				return;
			}
			JSONObject jsonD;

			for(int i = 0; i < json.length(); i++)
			{
				jsonD = null;

				try
				{
					jsonD = json.getJSONObject(i);
				}
				catch (JSONException e)
				{
					Utilities.recordStackTrace(e);
				}
				
				try
				{
					JSONObject parent = jsonD.getJSONObject("parent");
					String temp = "";
					temp = "<b>" + parent.getString("body") + "</b>";
					temp += "<br /><i>" + jsonD.getString("hdate") + "</i>";
					temp += "<br />"+jsonD.getString("body");
					TableRow tr = (TableRow)inflater.inflate(R.layout.hansardrow, hansInnerLayout, false);
					TextView tvr = (TextView)tr.findViewById(R.id.content);
					tvr.setId(500+i);
					
					tvr.setText(Html.fromHtml(temp), TextView.BufferType.SPANNABLE );
					final String oaurl = "http://www.openaustralia.org"+jsonD.getString("listurl");
					Log.d("listurl", jsonD.getString("listurl"));
					Log.d("Url", oaurl);
					tvr.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent webView = new Intent(v.getContext(), OAWebView.class);
							webView.putExtra("finurl", oaurl);
							startActivityForResult(webView, 0);
						}
					});
					tvr.setBackgroundResource(R.drawable.border);
					hansInnerLayout.addView(tr);
				}
				catch (JSONException e)
				{
					Utilities.recordStackTrace(e);
				}
				
			}
		}
	}

}
