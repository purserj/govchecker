package com.openaussearchdroid;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

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
	public static Bitmap fetchImage(String imgLoc) throws IOException
	{
		/*
		android is so awesome that the Log.i a few lines down
		will trigger a null pointer exception - because it cannot
		print null ... right ... - bail out early.
		The pwnie has severed it's place in debugging!
		 */
		if (imgLoc == null)
		{
			Log.i("fetchImage", "imgLoc was null");
			return null;
		}
		URL aURL;
		URLConnection con;
		Log.i("fetchImage", imgLoc);
		aURL = new URL(imgLoc);
		con = aURL.openConnection();
		con.setConnectTimeout(10000);
		con.setReadTimeout(10000);
		con.setDefaultUseCaches(true);
		/*
		android is so awesome that if you were to do
		bitmap = BitmapFactory.decodeStream(...)
		it can fail! (except for pwnies - no joke! - test it!)
			(on low bandwidth high latency connections - but not in the vm ;P)
		awesome++
		 */
		byte [] content = convertInputStreamToByteArray(con.getInputStream());
		closeStream(con.getInputStream());

		Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0, content.length);
		if (bitmap == null)
		{
			throw new IOException("bitmap is null");
		}
		return bitmap;
	}
	public static byte[] convertInputStreamToByteArray(InputStream is) throws IOException
	{
		BufferedInputStream bis = new BufferedInputStream(is);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int result = bis.read();
		while(result !=-1)
		{
			byte b = (byte)result;
			buf.write(b);
			result = bis.read();
		}
		return buf.toByteArray();
	}

}
