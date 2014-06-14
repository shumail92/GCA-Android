package com.shumail.gca.newsroom;

import java.io.FileNotFoundException;

import com.shumail.newsroom.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NewsRoomActivity extends Activity {

	private NewsAdapter mAdapter;
	private ListView newsItems;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("incf", "OnCreate()");
		setContentView(R.layout.activity_newsroom);

		//Get reference to our ListView
		newsItems = (ListView)findViewById(R.id.sitesList);
		
		//Set the click listener to launch the browser when a row is clicked.
		newsItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,long id) {
				String url = mAdapter.getItem(pos).getLink();
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				
			}
			
		});
		
		/*
		 * If network is available download the xml from the Internet.
		 * If not then try to use the local file from last time.
		 */
		if(isNetworkAvailable()){
			Log.i("incf-rss", "starting download Task");
			SitesDownloadTask download = new SitesDownloadTask();
			download.execute();
		}else{
			mAdapter = new NewsAdapter(getApplicationContext(), -1, FeedXmlPullParser.getNewsItemFromFile(NewsRoomActivity.this));
			newsItems.setAdapter(mAdapter);
		}

	}
	
	//Helper method to determine if Internet connection is available.
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	} 
	
	/*
	 * AsyncTask that will download the xml file for us and store it locally.
	 * After the download is done we'll parse the local file.
	 */
	private class SitesDownloadTask extends AsyncTask<Void, Void, Void>{
		
		private ProgressDialog Dialog = new ProgressDialog(NewsRoomActivity.this);
		
		@Override
		protected void onPreExecute()
	    {
	        Dialog.setMessage("Updating...");
	        Dialog.setCancelable(false);
	        Dialog.show();
	    }
		
		@Override
		protected Void doInBackground(Void... arg0) {
			//Download the file
			try {
				Downloader.DownloadFromUrl("http://www.incf.org/newsroom/aggregator/rss.xml", openFileOutput("news.xml", Context.MODE_PRIVATE));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			Dialog.dismiss();
			//setup our Adapter and set it to the ListView.
			mAdapter = new NewsAdapter(NewsRoomActivity.this, -1, FeedXmlPullParser.getNewsItemFromFile(NewsRoomActivity.this));
			newsItems.setAdapter(mAdapter);
			Log.i("incf-rss", "adapter size = "+ mAdapter.getCount());
		}
	}

}