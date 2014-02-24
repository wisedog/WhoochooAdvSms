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
package net.wisedog.android.whooing.advsms.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.advsms.AppDefine;
import net.wisedog.android.whooing.advsms.CardInfo;
import net.wisedog.android.whooing.advsms.R;
import net.wisedog.android.whooing.advsms.db.DatabaseHandler;
import net.wisedog.android.whooing.advsms.network.ThreadRestAPI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

@SuppressLint("HandlerLeak")
public class MainFragment extends Fragment{
	
	private ArrayList<MessageEntity> mDataArray;
	private SmsListAdapter mListAdapter;
	private int mSelectIndex = -1;
	
	private long mFromTimeStamp;
	private long mToTimeStamp;
	
	DatabaseHandler mDb = null;
	
	public void setArgument(DatabaseHandler db){
		mDb = db;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.sms_main_fragment, container, false);
		if(view != null){
			mDataArray = new ArrayList<MessageEntity>();
			mListAdapter = new SmsListAdapter(getActivity(), mDataArray);
			ListView listView = (ListView)view.findViewById(R.id.smsListView); 
			if(listView != null){
				listView.setAdapter(mListAdapter);
			}
			final Spinner spinnerDate = (Spinner) view.findViewById(R.id.smsMainDateSpinner);
			final Spinner spinnerCard = (Spinner) view.findViewById(R.id.smsMainCardSpinner);
			TextView messageBoard = (TextView) view.findViewById(R.id.smsMessageBoard);
			final Button searchBtn = (Button) view.findViewById(R.id.smsBtnSearch);
			final Button sendBtn = (Button) view.findViewById(R.id.smsBtnUpload);
			
			SharedPreferences prefs = getActivity().getSharedPreferences(AppDefine.SHARED_PREFERENCE, 0);
			
			if(sendBtn != null){
				sendBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						invokeThread(AppDefine.API_POST_SMS);
					}
				});				
			}
			
			String holdingCards = prefs.getString(AppDefine.KEY_SHARED_HOLDING_CARD, null);
			if(holdingCards == null){
				if(spinnerDate != null && spinnerCard != null
						&& searchBtn != null){
					spinnerDate.setEnabled(false);
					spinnerCard.setEnabled(false);
					searchBtn.setEnabled(false);
				}
				if(messageBoard != null){
					messageBoard.setText(getString(R.string.main_msg_need_setting));
				}
			}
			else{
				if(searchBtn != null){
					searchBtn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							readSmsMessage(mFromTimeStamp, mToTimeStamp);							
						}
					});
				}
				//setting holding cards array
				if(spinnerCard != null){
					ArrayList<Integer> array = CardInfo.convertStringToIntArray(holdingCards);
					String[] cards = new String[array.size()];
					for(int i = 0; i < array.size(); i++){
						cards[i] = CardInfo.cardAddressList[(array.get(i)*2) +1];
					}
					
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
			                R.layout.spinner_item, cards) {
						
			            @Override
			            public View getView(int position, View convertView, ViewGroup parent) {
			                View v = super.getView(position, convertView, parent);
			                ((TextView) v).setTextColor(Color.rgb(0x33, 0x33, 0x33));
			                return v;
			            }

			        };
			        spinnerCard.setAdapter(adapter);
			        spinnerCard.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int pos, long id) {
							mSelectIndex = pos;
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// Do nothing
						}
					});
				}
				
				if(spinnerDate != null){
					String[] str = {"3일 전","최근 전송성공일부터", "특정 날짜"};
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
							R.layout.spinner_item, str) {
						
			            @Override
			            public View getView(int position, View convertView, ViewGroup parent) {
			                View v = super.getView(position, convertView, parent);
			                ((TextView) v).setTextColor(Color.rgb(0x33, 0x33, 0x33));
			                return v;
			            }

			        };
			        spinnerDate.setAdapter(adapter);
			        
			        spinnerDate.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent, View view1, 
					            int pos, long id) {
							if(pos == 0){
								view.findViewById(R.id.smsDateSelectLayout).setVisibility(View.GONE);
								Calendar rightNow = Calendar.getInstance(TimeZone.getDefault());
								Date now = rightNow.getTime();
								mToTimeStamp = now.getTime();	
								
						        rightNow.add(Calendar.DAY_OF_MONTH, -3);
						        Date date = rightNow.getTime();
						        
						        mFromTimeStamp = date.getTime();
						        searchBtn.setEnabled(true);
							}
							else if(pos == 1){
								view.findViewById(R.id.smsDateSelectLayout).setVisibility(View.GONE);
								SharedPreferences prefs = getActivity().getSharedPreferences(AppDefine.SHARED_PREFERENCE, 0);
								long lastTimeStamp = prefs.getLong(AppDefine.KEY_SHARED_LAST_TIMESTAMP, -1);
								if(lastTimeStamp == -1){
									Toast.makeText(getActivity(), getString(R.string.main_msg_no_history), Toast.LENGTH_LONG).show();
									searchBtn.setEnabled(false);
									mDataArray.clear();
									mListAdapter.notifyDataSetChanged();
								}
								else{
									Calendar rightNow = Calendar.getInstance(TimeZone.getDefault());
									Date now = rightNow.getTime();
									mToTimeStamp = now.getTime();
									mFromTimeStamp = lastTimeStamp;
									searchBtn.setEnabled(true);
								}						        
							}
							else if(pos == 2){
								view.findViewById(R.id.smsDateSelectLayout).setVisibility(View.VISIBLE);
								searchBtn.setEnabled(true);
								ImageButton btnFrom = (ImageButton)view.findViewById(R.id.smsDateFromBtn);
								ImageButton btnTo = (ImageButton)view.findViewById(R.id.smsDateToBtn);
								final TextView textFrom = (TextView) view.findViewById(R.id.smsDateTextFrom);
								final TextView textTo = (TextView) view.findViewById(R.id.smsDateTextTo);
								textFrom.setTag(0);
								textTo.setTag(1);
								btnFrom.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										invokeDateDlg(textFrom);
									}
								});
								
								btnTo.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										invokeDateDlg(textTo);							
									}
								});
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							//Do nothing
						}
					});
				}
				spinnerDate.setSelection(0);
			}
		}
		return view;
	}
	
	/**
	 * Show/Hide progressbar
	 * @param	onoff	true makes it show, false makes it gone 
	 * */
	public void showProgress(boolean onoff){
		ProgressBar progress = (ProgressBar)getView().findViewById(R.id.smsActivityProgress);
		if(progress != null){
			if(onoff == true){
				progress.setVisibility(View.VISIBLE);
			}else{
				progress.setVisibility(View.GONE);
			}
		}
	}
	
	public void invokeDateDlg(final TextView v){
		final Calendar c = Calendar.getInstance();
		final int yy = c.get(Calendar.YEAR);
        final int mm = c.get(Calendar.MONTH);
        final int dd = c.get(Calendar.DAY_OF_MONTH); 
		DatePickerDialog dlg = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				java.text.DateFormat df = java.text.DateFormat.getDateInstance(
			    		java.text.DateFormat.SHORT, Locale.KOREAN);
				Calendar c = Calendar.getInstance();
				c.set(year, monthOfYear, dayOfMonth);
				String dateStr = df.format(c.getTime()).toString();
				v.setText(dateStr);
				if(v.getTag() == Integer.valueOf(0)){//from
					mFromTimeStamp = c.getTimeInMillis();
				}else if(v.getTag() == Integer.valueOf(1)){	//to
					mToTimeStamp = c.getTimeInMillis();
				}
			}
		}, yy, mm, dd);
		dlg.show();
	}
	
	/**
	 * Get current selected card address
	 * @return	Card address string
	 * */
	public String getCurrentAddress(){
		SharedPreferences prefs = getActivity().getSharedPreferences(AppDefine.SHARED_PREFERENCE, 0);
		String holdingCards = prefs.getString(AppDefine.KEY_SHARED_HOLDING_CARD, null);
		
		if(holdingCards == null){
			return null;
		}
		ArrayList<Integer> array = CardInfo.convertStringToIntArray(holdingCards);
		String addr = CardInfo.cardAddressList[array.get(mSelectIndex)*2];
		return addr;
	}
	

	/**
	 * Read SMS messages in the phone
	 * @param	from	time stamp of received time from
	 * @param	to		time stamp of received time to 
	 * */
	public int readSmsMessage(long from, long to) {
		Uri allMessage = Uri.parse("content://sms");
		ContentResolver cr = getActivity().getContentResolver();
		
		if(mSelectIndex == -1){
			return 0;
		}
		
		showProgress(true);
		
		String addr = getCurrentAddress();
		if(addr == null){
			return 0;
		}
		
		Cursor c = null;
		String[] PROJECTION = { "_id", "thread_id",
			"address", "person", "date", "body" }; 
		String WHERE1 = "address = " + addr;
		String WHERE = "(date BETWEEN " + from + " AND " 
				+ to + ") AND (" + WHERE1 + ")";
		
		c = cr.query(allMessage, PROJECTION , WHERE, null, "date DESC");

		String string = "";
		int count = 0;
		mDataArray.clear();
		while (c.moveToNext()) {
			MessageEntity entity = new MessageEntity(c.getLong(0), c.getLong(1), c.getString(2), 
					c.getLong(3), c.getLong(4), c.getString(5));
			if(mDb != null){
				if(mDb.isSent(c.getLong(0), c.getString(2), from, to) == true){
					;	// do not insert to array
				}
				else{
					mDataArray.add(entity);
				}
			}
			
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
	
	void invokeThread(int apiKind){
		Bundle bundle = new Bundle();
		Boolean[] selected = mListAdapter.getSelectedArray();
		String rows = "";
		for(int i = 0; i < selected.length; i++){
			if(selected[i] == true){
				rows = rows + mDataArray.get(i).getBody() + "\n";
			}
		}
		
		if(apiKind == AppDefine.API_POST_SMS_REPORT){
			String currentAddr = getCurrentAddress();
			bundle.putString("source", currentAddr);
		}
		else{
			;// Do nothing
		}
		
		bundle.putString("rows", rows);
		showProgress(true);
		Button sendBtn = (Button) getView().findViewById(R.id.smsBtnUpload);
		if(sendBtn != null){
			sendBtn.setEnabled(false);
		}		
		ThreadRestAPI thread = new ThreadRestAPI(mHandler, getActivity(), 
				apiKind, bundle);
		thread.start();
	}
	
	 Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			showProgress(false);
			Button btn = (Button) getView().findViewById(R.id.smsBtnUpload);
			if(btn != null){
				btn.setEnabled(true);
			}
			if(msg.what == AppDefine.MSG_API_OK){
				if(msg.arg1 == AppDefine.API_POST_SMS){
					JSONObject obj = (JSONObject)msg.obj;
					try{
						int code = obj.getInt("code");
						if(code == 400){
							AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
							builder.setMessage(getString(R.string.main_alert_dlg_msg))
									.setPositiveButton(R.string.ok,
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,
														int id) {
													invokeThread(AppDefine.API_POST_SMS_REPORT);
													dialog.dismiss();
												}
											})
									.setNegativeButton(R.string.cancel,
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,
														int id) {
													dialog.dismiss();
												}
											});
							builder.show();
						}
						else if(code == 200){//Successful
							Boolean[] selected = mListAdapter.getSelectedArray();
							ArrayList<MessageEntity> arr = new ArrayList<MessageEntity>();
							for(int i = 0; i < selected.length; i++){
								if(selected[i] == true){
									arr.add(mDataArray.get(i));
								}
							}
							if(mDb.addSentSms(arr) == true){
								SharedPreferences prefs = getActivity().getSharedPreferences(AppDefine.SHARED_PREFERENCE, 0);
								SharedPreferences.Editor editor = prefs.edit();
								editor.putLong(AppDefine.KEY_SHARED_LAST_TIMESTAMP, mToTimeStamp);
								editor.commit();
								readSmsMessage(mFromTimeStamp, mToTimeStamp);
								Toast.makeText(getActivity(), getString(R.string.main_msg_success), Toast.LENGTH_LONG).show();
							}
							else{
								Toast.makeText(getActivity(), "DB Insert fail", Toast.LENGTH_LONG).show();
							}
						}
						else{
							Toast.makeText(getActivity(), "Failed with Code : " + code, 
									Toast.LENGTH_LONG).show();
						}
					}catch(JSONException e){
						e.printStackTrace();
					}
				}
				else if(msg.arg1 == AppDefine.API_POST_SMS_REPORT){
					JSONObject obj = (JSONObject)msg.obj;
					try{
						int code = obj.getInt("code");
						if(code == 200){
							Toast.makeText(getActivity(), getString(R.string.main_msg_report_success), 
									Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(getActivity(), "Failed with code " + code, Toast.LENGTH_LONG).show();
						}
					}
					catch(JSONException e){
						e.printStackTrace();
					}
					Log.i("wisedog", obj.toString());
					
				}
			}
			else{
				Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
			}
			super.handleMessage(msg);
		}
	 };
}
