package com.govchecker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

class OpenAusDB{
	
	private static final String DATABASE_NAME = "openausdb.db";
	private static final int DATABASE_VERSION = 2;
	private static final String REP_SEARCH = "reps_search";
	private static final String HANSARD_SEARCH = "hansard_search";
	private static final String DIVISIONS = "divisions";
	private static final String SEARCH_RESULTS = "search_results";
	
	private Context context;
	private SQLiteDatabase db;
	
	private SQLiteStatement insertStmt;
	private static final String INSERT_INTO = "insert into " 
		+ REP_SEARCH +"(name) values(?)";
	
	public OpenAusDB(Context context){
		this.context = context;
		OpenHelper openhelper = new OpenHelper(this.context);
		this.db = openhelper.getWritableDatabase();
		this.insertStmt = this.db.compileStatement(INSERT_INTO);
	}
	
	public void clearAll(){
		this.db.delete(REP_SEARCH, null, null);
		this.db.delete(HANSARD_SEARCH, null, null);
	}
	
	private static class OpenHelper extends SQLiteOpenHelper{
		
		OpenHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		public void onCreate(SQLiteDatabase db){
			db.execSQL("CREATE TABLE " + REP_SEARCH + " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
					" name TEXT," +
					" rep_image BLOB," +
					" rep_entered_parliament TEXT," +
					" rep_still_in_office INTEGER," +
					" rep_my_local INTEGER," +
					" rep_last_updated TEXT)");
			db.execSQL("CREATE TABLE " + DIVISIONS + " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
					" div_name TEXT," +
					" div_rep INTEGER," +
					" div_state TEXT)");
			db.execSQL("CREATE TABLE " + HANSARD_SEARCH + "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
					" search_type TEXT," +
					" search_text TEXT," +
					" search_last_updated TEXT)");
			db.execSQL("CREATE TABLE " + SEARCH_RESULTS + "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
					" hans_search_id INTEGER," +
					" search_text TEXT," +
					" search_url TEXT)");
		}
		
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			Log.w("DB_UPGRADE", "Upgrading database by dropping and rebuilding tables");
			db.execSQL("DROP TABLE IF EXISTS " + REP_SEARCH);
			db.execSQL("DROP TABLE IF EXISTS " + DIVISIONS);
			onCreate(db);
		}
	}
}