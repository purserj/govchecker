package com.openaussearchdroid;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;


public class SearchSenate extends Activity{
	
	private static String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private static Spinner states;
	private LinearLayout innerlayout;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchrep_senate);
        states = (Spinner) findViewById(R.id.StateSpinner);
        innerlayout = (LinearLayout) findViewById(R.id.senateinnerlayout);
        final String[] items = {"NSW", "VIC", "QLD", "TAS", "WA", "SA", "NT", "ACT"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        states.setAdapter(adapter);
        states.setOnItemSelectedListener(new OnItemSelectedListener(){ 
            @Override 
            public void onItemSelected(AdapterView parent, View v, 
                      int position, long id) { 
            	innerlayout.removeAllViewsInLayout();
            	String urlstring = "http://www.openaustralia.org/api/getSenators" +
      		  		"?key=" + oakey +
      		  		"&state=" + states.getSelectedItem().toString() +
      		  		"&output=json";
            	
            	Log.i("url", urlstring);
      		  	Context context = v.getContext();
            	URL url = null;
				try {
					url = new URL(urlstring);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
      		  	InputStream instream = null;
      		  	try{
      		  		instream = url.openStream();
      		  	} catch (IOException e){
      		  		
      		  	}
      		  	
      		  	try{
      		  		String result = convertStreamToString(url.openStream());
      		  		Log.i("GetResult",result);
      		  		JSONArray jsonr= null;
      		  		try{
      		  			jsonr = new JSONArray(result);
      		  		} catch(JSONException e){
      		  			Log.e("jsonerror", e.getMessage().toString());
      		  		}
      		  		
      		  		String memdata = null;
      		  		
      		  		for(int j = 0; j < jsonr.length(); j++){
      		  			JSONObject json = null;
						try {
							json = new JSONObject(jsonr.getJSONObject(j).toString());
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
      		  			JSONArray nameArray=json.names();
      		  			JSONArray valArray = null;
      		  			try {
      		  				valArray = json.toJSONArray(nameArray);
      		  			} catch (JSONException e) {
      		  				// TODO Auto-generated catch block
      		  				e.printStackTrace();
      		  			}
      		  		
      		  			String full_name = null;	
      		  			String party = null;
      		  			for(int i=0;i<valArray.length();i++)
      		  			{
      		  				try {
      		  					Log.i("Praeda","<jsonname"+i+">\n"+nameArray.getString(i)+"\n</jsonname"+i+">\n"
      		  							+"<jsonvalue"+i+">\n"+valArray.getString(i)+"\n</jsonvalue"+i+">");
      		  					if(nameArray.getString(i).equals("name")){
      		  						full_name = "Name: " + valArray.getString(i) + "\n";
      		  					}else if(nameArray.getString(i).equals("party")){
      		  						party = "Party: " + valArray.getString(i) + "\n";
      		  					}
      		  				}  catch (JSONException e) {
      		  					e.printStackTrace();
      		  				}
      		  			}
      		  			TextView tvr = new TextView(context);
      		  			tvr.setId(300+j);
      		  			tvr.setText(full_name + party);
      		  			innerlayout.addView(tvr);
      		  		}
      		  		
      		  	//results.setText(memdata);
      		  	}catch (IOException e){				
      		  		Log.e("DEBUGTAG", "Remtoe Image Exception", e);
      		  	}
      		  	
      		  } 

            @Override 
            public void onNothingSelected(AdapterView arg0) { 
                  
            } 
     });
    }
	
	private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
	}
}
