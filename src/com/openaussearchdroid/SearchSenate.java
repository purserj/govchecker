package com.openaussearchdroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class SearchSenate extends Activity
{

	private static final String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private Spinner _states;
	private TableLayout _innerlayout;
	private View _view;
	private AtomicBoolean searchInProgress;

	private String previousState = "";
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchrep_senate);
		searchInProgress = new AtomicBoolean(false);
		_states = (Spinner) findViewById(R.id.StateSpinner);
		_innerlayout = (TableLayout) findViewById(R.id.SenateTable);
		final String[] items = {"NSW", "VIC", "QLD", "TAS", "WA", "SA", "NT", "ACT"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);

		_states.setAdapter(adapter);
		_states.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView parent, View v, 	int position, long id)
			{
				String stateSearch;
				_view = v;
				_innerlayout.removeAllViewsInLayout();
				stateSearch = _states.getSelectedItem().toString();
				if (stateSearch.equals("QLD") )
				{
					stateSearch = "queensland";
				}
				if (previousState.equals(stateSearch))
				{
					Log.i("duplicate_state_selection", "in search senate");
					return;
				}
				/** only get and set after we have checked for a duplicate search */
				if (searchInProgress.getAndSet(true))
				{
					Log.i("search_already_in_progress", " .. search going Senate");
					return;
				}

				String urlString = "http://www.openaustralia.org/api/getSenators" +
				"?key=" + oakey +
				"&state=" + stateSearch +
				"&output=json";
				Toast toast = Toast.makeText(getApplicationContext(), "searching...", Toast.LENGTH_LONG);
				toast.show();
				new PerformSenateSearch().execute(urlString);
				previousState = stateSearch;

				searchInProgress.set(false);
				Log.i("search_in_progress", " search SENATE STOPPED");

			}
			public void onNothingSelected(AdapterView arg0)
			{

			}
		});
	}
	private class PerformSenateSearch extends AsyncTask <String, Integer, ArrayList<SenateRep>>
	{
		@Override
		protected ArrayList<SenateRep> doInBackground(String... stringUrlArray)
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
			ArrayList <SenateRep> senatorRepList = new ArrayList();
			SenateRep rep;
			for(int i = 0; i < jsonr.length(); i++)
			{
				try
				{
					rep = new SenateRep(new JSONObject(jsonr.getJSONObject(i).toString()));
					rep.setFromJsonBasicDetails();
					rep.setImgLoc();
				}
				catch (JSONException e)
				{
					rep = null;
					Utilities.recordStackTrace(e);
				}
				try
				{
					/* try fetch and set the reps image */
					rep.fetchAndSetRepImage();
				}
				catch (IOException e)
				{
					Utilities.recordStackTrace(e);
				}
				if (rep == null)
				{
					continue;
				}
				senatorRepList.add(rep);
			}

			return senatorRepList;
		}

		@Override
		protected void onPostExecute(ArrayList<SenateRep> senatorRepList)
		{
			Context context = _view.getContext();
			if (senatorRepList == null)
			{
				TextView noResultsMessage = new TextView(context);
				noResultsMessage.setText("An error occured\nNo results were found.");
				TableRow tableRow = new TableRow(context);
				tableRow.addView(noResultsMessage);
				_innerlayout.addView(tableRow);
				Log.e("jsonr is null", "on post exec in Perform Senate Search");
				return;
			}

			for(int i = 0; i < senatorRepList.size(); i++)
			{
				SenateRep rep = senatorRepList.get(i);
				context = _view.getContext();
				TableRow tabr = new TableRow(context);
				ImageView iv = new ImageView(context);

				/* if the rep has an image, set it */
				if (rep.getRepImage() != null)
				{
					iv.setImageBitmap(rep.getRepImage());
				}

				TextView tvr = new TextView(context);
				tvr.setId(300+i);
				tvr.setText(rep.getName() + rep.getParty());
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
