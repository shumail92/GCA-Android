/**
* Date: Jun 16, 2014
*/

package com.shumail.gca.abstracts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private String gtag = "GCA-DB";
	
	private static String Database_Name = "gca.db";

    private static int Database_Version = 5;

    public static SQLiteDatabase database;
    
    public long items_id;
    public long author_id;
    public long abs_auth_pos_id;
    
    /*
     * Tables Name
     */
    
    public static final String TABLE_ABSTRACT_DETAILS = "ABSTRACT_DETAILS";
    
    public static final String TABLE_AUTHORS_DETAILS = "AUTHORS_DETAILS";
    
    public static final String TABLE_ABSTRACT_AUTHOR_POSITION_AFFILIATION = "ABSTRACT_AUTHOR_POSITION_AFFILIATION";
    
    public static final String TABLE_AFFILIATION_DETAILS = "AFFILIATION_DETAILS";
    
    public static final String TABLE_ABSTRACT_AFFILIATION_ID_POSITION = "ABSTRACT_AFFILIATION_ID_POSITION";
    
    public static final String TABLE_ABSTRACT_REFERENCES = "ABSTRACT_REFERENCES";
    
    
    /*
     * Query for Creating Tables
     */
    
    public static final String CREATE_ABSTRACT_DETAILS = "CREATE TABLE IF NOT EXISTS ABSTRACT_DETAILS"
            + "(UUID VARCHAR PRIMARY KEY, TOPIC TEXT NOT NULL, "
            + "TITLE TEXT NOT NULL, ABSRACT_TEXT TEXT NOT NULL,"
            + "STATE TEXT NOT NULL, SORTID INTEGER NOT NULL, REASONFORTALK TEXT," 
            + "MTIME TEXT NOT NULL, TYPE TEXT NOT NULL, DOI TEXT, COI TEXT,"
            + "ACKNOWLEDGEMENTS TEXT );";
    
    public static final String CREATE_AUTHORS_DETAILS = "CREATE TABLE IF NOT EXISTS AUTHORS_DETAILS"
            + "( AUTHOR_UUID VARCHAR PRIMARY KEY, AUTHOR_FIRST_NAME TEXT NOT NULL, AUTHOR_MIDDLE_NAME TEXT, " 
    		+ "AUTHOR_LAST_NAME TEXT NOT NULL, AUTHOR_EMAIL TEXT NOT NULL);";
    
    public static final String CREATE_ABSTRACT_AUTHOR_POSITION_AFFILIATION = "CREATE TABLE IF NOT EXISTS ABSTRACT_AUTHOR_POSITION_AFFILIATION"
            + "( ABSTRACT_UUID VARCHAR NOT NULL, AUTHOR_UUID VARCHAR NOT NULL, " 
    		+ "AUTHOR_POSITION INTEGER NOT NULL, AUTHOR_AFFILIATION VARCHAR NOT NULL);";
    
    public static final String CREATE_AFFILIATION_DETAILS = "CREATE TABLE IF NOT EXISTS " + TABLE_AFFILIATION_DETAILS
            + "(AFFILIATION_UUID VARCHAR PRIMARY KEY, AFFILIATION_ADDRESS TEXT NOT NULL, AFFILIATION_COUNTRY TEXT NOT NULL, " 
    		+ "AFFILIATION_DEPARTMENT TEXT NOT NULL, AFFILIATION_SECTION TEXT);";
    
    public static final String CREATE_ABSTRACT_AFFILIATION_ID_POSITION = "CREATE TABLE IF NOT EXISTS " + TABLE_ABSTRACT_AFFILIATION_ID_POSITION 
            + "( ABSTRACT_UUID VARCHAR NOT NULL, AFFILIATION_UUID VARCHAR NOT NULL, " 
    		+ "AFFILIATION_POSITION INTEGER NOT NULL);";
    
    
    public static final String CREATE_ABSTRACT_REFERENCES = "CREATE TABLE IF NOT EXISTS " + TABLE_ABSTRACT_REFERENCES
    			+ "( ABSTRACT_UUID VARCHAR NOT NULL, REF_UUID VARCHAR NOT NULL, " 
    			+ "REF_TEXT TEXT, REF_LINK TEXT, REF_DOI TEXT);"; 
    
    
    public DatabaseHelper(Context context) {
        super(context, Database_Name, null, Database_Version);
        // TODO Auto-generated constructor stub
    }

	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		
		/*
         * Creating Tables
         */
		database.execSQL(CREATE_ABSTRACT_DETAILS);
		database.execSQL(CREATE_AUTHORS_DETAILS);
		database.execSQL(CREATE_ABSTRACT_AUTHOR_POSITION_AFFILIATION);
		database.execSQL(CREATE_AFFILIATION_DETAILS);
		database.execSQL(CREATE_ABSTRACT_AFFILIATION_ID_POSITION);
		database.execSQL(CREATE_ABSTRACT_REFERENCES);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_DETAILS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHORS_DETAILS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_AUTHOR_POSITION_AFFILIATION);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_AFFILIATION_DETAILS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_AFFILIATION_ID_POSITION);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_REFERENCES);
		
		
		onCreate(database);
		
	}
	
	public void open() {
        /*
         * Opening Database
         */

        database = this.getWritableDatabase();
    }
	
	public void close() {
        /*
         * Closing Database
         */
		
        database.close();
    }
	
	//function for adding data in ABSTRACT_DETAILS table
	public void addItems(String uuid, String topic, String Title, String text, String STATE, int SortID,
            String reasonsForTalk, String mtime, String type, String DOI, String COI, String acknowledgements) {
        ContentValues cd = new ContentValues();

        cd.put("UUID", uuid);

        cd.put("TOPIC", topic);

        cd.put("TITLE", Title);

        cd.put("ABSRACT_TEXT", text);

        cd.put("STATE", STATE);

        cd.put("SORTID", SortID);

        cd.put("REASONFORTALK", reasonsForTalk);

        cd.put("MTIME", mtime);

        cd.put("TYPE", type);

        cd.put("DOI", DOI);

        cd.put("COI", COI);
        
        cd.put("ACKNOWLEDGEMENTS", acknowledgements);

        items_id = database.insert(TABLE_ABSTRACT_DETAILS, null, cd);
        Log.i(gtag, "Abstract item insert id return: " + Long.toString(items_id));

    }	//end function addItems
	
	//function for adding data in AUTHORS_DETAILS table
	public void addAuthors(String uuid, String first_Name, String middle_Name, String last_Name, String author_mail) {
		
		ContentValues value = new ContentValues();

        value.put("AUTHOR_UUID", uuid);
        
        value.put("AUTHOR_FIRST_NAME", first_Name);
        
        value.put("AUTHOR_MIDDLE_NAME", middle_Name);
        
        value.put("AUTHOR_LAST_NAME", last_Name);
        
        value.put("AUTHOR_EMAIL", author_mail);
	
        author_id = database.insert(TABLE_AUTHORS_DETAILS, null, value);
        Log.i(gtag, "Author inserted - id: " + author_id);
	
	}	//end addAuthors function
	
	//function for adding data in ABSTRACT_AUTHOR_POSITION_AFFILIATION table
	public void addInABSTRACT_AUTHOR_POSITION_AFFILIATION(String abstractUUID, String authorUUID, int authorPosition, String authorAffiliation) {
		ContentValues values = new ContentValues();
		
		values.put("ABSTRACT_UUID", abstractUUID);
		
		values.put("AUTHOR_UUID", authorUUID);
		
		values.put("AUTHOR_POSITION", authorPosition);
		
		values.put("AUTHOR_AFFILIATION", authorAffiliation);
		
		abs_auth_pos_id = database.insert(TABLE_ABSTRACT_AUTHOR_POSITION_AFFILIATION, null, values);
		Log.i(gtag, "Abstract UUID, Auth uuid, position, affiliation inserted: " + abs_auth_pos_id);
		
	}	//end addInABSTRACT_AUTHOR_POSITION_AFFILIATION function
	
	//function for adding new affiliation in AFFILIATION_DETAILS Table
	public void addInAFFILIATION_DETAILS(String uuid, String aff_address, String aff_country, String aff_dept, String aff_section) {
		
		ContentValues values = new ContentValues();
		
		values.put("AFFILIATION_UUID", uuid);
		
		values.put("AFFILIATION_ADDRESS", aff_address);
		
		values.put("AFFILIATION_COUNTRY", aff_country);
		
		values.put("AFFILIATION_DEPARTMENT", aff_dept);
		
		values.put("AFFILIATION_SECTION", aff_section);
		
		long affiliation_id;
		affiliation_id = database.insert(TABLE_AFFILIATION_DETAILS, null, values);
		Log.i(gtag, "Affiliation inserted into directory: " + affiliation_id);
	}
	
	//function to add in ABSTRACT_AFFILIATION_ID_POSITION Table - maintaining affiliation position against each abstract in a separate table
	public void addInABSTRACT_AFFILIATION_ID_POSITION (String abs_uuid, String aff_uuid, int aff_position) {
		
		ContentValues values = new ContentValues();
		
		values.put("ABSTRACT_UUID", abs_uuid);
		
		values.put("AFFILIATION_UUID", aff_uuid);
		
		values.put("AFFILIATION_POSITION", aff_position);
		
		long abs_aff_id_pos;
		abs_aff_id_pos = database.insert(TABLE_ABSTRACT_AFFILIATION_ID_POSITION, null, values);
		Log.i(gtag, "abs uuid, aff uuid, aff pos inserted: id = > " + abs_aff_id_pos);
	}
	
	//function for adding in ABSTRACT_REFERENCES table 
	public void addInABSTRACT_REFERENCES (String ABSTRACT_UUID, String REF_UUID, String REF_TEXT, String REF_LINK, String REF_DOI) {
		
		ContentValues values = new ContentValues();
		
		values.put("ABSTRACT_UUID", ABSTRACT_UUID);
		
		values.put("REF_UUID", REF_UUID);
		
		values.put("REF_TEXT", REF_TEXT);
		
		values.put("REF_LINK", REF_LINK);
		
		values.put("REF_DOI", REF_DOI);
		
		long ref_id;
		ref_id = database.insert(TABLE_ABSTRACT_REFERENCES, null, values);
		Log.i(gtag, "reference inserted: id = > " + ref_id);
		
	}
	
	//function to check if author already exists in directory
	public boolean AuthorExists(String UUID) {
        Cursor cursor = database.rawQuery("select 1 from " + TABLE_AUTHORS_DETAILS + " where AUTHOR_UUID like '%" + UUID
                + "%'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
	
	//function to check if affiliation already exists in directory
	public boolean AffiliationExists(String UUID) {
        Cursor cursor = database.rawQuery("select 1 from " + TABLE_AFFILIATION_DETAILS + " where AFFILIATION_UUID like '%" + UUID
                + "%'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }	
	
	

}
