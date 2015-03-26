package com.govchecker;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONException;

import com.govchecker.R;

import android.app.Activity;
import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SearchRepsActivity extends Activity implements OnItemSelectedListener
{

	private static final String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private EditText _etext;
	private TextView _tv;
	private ImageView _iv;
	private Spinner _spinner;
	private Spinner _divspinner;
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
		String[] options = new String[] {"By Postcode", "By Seat", "By Party"};
		String[] divisions = new String[] {"Banks",
				"Barton",
				"Bennelong",
				"Berowra",
				"Blaxland",
				"Bradfield",
				"Calare",
				"Charlton",
				"Chifley",
				"Cook",
				"Cowper",
				"Cunningham",
				"Dobell",
				"Eden-Monaro",
				"Farrer",
				"Fowler",
				"Gilmore",
				"Grayndler",
				"Greenway",
				"Hughes",
				"Hume",
				"Hunter",
				"Kingsford Smith",
				"Lindsay",
				"Lyne",
				"Macarthur",
				"Mackellar",
				"Macquarie",
				"McMahon",
				"Mitchell",
				"New England",
				"Newcastle",
				"North Sydney",
				"Page",
				"Parkes",
				"Parramatta",
				"Paterson",
				"Reid",
				"Richmond",
				"Riverina",
				"Robertson",
				"Shortland",
				"Sydney",
				"Throsby",
				"Warringah",
				"Watson",
				"Wentworth",
				"Werriwa",
				"Aston",
				"Ballarat",
				"Batman",
				"Bendigo",
				"Bruce",
				"Calwell",
				"Casey",
				"Chisholm",
				"Deakin",
				"Dunkley",
				"Flinders",
				"Gellibrand",
				"Gippsland",
				"Goldstein",
				"Gorton",
				"Higgins",
				"Holt",
				"Hotham",
				"Indi",
				"Isaacs",
				"Jagajaga",
				"Kooyong",
				"Lalor",
				"La Trobe",
				"McEwen",
				"McMillan",
				"Mallee",
				"Maribyrnong",
				"Melbourne",
				"Melbourne Ports",
				"Menzies",
				"Murray",
				"Scullin",
				"Wannon",
				"Wills",
				"Blair",
				"Bonner",
				"Bowman",
				"Brisbane",
				"Capricornia",
				"Dawson",
				"Dickson",
				"Fadden",
				"Fairfax",
				"Fisher",
				"Flynn",
				"Forde",
				"Griffith",
				"Groom",
				"Herbert",
				"Hinkler",
				"Kennedy",
				"Leichhardt",
				"Lilley",
				"Longman",
				"Maranoa",
				"McPherson",
				"Moncrieff",
				"Moreton",
				"Oxley",
				"Petrie",
				"Rankin",
				"Ryan",
				"Wide Bay",
				"Wright",
				"Brand",
				"Canning",
				"Cowan",
				"Curtin",
				"Durack",
				"Forrest",
				"Fremantle",
				"Hasluck",
				"Moore",
				"O'Connor",
				"Pearce",
				"Perth",
				"Stirling",
				"Swan",
				"Tangney",
				"Adelaide",
				"Barker",
				"Boothby",
				"Grey",
				"Hindmarsh",
				"Kingston",
				"Makin",
				"Mayo",
				"Port Adelaide",
				"Sturt",
				"Wakefield",
				"Bass",
				"Braddon",
				"Denison",
				"Franklin",
				"Lyons",
				"Canberra",
				"Fraser",
				"Lingiari",
				"Solomon"};
        Arrays.sort(divisions);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchrep_reps);
		searchInProgress = new AtomicBoolean(false);
		//_iv = (ImageView) findViewById(R.id.MemberPic);
		_etext = (EditText) findViewById(R.id.EditText01);
		_spinner = (Spinner) findViewById(R.id.OptionSpinner);
		_divspinner = (Spinner) findViewById(R.id.DivisionSpinner);
		_repsbutton = (Button) findViewById(R.id.searchRepButton);
		ArrayAdapter<String> divadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, divisions);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, options);
		_spinner.setAdapter(adapter);
		_spinner.setOnItemSelectedListener(this);
		_divspinner.setAdapter(divadapter);
		_divspinner.setOnItemSelectedListener(this);
		results = (TableLayout) findViewById(R.id.repsresults);
		_repsbutton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				String _spinner_value = (String) _spinner.getSelectedItem();
				_view = v;
				
				String searchKey;
				
				if(_spinner_value == "By Seat"){
					stype = 1;
					searchKey = (String) _divspinner.getSelectedItem();
				}else {
					stype = 2;
					searchKey = _etext.getText().toString();
				}
				
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
				}else {
					urlString += "getRepresentatives";
				}
				
				urlString += "?key=" + oakey;
				urlString += "&output=json";

                switch(_spinner_value){
                    case "By Seat":
                        urlString += "&division=" + URLEncoder.encode(searchKey);
                        break;
                    case "By Postcode":
                        urlString += "&postcode=" + URLEncoder.encode(searchKey);
                        break;
                    case "By Party":
                        urlString += "&party=" + URLEncoder.encode(searchKey);
                        break;
                }
				Log.i("URL String", urlString);

				RepSearch rep = new RepSearch(urlString, searchKey, baseUrlPath, stype);
				new PerformRepsSearch(_view.getContext()).execute(rep);
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

	public class PerformRepsSearch extends AsyncTask <RepSearch, Integer, RepSearch>
	{
		
		private Context mContext;
		
		public PerformRepsSearch(Context context){
			mContext = context;
		}
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
					final TableRow tr =(TableRow)inflater.inflate(R.layout.tablerow, results, false);
					TextView tvr = (TextView)tr.findViewById(R.id.content);
					if(repSearch.getSType() == 1){
						Intent repIntent = new Intent(mContext, Rep_Display.class);
						Rep_Display.rep = rep;
						startActivityForResult(repIntent,0);
						return;
					}
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
							results.removeAllViews();
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
	
	/*
	 * TODO: Implement menu across all activities
	 *
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
	}*/


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(parent.getItemAtPosition(position).toString() == "By Seat"){
			this._etext.setVisibility(View.GONE);
			this._divspinner.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams rparams = (RelativeLayout.LayoutParams)_repsbutton.getLayoutParams();
			rparams.addRule(RelativeLayout.BELOW, R.id.DivisionSpinner);
			this._repsbutton.setLayoutParams(rparams);
		} else if(parent.getItemAtPosition(position).toString() == "By Postcode"){
			this._divspinner.setVisibility(View.GONE);
			this._etext.setText("");
			this._etext.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams rparams = (RelativeLayout.LayoutParams)_repsbutton.getLayoutParams();
			rparams.addRule(RelativeLayout.BELOW, R.id.EditText01);
			this._repsbutton.setLayoutParams(rparams);
		}
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

}
