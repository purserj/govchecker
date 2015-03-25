package com.govchecker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class VoteResultDisplay extends Activity{

	public LinearLayout _vresults;
	public View v;
	private String url;
	
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
		setContentView(R.layout.voteresultdisplay);
		
	}

}
