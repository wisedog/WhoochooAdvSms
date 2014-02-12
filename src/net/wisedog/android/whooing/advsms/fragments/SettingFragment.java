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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.advsms.AppDefine;
import net.wisedog.android.whooing.advsms.CardInfo;
import net.wisedog.android.whooing.advsms.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SettingFragment extends Fragment{
	
	private static TextView mHoldingCardTextView = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting, container, false);
		if(view != null){
			TextView cardSetting = (TextView)view.findViewById(R.id.setting_text_card);
			TextView alarmSetting = (TextView)view.findViewById(R.id.setting_text_alarm_time);
			TextView section = (TextView)view.findViewById(R.id.setting_text_section);
			TextView currentSection = (TextView)view.findViewById(R.id.setting_text_current_section);
			TextView currentCard = (TextView)view.findViewById(R.id.setting_text_hold_card);
			
			SharedPreferences pref = getActivity().getSharedPreferences(AppDefine.SHARED_PREFERENCE, 0);
			
			if(currentCard != null){
				mHoldingCardTextView = currentCard;
				String holdingCards = pref.getString(AppDefine.KEY_SHARED_HOLDING_CARD, null);
				if(holdingCards != null){
					ArrayList<Integer> array = CardInfo.convertStringToIntArray(holdingCards);
					String msg = "";
					for(int i = 0; i < array.size(); i++){
						msg = msg + CardInfo.cardAddressList[(array.get(i)*2) +1] + ", ";
					}
					msg = msg.substring(0, msg.length()-2); //remove ", "
					currentCard.setText(msg);
				}
			}
			
			
			if(cardSetting != null && alarmSetting != null && section != null &&
					currentSection != null){
				final FragmentManager mgr = getActivity().getFragmentManager();
				
				
				String currSection = pref.getString(AppDefine.KEY_SHARED_CURRENT_SECTION_ID, null);
				String sectionInfo = pref.getString(AppDefine.KEY_SHARED_SECTION_INFO, null);
				if(sectionInfo != null){
					try{
						JSONArray array = new JSONArray(sectionInfo);
						JSONObject obj = null;
						if(currSection == null){
							obj = (JSONObject) array.get(0);
							String sectionId = obj.getString("section_id");
							SharedPreferences.Editor editor = pref.edit();
							editor.putString(AppDefine.KEY_SHARED_CURRENT_SECTION_ID, sectionId);
							editor.commit();
							currentSection.setText(obj.getString("title"));
						}
						else{
							for(int i = 0; i < array.length(); i++){
								obj = (JSONObject) array.get(i);
								if(obj != null){
									String sectionId = obj.getString("section_id");
									
									if(sectionId != null && sectionId.compareTo(currSection) == 0){
										currentSection.setText(obj.getString("title"));
										break;
									}
								}
							}
						}
					}catch(JSONException e){
						e.printStackTrace();
					}
				}
				
				section.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						SectionDialogFragment dialog = new SectionDialogFragment();
						dialog.show(mgr, "");
					}
				});
				
				cardSetting.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						CardSelectDialogFragment dialog = new CardSelectDialogFragment();
						dialog.show(mgr, "");
						
					}
				});
				
				alarmSetting.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						AlarmDialogFragment dialog = new AlarmDialogFragment();
						dialog.show(mgr, "");
						
					}
				});
			}
		}
		return view;
	}
	
	static public class CardSelectDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			
			ArrayList<String> array = new ArrayList<String>();
			String[] rawData = CardInfo.cardAddressList;
			String item = "";
			for(int i = 0; i < rawData.length; i++){
				if((i % 2) == 0){
					item = rawData[i];
				}else{
					item = item + "("+rawData[i]+")";
					array.add(item);
				}
			}
			String[] items = new String[array.size()];
			items = array.toArray(items);
			
			// Set the dialog title
			builder.setTitle(getString(R.string.setting_dlg_card_title))
					.setMultiChoiceItems(items, null,
							new DialogInterface.OnMultiChoiceClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									if (isChecked) {
										mSelectedItems.add(which);
									} else if (mSelectedItems.contains(which)) {
										mSelectedItems.remove(Integer
												.valueOf(which));
									}
								}
							})
					// Set the action buttons
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									SharedPreferences prefs = getActivity().getSharedPreferences(AppDefine.SHARED_PREFERENCE, 0);
									SharedPreferences.Editor editor = prefs.edit();
									String str = CardInfo.convertIntArrayToString(mSelectedItems);
									editor.putString(AppDefine.KEY_SHARED_HOLDING_CARD, str);
									editor.commit();
									
									String msg = "";
									for(int i = 0; i < mSelectedItems.size(); i++){
										msg = msg + CardInfo.cardAddressList[(mSelectedItems.get(i)*2) +1] + ", ";
									}
									if(msg.length() > 1){
										msg = msg.substring(0, msg.length()-2);	//remove ", "
									}
									if(mHoldingCardTextView != null){
										mHoldingCardTextView.setText(msg);
									}
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									// do nothing
								}
							});

			return builder.create();
		}
	}
	
	static public class SectionDialogFragment extends DialogFragment {
		
		int mSelectIndex;
		JSONArray mArray = null;
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			
			SharedPreferences prefs = getActivity().getSharedPreferences(AppDefine.SHARED_PREFERENCE, 0);
			String sectionInfo = prefs.getString(AppDefine.KEY_SHARED_SECTION_INFO, null);
			if(sectionInfo != null){
				try{
					JSONArray array = new JSONArray(sectionInfo);
					mArray = array;
					String[] items = new String[array.length()];
					for(int i = 0; i < array.length(); i++){
						JSONObject obj = (JSONObject) array.get(i);
						items[i] = obj.getString("title");
					}
					// Set the dialog title
					builder.setTitle("")
							.setSingleChoiceItems(items, 0,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											mSelectIndex = which;

										}
									})

							// Set the action buttons
							.setPositiveButton(R.string.ok,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,
												int id) {
											SharedPreferences prefs = getActivity().getSharedPreferences(AppDefine.SHARED_PREFERENCE, 0);
											SharedPreferences.Editor editor = prefs.edit();
											try{
												JSONObject obj = mArray.getJSONObject(mSelectIndex);
												String sectionId = obj.getString("section_id");
												editor.putString(AppDefine.KEY_SHARED_CURRENT_SECTION_ID, sectionId);
												editor.commit();
											}catch(JSONException e){
												e.printStackTrace();
											}catch(NullPointerException e1){
												e1.printStackTrace();
											}
										}
									})
							.setNegativeButton(R.string.cancel,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,
												int id) {

										}
									});
				}
				catch(JSONException e){
					e.printStackTrace();
				}
				
			}
			else{
				Toast.makeText(getActivity(), getString(R.string.setting_warning_no_section), Toast.LENGTH_LONG).show();
			}

			return builder.create();
		}
	}

	static public class AlarmDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			
			final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>(); 
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			
			
			// Set the dialog title
			builder.setTitle("")
					// R.string.pick_toppings)
					// .setSingle
					.setMultiChoiceItems(R.array.alarm_interval, null,
							new DialogInterface.OnMultiChoiceClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									if (isChecked) {
										// If the user checked the item, add it
										// to the selected items
										mSelectedItems.add(which);
									} else if (mSelectedItems.contains(which)) {
										// Else, if the item is already in the
										// array, remove it
										mSelectedItems.remove(Integer
												.valueOf(which));
									}
								}
							})
					// Set the action buttons
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									// User clicked OK, so save the
									// mSelectedItems results somewhere
									// or return them to the component that
									// opened the dialog

								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});

			return builder.create();
		}
	}
}
