package com.govchecker;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONException;

import com.govchecker.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SearchRepsActivity extends Activity
{

	private static final String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private EditText _etext;
	private TextView _tv;
	private ImageView _iv;
	private Spinner _spinner;
	private Button _repsbutton;
	private ProgressBar _searchprog;
	@SuppressWarnings("unused")
	private String previousSearch = "";
	private Integer stype;
	private View _view;
	private TableLayout results;
	private AtomicBoolean searchInProgress;
	public static ArrayList<Rep_Object> reps = null;

	public void onCreate(Bundle savedInstanceState)
	{
		String[] options = new String[] {"By Postcode", "By Party", "By Date", "By Seat"};
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchrep_reps);
		searchInProgress = new AtomicBoolean(false);
		//_iv = (ImageView) findViewById(R.id.MemberPic);
		_spinner = (Spinner) findViewById(R.id.OptionSpinner);
		_repsbutton = (Button) findViewById(R.id.searchRepButton);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, options);
		_spinner.setAdapter(adapter);
		_spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
		results = (TableLayout) findViewById(R.id.repsresults);
		_repsbutton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				String _spinner_value = (String) _spinner.getSelectedItem();
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

				//_tv = (TextView) findViewById(R.id.TextView01);

				String baseUrlPath = "http://www.openaustralia.org";
				String urlString = "http://www.openaustralia.org/api/";
				
				
				if(_spinner_value == "By Seat"){
					urlString += "getRepresentative";
					stype = 1;
				}else if(_spinner_value == "By Party"){
					urlString += "getRepresentatives";
					stype = 3;
				}else {
					urlString += "getRepresentatives";
					stype = 2;
				}
				urlString += "?key=" + oakey;
				urlString += "&output=json";
				
				if(_spinner_value == "By Seat")
				{
					urlString += "&division=" + URLEncoder.encode(searchKey);
				} 
				else if(_spinner_value == "By Postcode")
				{
					urlString += "&postcode=" + URLEncoder.encode(searchKey);
				}
				else if(_spinner_value == "By Party")
				{
					urlString += "&party=" + URLEncoder.encode(searchKey);
				}
				Log.i("URL String", urlString);
				if (searchKey.equals("test"))
				{
					baseUrlPath = "http://d1b.org";
					urlString = "http://d1b.org/other/pub/tests/android/open_aus_search/test_get_rep";
				}

				RepSearch rep = new RepSearch(urlString, searchKey, baseUrlPath, stype);
				new PerformRepsSearch().execute(rep);
				previousSearch = searchKey;

				searchInProgress.set(false);
				Log.i("search_in_progress", " search REPS SEARCH STOPPED");
			}
		});
		
		_searchprog = (ProgressBar) findViewById(R.id.SearchProg);
	}


	public void searchRepClickHandler(View target)
	{

	}
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	      
	    }

	    public void onNothingSelected(AdapterView parent) {
	      // Do nothing.
	    }
	}

	public class PerformRepsSearch extends AsyncTask <RepSearch, Integer, RepSearch>
	{
		@Override
		protected void onPreExecute()
		{
			Toast toast = Toast.makeText(getApplicationContext(), "searching...", Toast.LENGTH_LONG);
			toast.show();
			_searchprog.setVisibility(View.VISIBLE);
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
				//repSearch.setImgLoc();
				reps = null;
				reps = repSearch.setFromJsonResultMemData();
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
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(RepSearch repSearch)
		{
			_searchprog.setVisibility(View.GONE);
			results.removeAllViews();
			LayoutInflater inflater = getLayoutInflater();
			if (repSearch == null)
			{
				//_tv.setText("An error occured\nNo results were found.");
				TableRow tr =(TableRow)inflater.inflate(R.layout.tablerow, results, false);
				TextView tvr = (TextView)tr.findViewById(R.id.content);
				Log.e("on_post_ex_search_rep", "rep is null");
				return;
			}

			if (repSearch.getSearchKey().equals("pwnie"))
			{
				Log.i("can haz pwnie?", "...pwnie!");
				_iv.setImageBitmap(repSearch.getRepImage());
				return;
			}
			if(reps != null){
				for(int i = 0; i < reps.size(); i++ )
				{
					final Rep_Object rep = reps.get(i);
					Log.d("Rep name", rep.get_Name());
					TableRow tr =(TableRow)inflater.inflate(R.layout.tablerow, results, false);
					TextView tvr = (TextView)tr.findViewById(R.id.content);
					tr.setId(100+i);
					tr.setLayoutParams(new LayoutParams(
		                    LayoutParams.FILL_PARENT,
		                    LayoutParams.WRAP_CONTENT)); 
					tvr.setBackgroundResource(R.drawable.border);
					tvr.setText(Html.fromHtml("<b>"
							+rep.get_Name()+" - "+rep.get_Constituency() +"<b>"));
					tvr.setOnClickListener(new OnClickListener()
					{
						public void onClick(View view)
						{
							Intent repIntent = new Intent(view.getContext(), Rep_Display.class);
							view.setBackgroundColor(1);
							Rep_Display.rep = rep;
							startActivityForResult(repIntent,0);
						}
					});
					results.addView(tr);
					//_iv.setImageBitmap(repSearch.getRepImage());
				}
			} else {
				Toast toast = Toast.makeText(getApplicationContext(), "No results", Toast.LENGTH_LONG);
				toast.show();
			}
			/* Grab Hansard Mentions */
			if (repSearch.getPersonID() == null)
			{
				Log.e("person_id", "is null ...");
				return;
			}
			/* XXX: perform code review of class member access - if is potentially null */
			/*String urlString = "http://www.openaustralia.org/api/getDebates" +
			"?key="+ oakey +
			"&type=representatives" +
			"&order=d" +
			"&person=" + repSearch.getPersonID();
			Log.i("OpenAusURL", urlString);
			new PerformHansardSearch().execute(new HansardSearch(urlString, _view, _tab));*/
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }

	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.Menu_My_Searchs:
			break;
		case R.id.Menu_Reps:
			break;
		case R.id.Menu_Senate:
			break;
		case R.id.Menu_Hansard:
			break;
		}
		
		return true;
	}

}
