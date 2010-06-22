package com.openaussearchdroid;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SearchRepsActivity extends Activity
{

	private static final String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private EditText _etext;
	private TextView _tv;
	private ImageView _iv;
	private Button _repsbutton;
	private LinearLayout _tab;
	@SuppressWarnings("unused")
	private TextView _tvhans;
	private String previousSearch = "";
	private View _view;
	private AtomicBoolean searchInProgress;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchrep_reps);
		searchInProgress = new AtomicBoolean(false);
		_iv = (ImageView) findViewById(R.id.MemberPic);
		_tab = (LinearLayout) findViewById(R.id.innerlayout);
		_tvhans = (TextView) findViewById(R.id.hansardmentions_label);
		_repsbutton = (Button) findViewById(R.id.searchRepButton);
		_repsbutton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				_view = v;
				_etext = (EditText) findViewById(R.id.EditText01);
				String searchKey = _etext.getText().toString();
				if (previousSearch.equals(searchKey))
				{
					Log.i("duplicate_search", " in search reps");
					return;
				}
				/** only get and set after we have checked for a duplicate search */
				if (searchInProgress.getAndSet(true))
				{
					Log.i("search_already_in_progress", " .. search going Reps");
					return;
				}

				_tab.removeAllViewsInLayout();
				_tv = (TextView) findViewById(R.id.TextView01);

				String baseUrlPath = "http://www.openaustralia.org";
				String urlString = "http://www.openaustralia.org/api/getRepresentative" +
				"?key=" + oakey +
				"&division=" + URLEncoder.encode(searchKey) +
				"&output=json";
				if (searchKey.equals("test"))
				{
					baseUrlPath = "http://d1b.org";
					urlString = "http://d1b.org/other/pub/tests/android/open_aus_search/test_get_rep";
				}

				RepSearch rep = new RepSearch(urlString, searchKey, baseUrlPath);
				new PerformRepsSearch().execute(rep);
				previousSearch = searchKey;

				searchInProgress.set(false);
				Log.i("search_in_progress", " search REPS SEARCH STOPPED");
			}
		});
	}


	public void searchRepClickHandler(View target)
	{

	}

	private class PerformRepsSearch extends AsyncTask <RepSearch, Integer, RepSearch>
	{
		@Override
		protected void onPreExecute()
		{
			Toast toast = Toast.makeText(getApplicationContext(), "searching...", Toast.LENGTH_LONG);
			toast.show();
		}

		@Override
		protected RepSearch doInBackground(RepSearch... repSearchArray)
		{
			RepSearch repSearch = repSearchArray[0];
			try
			{
				repSearch.fetchSearchResultAndSetJsonResult();
			}
			catch (IOException e)
			{
				Utilities.recordStackTrace(e);
				return null;
			}
			catch (JSONException e)
			{
				Utilities.recordStackTrace(e);
				return null;
			}
			try
			{
				repSearch.setImgLoc();
				repSearch.setFromJsonResultMemData();
			}
			catch (JSONException e)
			{
				Utilities.recordStackTrace(e);
			}

			if (repSearch.getSearchKey().equals("pwnie") )
			{
				repSearch.setPwnieImageUrl();
			}

			try
			{
				repSearch.fetchAndSetRepImage();
			}
			catch (IOException e)
			{
				Utilities.recordStackTrace(e);
			}

			return repSearch;
		}
		@Override
		protected void onPostExecute(RepSearch repSearch)
		{
			if (repSearch == null)
			{
				_tv.setText("An error occured\nNo results were found.");
				Log.e("on_post_ex_search_rep", "rep is null");
				return;
			}

			if (repSearch.getSearchKey().equals("pwnie"))
			{
				Log.i("can haz pwnie?", "...pwnie!");
				_iv.setImageBitmap(repSearch.getRepImage());
				return;
			}
			_tv.setText(repSearch.getMemData());
			_iv.setImageBitmap(repSearch.getRepImage());

			/* Grab Hansard Mentions */
			if (repSearch.getPersonID() == null)
			{
				Log.e("person_id", "is null ...");
				return;
			}
			/* XXX: perform code review of class member access - if is potentially null */
			String urlString = "http://www.openaustralia.org/api/getDebates" +
			"?key="+ oakey +
			"&type=representatives" +
			"&order=d" +
			"&person=" + repSearch.getPersonID();
			Log.i("OpenAusURL", urlString);
			new PerformHansardSearch().execute(new HansardSearch(urlString, _view, _tab));
		}
	}

}
