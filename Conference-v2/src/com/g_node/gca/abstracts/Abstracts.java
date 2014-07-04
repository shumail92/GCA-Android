package com.g_node.gca.abstracts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.g_node.gca.abstracts.AbstractCursorAdapter;
import com.g_node.gca.abstracts.DatabaseHelper;
import com.g_node.gca.utils.JSONReader;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shumail.newsroom.R;

import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Abstracts extends Activity {
	
	Cursor cursor;
	public static int cursorCount;
	EditText searchOption;
	ListView listView;
	AbstractCursorAdapter cursorAdapter;
	
	String gTag = "GCA-Abstracts";
	DatabaseHelper dbHelper = new DatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abstracts);
		listView = (ListView)findViewById(R.id.AbsListView);
		searchOption = (EditText)findViewById(R.id.abstractSearch);
		
		/*
         * Get Writable Database
         */
        dbHelper.open();
		
        //check if DB already has data
        
        /*
         * SQL Query to get data
         */
        String query = "SELECT UUID AS _id , TOPIC, TITLE, ABSRACT_TEXT, STATE, SORTID, REASONFORTALK, MTIME, TYPE,DOI, COI, ACKNOWLEDGEMENTS FROM ABSTRACT_DETAILS;";
        /*
         * Query execution
         */
        cursor = DatabaseHelper.database.rawQuery(query, null);
        int a = cursor.getCount();
        Log.i(gTag, "data got rows : " + Integer.toString(a));
        /*
         * Get number of data to check whether database has any data or it's
         * empty
         */
        cursorCount = cursor.getCount();
        /*
         * Check If Database is empty.
         */
        if (cursorCount <= 0) {

           //call jsonParse function to get data from abstracts JSON file
            jsonParse();
            /*
             * Query execution
             */
            cursor = DatabaseHelper.database.rawQuery(query, null);
            /*
             * get number of cursor data
             */
            cursorCount = cursor.getCount();
        }
        
        //set listview
        cursorAdapter = new AbstractCursorAdapter(this, cursor);
        listView.setAdapter(cursorAdapter);
        listView.setTextFilterEnabled(true);
        listView.setFastScrollEnabled(true);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                // TODO Auto-generated method stub

                try {
                    cursor = (Cursor)cursorAdapter.getCursor();
                    /*
                     * Getting data from Cursor
                     */
                    String Text = cursor.getString(cursor.getColumnIndexOrThrow("ABSRACT_TEXT"));
                    Log.i(gTag, "ABSTRACT_TEXT => " + Text);
                    String Title = cursor.getString(cursor.getColumnIndexOrThrow("TITLE"));
                    String Topic = cursor.getString(cursor.getColumnIndexOrThrow("TOPIC"));
                    String value = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    String acknowledgements = cursor.getString(cursor.getColumnIndexOrThrow("ACKNOWLEDGEMENTS"));
                    Intent in = new Intent(getApplicationContext(), AbstractContent.class);
                    /*
                     * Passing data by Intent
                     */
                    in.putExtra("abstracts", Text);
                    in.putExtra("Title", Title);
                    in.putExtra("Topic", Topic);
                    in.putExtra("value", value);
                    in.putExtra("acknowledgements", acknowledgements);
                    startActivity(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        /*
         * Searching filter to search data by Keywords, Title, Author Names
         */
        cursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchDataByName(constraint.toString());
            }
        });
        
        searchOption.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                // TODO Auto-generated method stub
                Abstracts.this.cursorAdapter.getFilter().filter(cs);
                Abstracts.this.cursorAdapter.notifyDataSetChanged();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        
		/*
         * Close Writable Database
         */
        //dbHelper.close();
        
	}//end onCreate
	
	private void jsonParse() {
		try {
			Log.i(gTag, "in JSON PARSING FUNCTION");
			//get json file from raw 
			InputStream inStream = this.getResources().openRawResource(R.raw.abstracts);
			JSONArray jsonArray = JSONReader.parseStream(inStream);	//read json file and put in JSONarray
			
			 for (int index = 0; index < jsonArray.length(); index++) {
				 
				 //get first abstract item object
				 JSONObject jsonObject = jsonArray.getJSONObject(index);
				 Log.i(gTag, "json got: " + jsonObject);	             
				 
				 //iterate over the object got, to extract required keys and values
				 
				 //abstract UUID
				 String abs_uuid = jsonObject.getString("uuid");
	             Log.i(gTag, "abstract uuid: " + abs_uuid);
	             
				 //abstract topic
				 String topic = jsonObject.getString("topic");
	             Log.i(gTag, "topic: " + topic);
	             
	             //abstract title
	             String title = jsonObject.getString("title");
	             Log.i(gTag, "title: " + title);
	             
	             //abstract text
	             String text = jsonObject.getString("text");
	             Log.i(gTag, "text: " + text);
	             
	             //abstract state
	             String state = jsonObject.getString("state");
	             Log.i(gTag, "state: " + state);
	             
	             //abstract sortID
	             int sortID = jsonObject.getInt("sortId");
	             Log.i(gTag, "sortID: " + sortID);
	             
	             //abstract reasonForTalk
	             String reasonForTalk = jsonObject.getString("reasonForTalk");
	             Log.i(gTag, "reasonForTalk: " + reasonForTalk);
	             
	             //abstract mtime
	             String mtime = jsonObject.getString("mtime");
	             Log.i(gTag, "mtime: " + mtime);
	             
	             //abstract isTalk
	             Boolean isTalk = jsonObject.getBoolean("isTalk");
	             String abstractType;
	             Log.i(gTag, "isTalk: " + isTalk);
	             
	             if(!isTalk) {	//if isTalk is false, then type is poster
	            	abstractType = "poster"; 
	             } else {
	            	 abstractType = "Talk";
	             }
	             Log.i(gTag, "abstract type: " + abstractType);
	             
	             //abstract DOI
	             String doi = jsonObject.getString("doi");
	             Log.i(gTag, "doi: " + doi);
	             
	             //Abstract conflictOfInterest
	             String coi = jsonObject.getString("conflictOfInterest");
	             Log.i(gTag, "conflictOfInterest: " + coi);
	             
	             //Abstract acknowledgements
	             String acknowledgements = jsonObject.getString("acknowledgements");
	             Log.i(gTag, "acknowledgements: " + acknowledgements);
	             
	             //Table insertion
	             //add the basic abstract json keys into abstract_details table
	             dbHelper.addItems(abs_uuid, topic, title, text, state, sortID, reasonForTalk, mtime, abstractType, doi, coi, acknowledgements);
	             
	             //Abstract affiliations JSONarray
	             JSONArray abs_Aff_Array = jsonObject.getJSONArray("affiliations");
	             
	             //now iterate over this array for extracting each affiliation 
	             for (int j=0; j<abs_Aff_Array.length(); j++) {
	            	 //get affiliation object
	            	 JSONObject affiliationJSONObject = abs_Aff_Array.getJSONObject(j);
	            	 
	            	 //affiliation UUID
	            	 String affiliation_uuid = affiliationJSONObject.getString("uuid");
	            	 Log.i(gTag, "aff uuid: " + affiliation_uuid);
	            	 
	            	 //affiliation section
	            	 String affiliation_section = affiliationJSONObject.getString("section");
	            	 Log.i(gTag, "aff section: " + affiliation_section);
	            	 
	            	 //affiliation department
	            	 String affiliation_department = affiliationJSONObject.getString("department");
	            	 Log.i(gTag, "aff department: " + affiliation_department);
	            	 
	            	 //affiliation country
	            	 String affiliation_country = affiliationJSONObject.getString("country");
	            	 Log.i(gTag, "aff country: " + affiliation_country);
	            	 
	            	 //affiliation address
	            	 String affiliation_address = affiliationJSONObject.getString("address");
	            	 Log.i(gTag, "aff address: " + affiliation_address);
	            	 
	            	 //affiliation position - (different for each abstract)
	            	 int affiliation_position = affiliationJSONObject.getInt("position");
	            	 Log.i(gTag, "aff position: " + affiliation_position);
	            	 
	            	 //check if affiliation UUID is not already in table
	            	 if (!dbHelper.AffiliationExists(affiliation_uuid)) {
		            	 //add affiliation detail into AFFILIATION_DETAILS Table
		            	 dbHelper.addInAFFILIATION_DETAILS(affiliation_uuid, affiliation_address, affiliation_country, affiliation_department, affiliation_section); 
	            	 }
	            	 
	            	 //add affiliation position against particular abstract in ABSTRACT_AFFILIATION_ID_POSITION table
	            	 dbHelper.addInABSTRACT_AFFILIATION_ID_POSITION(abs_uuid, affiliation_uuid, affiliation_position);
	            	 
	             }//loop end for each affiliation object
				 
	             //Abstract Authors JSONarray
	             JSONArray abs_authors_Array = jsonObject.getJSONArray("authors");
	             
	             //now iterate over authors array to extract each author information
	             for (int j=0; j<abs_authors_Array.length(); j++) {
	            	 //get author object
	            	 
	            	 JSONObject authorJSONObject = abs_authors_Array.getJSONObject(j);
	            	
	            	 //author UUID
	            	 String author_uuid = authorJSONObject.getString("uuid");
	            	 Log.i(gTag, "auth uuid: " + author_uuid);
	            	 
	            	 //author first Name
	            	 String author_fName = authorJSONObject.getString("firstName");
	            	 Log.i(gTag, "auth first name: " + author_fName);
	            	 
	            	 //author last Name
	            	 String author_lName = authorJSONObject.getString("lastName");
	            	 Log.i(gTag, "auth last name: " + author_lName);
	            	 
	            	 //author middle Name
	            	 String author_middleName = authorJSONObject.getString("middleName");
	            	 Log.i(gTag, "auth middle name: " + author_middleName);
	            	 
	            	 //author mail
	            	 String author_mail = authorJSONObject.getString("mail");
	            	 Log.i(gTag, "auth mail: " + author_mail);
	            	 
	            	 //author position (unique for each abstract)
	            	 int author_position = authorJSONObject.getInt("position");
	            	 Log.i(gTag, "auth position: " + author_position);
	            	 
	            	 //now get affiliations of a particular author for an abstract for example
	            	 // "affiliations": [0,1]
	            	 JSONArray authorAffiliationsArray = authorJSONObject.getJSONArray("affiliations");
	            	 Log.i(gTag, "auth affiliations: " + authorAffiliationsArray.toString());
	            	 
	            	 if (!dbHelper.AuthorExists(author_uuid)) {
		            	 //Add authors data in AUTHORS_DETAILS table 
		            	 dbHelper.addAuthors(author_uuid, author_fName, author_middleName, author_lName, author_mail);
	            	 }
	            	 
	            	 //Remove brackets from author affiliation that's to be written in super script
	            	 String authorAffiliationsWithoutBraces = authorAffiliationsArray.toString().replaceAll("\\[", "").replaceAll("\\]", "");
	            	 
	            	 //Add position, affiliation data in ABSTRACT_AUTHOR_POSITION_AFFILIATION table
	            	 dbHelper.addInABSTRACT_AUTHOR_POSITION_AFFILIATION(abs_uuid, author_uuid, author_position, authorAffiliationsWithoutBraces );
	            	 
	             } //end authors array loop
	             
	             
	             //Abstract references JSONarray
	             JSONArray abs_References_Array = jsonObject.getJSONArray("references");
	             
	             //now iterate over this array for extracting each reference
	             for (int j=0; j<abs_References_Array.length(); j++) {
	            	 //get reference object
	            	 JSONObject referenceJSONObject = abs_References_Array.getJSONObject(j);
	            	 
	            	 //Reference UUID
	            	 String reference_uuid = referenceJSONObject.getString("uuid");
	            	 Log.i(gTag, "ref uuid: " + reference_uuid);
	            	 
	            	 //Reference text
	            	 String reference_text = referenceJSONObject.getString("text");
	            	 Log.i(gTag, "ref text: " + reference_text);
	            	 
	            	 //Reference link
	            	 String reference_link = referenceJSONObject.getString("link");
	            	 Log.i(gTag, "ref link: " + reference_link);
	            	 
	            	 //Reference DOI
	            	 String reference_doi = referenceJSONObject.getString("doi");
	            	 Log.i(gTag, "ref DOI: " + reference_doi);
	            	 
	            	 //insert the reference into ABSTRACT_REFERENCES table 
	            	 dbHelper.addInABSTRACT_REFERENCES(abs_uuid, reference_uuid, reference_text, reference_link, reference_doi);
	             	             
	             }//end references array loop
	             
			 }//end abstracts array parsing
		
		} catch (FileNotFoundException e) {
            Log.e("jsonFile", "file not found");
        } catch (IOException e) {
            Log.e("jsonFile", "ioerror");
        } catch (JSONException e) {
            e.printStackTrace();
        }
	
	}	//end json parseing

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.abstracts, menu);
		return true;
	}
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
        // The activity is about to be destroyed.
    }


}