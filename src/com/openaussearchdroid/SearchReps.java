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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

public class SearchReps extends Activity{
	
	private static String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private EditText etext;
	private TextView tv;
	private ImageView iv;
	private Button repsbutton;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchrep_reps);
        repsbutton = (Button) findViewById(R.id.searchRepButton);
        repsbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
		    		etext = (EditText) findViewById(R.id.EditText01);
		    		tv = (TextView) findViewById(R.id.TextView01);
		  		  	String urlstring = "http://www.openaustralia.org/api/getRepresentative" +
		  		  	"?key=" + oakey +
		  		  	"&division=" + URLEncoder.encode(etext.getText().toString()) +
		  		  	"&output=json";
		  		  	Log.i("OpenAusURL", urlstring);
		  		  	URL url = new URL(urlstring);
		  		  	InputStream instream = null;
		  		  	try{
		  		  		instream = url.openStream();
		  		  	} catch (IOException e){
		  		  	}
		  		  	try{
		  		  		String result = convertStreamToString(url.openStream());
		  		  		Log.i("GetResult",result);
		  		  		JSONObject json= null;
		  		  		try{
		  		  			json = new JSONObject(result);
		  		  		} catch(JSONException e){
		  		  			
		  		  		}
		  		  		
		  		  		JSONArray nameArray=json.names();
		  		  		JSONArray valArray = null;
						try {
							valArray = json.toJSONArray(nameArray);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String memdata = null;
						String full_name = null;
						String date_entered = null;
						String party = null;
		  		  		for(int i=0;i<valArray.length();i++)
		  		  		{
		  		  			try {
								Log.i("Praeda","<jsonname"+i+">\n"+nameArray.getString(i)+"\n</jsonname"+i+">\n"
										+"<jsonvalue"+i+">\n"+valArray.getString(i)+"\n</jsonvalue"+i+">");
								if(nameArray.getString(i).equals("full_name")){
									full_name = "Name: " + valArray.getString(i) + "\n";
								}else if(nameArray.getString(i).equals("party")){
									party = "Party: " + valArray.getString(i) + "\n";
								}else if(nameArray.getString(i).equals("entered_house")){
									date_entered = "Date Elected: " + valArray.getString(i) + "\n";
								}else if(nameArray.getString(i).equals("image")){
									URL aURL = null;
									try
									{
										aURL = new URL("http://www.openaustralia.org" + valArray.getString(i));
										Log.i("searchrepimage", "http://www.openaustralia.org" + valArray.getString(i));
									}
									catch (MalformedURLException e1)
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									try
									{
										URLConnection con = aURL.openConnection();
										con.connect();
										InputStream is = con.getInputStream();
										/* Buffered is always good for a performance plus. */
										BufferedInputStream bis = new BufferedInputStream(is);
										/* Decode url-data to a bitmap. */
										Bitmap bm = BitmapFactory.decodeStream(bis);
										/*try{
											iv.setImageBitmap(bm);
										} catch (Exception e){
											Log.e("imagegraberror", e.getMessage().toString());
										}*/
										bis.close();
										is.close();
										/* Apply the Bitmap to the ImageView that will be
										returned. */

									}
									catch (IOException e)
									{
										Log.e("DEBUGTAG", "Remtoe Image Exception", e);
									}

								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
		  		  		}
		  		  		
		  		 		memdata = full_name + date_entered + party;
		  		  			
						tv.setText(memdata);
		  		  		// A Simple JSONObject Value Pushing
		  		  		try {
							json.put("sample key", "sample value");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		  		  		Log.i("Praeda","<jsonobject>\n"+json.toString()+"\n</jsonobject>");

		  		  		// Closing the input stream will trigger connection release
		  		  		instream.close();


		  		  	} catch(IOException e){
		  		  		Log.e("searchRepFail", e.getMessage().toString());
		  		  	}
		    	} catch( MalformedURLException e){
		    		e.printStackTrace();
		  	  	}

			}
		});
    }
	
	public void searchRepClickHandler(View target){
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
