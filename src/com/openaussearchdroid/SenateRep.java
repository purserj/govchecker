package com.openaussearchdroid;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class SenateRep
{
	private String name;
	private JSONObject resultJson;
	private String party;
	private String personID;
	private String imgLoc;
	private Bitmap repImage;

	private String baseUrlPath = "http://www.openaustralia.org/images/mpsL/";

	public SenateRep(JSONObject resultJson)
	{
		this.resultJson = resultJson;
	}
	public SenateRep(JSONObject resultJson, String baseUrlPath)
	{
		this.resultJson = resultJson;
		this.baseUrlPath = baseUrlPath;
	}
	public void setName() throws JSONException
	{
		this.name = "Name: " + this.resultJson.getString("name") + "\n";
	}

	public void setParty() throws JSONException
	{
		this.party = "Party: " + this.resultJson.getString("party") + "\n";
	}

	public void setPersonID() throws JSONException
	{
		this.personID = this.resultJson.getString("person_id");
	}

	public void setImgLoc() throws JSONException
	{
		this.imgLoc = this.baseUrlPath + this.getPersonID();
	}

	public void fetchAndSetRepImage() throws IOException
	{
		this.repImage = Utilities.fetchImage(this.imgLoc);
	}

	public void setFromJsonBasicDetails() throws JSONException
	{
		this.setPersonID();
		this.setName();
		this.setParty();
	}

	public Bitmap getRepImage()
	{
		return this.repImage;
	}
	public String getName()
	{
		return this.name;
	}
	public String getPersonID()
	{
		return this.personID;
	}
	public String getParty()
	{
		return this.party;
	}

}
