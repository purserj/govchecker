package com.govchecker;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Utilities
{

	public static String convertStreamToString(InputStream is) throws IOException
	{
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		while ((line = reader.readLine()) != null)
		{
			sb.append(line +"\n");
		}
		return sb.toString();
	}

	public static void closeStream(InputStream instream)
	{
		try
		{
			if (instream != null)
			{
				instream.close();
			}
		}
		catch (IOException e)
		{
			recordStackTrace(e);
		}
	}

	public static String getDataFromUrl(String urlString, String logUrlEntry) throws IOException
	{
		Log.i(logUrlEntry, urlString);
		URL url = new URL(urlString);
		InputStream instream = null;
		instream = url.openStream();
		String result = convertStreamToString(instream);
		/* close the inputstream */
		closeStream(instream);
		return result;
	}

	public static void recordStackTrace(Exception e)
	{
		StringWriter tempSW = new StringWriter();
		/* capture the stack trace */
		e.printStackTrace(new PrintWriter(tempSW));
		Log.e(e.getMessage(),tempSW.toString());
	}
	
	public static void getRepVotes(Rep_Object rep){
		String resultRaw = null;
		String voteUrl;
		
		// First get the last 100 votes
		voteUrl = "https://theyvoteforyou.org.au/api/v1/divisions.json?key=UkkoIv7WVKsjpleqZEBD";
		
		try{
			resultRaw = Utilities.getDataFromUrl(voteUrl, "url");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try{
			JSONArray votesObj = new JSONArray(resultRaw);
			for(int i = 0; i < votesObj.length(); i++){
				JSONObject voteObj = votesObj.getJSONObject(i);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void getActivityData(Rep_Object rep){
		
		String resultRaw = null;
		JSONObject fullresult;
		
		String activityurl = "https://theyvoteforyou.org.au/api/v1/people/"+rep.get_personID().toString()
				+".json?key=UkkoIv7WVKsjpleqZEBD";
		
		try {
			resultRaw = Utilities.getDataFromUrl(activityurl, "url");		
			Log.d("ActivityResult", resultRaw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try{
			fullresult = new JSONObject(resultRaw);
			rep.set_Rebellions(fullresult.getInt("rebellions"));
			double v_attended = (double) fullresult.getInt("votes_attended");
			double v_possible = (double) fullresult.getInt("votes_possible");
			
			float pct = (float) ((v_attended*100)/v_possible);
			String attendance = "Not known";
			if(pct > (float)75){
				attendance = "Good attendance";
			} else if(pct > (float)50){
				attendance = "Fair attendance";
			} else if(pct > (float)25){
				attendance = "Poor attendance";
			} else {
				attendance = "Truant";
			}
			rep.set_Attendance(attendance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	public static boolean saveImage(Bitmap bm, String personId, Context context){
		File image = new File(context.getFilesDir(), personId+".jpg");
		
		try{
			FileOutputStream fos = new FileOutputStream(image);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, fos);
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
