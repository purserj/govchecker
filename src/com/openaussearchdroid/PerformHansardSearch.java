package com.openaussearchdroid;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PerformHansardSearch extends AsyncTask <HansardSearch, Integer, JSONArray>
{
	private View v;
	private LinearLayout hansInnerLayout;

	@Override
	protected JSONArray doInBackground(HansardSearch... hansSearchArray)
	{
		HansardSearch hansSearch = hansSearchArray[0];
		this.v = hansSearch.getView();
		this.hansInnerLayout = hansSearch.getHansInnerLayout();
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

			TextView tvr = new TextView(v.getContext());
			tvr.setId(500+i);
			String temp = "";
			try
			{
				temp = jsonD.getString("body");
				tvr.setText(Html.fromHtml(temp), TextView.BufferType.SPANNABLE );
			}
			catch (JSONException e)
			{
				Utilities.recordStackTrace(e);
			}
			hansInnerLayout.addView(tvr);
		}
	}
}
