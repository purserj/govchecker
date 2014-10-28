package com.govchecker;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.openaussearchdroid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchHansardActivity extends Activity
{

	private EditText _et;

	private TextView _tv;
	private Spinner houseselect;
	private static final String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private Button hansButton;
	private AtomicBoolean searchInProgress;

	private String previousHouseSelection = "";
	private String previousKeySelection = "";

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchhansard);
		_et = (EditText) findViewById(R.id.SearchHansardText);
		searchInProgress = new AtomicBoolean(false);
		houseselect = (Spinner) findViewById(R.id.HouseSelector);
		final String[] items = {"representatives", "senate"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		houseselect.setAdapter(adapter);
		hansButton = (Button) findViewById(R.id.SearchHansardButton);
		hansButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent hansIntent = new Intent(v.getContext(), HansardSearchDisplay.class);
				hansIntent.putExtra("searchTerm", _et.getText().toString());
				hansIntent.putExtra("searchHouse", houseselect.getSelectedItem().toString());
				hansIntent.putExtra("searchType", 2);
				startActivityForResult(hansIntent, 0);

				Log.i("search_in_progress", " search HANSARD STOPPED");
			}
		});
	}


	public String getHansardUrl()
	{
		String urlString = "http://www.openaustralia.org/api/getDebates" +
		"?key=" + oakey +
		"&type=" + houseselect.getSelectedItem().toString() +
		"&search=" + URLEncoder.encode(_et.getText().toString()) +
		"&output=json";
		Log.i("Hansard URL", urlString);
		return urlString;
	}
}

