package com.govchecker;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.govchecker.R;

public class Rep_Display extends Activity{
	
	private static final String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private TextView _tv;
	private TextView _ntv;
	private ImageView _iv;
	private ImageView _hansv;
	private ImageView _votev;
	@SuppressWarnings("unused")
	private TextView _tvhans;
    private TextView _postv;
	private TextView _htv;
	private TextView _ptv;
	private TextView _dtv;
	private TextView _distv;
	private TextView _aview;
	private TextView _rview;
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
		_hansv = (ImageView) findViewById(R.id.hansardSearchImage);
		_votev = (ImageView) findViewById(R.id.voteRecordImage);
        _postv = (TextView) findViewById(R.id.PosContent);
		_ntv = (TextView) findViewById(R.id.NameTextView);
		_htv = (TextView) findViewById(R.id.HouseContent);
		_ptv = (TextView) findViewById(R.id.PartyContent);
		_dtv = (TextView) findViewById(R.id.DateContent);
		_distv = (TextView) findViewById(R.id.DivisionContent);
		_aview = (TextView) findViewById(R.id.AttendanceContent);
		_rview = (TextView) findViewById(R.id.RebellionsContent);
		_iv = (ImageView) findViewById(R.id.RepImage);
		Log.d("Rep Display, Rep name: ", rep.get_Name());
		_ntv.setText(rep.get_Name());
		if(rep.get_House() == 1){
			_htv.setText("House of Representatives");
			house = "representatives";
		} else {
			_htv.setText("Senate");
			house = "senate";
			_hansv.setImageResource(R.drawable.hansard_sen);
		}
        _postv.setText(rep.get_Position());
		_ptv.setText(rep.get_Party());
		_dtv.setText(rep.get_DateEntered());
		_distv.setText(rep.get_Constituency());
		_aview.setText(rep.get_Attendance());
		_rview.setText(rep.get_Rebellions().toString());
		String uri = "@drawable/a_"+rep.get_personID();

		int imageResource = getResources().getIdentifier(uri, null, getPackageName());

		Drawable res = getResources().getDrawable(imageResource);
		_iv.setImageDrawable(res);
		/*File image = new File(this.getFilesDir(), Integer.toString(rep.get_personID())+".jpg");
		
		if(!image.exists()){
			ipath = "http://www.openaustralia.org/images/mpsL/"+Integer.toString(rep.get_personID())+".jpg";
			Log.d("Image Download", "Downloading image");
			new GetRepImageTask(this).execute(ipath, Integer.toString(rep.get_personID()));
		} else {
			Bitmap bm = BitmapFactory.decodeFile(image.getAbsolutePath());
			Log.d("Image Download", "Loading from file");
			_iv.setImageBitmap(bm);
		}*/

		_hansv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent hansIntent = new Intent(v.getContext(), HansardSearchDisplay.class);
				hansIntent.putExtra("personId", rep.get_personID());
				hansIntent.putExtra("searchHouse", house);
				hansIntent.putExtra("searchType", 3);
				startActivityForResult(hansIntent, 0);
				
			}
		});
		
		_votev.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent voteIntent = new Intent(v.getContext(), VoteResultDisplay.class);
				voteIntent.putExtra("personId", rep.get_personID());
			}
		});
	}

	private class GetRepImageTask extends AsyncTask<String, Void, Bitmap>{
		
		Context econtext;
		
		public GetRepImageTask(Context context){
			this.econtext = context;
		}
		@Override
		protected Bitmap doInBackground(String... params) {
			return downloadBitmap(params[0], params[1]);		
		}
		
		protected void onPreExecute(){
			Log.i("Picture Downloading", "onPreExecute called");
		}
		
		protected void onPostExecute(Bitmap result){
			Log.i("Picture Downloading", "onPostExecute called");
			_iv.setImageBitmap(result);
		}
		
		private Bitmap downloadBitmap(String url, String personId){
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
						Utilities.saveImage(bm, personId, econtext);
						
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
