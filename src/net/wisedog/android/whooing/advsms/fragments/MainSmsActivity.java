package net.wisedog.android.whooing.advsms.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import net.wisedog.android.whooing.advsms.AppDefine;
import net.wisedog.android.whooing.advsms.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainSmsActivity extends Activity {
	
	private ArrayList<MessageEntity> mDataArray;
	private SmsListAdapter mListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms_main);
		
		mDataArray = new ArrayList<MessageEntity>();
		mListAdapter = new SmsListAdapter(this, mDataArray);
		ListView listView = (ListView)findViewById(R.id.smsListView); 
		if(listView != null){
			listView.setAdapter(mListAdapter);
		}
		
		//TODO Get authorization
		// or there is no auth info, call auth activity
		
		//최소 3일 전이 기본
		//TODO read date from ui
		
		String[] str = {"3일 전","특정 날짜 명시"};
		Spinner spinner = (Spinner) findViewById(R.id.smsActivitySpinner);
		TextView textview = (TextView) findViewById(R.id.smsActivityDateText);
		if(spinner != null){
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	                android.R.layout.select_dialog_item, str) {
				
	            @Override
	            public View getView(int position, View convertView, ViewGroup parent) {
	                View v = super.getView(position, convertView, parent);
	                ((TextView) v).setTextColor(Color.rgb(0x33, 0x33, 0x33));
	                return v;
	            }

	        };
	        spinner.setAdapter(adapter);
	        
	        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	        	final Calendar c = Calendar.getInstance();

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, 
			            int pos, long id) {
					if(pos == 0){ 
				        /*c.add(Calendar.DAY_OF_MONTH, -3);
						readSMSMessage(MODE_LAST_THREE_DAYS, c.get(Calendar.YEAR), 
								c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));*/
						Calendar rightNow = Calendar.getInstance(TimeZone.getDefault());
						Date now = rightNow.getTime();
						long nowTimestamp = now.getTime();
						
				        rightNow.add(Calendar.DAY_OF_MONTH, -3);
				        Date date = rightNow.getTime();
				        long threeDaysAgo = date.getTime();
				        
						readSmsMessage(threeDaysAgo, nowTimestamp);
					}
					else if(pos == 1){
						
				        
				        int year = c.get(Calendar.YEAR);
				        int month = c.get(Calendar.MONTH)+1;
				        int day = c.get(Calendar.DAY_OF_MONTH); 
						
						DatePickerDialog dlg = new DatePickerDialog(MainSmsActivity.this, new DatePickerDialog.OnDateSetListener() {
							
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear,
									int dayOfMonth) {
								/*readSMSMessage(MainActivity.MODE_SPECIFIED_DATE, year, monthOfYear, dayOfMonth);*/
								
							}
						}, year, month, day);
						dlg.show();
					}
					
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
	        if(textview != null){
	        	textview.setText(str[0]);
	        }
		}
		spinner.setSelection(0);
        
		//TODO get Preference 
		// if the preference is empty, set 0
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {	
		Spinner spinner = (Spinner) findViewById(R.id.smsActivitySpinner);
		int pos = spinner.getSelectedItemPosition();
		if(pos == 0){
			/*Calendar rightNow = Calendar.getInstance();
			Date now = rightNow.getTime();
			long nowTimestamp = now.getTime();
			
	        rightNow.add(Calendar.DAY_OF_MONTH, -3);
	        Date date = rightNow.getTime();
	        long threeDaysAgo = date.getTime();
	        
			readSmsMessage(threeDaysAgo, nowTimestamp);*/
		}
		else if(pos == 1){
			
		}
		else if(pos == 2){
			
		}
		//readSMSMessage();
		super.onResume();
	}
	
	public void showProgress(boolean onoff){
		ProgressBar progress = (ProgressBar)findViewById(R.id.smsActivityProgress);
		if(progress != null){
			if(onoff == true){
				progress.setVisibility(View.VISIBLE);
			}else{
				progress.setVisibility(View.GONE);
			}
			
		}
	}
	

	public int readSmsMessage(long from, long to) {
		Uri allMessage = Uri.parse("content://sms");
		ContentResolver cr = getContentResolver();
		
		showProgress(true);
		
		//TODO add from when selective query
		Cursor c = null;
		String[] PROJECTION = { "_id", "thread_id",
			"address", "person", "date", "body" }; 
		String WHERE1 = "address = '15661000'";
		String WHERE = "(date BETWEEN " + from + " AND " 
				+ to + ") AND (" + WHERE1 + ")";
		
		c = cr.query(allMessage, PROJECTION , WHERE, null, "date DESC");
		

		String string = "";
		int count = 0;
		while (c.moveToNext()) {
			MessageEntity entity = new MessageEntity(c.getLong(0), c.getLong(1), c.getString(2), 
					c.getLong(3), c.getLong(4), c.getString(5));
			mDataArray.add(entity);
			
			if(AppDefine.IS_DEBUG){
				//For debugging
				long messageId = c.getLong(0);
				long threadId = c.getLong(1);
				String address = c.getString(2);
				long contactId = c.getLong(3);
				String contactId_string = String.valueOf(contactId);
				long timestamp = c.getLong(4);
				String body = c.getString(5);

				string = String.format("msgid:%d, threadid:%d, address:%s, "
						+ "contactid:%d, contackstring:%s, timestamp:%d, body:%s",
						messageId, threadId, address, contactId, contactId_string,
						timestamp, body);
				Log.d("wisedog", ++count + "st, Message: " + string);
			}
		}
		mListAdapter.notifyDataSetChanged();
		showProgress(false);

		return 0;
	}

}
