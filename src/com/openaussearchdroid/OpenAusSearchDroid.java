package com.openaussearchdroid;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.openaussearchdroid.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class OpenAusSearchDroid extends Activity {
    /** Called when the activity is first created. */
	
	private static Button repbutt;
	private static Button senbutt;
	private static Button hansbutt;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        repbutt = (Button) findViewById(R.id.SearchRep_Reps);
        senbutt = (Button) findViewById(R.id.SearchRep_Senate);
        hansbutt = (Button) findViewById(R.id.SearchHansard);
        
        repbutt.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(v.getContext(), SearchReps.class);
		        startActivityForResult(myIntent, 0);
			}
		});
        
    senbutt.setOnClickListener(new View.OnClickListener() {
    	
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		Intent myIntent = new Intent(v.getContext(), SearchSenate.class);
    		startActivityForResult(myIntent, 0);
		}
		});
    
    hansbutt.setOnClickListener(new View.OnClickListener() {
		
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent myIntent = new Intent(v.getContext(), SearchHansard.class);
	        startActivityForResult(myIntent, 0);
		}
	});
        
	}	

}