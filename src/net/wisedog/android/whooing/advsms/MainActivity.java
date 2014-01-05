/*
 * Copyright (C) 2013 Jongha Kim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wisedog.android.whooing.advsms;

import java.util.Calendar;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final int MODE_LAST_THREE_DAYS = 0;
	private static final int MODE_SPECIFIED_DATE = 1;
	
	private long lastTimeStamp = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
				        c.add(Calendar.DAY_OF_MONTH, -3);
						readSMSMessage(MODE_LAST_THREE_DAYS, c.get(Calendar.YEAR), 
								c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
					}
					else if(pos == 1){
						
				        
				        int year = c.get(Calendar.YEAR);
				        int month = c.get(Calendar.MONTH)+1;
				        int day = c.get(Calendar.DAY_OF_MONTH); 
						
						DatePickerDialog dlg = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
							
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear,
									int dayOfMonth) {
								readSMSMessage(MainActivity.MODE_SPECIFIED_DATE, year, monthOfYear, dayOfMonth);
								
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
		
		//readSMSMessage();
		super.onResume();
	}

	public int readSMSMessage(int mode, int year, int month, int day) {
		Uri allMessage = Uri.parse("content://sms");
		ContentResolver cr = getContentResolver();
		
		Date date = new Date(year, month, day);
		
		
		//TODO add from when selective query
		Cursor c = null;
		if(mode == MODE_LAST_THREE_DAYS){	//last 3 days
			c = cr.query(allMessage, new String[] { "_id", "thread_id",
					"address", "person", "date", "body" }, null, null, "date DESC");
		}
		else if(mode == MODE_SPECIFIED_DATE){	//from specified date 
			
		}else{
			return 0;			
		}
		

		String string = "";
		int count = 0;
		while (c.moveToNext()) {
			MessageEntity entity = new MessageEntity(c.getLong(0), c.getLong(1), c.getString(2), 
					c.getLong(3), c.getLong(4), c.getString(5));
			
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

		return 0;
	}

}
