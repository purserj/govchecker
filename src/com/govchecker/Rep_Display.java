package com.govchecker;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.govchecker.R;

public class Rep_Display extends Activity{
	
	private static final String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private TextView _tv;
	private TextView _ntv;
	private ImageView _iv;
	private Button _repsbutton;
	private Button _repshansard;
	private LinearLayout _tab;
	@SuppressWarnings("unused")
	private TextView _tvhans;
    private TextView _postv;
	private TextView _htv;
	private TextView _ptv;
	private TextView _dtv;
	private TextView _distv;
	private String house;
	private View _view;
	private String ipath;
	public static Rep_Object rep;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rep_display);
		
		Bundle extras = getIntent().getExtras();
		
		/*_iv = (ImageView) findViewById(R.id.MemberPic);
		_tab = (LinearLayout) findViewById(R.id.innerlayout);
		_tvhans = (TextView) findViewById(R.id.hansardmentions_label);*/
		_repshansard = (Button) findViewById(R.id.RepsHansardButton);
        _postv = (TextView) findViewById(R.id.PosContent);
		_ntv = (TextView) findViewById(R.id.NameTextView);
		_htv = (TextView) findViewById(R.id.HouseContent);
		_ptv = (TextView) findViewById(R.id.PartyContent);
		_dtv = (TextView) findViewById(R.id.DateContent);
		_distv = (TextView) findViewById(R.id.DivisionContent);
		_iv = (ImageView) findViewById(R.id.RepImage);
		Log.d("Rep Display, Rep name: ", rep.get_Name());
		_ntv.setText(rep.get_Name());
		if(rep.get_House() == 1){
			_htv.setText("House of Representatives");
			house = "representatives";
		} else {
			_htv.setText("Senate");
			house = "senate";
		}
        _postv.setText(rep.get_Position());
		_ptv.setText(rep.get_Party());
		_dtv.setText(rep.get_DateEntered());
		_distv.setText(rep.get_Constituency());
		ipath = "http://www.openaustralia.org/images/mpsL/"+Integer.toString(rep.get_personID())+".jpg";
		
		new GetRepImageTask().execute(ipath);
		
		_repshansard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent hansIntent = new Intent(v.getContext(), HansardSearchDisplay.class);
				hansIntent.putExtra("personId", rep.get_personID());
				hansIntent.putExtra("searchHouse", house);
				hansIntent.putExtra("searchType", 3);
				startActivityForResult(hansIntent, 0);
				
			}
		});
	}

	private class GetRepImageTask extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			return downloadBitmap(params[0]);		
		}
		
		protected void onPreExecute(){
			Log.i("Picture Downloading", "onPreExecute called");
		}
		
		protected void onPostExecute(Bitmap result){
			Log.i("Picture Downloading", "onPostExecute called");
			_iv.setImageBitmap(result);
		}
		
		private Bitmap downloadBitmap(String url){
			final DefaultHttpClient client = new DefaultHttpClient();
			
			final HttpGet getRequest = new HttpGet(url);
			
			try{
				HttpResponse response = client.execute(getRequest);
				final int statuscode = response.getStatusLine().getStatusCode();
				if(statuscode != HttpStatus.SC_OK){
					Log.w("Picture Downloading", "Error " + statuscode +
							" while retrieving " + url);
					return null;
				}
				
				final HttpEntity entity = response.getEntity();
				if(entity != null){
					InputStream inputstream = null;
					try{
						inputstream = entity.getContent();
						
						final Bitmap bm = BitmapFactory.decodeStream(inputstream);
						
						return bm;
						
					} finally {
						if (inputstream != null){
							inputstream.close();
						}
						entity.consumeContent();
					}
				}
			} catch (Exception e){
				getRequest.abort();
				Log.e("Download Picture", "Something went horribly wrong" +
						url + " " + e.toString());
			}
			
			return null;
		}
	}


}
