package com.openaussearchdroid;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.LinearLayout;


public class HansardSearch
{
	private final String url;
	private JSONObject resultJson;
	private String resultRaw;
	private View view;
	private LinearLayout hansInnerLayout;

	public HansardSearch(String url)
	{
		this.url = url;
	}
	public HansardSearch(String url, View view, LinearLayout hansInnerLayout)
	{
		this.url = url;
		this.view = view;
		this.hansInnerLayout = hansInnerLayout;
	}

	public void fetchSearchResult() throws IOException
	{
		this.resultRaw = Utilities.getDataFromUrl(this.url, "url");
	}
	public void setJsonResultFromRawResult() throws JSONException
	{
		this.resultJson = new JSONObject(this.resultRaw);
	}
	public void fetchSearchResultAndSetJsonResult() throws IOException, JSONException
	{
		fetchSearchResult();
		setJsonResultFromRawResult();
	}
	public String getResultRaw()
	{
		return this.resultRaw;
	}
	public JSONObject getResultJson()
	{
		return this.resultJson;
	}
	public View getView()
	{
		return this.view;
	}
	public LinearLayout getHansInnerLayout()
	{
		return this.hansInnerLayout;
	}

}
