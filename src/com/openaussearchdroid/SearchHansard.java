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
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

public class SearchHansard extends Activity{
	
	private static EditText et;
	private static TextView tv;
	private static Spinner houseselect;
	private static String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private static Button hansbutton;
	private static LinearLayout hansinner;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchhansard);
        houseselect = (Spinner) findViewById(R.id.HouseSelector);
        hansinner = (LinearLayout) findViewById(R.id.hansinnerlayout);
        final String[] items = {"representatives", "senate"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        houseselect.setAdapter(adapter);
        hansbutton = (Button) findViewById(R.id.SearchHansardButton);
        hansbutton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				et = (EditText) findViewById(R.id.SearchHansardText);
				String urlstring = "http://www.openaustralia.org/api/getDebates" +
			  		"?key=" + oakey +
			  		"&type=" + houseselect.getSelectedItem().toString() +
			  		"&search=" + URLEncoder.encode(et.getText().toString()) +
			  		"&output=json";
				
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
			  		
				Log.i("url", urlstring);
				
				try{
				  		String result = convertStreamToString(url.openStream());
				  		Log.i("GetResult",result);
				  		JSONObject jsonr = null;
				  		try{
				  			jsonr = new JSONObject(result);
				  		} catch(JSONException e){
				  			Log.e("jsonerror", e.getMessage().toString());
				  		}
				  		
				  		String memdata = null;
				  		hansinner.removeAllViewsInLayout();
				  		
				  	
				  		JSONArray json = null;
				  		try {
				  			json = jsonr.getJSONArray("rows");
				  		} catch (JSONException e1) {
				  				// TODO Auto-generated catch block
				  			e1.printStackTrace();
				  		}
				  			
				  		for(int i = 0; i < json.length(); i++){
				  			memdata = "";
				  			JSONObject jsond = null;
				  			try {
				  				jsond = json.getJSONObject(i);
				  			} catch (JSONException e1) {
				  				// TODO Auto-generated catch block
				  				e1.printStackTrace();
				  			}
				  			
				  			
				  			//memdata += quote + "\n\n";
				  			TextView tvr = new TextView(v.getContext());
				  			tvr.setId(500+i);
				  			try{
				  				tvr.setText(jsond.getString("body"));
				  			} catch (JSONException e) {
				  				e.printStackTrace();
				  			}
				  			hansinner.addView(tvr);
				  		}
				  	
				  }catch (IOException e){				
					  Log.e("DEBUGTAG", "Remtoe Image Exception", e);
				  }
			}
		});
	}
	
	public void searchHansardButtonClick(View target){
		et = (EditText) findViewById(R.id.SearchHansardText);
		String urlstring = "http://www.openaustralia.org/api/getDebates" +
	  		"?key=" + oakey +
	  		"&type=" + houseselect.getSelectedItem().toString() +
	  		"&search=" + URLEncoder.encode(et.getText().toString()) +
	  		"&output=json";
		
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
	  		
		Log.i("url", urlstring);
		
		try{
		  		String result = convertStreamToString(url.openStream());
		  		Log.i("GetResult",result);
		  		JSONObject jsonr = null;
		  		try{
		  			jsonr = new JSONObject(result);
		  		} catch(JSONException e){
		  			Log.e("jsonerror", e.getMessage().toString());
		  		}
		  		
		  		String memdata = null;
		  		
		  		for(int j = 0; j < jsonr.length(); j++){
		  			JSONArray json = null;
		  			try {
		  				json = jsonr.getJSONArray("rows");
		  			} catch (JSONException e1) {
		  				// TODO Auto-generated catch block
		  				e1.printStackTrace();
		  			}
		  			
		  			for(int i = 0; i < json.length(); i++){
		  			
		  				JSONObject jsond = null;
						try {
							jsond = new JSONObject(json.getJSONObject(j).toString());
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		  			
		  				JSONArray nameArray=jsond.names();
		  				JSONArray valArray = null;
		  				try {
		  					valArray = jsond.toJSONArray(nameArray);
		  				} catch (JSONException e) {
		  					// TODO Auto-generated catch block
		  					e.printStackTrace();
		  				}
		  		
		  			String person = null;	
		  			String quote = "";
		  			for(int k=0;k<valArray.length();k++)
		  			{
		  				try {
		  					/*Log.i("Praeda","<jsonname"+k+">\n"+nameArray.getString(k)+"\n</jsonname"+i+">\n"
		  							+"<jsonvalue"+k+">\n"+valArray.getString(k)+"\n</jsonvalue"+i+">");*/
		  						if(nameArray.getString(k).equals("body")){
		  							quote = valArray.getString(k).toString();
		  						}
		  					} catch (JSONException e) {
		  					e.printStackTrace();
		  					}
		  						  					
		  			}
		  			memdata += quote + "\n\n";
		  			
		  		}
		  			tv.setText(memdata);
		  		}
		  	}catch (IOException e){				
		  		Log.e("DEBUGTAG", "Remtoe Image Exception", e);
		  	}
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
