package com.openaussearchdroid;

import org.json.JSONObject;

public class Rep_Object {
	
	/* This object is a representation of an Elected Representative, either Senate or
	 * House of Reps.
	 * 
	 * The idea is to farm out the rep specific functions to this object.
	 */
	
	private String first_name;
	private JSONObject resultJson;
	private String last_name;
	private String full_name;
	private String party;
	private Integer PID;
	private Integer house;
	private String constituency;
	private String date_entered;
    private String position;
	private String date_left;
	private String left_reason;
	private String last_updated;
	
	public Rep_Object()
	{
		
	}
	
	public Rep_Object(Integer pID)
	{
		getRepDetails(pID);
	}
	
	public void getRepDetails(Integer pID)
	{
		
	}
	
	public void populate_Rep()
	{
		
	}
	
	public void set_pID(Integer pID)
	{
		this.PID = pID;
	}
	
	public void set_FirstName(String name)
	{
		this.first_name = name;
	}
	
	public void set_LastName(String name)
	{
		this.last_name = name;
	}
	
	public void set_FullName(String name)
	{
		this.full_name = name;
	}
	public void set_House(Integer houseid)
	{
		house = houseid;
	}
	
	public void set_Constituency(String constit)
	{
		constituency = constit;
	}
	
	public void set_DateEntered(String Dstring)
	{
		date_entered = Dstring;
	}

    public void set_Position(String pos){
        position = pos;
    }
	
	public void set_Party(String partyid)
	{
		party = partyid;
	}
	
	public String get_Name()
	{
		if(first_name == null && last_name == null)
		{
			return full_name;
		} else {
			return first_name +" "+ last_name;
		}
	}
	
	public String get_FullName()
	{
		return full_name;
	}
	
	public String get_Party()
	{
		return party;
	}
	
	public Integer get_House()
	{
		return house;
	}
	
	public String get_Constituency()
	{
		return constituency;
	}
	
	public String get_DateEntered()
	{
		return date_entered;
	}
	
	public String get_DateLeft()
	{
		return date_left;
	}
	
	public String get_ReasonLeft()
	{
		return left_reason;
	}
	
	public Integer get_personID()
	{
		return this.PID;
	}

    public String get_Position(){
        String nullpos = "Backbencher";
        if(this.position != null){
            return this.position;
        } else {
            return nullpos;
        }
    }

}
