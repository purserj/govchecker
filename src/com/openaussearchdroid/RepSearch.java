package com.openaussearchdroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class RepSearch
{
	private final String url;
	private JSONObject resultJson;
	private JSONArray resultJsonArray;
	private String resultRaw;
	private String memData;
	private String fullName;
	private String dateEntered;
	private String party;
	private String personID;
	private String imgLoc;
	private Bitmap repImage;
	private Integer stype;
    public static ArrayList<Rep_Object> reps = null;
    
	private String searchKey = "";
	private String baseUrlPath = "http://www.openaustralia.org";

	public RepSearch(String url, String searchKey, Integer st)
	{
		this.url = url;
		this.searchKey = searchKey;
		this.stype = st;
	}
	public RepSearch(String url, String searchKey, String baseUrlPath, Integer st)
	{
		this.url = url;
		this.searchKey = searchKey;
		this.baseUrlPath = baseUrlPath;
		this.stype = st;
	}

	public void fetchSearchResult() throws IOException
	{
		this.resultRaw = Utilities.getDataFromUrl(this.url, "url");
	}
	public void setJsonResultFromRawResult() throws JSONException
	{
		if(stype == 1)
		{
			this.resultJson = new JSONObject(this.resultRaw);
		}
		else
		{
			this.resultJsonArray = new JSONArray(this.resultRaw);
		}
	}
	public void fetchSearchResultAndSetJsonResult() throws IOException, JSONException
	{
		fetchSearchResult();
		setJsonResultFromRawResult();
	}

	public void setMemdata()
	{
		this.memData = this.fullName + dateEntered + party;
	}

	public void setFullName() throws JSONException
	{
		this.fullName = "Name: " + this.resultJson.getString("full_name") + "\n";
	}

	public void setDateEntered() throws JSONException
	{
		this.dateEntered = "Date Elected: " + this.resultJson.getString("entered_house") + "\n";
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
		this.imgLoc = this.baseUrlPath + this.resultJson.getString("image");
	}

	public void fetchAndSetRepImage() throws IOException
	{
		this.repImage = Utilities.fetchImage(this.imgLoc);
	}

	public void setPwnieImageUrl()
	{
		this.imgLoc = "http://d1b.org/other/pub/tests/android/open_aus_search/pwnie.png";
	}

	public ArrayList<Rep_Object> setFromJsonResultMemData() throws JSONException
	{
		reps = new ArrayList<Rep_Object>();
		if(this.stype == 1){
			Rep_Object rep = new Rep_Object();
			rep.set_pID(Integer.parseInt(this.resultJson.getString("person_id")));
			rep.set_FirstName(this.resultJson.getString("first_name"));
			rep.set_LastName(this.resultJson.getString("last_name"));
			rep.set_DateEntered(this.resultJson.getString("entered_house"));
			rep.set_Party(this.resultJson.getString("party"));
			rep.set_Constituency(this.resultJson.getString("constituency"));
			rep.set_House(Integer.parseInt(this.resultJson.getString("house")));
			reps.add(rep);	
		} else if (this.stype == 2) {
			for(int i = 0; i < resultJsonArray.length(); i++)
			{
				Rep_Object rep = new Rep_Object();
				JSONObject obj = resultJsonArray.getJSONObject(i);
				rep.set_pID(Integer.parseInt(obj.getString("person_id")));
				rep.set_FirstName(obj.getString("first_name"));
				rep.set_LastName(obj.getString("last_name"));
				rep.set_DateEntered(obj.getString("entered_house"));
				rep.set_Party(obj.getString("party"));
				rep.set_Constituency(obj.getString("constituency"));
				rep.set_House(Integer.parseInt(obj.getString("house")));
				reps.add(rep);
			}
		} else if (this.stype == 3){
			for(int i = 0; i < resultJsonArray.length(); i++)
			{
				Rep_Object rep = new Rep_Object();
				JSONObject obj = resultJsonArray.getJSONObject(i);
				rep.set_pID(Integer.parseInt(obj.getString("person_id")));
				rep.set_FullName(obj.getString("name"));
				rep.set_Party(obj.getString("party"));
				rep.set_Constituency(obj.getString("constituency"));
				rep.set_House(Integer.parseInt(obj.getString("house")));
				reps.add(rep);
			}
		}
		
		return reps;
	}

	public String getResultRaw()
	{
		return this.resultRaw;
	}
	public JSONObject getResultJson()
	{
		return this.resultJson;
	}
	public Bitmap getRepImage()
	{
		return this.repImage;
	}
	public String getFullName()
	{
		return this.fullName;
	}
	public String getImgLoc()
	{
		return this.imgLoc;
	}
	public String getDateEntered()
	{
		return this.dateEntered;
	}
	public String getPersonID()
	{
		return this.personID;
	}
	public String getParty()
	{
		return this.party;
	}
	public String getMemData()
	{
		return this.memData;
	}
	public String getSearchKey()
	{
		return this.searchKey;
	}
}
