package com.openaussearchdroid;

import java.io.IOException;
import java.net.URLEncoder;

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

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchrep_reps);
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

				_tab.removeAllViewsInLayout();

				_tv = (TextView) findViewById(R.id.TextView01);
				String urlString = "http://www.openaustralia.org/api/getRepresentative" +
				"?key=" + oakey +
				"&division=" + URLEncoder.encode(searchKey) +
				"&output=json";

				RepSearch rep = new RepSearch(urlString, searchKey);
				new PerformRepsSearch().execute(rep);

			}
		});
	}



	public void searchRepClickHandler(View target)
	{

	}

	private class PerformRepsSearch extends AsyncTask <RepSearch, Integer, RepSearch>
	{
		@Override
		protected RepSearch doInBackground(RepSearch... repSearchArray)
		{
			RepSearch rep = repSearchArray[0];
			try
			{
					rep.fetchSearchResultAndSetJsonResult();
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
				rep.setFromJsonResultMemData();
			}
			catch (JSONException e)
			{
				Utilities.recordStackTrace(e);
			}
			try
			{
				rep.setImgLoc();
			}
			catch (JSONException e)
			{
				Utilities.recordStackTrace(e);
			}

			if (rep.getSearchKey().equals("pwnie") )
			{
				rep.setPwnieImageUrl();
			}

			try
			{
				rep.fetchAndSetRepImage();
			}
			catch (IOException e)
			{
				Utilities.recordStackTrace(e);
			}
			return rep;
		}
		@Override
		protected void onPostExecute(RepSearch rep)
		{
			if (rep.getSearchKey().equals("pwnie") && rep.getRepImage() != null)
			{
				_iv.setImageBitmap(rep.getRepImage());
				return;
			}
			_tv.setText(rep.getMemData());
			/* Grab Hansard Mentions */
			if (rep.getPersonID() == null)
			{
				Log.e("person_id", "is null ...");
				return;
			}
			/* XXX: perform code review of class member access - if is potentially null */
			String urlString = "http://www.openaustralia.org/api/getDebates" +
			"?key="+ oakey +
			"&type=representatives" +
			"&order=d" +
			"&person=" + rep.getPersonID();
			Log.i("OpenAusURL", urlString);
			new PerformHansardSearch().execute(new HansardSearch(urlString, _view, _tab));
			previousSearch = rep.getSearchKey();
		}
	}


}
