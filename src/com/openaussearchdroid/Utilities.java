package com.openaussearchdroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

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
			Log.e("close stream", e.getMessage());
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
}
