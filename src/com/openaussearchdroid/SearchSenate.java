package com.openaussearchdroid;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;


public class SearchSenate extends Activity
{

	private static final String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private Spinner _states;
	private TableLayout _innerlayout;
	private View _view;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchrep_senate);
		_states = (Spinner) findViewById(R.id.StateSpinner);
		_innerlayout = (TableLayout) findViewById(R.id.SenateTable);
		final String[] items = {"NSW", "VIC", "queensland", "TAS", "WA", "SA", "NT", "ACT"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);

		_states.setAdapter(adapter);
		_states.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView parent, View v, 	int position, long id)
			{
				_view = v;
				_innerlayout.removeAllViewsInLayout();

				String urlString = "http://www.openaustralia.org/api/getSenators" +
				"?key=" + oakey +
				"&state=" + _states.getSelectedItem().toString() +
				"&output=json";
				new PerformSenateSearch().execute(urlString);


			}
			public void onNothingSelected(AdapterView arg0)
			{

			}
		});
	}
	private class PerformSenateSearch extends AsyncTask <String, Integer, JSONArray>
	{
		@Override
		protected JSONArray doInBackground(String... stringUrlArray)
		{
			String result = "";
			String urlString = stringUrlArray[0];
			try
			{
				result = Utilities.getDataFromUrl(urlString, "url");
			}
			catch (IOException e)
			{
				Utilities.recordStackTrace(e);
				return null;
			}
			Log.i("GotResult", result);
			JSONArray jsonr = null;
			try
			{
				jsonr = new JSONArray(result);
			}
			catch(JSONException e)
			{
				Utilities.recordStackTrace(e);
				return null;
			}
			return jsonr;
		}

		@Override
		protected void onPostExecute(JSONArray jsonr)
		{
			for(int j = 0; j < jsonr.length(); j++)
			{
				Context context = _view.getContext();
				TableRow tabr = new TableRow(context);
				ImageView iv = new ImageView(context);
				JSONObject json;
				try
				{
					json = new JSONObject(jsonr.getJSONObject(j).toString());
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					Utilities.recordStackTrace(e);
					return;
				}

				String full_name = null;
				String party = null;
				try
				{
					full_name =  "Name: " + json.get("name") + "\n";
				}
				catch (JSONException e)
				{
					Utilities.recordStackTrace(e);
				}
				try
				{
					party = "Party: " + json.get("party") + "\n";
				}
				catch (JSONException e)
				{
					Utilities.recordStackTrace(e);
				}

				TextView tvr = new TextView(context);
				tvr.setId(300+j);
				tvr.setText(full_name + party);
				tvr.setOnClickListener(new OnClickListener()
				{
					public void onClick(View view)
					{
						view.setBackgroundColor(1);
					}
				});
				tabr.addView(iv);
				tabr.addView(tvr);
				_innerlayout.addView(tabr);
			}
		}
	}



}
