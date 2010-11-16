package com.openaussearchdroid;

import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONException;

import com.openaussearchdroid.SearchRepsActivity.PerformRepsSearch;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Rep_Display extends Activity{
	
	private static final String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private TextView _tv;
	private TextView _ntv;
	private ImageView _iv;
	private Button _repsbutton;
	private LinearLayout _tab;
	@SuppressWarnings("unused")
	private TextView _tvhans;
	private TextView _htv;
	private TextView _ptv;
	private TextView _dtv;
	private View _view;
	public static Rep_Object rep;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rep_display);
		
		Bundle extras = getIntent().getExtras();
		
		/*_iv = (ImageView) findViewById(R.id.MemberPic);
		_tab = (LinearLayout) findViewById(R.id.innerlayout);
		_tvhans = (TextView) findViewById(R.id.hansardmentions_label);*/
		_ntv = (TextView) findViewById(R.id.NameTextView);
		_htv = (TextView) findViewById(R.id.HouseContent);
		_ptv = (TextView) findViewById(R.id.PartyContent);
		_dtv = (TextView) findViewById(R.id.DateContent);
		Log.d("Rep Display, Rep name: ", rep.get_Name());
		_ntv.setText(rep.get_Name());
		if(rep.get_House() == 1){
			_htv.setText("House of Representatives");
		} else {
			_htv.setText("Senate");
		}
		_ptv.setText(rep.get_Party());
		_dtv.setText(rep.get_DateEntered());
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
			"&type=senate" +
			"&order=d" +
			"&person=" + repSearch.getPersonID();
			Log.i("OpenAusURL", urlString);
			new PerformHansardSearch().execute(new HansardSearch(urlString, _view, _tab));
		}
	}
}
